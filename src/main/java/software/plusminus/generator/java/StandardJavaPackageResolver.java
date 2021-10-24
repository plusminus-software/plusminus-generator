package software.plusminus.generator.java;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class StandardJavaPackageResolver implements JavaPackageResolver {
    
    @Override
    public String getPackageName(Class<?> sourceClass, String sourceType, String targetType) {
        String sourcePackage = sourceClass.getPackage().getName();
        return replaceSourceTypeWithTargetType(sourcePackage, sourceType, targetType);
    }
    
    private String replaceSourceTypeWithTargetType(String packageName, String sourceType, String targetType) {
        List<String> packageElements = Arrays.asList(packageName.split("\\."));
        int sourceTypeIndex = packageElements.indexOf(sourceType);
        if (sourceTypeIndex == -1) {
            return packageName + '.' + targetType;
        }

        packageElements.set(sourceTypeIndex, targetType);
        return String.join(".", packageElements);
    }
}
