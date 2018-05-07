package src.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public enum Diag {
    NULL(""),
    BASO("Basocellulaire"),
    BASOSUP("Basocellulaire superficiel"),
    BASONOD("Basocellulaire nodulaire"),
    BASOINF("Basocellulaire infiltrant"),
    BASOTUB("Basocellulaire tubérculaire"),
    SPINO("Spinocellulaire"),
    SPINOSUP("Spinocellulaire infiltrant"),
    SPINOKER("Spinocellulaire kératinisant"),
    SPINOBIEN("Spinocellulaire bien différencié"),
    SPINOPEU("Spinocellulaire peu différencié"),
    SPINOMOY("Spinocellulaire moyennement différencié"),
    KERATOSE("Keratose actinique"),
    KERATOSEI("Keratose grade I"),
    KERATOSEII("Keratose grade II"),
    KERATOSEIII("Keratose grade III"),
    INFL("Inflammatoire"),
    FIBROSE("Fibrose"),
    CICA("Cicatricielle"),
    MEL("Mélanome"),
    AUTRE("Autre..."),
    FICHIER("Fichier"),
    RIEN("Pas de malignité");

    private StringProperty name;

    Diag(String name) {
        this.name = new SimpleStringProperty("");
        this.setName(name);
    }

    public StringProperty getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public String toString() {
        return this.name.get();
    }
}
