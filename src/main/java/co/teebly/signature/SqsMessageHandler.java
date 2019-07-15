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

    private static String[] prepareArgs(SignatureRequest sr) {
        ArrayList<String> res = new ArrayList<>();
        res.add("WAFFLES ETC"); // TODO add values

        return res.toArray(new String[0]);
    }

    private static void process(SignatureRequest sr) {
        System.out.println("Signer handling message from " + sr.getFileReference().toString());
        File xmlFile = new File(FileUtils.getTempDirectory(), System.currentTimeMillis() + ".pdf");

        // make sure the file is available locally
        try (InputStream is = sr.getFileReference().getContent()) {
            FileUtils.copyInputStreamToFile(is, xmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String[] args = prepareArgs(sr);
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
