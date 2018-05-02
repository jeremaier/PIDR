package src.utils;

public enum Genre {
    NULL(""),
    M("Homme"),
    F("Femme");

    private String name;

    Genre(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
