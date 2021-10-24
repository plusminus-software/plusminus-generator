package software.plusminus.generator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.file.Path;

@Data
@AllArgsConstructor
public class GeneratedFile {
    
    private String code;
    private Path path;
    
}
