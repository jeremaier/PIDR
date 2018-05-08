package src.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.utils.Diag;

import java.util.ArrayList;
import java.util.Arrays;

public class Lesion {
    private int id;
    private int idInclusion;
    private String photoSur = "Aucun";
    private String photoHors = "Aucun";
    private String photoFixe = "Aucun";
    private StringProperty siteAnatomique;
    private Diag diag;
    private StringProperty autreDiag;
    private String fileDiag = "Aucun";

    public Lesion() {
        this.siteAnatomique = new SimpleStringProperty();
        this.autreDiag = new SimpleStringProperty();
    }

    public Lesion(int id, int idInclusion, String photoSur, String photoHors, String photoFixe, String siteAnatomique, String diag, String autreDiag, String fileDiag) {
        this();
        this.setId(id);
        this.setIdInclusion(idInclusion);
        this.setPhotoSur(photoSur);
        this.setPhotoHors(photoHors);
        this.setPhotoFixe(photoFixe);
        this.setSiteAnatomique(siteAnatomique);
        this.setDiag(Diag.valueOf(diag));
        this.setAutreDiag(autreDiag);
        this.setFileDiag(fileDiag);
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

    public void setDiag(Diag diag) {
        this.diag = diag;
    }

    public void setDiag(String diag) {
        ObservableList<Diag> diags = FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(Diag.values())));

        if (diag != null) {
            for (Diag diagValue : diags) {
                if (diagValue.toString().equals(diag)) {
                    this.diag = diagValue;
                    break;
                }
            }
        }
    }

    public String getAutreDiag() {
        return this.autreDiag.get();
    }

    public void setAutreDiag(String autreDiag) {
        this.autreDiag.set(autreDiag);
    }

    public StringProperty autreDiagProperty() {
        return this.autreDiag;
    }

    public String getFileDiag() {
        return this.fileDiag;
    }

    public void setFileDiag(String fileDiag) {
        this.fileDiag = fileDiag;
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

