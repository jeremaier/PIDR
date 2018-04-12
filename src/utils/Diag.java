package src.utils;

public enum Diag {
    BASO("Basocellulaire"),
    SPINO("Spinocellulaire"),
    KERATOSE("Keratose actinique"),
    AUTRE("Autre..."),
    RIEN("Pas de malignité");

    private String name;

    Diag(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
