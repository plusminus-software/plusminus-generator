package software.plusminus.generator.java.model;

public enum AccessLevel {

    PUBLIC,
    PRIVATE,
    PROTECTED,
    DEFAULT;

    @Override
    public String toString() {
        if (this == DEFAULT) {
            return "";
        }
        return name().toLowerCase();
    }

}
