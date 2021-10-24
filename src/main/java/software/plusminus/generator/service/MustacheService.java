package software.plusminus.generator.service;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.plusminus.generator.exception.GeneratorException;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MustacheService {

    @Autowired
    private Mustache.TemplateLoader templateLoader;

    private Mustache.Compiler compiler;

    private Map<String, Template> templates = new HashMap<>();

    public MustacheService(Mustache.Compiler compiler) {
        this.compiler = compiler.escapeHTML(false);
    }

    public <T> String render(String templateName, T context) {
        Template template = getTemplate(templateName);
        return template.execute(context);
    }

    private Template getTemplate(String templateName) {
        if (!templates.containsKey(templateName)) {
            try (Reader reader = loadTemplate(templateName)) {
                Template template = compiler.escapeHTML(false).compile(reader);
                templates.put(templateName, template);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return templates.get(templateName);
    }

    private Reader loadTemplate(String templateName) {
        try {
            return templateLoader.getTemplate(templateName);
        } catch (Exception e) {
            throw new GeneratorException(e);
        }
    }
}
