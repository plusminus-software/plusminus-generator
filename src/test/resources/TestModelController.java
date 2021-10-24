package software.plusminus.generator.java.controller;

import software.plusminus.data.controller.CrudController;
import software.plusminus.generator.java.TestModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-models")
public class TestModelController extends CrudController<TestModel, Long> {
}
