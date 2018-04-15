package src.table;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import src.utils.Diag;

public class Lesion {
    private IntegerProperty id;
    private IntegerProperty idInclusion;
    private String photoSur;
    private String photoHors;
    private String photoFixe;
    private StringProperty siteAnatomique;
    private Diag diag;
    private StringProperty autreDiag;

    public Lesion() {
        this.id = new SimpleIntegerProperty();
        this.idInclusion = new SimpleIntegerProperty();
        this.siteAnatomique = new SimpleStringProperty();
        this.autreDiag = new SimpleStringProperty();
    }

    public Lesion(int id, int idInclusion, String photoSur, String photoHors, String photoFixe, String siteAnatomique, String diag, String autreDiag) {
        this();
        this.setId(id);
        this.setIdInclusion(idInclusion);
        this.photoSur = photoSur;
        this.photoHors = photoHors;
        this.photoFixe = photoFixe;
        this.setSiteAnatomique(siteAnatomique);
        this.diag = Diag.valueOf(diag);
        this.setAutreDiag(autreDiag);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getIdInclusion() {
        return idInclusion.get();
    }

    public void setIdInclusion(int idInclusion) {
        this.idInclusion.set(idInclusion);
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

    public IntegerProperty idInclusionProperty() {
        return this.idInclusion;
    }

    public StringProperty siteAnatomiqueProperty() {
        return this.siteAnatomique;
    }

    public StringProperty diagProperty() {
        return this.diag.getName();
    }

    public StringProperty autreDiagProperty() {
        return this.autreDiag;
    }
}

