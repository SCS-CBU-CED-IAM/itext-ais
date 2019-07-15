package co.teebly.signature;

import co.teebly.utils.files.FileReference;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class TestSign {
    @BeforeClass
    public static void init_class() throws Exception {
        WebServer.initService();
    }

    private FileReference getRef(String resource) throws URISyntaxException {
        String infile = getClass().getResource("/" + resource).getFile();
        String path = String.format("file://%s", infile);
        return FileReference.createFileReference(new URI(path));
    }

//    TODO commented out because it requires interaction and cannot be ran easily without supervision
//    @Test
//    public void test_advanced_signature() throws URISyntaxException {
//        SignatureRequest sr = new SignatureRequest(
//                "sig.request.12345", "Max Musterman", "Maximilian Rudolph",
//                "Musterman", "en", "447708216475", "GB",
//                "miro@teebly.co", getRef("teebly.pdf"), getRef("test.png"), true);
//
//        SqsMessageHandler.process(sr);
//    }

    @Test
    public void test_org_signature() throws URISyntaxException {
        SignatureRequest sr = new SignatureRequest(
                "sig.request.54321", "Max Musterman", "Maximilian Rudolph",
                "Musterman", "en", "447708216475", "GB",
                "miro@teebly.co", getRef("teebly.pdf"), getRef("test.png"),
                false);

        SqsMessageHandler.process(sr);
    }
}