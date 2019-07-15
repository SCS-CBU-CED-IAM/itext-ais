package co.teebly.signature;

import co.teebly.utils.files.FileReference;

public class SignatureRequest {
    private String firstName, lastName, language, phoneNumber;
    private FileReference fileReference;

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
