package software.plusminus.generator.java;

import org.springframework.stereotype.Component;
import software.plusminus.generator.java.model.Class;

import java.util.Arrays;
import java.util.Collections;

@Component
public class EntityCrudServiceGenerator extends JavaClassGenerator {

    private JavaPackageResolver packager;
    
    @Override
    public Class generateModel(java.lang.Class<?> sourceClass) {
        Class c = new Class();
        c.setPackageName(packager.getPackageName(sourceClass, "model", "service"));
        c.setImports(Arrays.asList(
                "software.plusminus.data.service.crud.EntityCrudService",
                sourceClass.getPackage().getName() + '.' + sourceClass.getSimpleName(),
                "org.springframework.web.bind.annotation.Service"
        ));
        c.setAnnotations(Collections.singletonList("@Service"));
        c.setName(sourceClass.getSimpleName() + "Service");
        c.setSuperclass("EntityCrudService<" + sourceClass.getSimpleName() + ", Long>");
        return c;
    }
}
