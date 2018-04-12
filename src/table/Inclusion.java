package src.table;

import src.utils.FileToBlob;

import java.io.File;
import java.sql.Blob;
import java.sql.Date;

public class Inclusion {
    private int id;
    private int idPatient;
    private Blob reference1;
    private Blob reference2;
    private Date dateInclusion;
    private int numAnaPath;

    public Inclusion() {}

    public Inclusion(int id, int idPatient, File reference1, File reference2, Date dateInclusion, int numAnaPath) {
        this.id = id;
        this.idPatient = idPatient;
        this.reference1 = new FileToBlob(reference1).getBlob();
        this.reference2 = new FileToBlob(reference2).getBlob();
        this.dateInclusion = dateInclusion;
        this.numAnaPath = numAnaPath;
    }

    public int getId() {
        return id;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public Blob getReference1() {
        return reference1;
    }

    public Blob getReference2() {
        return reference2;
    }

    public Date getDateInclusion() {
        return dateInclusion;
    }

    public int getNumAnaPat() {
        return numAnaPath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public void setReference1(Blob reference) {
        this.reference1 = reference;
    }

    public void setReference2(Blob reference) {
        this.reference2 = reference;
    }

    public void setDateInclusion(Date dateInclusion) {
        this.dateInclusion = dateInclusion;
    }

    public void setNumAnaPath(int numAnaPath) {
        this.numAnaPath = numAnaPath;
    }
}
