package src.table;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import src.utils.FileToBlob;

import java.io.File;
import java.sql.Blob;
import java.sql.Date;

public class Inclusion {
    private IntegerProperty id;
    private IntegerProperty idPatient;
    private Blob reference1;
    private Blob reference2;
    private ObjectProperty<Date> dateInclusion;
    private IntegerProperty numAnaPath;

    public Inclusion() {
        this.id = new SimpleIntegerProperty();
        this.idPatient = new SimpleIntegerProperty();
        this.dateInclusion = new SimpleObjectProperty<>();
        this.numAnaPath = new SimpleIntegerProperty();
    }

    public Inclusion(int id, int idPatient, File reference1, File reference2, Date dateInclusion, int numAnaPath) {
        this();
        this.setId(id);
        this.setIdPatient(idPatient);
        this.reference1 = new FileToBlob(reference1).getBlob();
        this.reference2 = new FileToBlob(reference2).getBlob();
        this.setDateInclusion(dateInclusion);
        this.setNumAnaPath(numAnaPath);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getIdPatient() {
        return idPatient.get();
    }

    public void setIdPatient(int idPatient) {
        this.idPatient.set(idPatient);
    }

    public Blob getReference1() {
        return reference1;
    }

    public void setReference1(Blob reference) {
        this.reference1 = reference;
    }

    public Blob getReference2() {
        return reference2;
    }

    public void setReference2(Blob reference) {
        this.reference2 = reference;
    }

    public Date getDateInclusion() {
        return dateInclusion.get();
    }

    public void setDateInclusion(Date dateInclusion) {
        this.dateInclusion.set(dateInclusion);
    }

    public int getNumAnaPat() {
        return numAnaPath.get();
    }

    public IntegerProperty idProperty() {
        return this.id;
    }

    public IntegerProperty idPatientProperty() {
        return this.idPatient;
    }

    public ObjectProperty<Date> dateInclusionProperty() {
        return this.dateInclusion;
    }

    public IntegerProperty numAnaPathProperty() {
        return this.numAnaPath;
    }

    public void setNumAnaPath(int numAnaPath) {
        this.numAnaPath.set(numAnaPath);
    }
}
