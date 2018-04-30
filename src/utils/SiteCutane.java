package src.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public enum SiteCutane {
    L("Site lesionnel"),
    PL("Site Perilesionnel"),
    NL("Site Non lésionnel");

    private SimpleStringProperty name;

    SiteCutane(String name){
        this.name= new SimpleStringProperty();

    }

    public StringProperty getName(){ return this.name;}

    public void setName(String name) {this.name.set(name);}


    @Override
    public String toString() {return this.name.get();}

}
