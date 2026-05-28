package dk.sdu.cbse.scoringsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ScoringSystem {

    private Long totalScore = 0L;

    public static void main(String[] args) {
        SpringApplication.run(ScoringSystem.class, args);
    }

    @GetMapping("/score")
    public Long addScore(@RequestParam(value = "point") Long point) {
        totalScore += point;
        return totalScore;
    }

    @GetMapping("/reset")
    public Long reset() {
        totalScore = 0L;
        return totalScore;
    }
}
