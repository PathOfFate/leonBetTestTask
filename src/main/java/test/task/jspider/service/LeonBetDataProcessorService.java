package test.task.jspider.service;


import test.task.jspider.model.*;
import test.task.jspider.client.LeonBetApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeonBetDataProcessorService {

    private static final int MATCHES_TO_PROCESS = 2;

    private final LeonBetApiClient leonBetApiClient;
    private final ReportGenerationService reportGenerationService;

    public void aggregateBetData() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        final var futures = Arrays.stream(SportType.values())
                .map(sportType -> CompletableFuture.supplyAsync(() -> extractLeagueIdForSport(sportType), executorService))
                .map(future ->
                        future.thenCompose(
                                matchId -> CompletableFuture.supplyAsync(() -> fetchMatchesForLeague(matchId))
                        )
                )
                .toList();

        final var matches = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        executorService.shutdown();
        reportGenerationService.generateReport(matches);
    }

    private String extractLeagueIdForSport(SportType sportType){
        var sports = leonBetApiClient.fetchSports();

        return sports.stream()
                .filter(sport -> sportType.sportName.equalsIgnoreCase(sport.name()))
                .flatMap(sport -> sport.regions().stream())
                .flatMap(region -> region.leagues().stream())
                .filter(league -> league.top() && league.topOrder() == 0)
                .map(League::id)
                .findFirst()
                .orElse(null);
    }

    private List<Match> fetchMatchesForLeague(String leagueId) {
        if (leagueId == null) {
            return Collections.emptyList();
        }
        var events = leonBetApiClient.fetchMatchesByLeagueId(leagueId).events();
        return events.stream()
                .limit(MATCHES_TO_PROCESS)
                .map(event -> leonBetApiClient.fetchMatchDetails(event.id()))
                .collect(Collectors.toList());
    }

    enum SportType {
        FOOTBALL("Football"),
        HOCKEY("Ice Hockey"),
        TENNIS("Tennis"),
        BASKETBALL("Basketball");

        public final String sportName;

        SportType(String name) {
            this.sportName = name;
        }
    }
}