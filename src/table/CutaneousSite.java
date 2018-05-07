package src.table;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.utils.Diag;
import src.utils.SiteCutane;

import java.util.ArrayList;
import java.util.Arrays;

public class CutaneousSite {
    private IntegerProperty id;
    private IntegerProperty idLesion;
    private SiteCutane site;
    private IntegerProperty orientation;
    private Diag diag;
    private StringProperty autreDiag;
    private StringProperty fichierDiag;
    private StringProperty spectre;
    private String fichierMoy;
    private String fileDiag;

    public CutaneousSite() {
        this.id = new SimpleIntegerProperty();
        this.idLesion = new SimpleIntegerProperty();
        this.orientation = new SimpleIntegerProperty();
        this.autreDiag = new SimpleStringProperty();
        this.spectre = new SimpleStringProperty();
        this.fichierDiag = new SimpleStringProperty();
    }

    public CutaneousSite(int idLesion, String site, int orientation, String diag, String autreDiag, String fileDiag, String fichierMoy, String spectre) {
        this();
        this.setFichierDiag(fileDiag);
        this.setIdLesion(idLesion);
        this.setSite(site);
        this.setOrientation(orientation);
        this.setDiag(diag);
        this.setAutreDiag(autreDiag);
        this.setSpectre(spectre);
        this.setFichierMoy(fichierMoy);
    }

    public int getId() {
        return this.id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getIdLesion() {
        return this.idLesion.get();
    }

    public void setIdLesion(int idLesion) {
        this.idLesion.set(idLesion);
    }

    public SiteCutane getSite() {
        return this.site;
    }

    public void setSite(SiteCutane site) {
        this.site = site;
    }

    public void setSite(String site) {
        ObservableList<SiteCutane> diags = FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(SiteCutane.values())));

        if (diag != null) {
            for (SiteCutane siteValue : diags) {
                if (siteValue.toString().equals(site)) {
                    this.site = siteValue;
                    break;
                }
            }
        }
    }

    public int getOrientation() {
        return this.orientation.get();
    }

    public void setOrientation(int orientation) {
        this.orientation.set(orientation);
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

    public String getFichierDiag(){ return this.fichierDiag.get();}

    public void setFichierDiag(String fichierDiag){ this.fichierDiag.set(fichierDiag);}

    public String getSpectre() { return this.spectre.get();}

    public void setSpectre(String spectre) { this.spectre.set(spectre);}

    public StringProperty siteProperty() {
        if (this.site != null)
            return this.site.getName();
        else return new SimpleStringProperty("");
    }

    public IntegerProperty orientationProperty() {
        return this.orientation;
    }

    public StringProperty diagProperty() {
        if (this.diag != null)
            return this.diag.getName();
        else return new SimpleStringProperty("");
    }

    public String getFichierMoy() {
        return this.fichierMoy;
    }

    public void setFichierMoy(String fichierMoy) {
        this.fichierMoy = fichierMoy;
    }

    public StringProperty autreDiagProperty() {
        return this.autreDiag;
    }

    public String getFileDiag() {
        return fileDiag;
    }

    public void setFileDiag(String fileDiag) {
        this.fileDiag = fileDiag;
    }
}
