package src.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public enum SiteCutane {
    NULL(""),
    SAIN("Sain"),
    MAIN("Main"),
    POIGNET("Poignet"),
    L("L"),
    PL("PL"),
    NL("NL");

    private SimpleStringProperty name;

    SiteCutane(String name){
        this.name = new SimpleStringProperty("");
        this.setName(name);

    }

    public StringProperty getName(){ return this.name;}

    public void setName(String name) {this.name.set(name);}

    @Override
    public String toString() {return this.name.get();}
}
