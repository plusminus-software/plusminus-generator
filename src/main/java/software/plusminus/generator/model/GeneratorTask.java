package software.plusminus.generator.model;

import lombok.Data;
import software.plusminus.generator.Generator;

import javax.annotation.Nullable;

@Data
public class GeneratorTask {
    
    private Class<?> sourceClass;
    private Generator generator;
    private GeneratedFile file;
    private GeneratorAction action;
    @Nullable
    private Exception exception;
    
}
