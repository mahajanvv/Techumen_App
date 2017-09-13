package com.example.dontknow.techumen;

/**
 * Created by Dont know on 27-08-2017.
 */

public class TeDatabase {
    private int _id;
    private String _name;
    private String _email;
    private String _phone;
    private String _college;
    private String _year;
    private String _image;
    private String _remaining;
    private String _transaction;
    private String _eventname;
    private String _status;

    public TeDatabase()
    {}


    public TeDatabase(String _name, String _email, String _phone, String _college, String _year, String _image, String _remaining, String _transaction, String _eventname, String _status) {
        this._name = _name;
        this._email = _email;
        this._phone = _phone;
        this._college = _college;
        this._year = _year;
        this._image = _image;
        this._remaining = _remaining;
        this._transaction = _transaction;
        this._eventname = _eventname;
        this._status = _status;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public String get_college() {
        return _college;
    }

    public void set_college(String _college) {
        this._college = _college;
    }

    public String get_year() {
        return _year;
    }

    public void set_year(String _year) {
        this._year = _year;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public String get_remaining() {
        return _remaining;
    }

    public void set_remaining(String _remaining) {
        this._remaining = _remaining;
    }

    public String get_transaction() {
        return _transaction;
    }

    public void set_transaction(String _transaction) {
        this._transaction = _transaction;
    }

    public String get_eventname() {
        return _eventname;
    }

    public void set_eventname(String _eventname) {
        this._eventname = _eventname;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }
}
