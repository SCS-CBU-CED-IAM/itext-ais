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

  private boolean debug; // Legacy

  private int docId; // Legacy

  private String email;

  private FileReference fileReference;

  private FileReference fileReferenceSigned;

  private String firstName;

  private String fullName;

  private String id;

  private String language;

  private String lastName;

  private int page; // Legacy

  private String phoneNumber;

  private FileReference signatureAppearance;

  private int signBucket; // Legacy

  private int signFile; // Legacy

  private int sigWidth; // Legacy

  private int sigX; // Legacy

  private int sigY; // Legacy

  private String sourcePdf; // Legacy

  private int sourcePdfBucket; // Legacy

  public SignatureRequest(SignatureRequest other) {
    this.id = other.id;
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
    this.advanced = other.advanced;
  }

  public SignatureRequest(String id, String fullName, String firstName, String lastName,
      String language, String phoneNumber, String countryCode, String email,
      FileReference fileReference, FileReference fileReferenceSigned,
      FileReference signatureAppearance, boolean advanced) {
    this.id = id;
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
    this.advanced = advanced;
  }

  public String getCountryCode() {
    return countryCode;
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

  public String getId() {
    return id;
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

  public boolean isAdvanced() {
    return advanced;
  }

  public void setAdvanced(boolean advanced) {
    this.advanced = advanced;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
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

  public void setId(String id) {
    this.id = id;
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

  @Override
  public String toString() {
    return Messages.GSON.toJson(this);
  }
}
