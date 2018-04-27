package src.table;

import javafx.beans.property.*;

import java.sql.Date;

public class Inclusion {
    private StringProperty id;
    private IntegerProperty idPatient;
    private StringProperty initialesPatient;
    private String reference1 = "Aucun";
    private String reference2 = "Aucun";
    private ObjectProperty<Date> dateInclusion;
    private IntegerProperty numAnaPath;
    private StringProperty diag;

    public Inclusion() {
        this.id = new SimpleStringProperty();
        this.idPatient = new SimpleIntegerProperty();
        this.dateInclusion = new SimpleObjectProperty<>();
        this.numAnaPath = new SimpleIntegerProperty();
        this.diag = new SimpleStringProperty();
    }

    public Inclusion(int id, int idPatient, String initialesPatient, String reference1, String reference2, Date dateInclusion, int numAnaPath, String diag) {
        this();
        this.setId(id);
        this.setIdPatient(idPatient);
        this.setInitialesPatient(initialesPatient);
        this.reference1 = reference1;
        this.reference2 = reference2;
        this.setDateInclusion(dateInclusion);
        this.setNumAnaPath(numAnaPath);
        this.setDiag(diag);
    }

    public String getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(String.format("%03d", id));
    }

    public int getIdPatient() {
        return idPatient.get();
    }

    public void setIdPatient(int idPatient) {
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

    public Date getDateInclusion() {
        return dateInclusion.get();
    }

    public void setDateInclusion(Date dateInclusion) {
        this.dateInclusion.set(dateInclusion);
    }

    public int getNumAnaPat() {
        return numAnaPath.get();
    }

    public StringProperty idProperty() {
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

    public String getDiag() {
        return diag.get();
    }

    public StringProperty diagProperty() {
        return diag;
    }

    public void setDiag(String diag) {
        this.diag.set(diag);
    }

    public String getInitialesPatient() {
        return initialesPatient.get();
    }

    public StringProperty initialesPatientProperty() {
        return initialesPatient;
    }

    public void setInitialesPatient(String initialesPatient) {
        this.initialesPatient.set(initialesPatient);
    }
}
