package software.plusminus.generator.typescript.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class TypescriptClass {
    
    private List<String> imports;
    private String title;
    private String name;
    private String constructor;
    private List<String> fields;
    
}
