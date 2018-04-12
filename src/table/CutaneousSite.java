package src.table;

import javafx.beans.property.*;
import src.utils.Diag;

public class CutaneousSite {
    private IntegerProperty id;
    private IntegerProperty idLesion;
    private BooleanProperty healthy;
    private IntegerProperty measurementNumber;
    private StringProperty site;
    private IntegerProperty orientation;
    private Diag diag;
    private StringProperty autreDiag;

    public CutaneousSite() {
        this.id = new SimpleIntegerProperty();
        this.idLesion = new SimpleIntegerProperty();
        this.healthy = new SimpleBooleanProperty();
        this.measurementNumber = new SimpleIntegerProperty();
        this.site = new SimpleStringProperty();
        this.orientation = new SimpleIntegerProperty();
        this.autreDiag = new SimpleStringProperty();
    }

    public CutaneousSite(int id, int idLesion, boolean healthy, int measurementNumber, String site, int orientation, String diag, String autreDiag) {
        this();
        this.setId(id);
        this.setIdLesion(idLesion);
        this.setHealthy(healthy);
        this.setMeasurementNumber(measurementNumber);
        this.setSite(site);
        this.setOrientation(orientation);
        this.setDiag(diag);
        this.setAutreDiag(autreDiag);
    }

    public int getId() {
        return this.id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getIdLesion() {
        return this.idLesion.get();
    }

    public void setIdLesion(int idLesion) {
        this.idLesion.set(idLesion);
    }

    public boolean getHealthy() {
        return this.healthy.get();
    }

    public void setHealthy(boolean healthy) {
        this.healthy.set(healthy);
    }

    public int getMeasurementNumber() {
        return this.measurementNumber.get();
    }

    public void setMeasurementNumber(int measurementNumber) {
        this.measurementNumber.set(measurementNumber);
    }

    public String getSite() {
        return this.site.get();
    }

    public void setSite(String site) {
        this.site.set(site);
    }

    public int getOrientation() {
        return this.orientation.get();
    }

    public void setOrientation(int orientation) {
        this.orientation.set(orientation);
    }

    public Diag getDiag() {
        return this.diag;
    }

    public void setDiag(String diag) {
        this.diag = Diag.valueOf(diag);
    }

    public String getAutreDiag() {
        return this.autreDiag.get();
    }

    public void setAutreDiag(String autreDiag) {
        this.autreDiag.set(autreDiag);
    }

    public IntegerProperty idProperty() {
        return this.id;
    }

    public IntegerProperty idLesionProperty() {
        return this.idLesion;
    }

    public BooleanProperty healthyProperty() {
        return this.healthy;
    }

    public IntegerProperty measurementNumberProperty() {
        return this.measurementNumber;
    }

    public StringProperty siteProperty() {
        return this.site;
    }

    public IntegerProperty orientationProperty() {
        return this.orientation;
    }

    public StringProperty diagProperty() {
        return this.diag.getName();
    }

    public StringProperty autreDiagProperty() {
        return this.autreDiag;
    }
}
