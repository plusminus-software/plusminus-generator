package software.plusminus.generator.typescript;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableMap;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.plusminus.generator.TemplateGenerator;
import software.plusminus.generator.exception.GeneratorException;
import software.plusminus.generator.typescript.model.TypescriptClass;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TypescriptGenerator extends TemplateGenerator<TypescriptClass> {

    @SuppressWarnings("squid:S1192")
    private static final Map<String, String> JAVA_TYPE_TO_TYPESCRIPT_TYPE = ImmutableMap.<String, String>builder()
            .put(String.class.getName(), "string")
            .put(Boolean.class.getName(), "boolean")
            .put(boolean.class.getName(), "boolean")
            .put(Byte.class.getName(), "number")
            .put(byte.class.getName(), "number")
            .put(Character.class.getName(), "number")
            .put(char.class.getName(), "number")
            .put(Short.class.getName(), "number")
            .put(short.class.getName(), "number")
            .put(Integer.class.getName(), "number")
            .put(int.class.getName(), "number")
            .put(Long.class.getName(), "number")
            .put(long.class.getName(), "number")
            .put(Float.class.getName(), "number")
            .put(float.class.getName(), "number")
            .put(Date.class.getName(), "Date")
            .put(UUID.class.getName(), "string")
            .put(BigDecimal.class.getName(), "number")
            .put(LocalDate.class.getName(), "string")
            .put(ZonedDateTime.class.getName(), "string")
            .put(URL.class.getName(), "URL")
            .build();

    private static final List<String> IGNORE_FIELDS = Collections.singletonList("$jacocoData");

    @Autowired
    private AngularTypescriptPathResolver pathResolver;

    @Override
    public String templateName() {
        return "typescript/class";
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    @Override
    public Path targetPath(Class<?> sourceClass, TypescriptClass context) {
        return pathResolver.targetPath(sourceClass, "model", "model", context);
    }

    @Override
    public TypescriptClass generateModel(Class<?> sourceClass) {
        Set<Class<?>> dependencies = new HashSet<>();
        String name = sourceClass.getSimpleName();
        String title = generateTitle(sourceClass, name, dependencies);
        List<String> fields = generateFields(sourceClass, dependencies);
        List<String> imports = generateImports(sourceClass, dependencies);

        return new TypescriptClass()
                .imports(imports)
                .title(title)
                .name(name)
                .fields(fields);
    }

    private List<String> generateImports(Class<?> current, Set<Class<?>> imports) {
        return imports.stream()
                .filter(i -> current != i)
                .map(i -> buildImport(current, i))
                .collect(Collectors.toList());
    }

    private String buildImport(Class<?> current, Class<?> toImport) {
        List<String> currentPath = Stream.of(current.getPackage().getName().split("\\."))
                .collect(Collectors.toList());
        List<String> toImportPath = Stream.of(toImport.getPackage().getName().split("\\."))
                .collect(Collectors.toList());

        while (!currentPath.isEmpty()
                && !toImportPath.isEmpty()
                && currentPath.get(0).equals(toImportPath.get(0))) {

            currentPath.remove(0);
            toImportPath.remove(0);
        }

        StringBuilder path = new StringBuilder();
        if (currentPath.isEmpty()) {
            path.append("./");
        } else {
            path.append(StringUtils.repeat("../", currentPath.size()));
        }

        if (toImportPath.isEmpty()) {
            path.append(CaseFormat.UPPER_CAMEL
                    .to(CaseFormat.LOWER_HYPHEN, toImport.getSimpleName()));
        } else {
            path.append(String.join("/", toImportPath));
            path.append("/");
            path.append(CaseFormat.UPPER_CAMEL
                    .to(CaseFormat.LOWER_HYPHEN, toImport.getSimpleName()));
        }

        return String.format("import { %s } from '%s';", toImport.getSimpleName(), path);
    }

    private String generateTitle(Class<?> sourceClass, String name, Set<Class<?>> imports) {
        StringBuilder title = new StringBuilder();
        addTitleModifiers(sourceClass, title);
        title.append(name);
        if (sourceClass.getTypeParameters().length > 0) {
            title.append("<");
            title.append(Stream.of(sourceClass.getTypeParameters())
                    .map(t -> buildType(t, imports))
                    .collect(Collectors.joining(", ")));
            title.append(">");
        }
        if (sourceClass.getSuperclass() != null
                && sourceClass.getSuperclass() != Object.class
                && sourceClass.getSuperclass() != Enum.class) {
            title.append(" extends ");
            title.append(buildType(sourceClass.getGenericSuperclass(), imports));
        }
        if (sourceClass.getGenericInterfaces().length > 0) {
            title.append(" implements ");
            title.append(buildType(sourceClass.getGenericInterfaces()[0], imports));
            for (int i = 1; i < sourceClass.getGenericInterfaces().length; i++) {
                title.append(", ");
                title.append(buildType(sourceClass.getGenericInterfaces()[i], imports));
            }
        }
        return title.toString();
    }

    private void addTitleModifiers(Class<?> sourceClass, StringBuilder title) {
        if (Modifier.isPublic(sourceClass.getModifiers())) {
            title.append("export ");
        }
        if (sourceClass.isEnum()) {
            title.append("const enum ");
        } else if (sourceClass.isInterface()) {
            title.append("interface ");
        } else {
            title.append("class ");
        }
    }

    private List<String> generateFields(Class<?> sourceClass, Set<Class<?>> imports) {
        if (sourceClass.isEnum()) {
            return Stream.of(sourceClass.getEnumConstants())
                    .map(enumConstant -> String.format("%s = \'%s\',", enumConstant, enumConstant))
                    .collect(Collectors.toList());
        }
        return Stream.of(sourceClass.getDeclaredFields())
                .filter(field -> !IGNORE_FIELDS.contains(field.getName()))
                .map(field -> String.format("%s: %s;",
                        field.getName(), buildType(field.getGenericType(), imports)))
                .collect(Collectors.toList());
    }

    private String buildType(Type container, Set<Class<?>> imports) {
        StringBuilder result = new StringBuilder();
        if (container instanceof ParameterizedType
                && ((ParameterizedType) container).getRawType() instanceof Class
                && Iterable.class.isAssignableFrom(
                (Class) ((ParameterizedType) container).getRawType())) {
            result.append(
                    ((ParameterizedType) container).getActualTypeArguments()[0].getTypeName());
            addImports(
                    ((ParameterizedType) container).getActualTypeArguments()[0], result, imports);
            result.append("[]");
        } else {
            result.append(container.getTypeName());
            addImports(container, result, imports);
        }
        return result.toString();
    }

    private void addImports(Type container, StringBuilder result, Set<Class<?>> imports) {
        if (container instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) container;
            addImportsOfParameterizedType(parameterizedType, result, imports);
        } else if (container instanceof Class) {
            Class c = (Class) container;
            addImportsOfClass(c, result, imports);
        } else if (container instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) container;
            addImportsOfWildcardType(wildcardType, result, imports);
        }
    }

    private void addImportsOfParameterizedType(ParameterizedType parameterizedType, StringBuilder result,
                                               Set<Class<?>> imports) {
        String typeName = parameterizedType.getRawType().getTypeName();
        if (JAVA_TYPE_TO_TYPESCRIPT_TYPE.containsKey(typeName)) {
            String newResult = result.toString().replace(
                    typeName, JAVA_TYPE_TO_TYPESCRIPT_TYPE.get(typeName));
            result.replace(0, result.length(), newResult);
        } else if (typeName.contains(".")) {
            try {
                Class<?> rawClass = Class.forName(typeName);
                imports.add(rawClass);
                String newResult = result.toString().replace(
                        rawClass.getName(), rawClass.getSimpleName());
                result.replace(0, result.length(), newResult);
            } catch (ClassNotFoundException e) {
                throw new GeneratorException(e);
            }
        }
        for (Type subType : parameterizedType.getActualTypeArguments()) {
            addImports(subType, result, imports);
        }
    }

    private void addImportsOfClass(Class c, StringBuilder result,
                                   Set<Class<?>> imports) {
        if (!JAVA_TYPE_TO_TYPESCRIPT_TYPE.containsKey(c.getName())) {
            imports.add(c);
            String newResult = result.toString().replace(
                    c.getName(), c.getSimpleName());
            result.replace(0, result.length(), newResult);
        } else {
            String newResult = result.toString().replace(
                    c.getName(), JAVA_TYPE_TO_TYPESCRIPT_TYPE.get(c.getName()));
            result.replace(0, result.length(), newResult);
        }
    }

    private void addImportsOfWildcardType(WildcardType wildcardType, StringBuilder result,
                                          Set<Class<?>> imports) {
        if (wildcardType.getUpperBounds().length > 0) {
            Type bound = wildcardType.getUpperBounds()[0];
            result.replace(0, result.length(), bound.getTypeName());
            addImports(bound, result, imports);
        }
        // TODO lower bounds
    }
}
