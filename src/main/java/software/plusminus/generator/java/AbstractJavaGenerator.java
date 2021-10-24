package software.plusminus.generator.java;

import software.plusminus.generator.TemplateGenerator;
import software.plusminus.generator.java.model.JavaFile;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractJavaGenerator<T extends JavaFile> extends TemplateGenerator<T> {

    @Override
    protected Path targetPath(Class<?> sourceClass, T context) {
        Path path = Paths.get("src", "main", "java");
        if (context.getPackageName() != null) {
            for (String packageFolder : context.getPackageName().split("\\.")) {
                path = path.resolve(packageFolder);
            }
        }
        return path.resolve(context.getName() + ".java");
    }

}
