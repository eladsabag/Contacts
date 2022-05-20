package com.example.contacts.objects;

public class Contact {
    private String _id;
    private String firstName, lastName;
    private String mobileNumber;

    public Contact() { }

    public String getLastName() {
        return lastName;
    }

    public Contact setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Contact setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String get_id() {
        return _id;
    }

    public Contact set_id(String _id) {
        this._id = _id;
        return this;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public Contact setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }
}
