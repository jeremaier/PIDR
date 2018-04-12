package src.utils;

public enum Gender {
    M("Homme"),
    F("Femme");

    private String name;

    Gender(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
