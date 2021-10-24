package software.plusminus.generator;

import software.plusminus.generator.model.GeneratedFile;
import software.plusminus.generator.service.MustacheService;

import java.nio.file.Path;

public abstract class TemplateGenerator<T> implements Generator {
    
    private MustacheService mustacheService;

    @Override
    public GeneratedFile generate(Class<?> sourceClass) {
        T context = generateModel(sourceClass);
        String code = mustacheService.render(templateName(), context);
        Path targetPath = targetPath(sourceClass, context);

        return new GeneratedFile(code, targetPath);
    }

    protected abstract T generateModel(Class<?> sourceClass);
    
    protected abstract Path targetPath(Class<?> sourceClass, T context);
    
    protected abstract String templateName();
    
}
