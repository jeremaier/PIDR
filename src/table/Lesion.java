package src.table;

public class Lesion {
    private int id;
    private int idInclusion;
    private String siteAnatomique;
    private Diag diag;

    public Lesion(int id, int idInclusion, String siteAnatomique, String diagnostique) {
        this.id = id;
        this.idInclusion = idInclusion;
        this.siteAnatomique = siteAnatomique;
        this.diag = Diag.valueOf(diagnostique);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdInclusion() {
        return idInclusion;
    }

    public void setIdInclusion(int idInclusion) {
        this.idInclusion = idInclusion;
    }

    public String getSiteAnatomique() {
        return siteAnatomique;
    }

    public void setSiteAnatomique(String siteAnatomique) {
        this.siteAnatomique = siteAnatomique;
    }

    public Diag getDiag() {
        return this.diag;
    }

    public void setDiag(String diag) {
        this.diag = Diag.valueOf(diag);
    }
}

