package software.plusminus.generator.java;

import software.plusminus.generator.java.model.Class;

public abstract class JavaClassGenerator extends AbstractJavaGenerator<Class> {
    
    @Override
    protected String templateName() {
        return "java/class";
    }
}
