package co.teebly.signature;

import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import com.swisscom.ais.itext.SignPDF;
import co.teebly.utils.UX;
import co.teebly.utils.files.FileReference;
import co.teebly.utils.id.EventId;

/**
 * Top enable tests read {@link TestContext].
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModuleTest {

  private static final File _DIR = new File("target/test-classes");

  private static final File _SRC_TEST_RES_DIR = new File("src/test/resources");

  private static final Logger LOG = LoggerFactory.getLogger(ModuleTest.class);

  private static final File T01_PDF_IN_FILE =
      new File(_DIR, "Reference_Guide-All-in-Signing-Service-en.pdf");

  private static URI T01_PDF_IN_URI;

  private static final File T01_PDF_OUT_FILE =
      new File(_DIR, "Reference_Guide-All-in-Signing-Service-en.pdf-signed.pdf");

  private static URI T01_PDF_OUT_URI;

  private static final File T01_PNG_IN_FILE = new File(_SRC_TEST_RES_DIR, "test.png");

  private static URI T01_PNG_IN_URI;

  private static TestContext testContext;

  static {
    String uri = "file://" + T01_PDF_IN_FILE.getAbsolutePath();
    try {
      T01_PDF_IN_URI = new URI(uri);
    } catch (URISyntaxException e1) {
      throw new IllegalStateException("Failed to create URI using string '" + uri + "'");
    }
  }

  static {
    String uri = "file://" + T01_PNG_IN_FILE.getAbsolutePath();
    try {
      T01_PNG_IN_URI = new URI(uri);
    } catch (URISyntaxException e1) {
      throw new IllegalStateException("Failed to create URI using string '" + uri + "'");
    }
  }

  static {
    String uri = "file://" + T01_PDF_OUT_FILE.getAbsolutePath();
    try {
      T01_PDF_OUT_URI = new URI(uri);
    } catch (URISyntaxException e1) {
      throw new IllegalStateException("Failed to create URI using string '" + uri + "'");
    }
  }

  @BeforeClass
  public static void init_class() throws Exception {
    testContext = TestContext.instance();
  }

  @Test
  public void test_01() throws Exception {

    assumeNotNull(testContext.getTeeblyDatabase());
    assumeTrue(TestContext.CRT_FILE.exists());
    assumeTrue(TestContext.KEY_FILE.exists());

    UX.rm_f(T01_PDF_OUT_FILE);

    // Setup SignatureRequest
    SignatureRequest sr = new SignatureRequest( //
        new EventId().toString(), //
        new EventId().toString(), //
        "Max Musterman", //
        "Maximilian Rudolph", //
        "Musterman", //
        "en", //
        "447708216475", //
        "GB", //
        "miro@teebly.co", //
        FileReference.createFileReference(T01_PDF_IN_URI), //
        FileReference.createFileReference(T01_PDF_OUT_URI), //
        FileReference.createFileReference(T01_PNG_IN_URI), //
        "theUserId", //
        false //
    );

    // Create document on the DB
    Document document = new Document(DBCollection.ID_FIELD_NAME, sr.getDocId());
    List<Document> signatures = new ArrayList<>();
    document.put(Worker.ATTR_SIGNATURES, signatures);
    Document signature = new Document(Worker.ATTR_SIGNATURES_USER_ID, sr.getUserId());
    signatures.add(signature);
    signature.put(Worker.ATTR_SIGNATURES_STATUS,
        Worker.PdfSignStatus.SIGN_PROCESS_STARTED.getXmlValue());
    signature.put(Worker.ATTR_SIGNATURES_STATUS_TS, new Date());
    LOG.info("Inserting into DB: " + document);
    MongoCollection<Document> col =
        testContext.getTeeblyDatabase().getCollection(Worker.DB_COLLECTION_NAME);
    col.insertOne(document);

    // Register SignatureRequest
    Worker.set(new Worker(sr));

    String[] args = SqsMessageProducer.prepareArgs(sr, T01_PDF_IN_FILE, T01_PDF_OUT_FILE);

    SignPDF ais = new SignPDF();
    ais.runSigning(args);
  }
}
