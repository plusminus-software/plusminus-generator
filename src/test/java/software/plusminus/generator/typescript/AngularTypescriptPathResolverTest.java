package software.plusminus.generator.typescript;

import model.RootClass;
import org.junit.Test;
import software.plusminus.generator.java.model.TestModel;
import software.plusminus.generator.java.model.suffix.PackageWithSuffix;

import java.nio.file.Path;

import static software.plusminus.check.Checks.check;

public class AngularTypescriptPathResolverTest {
    
    private AngularTypescriptPathResolver pathResolver = new AngularTypescriptPathResolver();
    
    @Test
    public void targetPath() {
        Path actual = pathResolver.targetPath(TestModel.class, "model", "service", "MyTypescript");
        check(actual).is("angular/src/app/service/my-typescript.ts");
    }
    
    @Test
    public void targetPathWithPackageSuffix() {
        Path actual = pathResolver.targetPath(PackageWithSuffix.class, "model", "service", "MyTypescript");
        check(actual).is("angular/src/app/service/suffix/my-typescript.ts");
    }

    @Test
    public void targetPathWithoutSourceType() {
        Path actual = pathResolver.targetPath(TestModel.class, null, "service", "MyTypescript");
        check(actual).is("angular/src/app/service/my-typescript.ts");
    }
    
    @Test
    public void targetPathForRootClass() {
        Path actual = pathResolver.targetPath(RootClass.class, "model", "service", "MyTypescript");
        check(actual).is("angular/src/app/service/my-typescript.ts");
    }

}