package software.plusminus.generator.java;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import software.plusminus.generator.java.model.Class;
import software.plusminus.generator.java.model.TestModel;

import java.io.File;
import java.nio.file.Path;

import static software.plusminus.check.Checks.check;

@RunWith(MockitoJUnitRunner.class)
public class AbstractJavaGeneratorTest {
    
    @Spy
    private AbstractJavaGenerator<Class> generator;
    
    @Test
    public void targetPath() {
        Class javaClass = new Class();
        javaClass.setPackageName("software.plusminus.test");
        javaClass.setName("TestGenerated");
        
        Path path = generator.targetPath(TestModel.class, javaClass);
        
        String expectedPath = "src/main/java/software/plusminus/test/TestGenerated.java"
                .replace('/', File.separatorChar); 
        check(path).is(expectedPath);
    }

}