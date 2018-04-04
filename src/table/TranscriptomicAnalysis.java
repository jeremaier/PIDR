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
        return id;
    }

    public int getIdCutaneousSite() {
        return idCutaneousSite;
    }

    public int getRIN() {
        return RIN;
    }

    public double getConcentration() {
        return concentration;
    }

    public double getARNc() {
        return ARNc;
    }

    public double getCyanine() {
        return cyanine;
    }

    public double getYield() {
        return yield;
    }

    public String getSpecificActivity() {
        return specificActivity;
    }

    public String getExclusionCriteria() {
        return exclusionCriteria;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public int getLamellaLocation() {
        return lamellaLocation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdCutaneousSite(int idCutaneousSite) {
        this.idCutaneousSite = idCutaneousSite;
    }

    public void setRIN(int RIN) {
        this.RIN = RIN;
    }

    public void setConcentration(double concentration) {
        this.concentration = concentration;
    }

    public void setARNc(double ARNc) {
        this.ARNc = ARNc;
    }

    public void setCyanine(double cyanine) {
        this.cyanine = cyanine;
    }

    public void setYield(double yield) {
        this.yield = yield;
    }

    public void setSpecificActivity(String specificActivity) {
        this.specificActivity = specificActivity;
    }

    public void setExclusionCriteria(String exclusionCriteria) {
        this.exclusionCriteria = exclusionCriteria;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setLamellaLocation(int lamellaLocation) {
        this.lamellaLocation = lamellaLocation;
    }
}
