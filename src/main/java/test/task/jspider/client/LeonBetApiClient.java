package test.task.jspider.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import test.task.jspider.model.Betline;
import test.task.jspider.model.Match;
import test.task.jspider.model.Sport;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeonBetApiClient {

    private static final String LANGUAGE = "ctag";
    private static final String ENGLISH = "en-US";
    private static final String FLAGS = "flags";

    @Value("${web-client.all-sports-path}")
    private String allSports;

    @Value("${web-client.event-path}")
    private String eventPath;

    @Value("${web-client.all-events-path}")
    private String allEventsPath;

    private final WebClient leonWebClient;

    public List<Sport> fetchSports() {
        return leonWebClient.get()
                .uri(
                        UriComponentsBuilder.fromUriString(allSports)
                                .queryParam(LANGUAGE, ENGLISH)
                                .queryParam(FLAGS, "urlv2")
                                .build()
                                .toUriString()
                )
                .retrieve()
                .bodyToFlux(Sport.class)
                .collectList()
                .block();
    }

    public Betline fetchMatchesByLeagueId(String id) {
        return leonWebClient.get()
                .uri(
                        UriComponentsBuilder.fromUriString(allEventsPath)
                                .queryParam(LANGUAGE, ENGLISH)
                                .queryParam("league_id", id)
                                .queryParam("hideClosed", true)
                                .queryParam(FLAGS, "reg,urlv2,mm2,rrc,nodup")
                                .build()
                                .toUriString()
                )
                .retrieve()
                .bodyToMono(Betline.class)
                .block();
    }

    public Match fetchMatchDetails(String id) {
        return leonWebClient.get()
                .uri(
                        UriComponentsBuilder.fromUriString(eventPath)
                                .queryParam(LANGUAGE, ENGLISH)
                                .queryParam("eventId", id)
                                .queryParam(FLAGS, "reg,urlv2,mm2,rrc,nodup,smgv2,outv2")
                                .build()
                                .toUriString()
                )
                .retrieve()
                .bodyToMono(Match.class)
                .block();
    }
}