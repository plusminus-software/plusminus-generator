package software.plusminus.generator.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import software.plusminus.generator.Generate;
import software.plusminus.generator.Generator;
import software.plusminus.generator.model.GeneratedFile;
import software.plusminus.generator.model.GeneratorAction;
import software.plusminus.generator.model.GeneratorTask;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static software.plusminus.check.Checks.check;

@RunWith(MockitoJUnitRunner.class)
public class GeneratorServiceTest {

    @Mock
    private CodeService codeService;
    @Mock
    private ApplicationContext applicationContext;
    @InjectMocks
    private GeneratorService generatorService;
    
    @Test
    public void run() {
        String code = "generatedCode";
        Path path = Paths.get("test");
        TestGenerator generator = mock(TestGenerator.class);
        GeneratedFile generatedFile = new GeneratedFile(code, path);
        GeneratorAction action = GeneratorAction.CONTENT_IS_EQUAL;
        
        when(generator.supports(TestModel.class)).thenReturn(true);
        when(generator.generate(TestModel.class)).thenReturn(generatedFile);
        when(applicationContext.getBean(TestGenerator.class)).thenReturn(generator);
        when(codeService.processCode(path, code)).thenReturn(action);
        
        List<GeneratorTask> tasks = generatorService.run(Collections.singletonList(TestModel.class));
        
        check(tasks).hasSize(1);
        GeneratorTask task = tasks.get(0);
        check(task.getSourceClass()).is((Class) TestModel.class);
        check(task.getGenerator()).is(generator);
        check(task.getFile()).is(generatedFile);
        check(task.getAction()).is(action);
        check(task.getException()).isNull();
    }
    
    private interface TestGenerator extends Generator {
    }
    
    @Generate(TestGenerator.class)
    private class TestModel {
    }
}