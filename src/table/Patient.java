package src.table;

import java.sql.Date;

public class Patient {
    private int id;
    private String initials;
    private String gender;
    private Date birthDate;

    public Patient() {}

    public Patient(int id, String initials, String gender, Date birthDate) {
        this.id = id;
        this.initials = initials;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public int getId() {
        return this.id;
    }

    public String getGender() {
        return this.gender;
    }

    public String getInitials() {
        return this.initials;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
