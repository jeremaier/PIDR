package src.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public enum Diag {
    BASO("Basocellulaire"),
    SPINO("Spinocellulaire"),
    KERATOSE("Keratose actinique"),
    AUTRE("Autre..."),
    FICHIER("Fichier"),
    RIEN("Pas de malignité");

    private StringProperty name;

    Diag(String name) {
        this.name = new SimpleStringProperty();
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
