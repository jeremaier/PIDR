package src.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import src.utils.Diag;

public class Lesion {
    private int id;
    private int idInclusion;
    private String photoSur = "Aucun";
    private String photoHors = "Aucun";
    private String photoFixe = "Aucun";
    private StringProperty siteAnatomique;
    private Diag diag;
    private String autreDiag = "Aucun";
    private String fichierMoy = "Aucun";

    public Lesion() {
        this.siteAnatomique = new SimpleStringProperty();
    }

    public Lesion(int id, int idInclusion, String photoSur, String photoHors, String photoFixe, String siteAnatomique, String diag, String autreDiag, String fichierMoy) {
        this();
        this.setId(id);
        this.setIdInclusion(idInclusion);
        this.photoSur = photoSur;
        this.photoHors = photoHors;
        this.photoFixe = photoFixe;
        this.setSiteAnatomique(siteAnatomique);
        this.diag = Diag.valueOf(diag);
        this.setAutreDiag(autreDiag);
        this.fichierMoy=fichierMoy;
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

    public String getPhotoSur() {
        return photoSur;
    }

    public void setPhotoSur(String photoSur) {
        this.photoSur = photoSur;
    }

    public String getPhotoHors() {
        return photoHors;
    }

    public void setPhotoHors(String photoHors) {
        this.photoHors = photoHors;
    }

    public String getPhotoFixe() {
        return photoFixe;
    }

    public void setPhotoFixe(String photoFixe) {
        this.photoFixe = photoFixe;
    }

    public String getSiteAnatomique() {
        return siteAnatomique.get();
    }

    public void setSiteAnatomique(String siteAnatomique) {
        this.siteAnatomique.set(siteAnatomique);
    }

    public Diag getDiag() {
        return this.diag;
    }

    public void setDiag(String diag) {
        switch (diag) {
            case "Basocellulaire":
                this.diag = Diag.BASO;
                break;
            case "Spinocellulaire":
                this.diag = Diag.SPINO;
                break;
            case "Keratose actinique":
                this.diag = Diag.KERATOSE;
                break;
            case "Autre...":
                this.diag = Diag.AUTRE;
                break;
            case "Fichier":
                this.diag = Diag.FICHIER;
                break;
            case "Pas de malignité":
                this.diag = Diag.RIEN;
                break;
        }
    }

    public String getAutreDiag() {
        return this.autreDiag;
    }

    public void setAutreDiag(String autreDiag) {
        this.autreDiag = autreDiag;
    }

    public String getFichierMoy(){ return this.fichierMoy;    }

    public void setFichierMoy(String fichierMoy) {
        this.fichierMoy = fichierMoy;
    }

    public StringProperty siteAnatomiqueProperty() {
        return this.siteAnatomique;
    }

    public StringProperty diagProperty() {
        if (this.diag != null)
            return this.diag.getName();
        else return new SimpleStringProperty("");
    }
}

