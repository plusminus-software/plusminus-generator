package software.plusminus.generator;

import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.file.Path;

@Data
@Accessors(fluent = true)
public class GeneratorTestItem {
    
    private Class<?> sourceClass;
    private Path expectedPath;
    private String expectedCode;
    
}
