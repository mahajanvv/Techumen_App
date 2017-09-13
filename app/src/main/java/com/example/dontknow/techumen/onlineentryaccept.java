package com.example.dontknow.techumen;

/**
 * Created by Dont know on 27-08-2017.
 */

public class onlineentryaccept {
    private String ID;
    private String Name;
    private String Email;
    private String Phone;
    private String Remaining;
    private String Signature;
    private String TransactionID;
    private String College;
    private String Year;
    private String Timestamp;
    private String Useremail;
    private String Date;
    private String Mailstatus;
    private String Messagestatus;
    private String Eventname;

    public onlineentryaccept()
    {

    }

    public onlineentryaccept(String ID, String name, String email, String phone, String remaining, String signature, String transactionID, String college, String year, String timestamp, String useremail, String date, String mailstatus, String messagestatus, String eventname) {
        this.ID = ID;
        Name = name;
        Email = email;
        Phone = phone;
        Remaining = remaining;
        Signature = signature;
        TransactionID = transactionID;
        College = college;
        Year = year;
        Timestamp = timestamp;
        Useremail = useremail;
        Date = date;
        Mailstatus = mailstatus;
        Messagestatus = messagestatus;
        Eventname = eventname;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMailstatus() {
        return Mailstatus;
    }

    public void setMailstatus(String mailstatus) {
        Mailstatus = mailstatus;
    }

    public String getMessagestatus() {
        return Messagestatus;
    }

    public void setMessagestatus(String messagestatus) {
        Messagestatus = messagestatus;
    }

    public String getEventname() {
        return Eventname;
    }

    public void setEventname(String eventname) {
        Eventname = eventname;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getRemaining() {
        return Remaining;
    }

    public void setRemaining(String remaining) {
        Remaining = remaining;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public String getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(String transactionID) {
        TransactionID = transactionID;
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getUseremail() {
        return Useremail;
    }

    public void setUseremail(String useremail) {
        Useremail = useremail;
    }
}
