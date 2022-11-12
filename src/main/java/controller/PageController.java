package controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/")
@CrossOrigin("*")
public class PageController {

    @GetMapping
    public String index() {
        return "index";
    }
}
