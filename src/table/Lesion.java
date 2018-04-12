package src.table;

import src.utils.Diag;
import src.utils.FileToBlob;

import java.io.File;
import java.sql.Blob;

public class Lesion {
    private int id;
    private int idInclusion;
    private Blob photoSur;
    private Blob photoHors;
    private Blob photoFixe;
    private String siteAnatomique;
    private Diag diag;
    private String autreDiag;

    public Lesion() {}

    public Lesion(int id, int idInclusion, File photoSur, File photoHors, File photoFixe, String siteAnatomique, String diag, String autreDiag) {
        this.id = id;
        this.idInclusion = idInclusion;
        this.photoSur = new FileToBlob(photoSur).getBlob();
        this.photoHors = new FileToBlob(photoHors).getBlob();
        this.photoFixe = new FileToBlob(photoFixe).getBlob();
        this.siteAnatomique = siteAnatomique;
        this.diag = Diag.valueOf(diag);
        this.autreDiag = autreDiag;
    }

    public int getId() {
        return id;
    }

    public int getIdInclusion() {
        return idInclusion;
    }

    public Blob getPhotoSur() {
        return photoSur;
    }

    public Blob getPhotoHors() {
        return photoHors;
    }

    public Blob getPhotoFixe() {
        return photoFixe;
    }

    public String getSiteAnatomique() {
        return siteAnatomique;
    }

    public Diag getDiag() {
        return this.diag;
    }

    public String getAutreDiag() {
        return this.autreDiag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdInclusion(int idInclusion) {
        this.idInclusion = idInclusion;
    }

    public void setPhotoSur(Blob photoSur) {
        this.photoSur = photoSur;
    }

    public void setPhotoHors(Blob photoHors) {
        this.photoHors = photoHors;
    }

    public void setPhotoFixe(Blob photoFixe) {
        this.photoFixe = photoFixe;
    }

    public void setSiteAnatomique(String siteAnatomique) {
        this.siteAnatomique = siteAnatomique;
    }

    public void setDiag(String diag) {
        this.diag = Diag.valueOf(diag);
    }

    public void setAutreDiag(String autreDiag) {
        this.autreDiag = autreDiag;
    }
}

