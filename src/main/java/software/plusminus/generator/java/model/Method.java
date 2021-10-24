package software.plusminus.generator.java.model;

import lombok.Data;

import java.util.List;

@Data
public class Method {

    private AccessLevel accessLevel;
    private String returnType;
    private String name;
    private List<Argument> arguments;
    private List<String> lines;

}
