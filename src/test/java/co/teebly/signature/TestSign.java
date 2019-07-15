package co.teebly.signature;

import co.teebly.utils.files.FileReference;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class TestSign {

    @Test
    public void test_00() throws URISyntaxException {
        SignatureRequest sr = new SignatureRequest();
        sr.setLanguage("en");
        sr.setPhoneNumber("447708216475");


        String infile = getClass().getResource("/teebly.pdf").getFile();
        String path = String.format("file://%s", infile);
        sr.setFileReference(FileReference.createFileReference(new URI(path)));
        SqsMessageHandler.process(sr);

    }
}
