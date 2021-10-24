package software.plusminus.generator.java.model;

import lombok.Data;

import java.util.List;

@Data
public class Class extends JavaFile {

    private List<String> imports;
    private List<String> annotations;
    private String superclass;
    private List<String> interfaces;
    private List<Field> fields;
    private List<Constructor> constructors;
    private List<Method> methods;

}
