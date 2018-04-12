package src.table;

public class TranscriptomicAnalysis {
    private int id;
    private int idCutaneousSite;
    private int RIN;
    private double concentration;
    private double ARNc;
    private double cyanine;
    private double yield;
    private String specificActivity;
    private String exclusionCriteria;
    private int serialNumber;
    private int lamellaLocation;

    public TranscriptomicAnalysis() {
    }

    public TranscriptomicAnalysis(int id, int idCutaneousSite, int RIN, double concentration, double ARNc, double cyanine, double yield, String specificActivity, String exclusionCriteria, int serialNumber, int lamellaLocation) {
        this.id = id;
        this.idCutaneousSite = idCutaneousSite;
        this.RIN = RIN;
        this.concentration = concentration;
        this.ARNc = ARNc;
        this.cyanine = cyanine;
        this.yield = yield;
        this.specificActivity = specificActivity;
        this.exclusionCriteria = exclusionCriteria;
        this.serialNumber = serialNumber;
        this.lamellaLocation = lamellaLocation;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCutaneousSite() {
        return this.idCutaneousSite;
    }

    public void setIdCutaneousSite(int idCutaneousSite) {
        this.idCutaneousSite = idCutaneousSite;
    }

    public int getRIN() {
        return this.RIN;
    }

    public void setRIN(int RIN) {
        this.RIN = RIN;
    }

    public double getConcentration() {
        return this.concentration;
    }

    public void setConcentration(double concentration) {
        this.concentration = concentration;
    }

    public double getARNc() {
        return this.ARNc;
    }

    public void setARNc(double ARNc) {
        this.ARNc = ARNc;
    }

    public double getCyanine() {
        return this.cyanine;
    }

    public void setCyanine(double cyanine) {
        this.cyanine = cyanine;
    }

    public double getYield() {
        return this.yield;
    }

    public void setYield(double yield) {
        this.yield = yield;
    }

    public String getSpecificActivity() {
        return this.specificActivity;
    }

    public void setSpecificActivity(String specificActivity) {
        this.specificActivity = specificActivity;
    }

    public String getExclusionCriteria() {
        return this.exclusionCriteria;
    }

    public void setExclusionCriteria(String exclusionCriteria) {
        this.exclusionCriteria = exclusionCriteria;
    }

    public int getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getLamellaLocation() {
        return this.lamellaLocation;
    }

    public void setLamellaLocation(int lamellaLocation) {
        this.lamellaLocation = lamellaLocation;
    }
}
