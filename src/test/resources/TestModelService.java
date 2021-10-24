package software.plusminus.generator.java.service;

import software.plusminus.data.service.crud.EntityCrudService;
import software.plusminus.generator.java.TestModel;
import org.springframework.web.bind.annotation.Service;

@Service
public class TestModelService extends EntityCrudService<TestModel, Long> {
}
