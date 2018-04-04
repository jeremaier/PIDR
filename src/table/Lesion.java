package src.table;

import src.util.Diag;

public class Lesion {
    private int id;
    private int idInclusion;
    private String anatomicalSite;
    private Diag diag;

    public Lesion(int id, int idInclusion, String anatomicalSite, String diagnostic) {
        this.id = id;
        this.idInclusion = idInclusion;
        this.anatomicalSite = anatomicalSite;
        this.diag = Diag.valueOf(diagnostic);
    }

    public int getId() {
        return id;
    }

    public int getIdInclusion() {
        return idInclusion;
    }

    public String getAnatomicalSite() {
        return anatomicalSite;
    }

    public Diag getDiag() {
        return this.diag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdInclusion(int idInclusion) {
        this.idInclusion = idInclusion;
    }

    public void setAnatomicalSite(String anatomicalSite) {
        this.anatomicalSite = anatomicalSite;
    }

    public void setDiag(String diag) {
        this.diag = Diag.valueOf(diag);
    }
}

