package software.plusminus.generator.service;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Service;
import software.plusminus.generator.Generate;
import software.plusminus.generator.Generator;
import software.plusminus.generator.exception.GeneratorException;
import software.plusminus.generator.model.GeneratedFile;
import software.plusminus.generator.model.GeneratorAction;
import software.plusminus.generator.model.GeneratorTask;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GeneratorService {

    private CodeService codeService;
    private ApplicationContext applicationContext;

    public List<GeneratorTask> run(List<Class<?>> sources) {
        List<GeneratorTask> tasks = sources.stream()
                .flatMap(this::createTasks)
                .collect(Collectors.toList());
        tasks.forEach(this::generate);
        checkExceptions(tasks);
        removeOverriddenTasks(tasks);
        tasks.forEach(this::process);
        checkExceptions(tasks);
        return tasks;
    }

    private Stream<GeneratorTask> createTasks(Class<?> sourceClass) {
        Set<Class<? extends Generator>> allGenerators = AnnotatedElementUtils.findAllMergedAnnotations(
                sourceClass, Generate.class)
                .stream()
                .flatMap(annotation -> Stream.of(annotation.value()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (allGenerators.isEmpty()) {
            return Stream.empty();
        }

        return allGenerators.stream()
                .map(generator -> applicationContext.getBean(generator))
                .filter(generator -> generator.supports(sourceClass))
                .flatMap(generator ->
                        Stream.concat(Stream.of(generator), getMissedDependencies(allGenerators, generator)))
                .map(generator -> createTask(sourceClass, generator));
    }

    private Stream<Generator> getMissedDependencies(Set<Class<? extends Generator>> currentGenerators,
                                                    Generator generator) {
        return generator.dependencies().stream()
                .filter(dependency -> !currentGenerators.contains(dependency.getClass()));
    }

    private GeneratorTask createTask(Class<?> sourceClass, Generator generator) {
        GeneratorTask task = new GeneratorTask();
        task.setSourceClass(sourceClass);
        task.setGenerator(generator);
        return task;
    }

    private void removeOverriddenTasks(List<GeneratorTask> tasks) {
        Map<Path, List<GeneratorTask>> targetDuplicates = tasks.stream()
                .filter(task -> task.getFile().getPath() != null)
                .collect(Collectors.groupingBy(task -> task.getFile().getPath()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!targetDuplicates.isEmpty()) {
            targetDuplicates.values().stream()
                    .map(list -> list.subList(0, list.size() - 1))
                    .flatMap(List::stream)
                    .forEach(tasks::remove);
        }
    }

    private void generate(GeneratorTask task) {
        if (task.getException() != null) {
            return;
        }

        Generator generator = task.getGenerator();
        try {
            GeneratedFile generatedFile = generator.generate(task.getSourceClass());
            task.setFile(generatedFile);
        } catch (Exception e) {
            task.setException(e);
        }
    }

    private void process(GeneratorTask task) {
        Path targetPath = task.getFile().getPath();
        String generatedCode = task.getFile().getCode();
        try {
            GeneratorAction result = codeService.processCode(targetPath, generatedCode);
            task.setAction(result);
        } catch (Exception e) {
            task.setException(e);
        }
    }

    private void checkExceptions(List<GeneratorTask> tasks) {
        List<GeneratorTask> tasksWithException = tasks.stream()
                .filter(task -> task.getException() != null)
                .collect(Collectors.toList());
        if (!tasksWithException.isEmpty()) {
            throw new GeneratorException(tasksWithException.get(0).getException());
        }
    }
}
