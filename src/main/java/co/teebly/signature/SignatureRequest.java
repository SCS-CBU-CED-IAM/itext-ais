package co.teebly.signature;

import co.teebly.utils.files.FileReference;

public class SignatureRequest {
    private String id, fullName, firstName, lastName, language, phoneNumber, countryCode, email;
    private FileReference fileReference;

    public SignatureRequest(String id, String fullName, String firstName, String lastName, String language, String phoneNumber, String countryCode, String email, FileReference fileReference) {
        this.id = id;
        this.fullName = fullName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.language = language;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.email = email;
        this.fileReference = fileReference;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FileReference getFileReference() {
        return fileReference;
    }

    public void setFileReference(FileReference fileReference) {
        this.fileReference = fileReference;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
