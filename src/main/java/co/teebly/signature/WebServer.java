package co.teebly.signature;

import io.sentry.Sentry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebServer {
    private static final Logger LOG = LoggerFactory.getLogger(WebServer.class);


    public static void main(String[] args) {
        // https://stackoverflow.com/questions/35696497/calling-web-service-javax-net-ssl-sslexception-received-fatal-alert-protocol
        // looking at the curl output when we successfully connect, this we can see what the cypher is
        System.setProperty("https.protocols", "TLSv1.2");

        // this will check the environment variable "SENTRY_DSN"
        Sentry.init();
        try {
            SqsMessageHandler sqs = new SqsMessageHandler();
            sqs.run("teebly-queue-name"); // TODO set up
        } catch (Throwable e) {
            Sentry.capture(e);
            LOG.error(e.getMessage(), e);
        }
    }
}
