package src.table;

import src.utils.Diag;

import java.io.File;

public class Lesion {
    private int id;
    private int idInclusion;
    private File photoSur;
    private File photoHors;
    private File photoFixe;
    private String anatomicalSite;
    private Diag diag;

    public Lesion() {}

    public Lesion(int id, int idInclusion, File photoSur, File photoHors, File photoFixe, String anatomicalSite, String diagnostic) {
        this.id = id;
        this.idInclusion = idInclusion;
        this.photoSur = photoSur;
        this.photoHors = photoHors;
        this.photoFixe = photoFixe;
        this.anatomicalSite = anatomicalSite;
        this.diag = Diag.valueOf(diagnostic);
    }

    public int getId() {
        return id;
    }

    public int getIdInclusion() {
        return idInclusion;
    }

    public File getPhotoSur() {
        return photoSur;
    }

    public File getPhotoHors() {
        return photoHors;
    }

    public File getPhotoFixe() {
        return photoFixe;
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

    public void setPhotoSur(File photoSur) {
        this.photoSur = photoSur;
    }

    public void setPhotoHors(File photoHors) {
        this.photoHors = photoHors;
    }

    public void setPhotoFixe(File photoFixe) {
        this.photoFixe = photoFixe;
    }

    public void setAnatomicalSite(String anatomicalSite) {
        this.anatomicalSite = anatomicalSite;
    }

    public void setDiag(String diag) {
        this.diag = Diag.valueOf(diag);
    }
}

