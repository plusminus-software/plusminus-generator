package software.plusminus.generator.java.model;

import lombok.Data;

@Data
public class Field {

    private AccessLevel accessLevel;
    private String type;
    private String name;

}
