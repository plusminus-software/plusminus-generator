package software.plusminus.generator.typescript;

import software.plusminus.generator.typescript.model.TypescriptClass;

import java.nio.file.Path;

public interface TypescriptPathResolver {
    
    Path targetPath(Class<?> sourceClass, String sourceType, String targetType,
                    TypescriptClass typescriptClass);
    
}
