package software.plusminus.generator.java;

import model.RootClass;
import org.junit.Test;
import software.plusminus.generator.java.model.TestModel;
import software.plusminus.generator.java.model.suffix.PackageWithSuffix;

import static software.plusminus.check.Checks.check;

public class StandardJavaPackageResolverTest {
    
    private StandardJavaPackageResolver packager = new StandardJavaPackageResolver(); 
    
    @Test
    public void getPackageName() {
        String actual = packager.getPackageName(TestModel.class, "model", "service");
        check(actual).is("software.plusminus.generator.java.service");
    }
    
    @Test
    public void getPackageNameWithSuffix() {
        String actual = packager.getPackageName(PackageWithSuffix.class, "model", "service");
        check(actual).is("software.plusminus.generator.java.service.suffix");
    }
    
    @Test
    public void getPackageNameForRootClass() {
        String actual = packager.getPackageName(RootClass.class, "model", "service");
        check(actual).is("service");
    }
}