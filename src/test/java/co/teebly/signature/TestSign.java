package co.teebly.signature;

import co.teebly.utils.files.FileReference;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class TestSign {
    @BeforeClass
    public static void init_class() throws Exception {
        WebServer.downloadCredentials();
    }

    @Test
    public void test_00() throws URISyntaxException {
        String infile = getClass().getResource("/teebly.pdf").getFile();
        String path = String.format("file://%s", infile);
        FileReference ref = FileReference.createFileReference(new URI(path));

        SignatureRequest sr = new SignatureRequest(
                "sig.request.12345", "Max Musterman", "Maximilian Rudolph",
                "Musterman", "en", "447708216475", "GB",
                "miro@teebly.co", ref);

        SqsMessageHandler.process(sr);

    }
}
