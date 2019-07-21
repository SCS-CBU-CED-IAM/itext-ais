package co.teebly.signature;

import java.util.Objects;
import co.teebly.utils.files.FileReference;

public class SignatureResponse extends SignatureRequest {

  public enum Result {
    ERROR, OK;
  }

  public static SignatureResponse fromJson(String json) {
    Objects.requireNonNull(json, "Supplied parameter 'json' is null");
    return Messages.GSON.fromJson(json, SignatureResponse.class);
  }

  private Result result;

  private String resultMessage;

  public SignatureResponse(SignatureRequest request, Result result, String resultMessage) {
    super(request);
    this.result = Objects.requireNonNull(result, "Supplied parameter 'result' is null");
    this.resultMessage = resultMessage;
  }

  public SignatureResponse(String signingTx, String docId, String fullName, String firstName,
      String lastName, String language, String phoneNumber, String countryCode, String email,
      FileReference fileReference, FileReference fileReferenceSigned,
      FileReference signatureAppearance, String userId, boolean advanced, Result result,
      String resultMessage) {
    super(signingTx, docId, fullName, firstName, lastName, language, phoneNumber, countryCode,
        email, fileReference, fileReferenceSigned, signatureAppearance, userId, advanced);
    this.result = Objects.requireNonNull(result, "Supplied parameter 'result' is null");
    this.resultMessage = resultMessage;
  }

  public Result getResult() {
    return result;
  }

  public String getResultMessage() {
    return resultMessage;
  }

  @Override
  public String toString() {
    return Messages.GSON.toJson(this);
  }
}
