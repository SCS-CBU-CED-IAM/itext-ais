package co.teebly.signature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.swisscom.ais.itext.SignPDF;
import co.teebly.utils.aws.SQS;
import co.teebly.utils.files.FileReference;

public class SqsMessageProducer {

  private static final Logger LOG = LoggerFactory.getLogger(SqsMessageProducer.class);

  public static void handleMsg(String[] args) {
    SignPDF ais = new SignPDF();
    try {
      ais.runSigning(args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String[] prepareArgs(SignatureRequest sr, File infile, File outfile) {
    String propsFile = SqsMessageProducer.class.getResource("/signpdf.properties").getFile();
    ArrayList<String> res = new ArrayList<>();
    res.add("-vv");
    res.add(String.format("-infile=%s", infile.getAbsolutePath()));
    res.add(String.format("-outfile=%s", outfile.getAbsolutePath()));
    res.add(String.format("-config=%s", propsFile));
    res.add("-type=sign");

    if (sr.isAdvanced()) {
      // TODO remove TEST prefix once we're live!
      res.add(String.format("-dn=cn=TEST %s, givenname=%s, surname=%s, c=%s, emailaddress=%s",
          sr.getFullName(), sr.getFirstName(), sr.getLastName(), sr.getCountryCode(),
          sr.getEmail()));
      res.add(String.format("-stepUpMsisdn=%s", sr.getPhoneNumber()));
      res.add("-stepUpMsg=teebly.co: Sign the PDF? (#TRANSID#)");
      res.add(String.format("-stepUpLang=%s", sr.getLanguage()));
    } else {
      res.add("-reason=Teebly");
      res.add(String.format("-location=%s", sr.getCountryCode()));
      res.add(String.format("-contact=%s", sr.getEmail()));
    }

    return res.toArray(new String[0]);
  }

  public static void process(SignatureRequest sr) throws IOException {

    check(sr);

    System.out.println("Signer handling message from " + sr.getFileReference().toString());
    Worker.set(new Worker(sr));

    try {
      File tempFile = new File(new File("scratch"), sr.getSigningTx());
      File tempFileOut = new File(new File("scratch"), sr.getSigningTx() + "-signed.pdf");

      // make sure the files are available locally
      try (InputStream is = sr.getFileReference().getContent()) {
        FileUtils.copyInputStreamToFile(is, tempFile);
      } catch (IOException e) {
        e.printStackTrace();
      }


      String[] args = prepareArgs(sr, tempFile, tempFileOut);
      handleMsg(args); // TODO restore
      // try {
      // Thread.sleep(10000);
      // } catch (InterruptedException e) {
      // e.printStackTrace();
      // }
    } finally {
      Worker.remove();
    }
  }

  private static void check(SignatureRequest sr) {
    LOG.info("Checking " + sr);
    StringBuilder err = new StringBuilder();
    if (sr.getDocId() == null) {
      errAppend(err, "docId is null");
    }
    if (sr.getSigningTx() == null) {
      errAppend(err, "signingTx is null");
    }
    if (sr.getFileReference() == null) {
      errAppend(err, "fileReference is null");
    }
    if (sr.getFileReferenceSigned() == null) {
      errAppend(err, "fileReferenceSigned is null");
    }
    if (err.length() > 0) {
      throw new IllegalArgumentException("Errors in SignatureRequest: " + err);
    }
  }

  private static void errAppend(StringBuilder err, String string) {
    if (err.length() > 0) {
      err.append(", ");
    }
    err.append(string);
  }

  public void run(String queueName) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(FileReference.class,
        (JsonDeserializer<FileReference>) (json, typeOfT, context) -> {
          try {
            return FileReference.createFileReference(new URI(json.getAsString()));
          } catch (URISyntaxException e) {
            e.printStackTrace();
          }
          return null;

        });
    Gson gson = gsonBuilder.create();
    SQS.readInThread(queueName, m -> {
      String msgBody = m.getBody().trim();
      System.out.println("Got a message " + msgBody);
      SignatureRequest sr = gson.fromJson(msgBody, SignatureRequest.class);
      try {
        process(sr);
        return true;
      } catch (IOException e) {
        LOG.info(e.getMessage(), e);
        return false;
      } // TODO enqueue instead and let things run in parallel
    });
  }
}
