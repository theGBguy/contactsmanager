package com.gbsoft.contactsmanager.model;

/**
 * Created by Ravi Lal Pandey on 17/01/2018.
 */

public class Contact {
    private int id;
    private String personName;
    private String personPhone;

    public Contact(String personName, String personPhone) {
        this.personName = personName;
        this.personPhone = personPhone;
    }

    public Contact(int id, String personName, String personPhone) {
        this.id = id;
        this.personName = personName;
        this.personPhone = personPhone;
    }

    public String getPersonName() {
        return personName;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonPhone() {
        return personPhone;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }
}
