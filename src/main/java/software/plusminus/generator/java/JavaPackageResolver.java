package software.plusminus.generator.java;

public interface JavaPackageResolver {
    
    String getPackageName(Class<?> sourceClass, String sourceType, String targetType);
    
}
