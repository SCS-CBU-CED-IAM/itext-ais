package co.teebly.signature;

import java.util.Date;
import java.util.Objects;
import co.teebly.utils.files.FileReference;

public class SignatureResultPollRequest extends SignatureRequest {

  public static SignatureResultPollRequest fromJson(String json) {
    Objects.requireNonNull(json, "Supplied parameter 'json' is null");
    return Messages.GSON.fromJson(json, SignatureResultPollRequest.class);
  }

  // Placeholder. Here we'd put what to do during/after completion. E.g. status-polling,
  // status-term-OK, status-error, etc... Most likely this should be in SignatureRequest
  private String reportHandle;

  private long sleepInterval;

  private Date timeOut;

  public SignatureResultPollRequest(SignatureRequest request, Date timeOut, long sleepInterval,
      String reportHandle) {
    super(request);
    this.timeOut = Objects.requireNonNull(timeOut, "Supplied parameter 'timeOut' is null");
    this.sleepInterval = sleepInterval;
    this.reportHandle =
        Objects.requireNonNull(reportHandle, "Supplied parameter 'reportHandle' is null");
  }

  public SignatureResultPollRequest(String signingTx, String docId, String fullName,
      String firstName, String lastName, String language, String phoneNumber, String countryCode,
      String email, FileReference fileReference, FileReference fileReferenceSigned,
      FileReference signatureAppearance, String userId, boolean advanced, Date timeOut,
      long sleepInterval, String reportHandle) {
    super(signingTx, docId, fullName, firstName, lastName, language, phoneNumber, countryCode,
        email, fileReference, fileReferenceSigned, signatureAppearance, userId, advanced);
    this.timeOut = Objects.requireNonNull(timeOut, "Supplied parameter 'timeOut' is null");
    this.sleepInterval = sleepInterval;
    this.reportHandle =
        Objects.requireNonNull(reportHandle, "Supplied parameter 'reportHandle' is null");
  }

  public String getReportHandle() {
    return reportHandle;
  }

  public long getSleepInterval() {
    return sleepInterval;
  }

  public Date getTimeOut() {
    return timeOut;
  }

  @Override
  public String toString() {
    return Messages.GSON.toJson(this);
  }
}
