package software.plusminus.generator.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneratorException extends RuntimeException {

    public GeneratorException(Throwable cause) {
        super(cause);
    }
    
    public GeneratorException(String message) {
        super(message);
    }
    
}
