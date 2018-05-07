package src.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Inclusion {
    private StringProperty id;
    private StringProperty idPatient;
    private String reference1 = "Aucun";
    private String reference2 = "Aucun";
    private StringProperty dateInclusion;
    private StringProperty numAnaPath;
    private StringProperty diag;

    public Inclusion() {
        this.id = new SimpleStringProperty();
        this.idPatient = new SimpleStringProperty();
        this.dateInclusion = new SimpleStringProperty();
        this.numAnaPath = new SimpleStringProperty();
        this.diag = new SimpleStringProperty();
    }

    public String getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(String.format("%03d", id));
    }

    public String getIdPatient() {
        return idPatient.get();
    }

    public void setIdPatient(String idPatient) {
        this.idPatient.set(idPatient);
    }

    public String getReference1() {
        return reference1;
    }

    public void setReference1(String reference) {
        this.reference1 = reference;
    }

    public String getReference2() {
        return reference2;
    }

    public void setReference2(String reference) {
        this.reference2 = reference;
    }

    public String getDateInclusion() {
        return dateInclusion.get();
    }

    public void setDateInclusion(Date dateInclusion) {
        if(dateInclusion != null) {
            DateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
            this.dateInclusion.set(fmt.format(dateInclusion));
        }
    }

    public String getNumAnaPat() {
        return numAnaPath.get();
    }

    public StringProperty idProperty() {
        return this.id;
    }

    public StringProperty idPatientProperty() {
        return this.idPatient;
    }

    public StringProperty dateInclusionProperty() {
        return this.dateInclusion;
    }

    public StringProperty numAnaPathProperty() {
        return this.numAnaPath;
    }

    public void setNumAnaPath(String numAnaPath) {
        this.numAnaPath.set(numAnaPath);
    }

    public String getDiag() {
        return this.diag.get();
    }

    public void setDiag(String s) {
        this.diag.set(s);
    }

    public StringProperty diagProperty() {
        return this.diag;
    }
}
