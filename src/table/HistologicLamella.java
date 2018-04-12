package src.table;

public class HistologicLamella {
    private int id;
    private int idLesion;
    private char cuttingSite;
    private int blackOrientation;
    private int greenOrientation;
    private String coloration;

    public HistologicLamella() {}

    public HistologicLamella(int id, int idLesion, char cuttingSite, int blackOrientation, int greenOrientation, String coloration) {
        this.id = id;
        this.idLesion = idLesion;
        this.cuttingSite = cuttingSite;
        this.blackOrientation = blackOrientation;
        this.greenOrientation = greenOrientation;
        this.coloration = coloration;
    }

    public int getId() {
        return id;
    }

    public int getIdLesion() {
        return idLesion;
    }

    public char getCuttingSite() {
        return cuttingSite;
    }

    public int getBlackOrientation() {
        return blackOrientation;
    }

    public int getGreenOrientation() {
        return greenOrientation;
    }

    public String getColoration() {
        return coloration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdLesion(int idLesion) {
        this.idLesion = idLesion;
    }

    public void setCuttingSite(char cuttingSite) {
        this.cuttingSite = cuttingSite;
    }

    public void setBlackOrientation(int blackOrientation) {
        this.blackOrientation = blackOrientation;
    }

    public void setGreenOrientation(int greenOrientation) {
        this.greenOrientation = greenOrientation;
    }

    public void setColoration(String coloration) {
        this.coloration = coloration;
    }
}
