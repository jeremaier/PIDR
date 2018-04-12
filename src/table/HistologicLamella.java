package src.table;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HistologicLamella {
    private IntegerProperty id;
    private IntegerProperty idLesion;
    private StringProperty siteCoupe;
    private IntegerProperty orientationNoir;
    private IntegerProperty orientationVert;
    private StringProperty coloration;

    public HistologicLamella() {
        this.id = new SimpleIntegerProperty();
        this.idLesion = new SimpleIntegerProperty();
        this.siteCoupe = new SimpleStringProperty();
        this.orientationNoir = new SimpleIntegerProperty();
        this.orientationVert = new SimpleIntegerProperty();
        this.coloration = new SimpleStringProperty();
    }

    public HistologicLamella(int id, int idLesion, String siteCoupe, int orientationNoir, int orientationVert, String coloration) {
        this();
        this.setId(id);
        this.setIdLesion(idLesion);
        this.setSiteCoupe(siteCoupe);
        this.setOrientationNoir(orientationNoir);
        this.setOrientationVert(orientationVert);
        this.setColoration(coloration);
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

    public String getSiteCoupe() {
        return this.siteCoupe.get();
    }

    public void setSiteCoupe(String siteCoupe) {
        this.siteCoupe.set(siteCoupe);
    }

    public int getOrientationNoir() {
        return this.orientationNoir.get();
    }

    public void setOrientationNoir(int orientationNoir) {
        this.orientationNoir.set(orientationNoir);
    }

    public int getOrientationVert() {
        return this.orientationVert.get();
    }

    public void setOrientationVert(int orientationVert) {
        this.orientationVert.set(orientationVert);
    }

    public String getColoration() {
        return this.coloration.get();
    }

    public void setColoration(String coloration) {
        this.coloration.set(coloration);
    }

    public IntegerProperty idProperty() {
        return this.id;
    }

    public IntegerProperty idLesionProperty() {
        return this.idLesion;
    }

    public StringProperty siteCoupeProperty() {
        return this.siteCoupe;
    }

    public IntegerProperty orientationNoirProperty() {
        return this.orientationNoir;
    }

    public IntegerProperty orientationVertProperty() {
        return this.orientationVert;
    }

    public StringProperty colorationProperty() {
        return this.coloration;
    }
}
