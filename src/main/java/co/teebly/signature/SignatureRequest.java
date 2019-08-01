package co.teebly.signature;

import java.nio.charset.Charset;
import java.util.Objects;
import co.teebly.utils.files.FileReference;

public class SignatureRequest {

  private static final Charset UTF8 = Charset.forName("UTF-8");

  public static SignatureRequest fromJson(byte[] json) {
    Objects.requireNonNull(json, "Supplied parameter 'json' is null");
    return fromJson(new String(json, UTF8));
  }

  public static SignatureRequest fromJson(String json) {
    Objects.requireNonNull(json, "Supplied parameter 'json' is null");
    return Messages.GSON.fromJson(json, SignatureRequest.class);
  }

  private boolean advanced;

  private String countryCode;

  private String docId;

  private String email;

  private FileReference fileReference;

  private FileReference fileReferenceSigned;

  private String firstName;

  private String fullName;

  private String language;

  private String lastName;

  private int page;

  private String phoneNumber;

  private FileReference signatureAppearance;

  private int sigWidth;

  private int sigX;

  private int sigY;

  private String signingTx;


  private String userId;

  public SignatureRequest(SignatureRequest other) {
    this.signingTx = other.signingTx;
    this.docId = other.docId;
    this.fullName = other.fullName;
    this.firstName = other.firstName;
    this.lastName = other.lastName;
    this.language = other.language;
    this.phoneNumber = other.phoneNumber;
    this.countryCode = other.countryCode;
    this.email = other.email;
    this.fileReference = other.fileReference;
    this.fileReferenceSigned = other.fileReferenceSigned;
    this.signatureAppearance = other.signatureAppearance;
    this.userId = other.userId;
    this.advanced = other.advanced;
  }

  public SignatureRequest(String signingTx, String docId, String fullName, String firstName,
      String lastName, String language, String phoneNumber, String countryCode, String email,
      FileReference fileReference, FileReference fileReferenceSigned,
      FileReference signatureAppearance, String userId, boolean advanced) {
    this.signingTx = signingTx;
    this.docId = docId;
    this.fullName = fullName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.language = language;
    this.phoneNumber = phoneNumber;
    this.countryCode = countryCode;
    this.email = email;
    this.fileReference = fileReference;
    this.fileReferenceSigned = fileReferenceSigned;
    this.signatureAppearance = signatureAppearance;
    this.userId = userId;
    this.advanced = advanced;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSigX() {
    return sigX;
  }

  public void setSigX(int sigX) {
    this.sigX = sigX;
  }

  public int getSigY() {
    return sigY;
  }

  public void setSigY(int sigY) {
    this.sigY = sigY;
  }

  public int getSigWidth() {
    return sigWidth;
  }

  public void setSigWidth(int sigWidth) {
    this.sigWidth = sigWidth;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public String getDocId() {
    return docId;
  }

  public String getEmail() {
    return email;
  }

  public FileReference getFileReference() {
    return fileReference;
  }

  public FileReference getFileReferenceSigned() {
    return fileReferenceSigned;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getFullName() {
    return fullName;
  }

  public String getLanguage() {
    return language;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public FileReference getSignatureAppearance() {
    return signatureAppearance;
  }

  public String getSigningTx() {
    return signingTx;
  }

  public String getUserId() {
    return userId;
  }

  public boolean isAdvanced() {
    return advanced;
  }

  public void setAdvanced(boolean advanced) {
    this.advanced = advanced;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public void setDocId(String docId) {
    this.docId = docId;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setFileReference(FileReference fileReference) {
    this.fileReference = fileReference;
  }

  public void setFileReferenceSigned(FileReference fileReferenceSigned) {
    this.fileReferenceSigned = fileReferenceSigned;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setSignatureAppearance(FileReference signatureAppearance) {
    this.signatureAppearance = signatureAppearance;
  }

  public void setSigningTx(String signingTx) {
    this.signingTx = signingTx;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return Messages.GSON.toJson(this);
  }
}
