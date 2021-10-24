package software.plusminus.generator.java;

import org.springframework.stereotype.Component;
import software.plusminus.generator.java.model.Interface;

import java.util.Arrays;
import java.util.Collections;

@Component
public class CrudRepositoryGenerator extends JavaInterfaceGenerator {

    private JavaPackageResolver packager;
    
    @Override
    public Interface generateModel(Class<?> sourceClass) {
        Interface i = new Interface();
        i.setPackageName(packager.getPackageName(sourceClass, "model", "repository"));
        i.setImports(Arrays.asList(
                "software.plusminus.data.repository.CrudRepository",
                sourceClass.getPackage().getName() + '.' + sourceClass.getSimpleName()
        ));
        i.setName(sourceClass.getSimpleName() + "Repository");
        i.setInterfaces(Collections.singletonList("CrudRepository<" + sourceClass.getSimpleName() + ", Long>"));
        return i;
    }
}
