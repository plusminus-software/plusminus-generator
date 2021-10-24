package software.plusminus.generator;

import software.plusminus.generator.model.GeneratedFile;

import java.util.Collections;
import java.util.List;

public interface Generator {

    GeneratedFile generate(Class<?> sourceClass);
    
    default List<Generator> dependencies() {
        return Collections.emptyList();
    }
    
}