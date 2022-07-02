package com.sendmail;

public class Customer {
    private String title;
    private String firstname;
    private String lastname;
    private String email;
    private Boolean hasMail = true;

    public Customer(String title, String firstname, String lastname, String email, Boolean hasMail) {
        this.title = title;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.hasMail = hasMail;
    }

    public Customer() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getHasMail() {
        return hasMail;
    }

    public void setHasMail(Boolean hasMail) {
        this.hasMail = hasMail;
    }
}
