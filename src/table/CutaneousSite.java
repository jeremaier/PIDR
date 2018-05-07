package src.table;

import javafx.beans.property.*;
import src.utils.Diag;
import src.utils.SiteCutane;

public class CutaneousSite {
    private IntegerProperty id;
    private IntegerProperty idLesion;
    private SiteCutane site;
    private IntegerProperty orientation;
    private Diag diag;
    private StringProperty autreDiag;
    private StringProperty fichierDiag;
    private StringProperty spectre;

    public CutaneousSite() {
        this.id = new SimpleIntegerProperty();
        this.idLesion = new SimpleIntegerProperty();
        this.orientation = new SimpleIntegerProperty();
        this.autreDiag = new SimpleStringProperty();
        this.spectre = new SimpleStringProperty();
        this.fichierDiag = new SimpleStringProperty();
    }

    public CutaneousSite( int idLesion,String site, int orientation, String diag, String autreDiag, String fichierDiag,  String spectre) {
        this();
        this.setIdLesion(idLesion);
        this.setSite(site);
        this.setOrientation(orientation);
        this.setDiag(diag);
        this.setAutreDiag(autreDiag);
        this.setSpectre(spectre);

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
        if (site != null) {
            switch (site) {
                case "SAIN":
                    this.site = SiteCutane.SAIN;
                    break;
                case "":
                    this.site = SiteCutane.NULL;
                    break;
                case "L":
                    this.site = SiteCutane.L;
                    break;
                case "PL":
                    this.site = SiteCutane.PL;
                    break;
                case "NL":
                    this.site = SiteCutane.NL;
                    break;
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
        if (diag != null) {
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

    public IntegerProperty idProperty() {
        return this.id;
    }

    public IntegerProperty idLesionProperty() {
        return this.idLesion;
    }

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



    //public StringProperty fichierDiag(){return  this.diag;}

    public StringProperty autreDiagProperty() {
        return this.autreDiag;
    }

    public StringProperty spectreProperty(){return this.spectre; }
}
