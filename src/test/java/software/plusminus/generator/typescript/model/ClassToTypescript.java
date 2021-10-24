package software.plusminus.generator.typescript.model;

import lombok.Data;
import software.plusminus.generator.Generate;
import software.plusminus.generator.typescript.TypescriptGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
@Generate(TypescriptGenerator.class)
public class ClassToTypescript {
    
    private String string;
    private boolean primitiveBoolean;
    private Boolean wrappedBoolean;
    private byte primitiveByte;
    private Byte wrappedByte;
    private char primitiveChar;
    private Character wrappedChar;
    private short primitiveShort;
    private Short wrappedShort;
    private int primitiveInt;
    private Integer wrappedInt;
    private long primitiveLong;
    private Long wrappedLong;
    private float primitiveFloat;
    private Float wrappedFloat;
    private Date date;
    private UUID uuid;
    private BigDecimal bigDecimal;
    private LocalDate localDate;
    private ClassToTypescriptChild child;
    
}
