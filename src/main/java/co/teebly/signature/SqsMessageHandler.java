package co.teebly.signature;

import co.teebly.utils.aws.SQS;
import com.google.gson.Gson;
import com.swisscom.ais.itext.SignPDF;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SqsMessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SqsMessageHandler.class);

    public static void handleMsg(String[] args) {
        SignPDF ais = new SignPDF();
        try {
            ais.runSigning(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String[] prepareArgs(SignatureRequest sr, File infile) {
        String propsFile = SqsMessageHandler.class.getResource("/signpdf.properties").getFile();
        ArrayList<String> res = new ArrayList<>();
        res.add("-vv");
        res.add(String.format("-infile=%s", infile.getAbsolutePath()));
        res.add(String.format("-outfile=%s-signed.pdf", infile.getAbsolutePath()));
        res.add(String.format("-config=%s", propsFile));
        res.add("-type=sign");

        if (sr.isAdvanced()) {
            // TODO remove TEST prefix once we're live!
            res.add(String.format("-dn=cn=TEST %s, givenname=%s, surname=%s, c=%s, emailaddress=%s",
                    sr.getFullName(), sr.getFirstName(), sr.getLastName(),
                    sr.getCountryCode(), sr.getEmail()));
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

    public static void process(SignatureRequest sr) {
        System.out.println("Signer handling message from " + sr.getFileReference().toString());
        WorkQueue.register(sr);
        File tempFile = new File(new File("scratch"), sr.getId());

        // make sure the files are available locally
        try (InputStream is = sr.getFileReference().getContent()) {
            FileUtils.copyInputStreamToFile(is, tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String[] args = prepareArgs(sr, tempFile);
        handleMsg(args);
    }

    public void run(String queueName) {
        Gson gson = new Gson();
        SQS.readInThread(queueName, m -> {
            SignatureRequest sr = gson.fromJson(m.getBody().trim(), SignatureRequest.class);
            process(sr);
            return true;
        });
    }
}