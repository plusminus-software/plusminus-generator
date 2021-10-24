package software.plusminus.generator.java;

import software.plusminus.generator.Generate;
import software.plusminus.generator.GeneratorTest;
import software.plusminus.generator.GeneratorTestItem;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class EntityCrudServiceGeneratorTest extends GeneratorTest {

    @Override
    protected List<GeneratorTestItem> testItems() {
        return Collections.singletonList(new GeneratorTestItem()
                .sourceClass(TestModel.class)
                .expectedPath(Paths.get("src", "main", "java", "software", "plusminus", "generator", "java",
                        "service", "TestModelService.java"))
                .expectedCode("/TestModelService.java"));
    }

    @Generate(EntityCrudServiceGenerator.class)
    private class TestModel {
    }

}