package src.table;

import java.io.File;
import java.sql.Date;

public class Inclusion {
    private int id;
    private int idPatient;
    private File reference1;
    private File reference2;
    private Date dateInclusion;
    private int numAnaPat;

    public Inclusion(int id, int idPatient, File reference1, File reference2, Date dateInclusion, int numAnaPat) {
        this.id = id;
        this.idPatient = idPatient;
        this.reference1 = reference1;
        this.reference2 = reference2;
        this.dateInclusion = dateInclusion;
        this.numAnaPat = numAnaPat;
    }

    public int getId() {
        return id;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public File getReference1() {
        return reference1;
    }

    public File getReference2() {
        return reference2;
    }

    public Date getDateInclusion() {
        return dateInclusion;
    }

    public int getNumAnaPat() {
        return numAnaPat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public void setReference1(File reference) {
        this.reference1 = reference;
    }

    public void setReference2(File reference) {
        this.reference2 = reference;
    }

    public void setDateInclusion(Date dateInclusion) {
        this.dateInclusion = dateInclusion;
    }

    public void setNumAnaPat(int numAnaPat) {
        this.numAnaPat = numAnaPat;
    }
}
