package src.table;

import java.sql.Date;

public class Patient {
    private int id;
    private String nom;
    private String genre;
    private Date dateNaissance;

    public Patient() {
    }


    public Patient(int id, String nom, String genre, Date dateBirth) {
        this.id = id;
        this.nom = nom;
        this.genre = genre;
        this.dateNaissance = dateBirth;

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Date getDateNaissance() {
        return this.dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
}
