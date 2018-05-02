package src.table;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {
    private IntegerProperty id;
    private StringProperty genre;
    private IntegerProperty anneeDeNaissance;

    public Patient() {
        this.id = new SimpleIntegerProperty();
        this.genre = new SimpleStringProperty();
        this.anneeDeNaissance = new SimpleIntegerProperty();
    }

    public Patient(int id, String genre, int anneeDeNaissance) {
        this();
        this.setId(id);
        this.setGenre(genre);
        this.setDateNaissance(anneeDeNaissance);
    }

    public int getId() {
        return this.id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getGenre() {
        return this.genre.get();
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public int getAnneeNaissance() {
        return this.anneeDeNaissance.get();
    }

    public IntegerProperty idProperty() {
        return this.id;
    }

    public StringProperty genreProperty() {
        return this.genre;
    }

    public IntegerProperty dateNaissanceProperty() {
        return this.anneeDeNaissance;
    }

    public void setDateNaissance(int dateNaissance) {
        this.anneeDeNaissance.set(dateNaissance);
    }
}
