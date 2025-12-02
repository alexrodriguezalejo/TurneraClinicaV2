package ps.tif.turnera.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaController {

    @GetMapping("/")
    public String index() {
        return "index"; // busca index.html en /templates
    }

}
