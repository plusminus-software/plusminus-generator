package software.plusminus.generator.java;

import software.plusminus.generator.java.model.Interface;

public abstract class JavaInterfaceGenerator extends AbstractJavaGenerator<Interface> {
    
    @Override
    protected String templateName() {
        return "java/interface";
    }
}
