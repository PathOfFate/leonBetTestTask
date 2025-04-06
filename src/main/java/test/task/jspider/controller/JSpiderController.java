package test.task.jspider.controller;


import test.task.jspider.service.LeonBetDataProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JSpiderController {

    private final LeonBetDataProcessorService parser;

    @PostMapping("/create_report")
    public ResponseEntity<String> analyzeLeon() {
        parser.aggregateBetData();

        return ResponseEntity.ok("Report created");
    }
}
