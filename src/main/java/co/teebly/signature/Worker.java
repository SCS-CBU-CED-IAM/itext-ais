package co.teebly.signature;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;
import javax.xml.bind.annotation.XmlEnumValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import co.teebly.utils.mongo.TeeblyMongoDatabase;
import co.teebly.utils.mongo.TeeblyMongoSubsystem;

public class Worker {

  public enum PdfSignStatus {

    @XmlEnumValue("appliedSignature")
    APPLIED_SIGNATURE("appliedSignature"),

    @XmlEnumValue("error")
    ERROR("error"),

    @XmlEnumValue("signed")
    FINISHED_OK("signed"),

    @XmlEnumValue("gotConsentUrl")
    GOT_CONSENT_URL("gotConsentUrl"),

    @XmlEnumValue("gotSignature")
    GOT_SIGNATURE("gotSignature"),

    @XmlEnumValue("signProcessStarted")
    SIGN_PROCESS_STARTED("signProcessStarted");

    private String xmlValue;

    private PdfSignStatus(String xmlValue) {
      this.xmlValue = xmlValue;
    }

    public String getXmlValue() {
      return xmlValue;
    }
  }

  public static final String DB_COLLECTION_NAME = "documents";

  public static final String ATTR_SIGNATURES = "signatures";

  public static final String ATTR_SIGNATURES_CONSENT_URL = "goToUrl";

  private static final String ATTR_SIGNATURES_CONSENT_URL_UPD =
      ATTR_SIGNATURES + ".$." + ATTR_SIGNATURES_CONSENT_URL;

  public static final String ATTR_SIGNATURES_STATUS = "status";

  private static final String ATTR_SIGNATURES_STATUS_UPD =
      ATTR_SIGNATURES + ".$." + ATTR_SIGNATURES_STATUS;

  public static final String ATTR_SIGNATURES_STATUS_TS = "timestamp";

  private static final String ATTR_SIGNATURES_STATUS_TS_UPD =
      ATTR_SIGNATURES + ".$." + ATTR_SIGNATURES_STATUS_TS;

  public static final String ATTR_SIGNATURES_USER_ID = "userId";

  private static final String ATTR_SIGNATURES_USER_ID_MATCH =
      ATTR_SIGNATURES + "." + ATTR_SIGNATURES_USER_ID;

  private static final Logger LOG = LoggerFactory.getLogger(Worker.class);

  private static final ThreadLocal<Worker> workers = new ThreadLocal<>();

  public static Worker get() {
    return workers.get();
  }

  public static void remove() {
    workers.remove();
  }

  public static void set(Worker worker) {
    workers.set(worker);
  }

  private Bson filter;

  private byte[] imageBytes;

  private TeeblyMongoDatabase mongoCfg;

  private SignatureRequest signatureRequest;

  public Worker(SignatureRequest signatureRequest) throws IOException {
    this.signatureRequest =
        Objects.requireNonNull(signatureRequest, "Supplied parameter 'signatureRequest' is empty");
    readImageBytes();
    filter = Filters.and( //
        Filters.eq(DBCollection.ID_FIELD_NAME, signatureRequest.getDocId()), //
        Filters.eq(ATTR_SIGNATURES_USER_ID_MATCH, signatureRequest.getUserId()) //
    );
    mongoCfg = TeeblyMongoDatabase.create(TeeblyMongoSubsystem.TEEBLY);
  }

  public void addConsentUrl(String consentUrl) {
    Document updates = mongoCreateUpdates(PdfSignStatus.GOT_CONSENT_URL, "Updated consent URL");
    updates.put(ATTR_SIGNATURES_CONSENT_URL_UPD, consentUrl);
    mongoUpdate(updates);
  }

  public byte[] getImageBytes() {
    return imageBytes;
  }

  public SignatureRequest getSignatureRequest() {
    return signatureRequest;
  }

  private Document mongoCreateUpdates(PdfSignStatus pdfSignStatus, String pdfSignStatusMessage) {
    Document ret = new Document();
    ret.put(ATTR_SIGNATURES_STATUS_UPD, pdfSignStatus.getXmlValue());
    ret.put(ATTR_SIGNATURES_STATUS_TS_UPD, new Date());
    return ret;
  }

  private void mongoUpdate(Document updates) {
    Document update = new Document("$set", updates);
    MongoCollection<Document> col =
        mongoCfg.getCollection(DB_COLLECTION_NAME);
    Document res = col.findOneAndUpdate(filter, update);
    if (res == null) {
      throw new IllegalStateException("This is a bug. No updates were done using filter='" + filter
          + "' and updates='" + updates + "'");
    }
  }

  private void readImageBytes() throws IOException {
    if (signatureRequest.getSignatureAppearance() == null) {
      return;
    }
    imageBytes = signatureRequest.getSignatureAppearance().getContent().readAllBytes();
  }

  public void setAppliedSignature() {
    LOG.info("Applied signature for " + signatureRequest.getDocId());
    mongoUpdate(mongoCreateUpdates(PdfSignStatus.APPLIED_SIGNATURE, "Applied signature"));
  }

  public void setGotSignature() {
    LOG.info("Got signature for " + signatureRequest.getDocId());
    mongoUpdate(mongoCreateUpdates(PdfSignStatus.GOT_SIGNATURE, "Got signature"));
  }

  public void setRequestId(String requestId) {
    LOG.info("Request-Id for " + signatureRequest.getDocId() + ": " + requestId);
  }

  public void setTransactionId(String transactionId) {
    LOG.info("Transaction-Id for " + signatureRequest.getDocId() + ": " + transactionId);
  }

  public void uploadSignedPdf(File file) throws IOException {

    LOG.info("Applied signature for " + signatureRequest.getDocId());
    mongoUpdate(mongoCreateUpdates(PdfSignStatus.APPLIED_SIGNATURE, "Applied signature"));

    LOG.info("Uploading signed PDF for " + signatureRequest.getDocId() + " to "
        + signatureRequest.getFileReferenceSigned());
    try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
      signatureRequest.getFileReferenceSigned().writeContent(is);
    }

    LOG.info("Uploaded signed PDF for " + signatureRequest.getDocId() + " to "
        + signatureRequest.getFileReferenceSigned());
    mongoUpdate(mongoCreateUpdates(PdfSignStatus.FINISHED_OK,
        "Uploaded signed PDF to " + signatureRequest.getFileReferenceSigned()));
  }
}
