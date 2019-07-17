package co.teebly.signature;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.swisscom.ais.itext.SignPDF;
import co.teebly.utils.UX;
import co.teebly.utils.files.FileReference;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModuleTest {

  private static final File _DIR = new File("target/test-classes");

  private static final Logger LOG = LoggerFactory.getLogger(ModuleTest.class);

  private static final File T01_PDF_IN_FILE =
      new File(_DIR, "Reference_Guide-All-in-Signing-Service-en.pdf");

  private static final String T01_PDF_IN_STR =
      "file:///src/test/resources/Reference_Guide-All-in-Signing-Service-en.pdf";

  private static URI T01_PDF_IN_URL;

  private static final File T01_PDF_OUT_FILE =
      new File(_DIR, "Reference_Guide-All-in-Signing-Service-en.pdf-signed.pdf");

  private static final String T01_PDF_OUT_STR =
      "file:///target/test-classes/Reference_Guide-All-in-Signing-Service-en.pdf-signed.pdf";

  private static URI T01_PDF_OUT_URL;

  private static final String T01_PNG_IN_STR = "file:///src/test/resources/test.png";

  private static URI T01_PNG_IN_URL;

  static {
    try {
      T01_PDF_IN_URL = new URI(T01_PDF_IN_STR);
    } catch (URISyntaxException e1) {
      throw new IllegalStateException("Failed to create URL using string '" + T01_PDF_IN_STR + "'");
    }
    try {
      T01_PNG_IN_URL = new URI(T01_PNG_IN_STR);
    } catch (URISyntaxException e1) {
      throw new IllegalStateException("Failed to create URL using string '" + T01_PNG_IN_STR + "'");
    }
    try {
      T01_PDF_OUT_URL = new URI(T01_PDF_OUT_STR);
    } catch (URISyntaxException e1) {
      throw new IllegalStateException(
          "Failed to create URL using string '" + T01_PDF_OUT_STR + "'");
    }
  }

  @BeforeClass
  public static void init_class() throws Exception {
    WorkQueue.init();
  }

  @Test
  public void test_01() throws Exception {
    UX.rm_f(T01_PDF_OUT_FILE);
    SignatureRequest sr = new SignatureRequest( //
        "sig.request.54321", //
        "Max Musterman", //
        "Maximilian Rudolph", //
        "Musterman", //
        "en", //
        "447708216475", //
        "GB", //
        "miro@teebly.co", //
        FileReference.createFileReference(T01_PDF_IN_URL), //
        FileReference.createFileReference(T01_PDF_OUT_URL), //
        // getRef("teebly.pdf"), //
        FileReference.createFileReference(T01_PNG_IN_URL), //
        // getRef("test.png"), //
        false //
    );

    String[] args = SqsMessageProducer.prepareArgs(sr, T01_PDF_IN_FILE);

    LOG.info("PASS: " + Arrays.asList(args));

    SignPDF ais = new SignPDF();
    ais.runSigning(args);
  }
}
