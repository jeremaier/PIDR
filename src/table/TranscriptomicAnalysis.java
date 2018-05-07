package src.table;

import javafx.beans.property.*;

public class TranscriptomicAnalysis {
    private IntegerProperty id;
    private IntegerProperty idCutaneousSite;
    private StringProperty fichierBrut;
    private DoubleProperty RIN;
    private DoubleProperty concentration;
    private DoubleProperty ARNC;
    private DoubleProperty cyanine;
    private DoubleProperty yield;
    private DoubleProperty specificActivity;
    private StringProperty exclusionCriteria;
    private IntegerProperty serialNumber;
    private IntegerProperty lamellaLocation;
    private StringProperty qualityReport;

    public TranscriptomicAnalysis() {
        this.id = new SimpleIntegerProperty();
        this.idCutaneousSite = new SimpleIntegerProperty();
        this.fichierBrut = new SimpleStringProperty();
        this.RIN = new SimpleDoubleProperty();
        this.concentration = new SimpleDoubleProperty();
        this.ARNC = new SimpleDoubleProperty();
        this.cyanine = new SimpleDoubleProperty();
        this.yield = new SimpleDoubleProperty();
        this.specificActivity = new SimpleDoubleProperty();
        this.exclusionCriteria = new SimpleStringProperty();
        this.serialNumber = new SimpleIntegerProperty();
        this.lamellaLocation = new SimpleIntegerProperty();
        this.qualityReport = new SimpleStringProperty();
    }

    public TranscriptomicAnalysis(int id, int idCutaneousSite, String fichierBrut, double RIN, double concentration, double ARNC, double cyanine, double yield, double specificActivity, String exclusionCriteria, int serialNumber, int lamellaLocation, String qualityReport) {
        this();
        this.setId(id);
        this.setIdCutaneousSite(idCutaneousSite);
        this.setFichierBrut(fichierBrut);
        this.setRIN(RIN);
        this.setConcentration(concentration);
        this.setARNC(ARNC);
        this.setCyanine(cyanine);
        this.setYield(yield);
        this.setSpecificActivity(specificActivity);
        this.setExclusionCriteria(exclusionCriteria);
        this.setSerialNumber(serialNumber);
        this.setLamellaLocation(lamellaLocation);
        this.setQualityReport(qualityReport);
    }

    public int getId() {
        return this.id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }




    public int getIdCutaneousSite() {
        return this.idCutaneousSite.get();
    }

    public void setIdCutaneousSite(int idCutaneousSite) {
        this.idCutaneousSite.set(idCutaneousSite);
    }

    public String getFichierBrut() {
        return fichierBrut.get();
    }

    public void setFichierBrut(String fichierBrut) {
        this.fichierBrut.set(fichierBrut);
    }

    public double getRIN() {
        return this.RIN.get();
    }

    public void setRIN(double RIN) {
        this.RIN.set(RIN);
    }

    public double getConcentration() {
        return this.concentration.get();
    }

    public void setConcentration(double concentration) {
        this.concentration.set(concentration);
    }

    public double getARNC() {
        return this.ARNC.get();
    }

    public void setARNC(double ARNC) {
        this.ARNC.set(ARNC);
    }

    public double getCyanine() {
        return this.cyanine.get();
    }

    public void setCyanine(double cyanine) {
        this.cyanine.set(cyanine);
    }

    public double getYield() {
        return this.yield.get();
    }

    public void setYield(double yield) {
        this.yield.set(yield);
    }

    public double getSpecificActivity() {
        return this.specificActivity.get();
    }

    public void setSpecificActivity(double specificActivity) {
        this.specificActivity.set(specificActivity);
    }

    public String getExclusionCriteria() {
        return this.exclusionCriteria.get();
    }

    public void setExclusionCriteria(String exclusionCriteria) {
        this.exclusionCriteria.set(exclusionCriteria);
    }

    public int getSerialNumber() {
        return this.serialNumber.get();
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber.set(serialNumber);
    }

    public int getLamellaLocation() {
        return this.lamellaLocation.get();
    }

    public void setLamellaLocation(int lamellaLocation) {
        this.lamellaLocation.set(lamellaLocation);
    }

    public void setQualityReport(String qualityReport){
        this.qualityReport.set(qualityReport);
    }

    public String getQualityReport(){
       return this.qualityReport.get();
    }
}
