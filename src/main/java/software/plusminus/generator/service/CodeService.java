package software.plusminus.generator.service;

import org.springframework.stereotype.Service;
import software.plusminus.generator.model.GeneratorAction;
import software.plusminus.util.FileUtils;

import java.nio.file.Path;
import javax.annotation.Nullable;

@Service
public class CodeService {
    
    public GeneratorAction processCode(@Nullable Path targetPath, String generatedCode) {
        if (targetPath == null) {
            return GeneratorAction.NO_ACTION;
        }
        if (!FileUtils.exists(targetPath)) {
            FileUtils.write(targetPath, generatedCode);
            return GeneratorAction.FILE_CREATED;
        }
        
        String currentCode = FileUtils.readString(targetPath);
        if (currentCode.equals(generatedCode)) {
            return GeneratorAction.CONTENT_IS_EQUAL;
        }

        FileUtils.write(targetPath, generatedCode);
        return GeneratorAction.FILE_OVERWRITTEN;
    }
    
}
