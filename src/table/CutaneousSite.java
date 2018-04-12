package src.table;

import src.utils.Diag;

public class CutaneousSite {
    private int id;
    private int idLesion;
    private boolean healthy;
    private int measurementNumber;
    private String site;
    private int orientation;
    private Diag diag;

    public CutaneousSite() {}

    public CutaneousSite(int id, int idLesion, boolean healthy, int measurementNumber, String site, int orientation, Diag diag) {
        this.id = id;
        this.idLesion = idLesion;
        this.healthy = healthy;
        this.measurementNumber = measurementNumber;
        this.site = site;
        this.orientation = orientation;
        this.diag = diag;
    }

    public int getId() {
        return id;
    }

    public int getIdLesion() {
        return idLesion;
    }

    public boolean getHealthy() {
        return this.healthy;
    }

    public int getMeasurementNumber() {
        return measurementNumber;
    }

    public String getSite() {
        return site;
    }

    public int getOrientation() {
        return orientation;
    }

    public Diag getDiag() {
        return diag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdLesion(int idLesion) {
        this.idLesion = idLesion;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public void setMeasurementNumber(int measurementNumber) {
        this.measurementNumber = measurementNumber;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setDiag(Diag diag) {
        this.diag = diag;
    }
}
