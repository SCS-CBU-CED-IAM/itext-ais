package co.teebly.signature;

import co.teebly.utils.aws.S3;
import co.teebly.utils.conf.Env;
import co.teebly.utils.mongo.TeeblyMongoDatabase;
import co.teebly.utils.mongo.TeeblyMongoSubsystem;
import io.sentry.Sentry;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


public class WebServer {
    private static final String QUEUE_NAME_ENV = "QUEUE_NAME";
    private static final String CERTIFICATES_BUCKET_ENV = "CERTIFICATES_BUCKET";
    private static final String PRIVATE_KEY_NAME_ENV = "PRIVATE_KEY_NAME";
    private static final String PRIVATE_KEY_PASS_ENV = "PRIVATE_KEY_PASS";
    private static final String PUBLIC_KEY_CERT_ENV = "PUBLIC_KEY_CERT";
    // TODO once we're live the static/ondemand auth keys will also have to be set from env vars

    private static final Logger LOG = LoggerFactory.getLogger(WebServer.class);

    public static void initService() throws IOException {
        TeeblyMongoDatabase.create(TeeblyMongoSubsystem.TEEBLY);
        String bucketName = Env.getStringEnv(CERTIFICATES_BUCKET_ENV);
        String certName = Env.getStringEnv(PUBLIC_KEY_CERT_ENV);
        String pkName = Env.getStringEnv(PRIVATE_KEY_NAME_ENV);
        getPKPassword(); // fail early if it's not there

        File workdir = new File("scratch");
        if(!workdir.exists()) workdir.mkdirs();
        File targetFile = new File("_mycert.crt");
        if (!targetFile.exists())
            FileUtils.copyInputStreamToFile(S3.download(bucketName, certName), targetFile);

        targetFile = new File("_mycert.key");
        if (!targetFile.exists())
            FileUtils.copyInputStreamToFile(S3.download(bucketName, pkName), targetFile);
    }

    public static String getPKPassword() throws IOException {
        String pass = Env.getStringEnv(PRIVATE_KEY_PASS_ENV);
        if (pass == null) throw new IOException("Private key pass missing");
        System.setProperty("keystore.password", pass);
        return pass;
    }

    public static void main(String[] args) throws IOException {
        // https://stackoverflow.com/questions/35696497/calling-web-service-javax-net-ssl-sslexception-received-fatal-alert-protocol
        // looking at the curl output when we successfully connect, this we can see what the cypher is
        System.setProperty("https.protocols", "TLSv1.2");
        initService();

        // this will check the environment variable "SENTRY_DSN"
        Sentry.init();

        String queueName = Env.getStringEnv(QUEUE_NAME_ENV, "teebly-dev-qualified-signatures", false);
        try {
            SqsMessageProducer sqs = new SqsMessageProducer();
            sqs.run(queueName);
        } catch (Throwable e) {
            Sentry.capture(e);
            LOG.error(e.getMessage(), e);
        }
    }
}
