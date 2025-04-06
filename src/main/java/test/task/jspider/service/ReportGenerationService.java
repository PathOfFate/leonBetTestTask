package test.task.jspider.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import test.task.jspider.model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ReportGenerationService {

    @Value("${report.file-path}")
    private String filePath;

    public void generateReport(List<Match> matches) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Match match : matches) {
                League league = match.league();
                Sport sport = league.sport();
                writer.write("Sport: " + sport.name() + ", League: " + league.name());
                writer.newLine();
                writer.write("Match: " + match.name() + ", Kickoff: " + match.kickoff().toString() + ", ID: " + match.id());
                writer.newLine();
                for (Market market : match.markets()) {
                    writer.write("Market: " + market.name());
                    writer.newLine();
                    for (Runner runner : market.runners()) {
                        writer.write("\tRunner: " + runner.name() + ", Value: " + runner.priceStr() + ", ID: " + runner.id());
                        writer.newLine();
                    }
                }
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("Error writing report file.", e);
        }
    }
}
