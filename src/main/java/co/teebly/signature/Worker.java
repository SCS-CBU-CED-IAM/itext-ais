package co.teebly.signature;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import co.teebly.tor.MongoTeeblyCollection;
import co.teebly.utils.mongo.TeeblyMongoDatabase;
import co.teebly.utils.mongo.TeeblyMongoSubsystem;

public class Worker {

  public enum PdfSignStatus {
    APPLIED_SIGNATURE, ERROR, FINISHED_OK, GOT_CONSENT_URL, GOT_SIGNATURE, SIGN_PROCESS_STARTED;
  }

  public static final String ATTR_PDF_SIGN_CONSENT_URL = "pdfSignConsentUrl";

  public static final String ATTR_PDF_SIGN_STATUS = "pdfSignStatus";

  public static final String ATTR_PDF_SIGN_STATUS_MESSAGE = "pdfSignStatusMessage";

  public static final String ATTR_PDF_SIGN_STATUS_TS = "pdfSignStatusTs";

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
    filter = Filters.eq(DBCollection.ID_FIELD_NAME, signatureRequest.getId());
    mongoCfg = TeeblyMongoDatabase.create(TeeblyMongoSubsystem.TEEBLY);
  }

  public void addConsentUrl(String consentUrl) {
    Document updates = mongoCreateUpdates(PdfSignStatus.GOT_CONSENT_URL, "Updated consent URL");
    updates.put(ATTR_PDF_SIGN_CONSENT_URL, consentUrl);
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
    ret.put(ATTR_PDF_SIGN_STATUS, pdfSignStatus.name());
    ret.put(ATTR_PDF_SIGN_STATUS_TS, new Date());
    ret.put(ATTR_PDF_SIGN_STATUS_MESSAGE, pdfSignStatusMessage);
    return ret;
  }

  private void mongoUpdate(Document updates) {
    Document update = new Document("$set", updates);
    MongoCollection<Document> col =
        mongoCfg.getCollection(MongoTeeblyCollection.TEEBLY_DOC.getName());
    col.findOneAndUpdate(filter, update);
  }

  private void readImageBytes() throws IOException {
    if (signatureRequest.getSignatureAppearance() == null) {
      return;
    }
    imageBytes = signatureRequest.getSignatureAppearance().getContent().readAllBytes();
  }

  public void setAppliedSignature() {
    LOG.info("Applied signature for " + signatureRequest.getId());
    mongoUpdate(mongoCreateUpdates(PdfSignStatus.APPLIED_SIGNATURE, "Applied signature"));
  }

  public void setGotSignature() {
    LOG.info("Got signature for " + signatureRequest.getId());
    mongoUpdate(mongoCreateUpdates(PdfSignStatus.GOT_SIGNATURE, "Got signature"));
  }

  public void setRequestId(String requestId) {
    LOG.info("Request-Id for " + signatureRequest.getId() + ": " + requestId);
  }

  public void setTransactionId(String transactionId) {
    LOG.info("Transaction-Id for " + signatureRequest.getId() + ": " + transactionId);
  }

  public void uploadSignedPdf(File file) throws IOException {

    LOG.info("Applied signature for " + signatureRequest.getId());
    mongoUpdate(mongoCreateUpdates(PdfSignStatus.APPLIED_SIGNATURE, "Applied signature"));

    LOG.info("Uploading signed PDF for " + signatureRequest.getId() + " to "
        + signatureRequest.getFileReferenceSigned());
    try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
      signatureRequest.getFileReferenceSigned().writeContent(is);
    }

    LOG.info("Uploaded signed PDF for " + signatureRequest.getId() + " to "
        + signatureRequest.getFileReferenceSigned());
    mongoUpdate(mongoCreateUpdates(PdfSignStatus.FINISHED_OK,
        "Uploaded signed PDF to " + signatureRequest.getFileReferenceSigned()));
  }
}
