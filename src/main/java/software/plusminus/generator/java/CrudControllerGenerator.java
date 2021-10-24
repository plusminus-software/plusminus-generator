package software.plusminus.generator.java;

import com.google.common.base.CaseFormat;
import org.atteo.evo.inflector.English;
import org.springframework.stereotype.Component;
import software.plusminus.generator.java.model.Class;

import java.util.Arrays;

@Component
public class CrudControllerGenerator extends JavaClassGenerator {
    
    private JavaPackageResolver packager;
    
    @Override
    public Class generateModel(java.lang.Class<?> sourceClass) {
        Class c = new Class();
        c.setPackageName(packager.getPackageName(sourceClass, "model",  "controller"));
        c.setImports(Arrays.asList(
                "software.plusminus.data.controller.CrudController",
                sourceClass.getPackage().getName() + '.' + sourceClass.getSimpleName(),
                "org.springframework.web.bind.annotation.RequestMapping",
                "org.springframework.web.bind.annotation.RestController"
        ));
        c.setAnnotations(Arrays.asList(
                "@RestController",
                "@RequestMapping(\"" + getUri(sourceClass) + "\")"
        ));
        c.setName(sourceClass.getSimpleName() + "Controller");
        c.setSuperclass("CrudController<" + sourceClass.getSimpleName() + ", Long>");
        return c;
    }

    private String getUri(java.lang.Class<?> clazz) {
        String plural = English.plural(clazz.getSimpleName());
        String hyphen = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, plural);
        return "/api/" + hyphen;
    }
}
