package software.plusminus.generator;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.support.GenericWebApplicationContext;
import software.plusminus.generator.model.GeneratedFile;
import software.plusminus.generator.model.GeneratorAction;
import software.plusminus.generator.model.GeneratorTask;
import software.plusminus.generator.service.GeneratorService;
import software.plusminus.util.FileUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static software.plusminus.check.Checks.check;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class GeneratorTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private GenericWebApplicationContext applicationContext;

    @Before
    public void before() {
        Map<Generator, List<GeneratorTestItem>> testItems = testItems().stream()
                .collect(Collectors.groupingBy(this::getSpiedGenerator));
        testItems.forEach((generator, items) -> {
            doAnswer(answer -> {
                GeneratedFile generatedFile = (GeneratedFile) answer.callRealMethod();
                GeneratorTestItem item = items.stream()
                        .filter(i -> i.expectedPath().equals(generatedFile.getPath()))
                        .findFirst()
                        .orElseThrow(() -> new AssertionError("Unknown path " + generatedFile.getPath()));
                generatedFile.setPath(pathInTemporaryFolder(item.expectedPath()));
                return generatedFile;
            }).when(generator).generate(any());
        });
    }
    
    @After
    public void after() {
        temporaryFolder.delete();
    }
    
    @Test
    public void checkTask() {
        List<GeneratorTestItem> testItems = testItems();
        List<GeneratorTask> tasks = generate(testItems);
        
        for (int i = 0; i < tasks.size(); i++) {
            GeneratorTestItem testItem = testItems.get(i);
            GeneratorTask task = tasks.get(i);
            
            check(task.getSourceClass())
                    .is((Class) testItem.sourceClass());
            check(task.getGenerator())
                    .is(getSpiedGenerator(testItem));
            check(task.getFile().getCode())
                    .is(testItem.expectedCode());
            check(pathWithoutTemporaryFolder(task.getFile().getPath()))
                    .is(testItem.expectedPath());
            check(task.getAction())
                    .is(GeneratorAction.FILE_CREATED);
            check(task.getException())
                    .isNull();
        }
        
    }

    @Test
    public void checkQuantityOfGeneratedFiles() {
        List<GeneratorTestItem> testItems = testItems();
        generate(testItems);
        List<Path> paths = allInTemporaryFolder();
        check(paths).hasSize(testItems.size());
    }
    
    @Test
    public void checkCode() {
        List<GeneratorTestItem> testItems = testItems();
        List<GeneratorTask> tasks = generate(testItems);
        for (int i = 0; i < tasks.size(); i++) {
            String actualCode = FileUtils.readString(tasks.get(i).getFile().getPath());
            String expectedCode = testItems.get(i).expectedCode();
            check(actualCode).is(expectedCode);
        }
    }

    @Test
    public void checkFilePath() {
        List<GeneratorTestItem> testItems = testItems();
        generate(testItems);
        List<Path> allPaths = allInTemporaryFolder();
        for (int i = 0; i < testItems.size(); i++) {
            Path expectedPath = testItems.get(i).expectedPath();
            Path expectedPathInTemporaryFolder = pathInTemporaryFolder(expectedPath);
            check(allPaths).contains(expectedPathInTemporaryFolder);
        }
    }

    private List<GeneratorTask> generate(List<GeneratorTestItem> testItems) {
        List<Class<?>> sources = testItems.stream()
                .map(GeneratorTestItem::sourceClass)
                .collect(Collectors.toList());
        List<GeneratorTask> tasks = generatorService.run(sources);
        check(tasks).hasSize(sources.size());
        return tasks;
    }

    private List<Path> allInTemporaryFolder() {
        try {
            return Files.find(temporaryFolder.getRoot().toPath(),
                    999,
                    (path, attributes) -> attributes.isRegularFile())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    private Generator getSpiedGenerator(GeneratorTestItem testItem) {
        Class<? extends Generator>[] generators = testItem.sourceClass().getAnnotation(Generate.class)
                .value();
        check(generators).hasSize(1);
        Generator currentGenerator = applicationContext.getBean(generators[0]);
        if (MockUtil.isSpy(currentGenerator)) {
            return currentGenerator;
        } else {
            Generator spiedGenerator = spy(currentGenerator);
            registerBean(currentGenerator, spiedGenerator);
            return spiedGenerator;
        }
    }

    private <T> void registerBean(T currentBean, T beanToReplace) {
        Class<T> beanClass = (Class<T>) currentBean.getClass();
        String beanName = applicationContext.getBeanNamesForType(beanClass)[0];
        applicationContext.removeBeanDefinition(beanName);
        applicationContext.registerBean(beanName, beanClass, () -> beanToReplace);
    }

    private Path pathInTemporaryFolder(Path canonicalPath) {
        return temporaryFolder.getRoot().toPath().resolve(canonicalPath);
    }

    private Path pathWithoutTemporaryFolder(Path fullPath) {
        return temporaryFolder.getRoot().toPath().relativize(fullPath);
    }

    protected abstract List<GeneratorTestItem> testItems();
}
