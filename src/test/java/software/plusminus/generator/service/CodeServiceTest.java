package software.plusminus.generator.service;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import software.plusminus.generator.model.GeneratorAction;
import software.plusminus.util.FileUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;
import static software.plusminus.check.Checks.check;

public class CodeServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    
    private CodeService codeService = new CodeService();
    private String code = "my generated code";
    
    @After
    public void after() {
        temporaryFolder.delete();
    }
    
    @Test
    public void noAction() {
        GeneratorAction result = codeService.processCode(null, code);
        check(result).is(GeneratorAction.NO_ACTION);
    }
    
    @Test
    public void fileCreated() {
        Path filePath = temporaryFolder.getRoot().toPath().resolve("GeneratedFile.txt");
        
        GeneratorAction result = codeService.processCode(filePath, code);
        
        check(result).is(GeneratorAction.FILE_CREATED);
        check(FileUtils.readString(actualPath())).is(code);
    }
    
    @Test
    public void contentIsEqual() {
        Path filePath = temporaryFolder.getRoot().toPath().resolve("GeneratedFile.txt");
        FileUtils.write(filePath, code);

        GeneratorAction result = codeService.processCode(filePath, code);

        check(result).is(GeneratorAction.CONTENT_IS_EQUAL);
        check(FileUtils.readString(actualPath())).is(code);
    }
    
    @Test
    public void fileOverwritten() {
        Path filePath = temporaryFolder.getRoot().toPath().resolve("GeneratedFile.txt");
        FileUtils.write(filePath, "some other code");

        GeneratorAction result = codeService.processCode(filePath, code);

        check(result).is(GeneratorAction.FILE_OVERWRITTEN);
        check(FileUtils.readString(actualPath())).is(code);
    }

    private Path actualPath() {
        List<Path> paths = allInTemporaryFolder();
        if (paths.size() != 1) {
            fail("Should be single file in temporal folder but there are " + paths.size() + " files");
        }
        return paths.get(0);
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

}