package src.table;

import javafx.beans.property.*;
import src.utils.Diag;
import src.utils.SiteCutane;

public class CutaneousSite {
    private IntegerProperty id;
    private IntegerProperty idLesion;
    private IntegerProperty healthy;
    private StringProperty site;
    private IntegerProperty orientation;
    private StringProperty diag;
    private StringProperty autreDiag;
    private StringProperty fichierDiag;
    private StringProperty spectre;

    public CutaneousSite() {
        this.id = new SimpleIntegerProperty();
        this.idLesion = new SimpleIntegerProperty();
        this.healthy = new SimpleIntegerProperty();
        this.site = new SimpleStringProperty();
        this.orientation = new SimpleIntegerProperty();
        this.diag = new SimpleStringProperty();
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

    public String getSite() {
        return this.site.get();
    }

    public void setSite(String site) {
        this.site.set(site);
    }

    public int getOrientation() {
        return this.orientation.get();
    }

    public void setOrientation(int orientation) {
        this.orientation.set(orientation);
    }

    public String getDiag() {
        return this.diag.get();
    }

    public void setDiag(String diag) {
        this.diag.set(diag);
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
        return this.site;
    }

    public IntegerProperty orientationProperty() {
        return this.orientation;
    }

    public StringProperty diagProperty() {
        return this.diag;
    }

    public StringProperty fichierDiag(){return  this.diag;}

    public StringProperty autreDiagProperty() {
        return this.autreDiag;
    }

    public StringProperty spectreProperty(){return this.spectre; }
}
