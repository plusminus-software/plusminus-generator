package software.plusminus.generator.typescript;

import com.google.common.base.CaseFormat;
import org.springframework.stereotype.Component;
import software.plusminus.generator.typescript.model.TypescriptClass;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

@Component
public class AngularTypescriptPathResolver implements TypescriptPathResolver {
    
    @Override
    public Path targetPath(Class<?> sourceClass, String sourceType, @Nullable String targetType,
                           TypescriptClass typescriptClass) {
        Path path = Paths.get("angular", "src", "app");
        if (targetType != null) {
            path = path.resolve(targetType);
        }
        for (String suffix : getPathSuffixes(sourceClass, sourceType)) {
            path = path.resolve(suffix);
        }
        String filename = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, typescriptClass.name()) + ".ts";
        path = path.resolve(filename);
        return path;
    }
    
    private List<String> getPathSuffixes(Class<?> sourceClass, String sourceType) {
        String sourcePackage = sourceClass.getPackage().getName();
        List<String> packageElements = Arrays.asList(sourcePackage.split("\\."));
        int sourceTypeIndex = packageElements.indexOf(sourceType);
        if (sourceTypeIndex == -1) {
            return Collections.emptyList();
        }
        return packageElements.subList(sourceTypeIndex + 1, packageElements.size());
    }
}
