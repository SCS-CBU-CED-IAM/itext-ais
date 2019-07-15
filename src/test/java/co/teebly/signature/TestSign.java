package co.teebly.signature;

import co.teebly.utils.files.FileReference;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class TestSign {

    @Test
    public void test_00() throws URISyntaxException {
        String infile = getClass().getResource("/teebly.pdf").getFile();
        String path = String.format("file://%s", infile);
        FileReference ref = FileReference.createFileReference(new URI(path));

        SignatureRequest sr = new SignatureRequest(
                "sig.request†.1234", "Max Musterman", "Maximilian Rudolph",
                "Musterman", "en", "447708216475", "GB",
                "miro@teebly.co", ref);

        SqsMessageHandler.process(sr);

    }
}
