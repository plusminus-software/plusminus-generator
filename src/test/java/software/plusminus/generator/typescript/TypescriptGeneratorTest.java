package software.plusminus.generator.typescript;

import software.plusminus.generator.GeneratorTest;
import software.plusminus.generator.GeneratorTestItem;
import software.plusminus.generator.typescript.model.ClassToTypescript;
import software.plusminus.generator.typescript.model.EnumToTypescript;
import software.plusminus.generator.typescript.model.InterfaceToTypescript;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class TypescriptGeneratorTest extends GeneratorTest {

    @Override
    protected List<GeneratorTestItem> testItems() {
        return Arrays.asList(
                new GeneratorTestItem()
                        .sourceClass(ClassToTypescript.class)
                        .expectedPath(Paths.get("angular", "src", "app", "model",
                                "class-to-typescript.ts"))
                        .expectedCode("/class-to-typescript.ts"),
                new GeneratorTestItem()
                        .sourceClass(InterfaceToTypescript.class)
                        .expectedPath(Paths.get("angular", "src", "app", "model",
                                "interface-to-typescript.ts"))
                        .expectedCode("/interface-to-typescript.ts"),
                new GeneratorTestItem()
                        .sourceClass(EnumToTypescript.class)
                        .expectedPath(Paths.get("angular", "src", "app", "model",
                                "enum-to-typescript.ts"))
                        .expectedCode("/enum-to-typescript.ts")

        );
    }
}