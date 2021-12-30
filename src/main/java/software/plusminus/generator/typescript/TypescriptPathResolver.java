package software.plusminus.generator.typescript;

import java.nio.file.Path;

public interface TypescriptPathResolver {
    
    Path targetPath(Class<?> sourceClass, String sourceType, String targetType,
                    String className);
    
}
