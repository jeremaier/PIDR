package src.table;

import java.io.File;
import java.sql.Date;

public class Inclusion {
    private int id;
    private int idPatient;
    private File teflon;
    private Date dateInclusion;
    private int numAnaPat;

    public Inclusion() {}

    public Inclusion(int idPatient, File teflon, Date dateInclusion, int numAnaPat) {
        this.idPatient = idPatient;
        this.teflon = teflon;
        this.dateInclusion = dateInclusion;
        this.numAnaPat = numAnaPat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public File getTeflon() {
        return teflon;
    }

    public void setTeflon(File teflon) {
        this.teflon = teflon;
    }

    public Date getDateInclusion() {
        return dateInclusion;
    }

    public void setDateInclusion(Date dateInclusion) {
        this.dateInclusion = dateInclusion;
    }

    public int getNumAnaPat() {
        return numAnaPat;
    }

    public void setNumAnaPat(int numAnaPat) {
        this.numAnaPat = numAnaPat;
    }
}
