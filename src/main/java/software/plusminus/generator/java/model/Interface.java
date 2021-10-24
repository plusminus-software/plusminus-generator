package software.plusminus.generator.java.model;

import lombok.Data;

import java.util.List;

@Data
public class Interface extends JavaFile {

    private List<String> imports;
    private List<String> annotations;
    private List<String> interfaces;
    private List<Method> methods;

}
