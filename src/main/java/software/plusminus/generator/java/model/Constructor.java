package software.plusminus.generator.java.model;

import lombok.Data;

import java.util.List;

@Data
public class Constructor {

    private AccessLevel accessLevel;
    private List<Argument> arguments;
    private List<String> lines;

}
