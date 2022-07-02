package com.no.dashboard.service;

import com.no.dashboard.exception.GameDashBoardExceptions;
import com.no.dashboard.model.Match;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import static com.no.dashboard.exception.GameDashBoardExceptions.*;
import static org.junit.jupiter.api.Assertions.*;

public class FifaDashboardServiceTest {

    GameDashboardService fifaDashboardService;

    @Test
    public void startMatchWithValidInputTest() throws StartGameException {
        fifaDashboardService = new FifaDashboardService();
        String startedMatchId = createSingleMatch(fifaDashboardService);
        Map<String, Match> currentLiveMatches =  fifaDashboardService.getAllLiveMatches();
        String matchOneKey = "brazilmexico";
        assertNotNull(startedMatchId);
        assertEquals(matchOneKey, startedMatchId);
        assertEquals(1, currentLiveMatches.size());
        assertTrue(currentLiveMatches.containsKey(matchOneKey));

        Match matchOne = currentLiveMatches.get(matchOneKey);
        assertEquals("Brazil", matchOne.getHomeTeam());
        assertEquals("Mexico", matchOne.getAwayTeam());
        assertEquals(0, matchOne.getHomeScore());
        assertEquals(0, matchOne.getAwayScore());

        String homeTeam = "spain";
        String awayTeam = "canada";
        startedMatchId = fifaDashboardService.startMatch(homeTeam, awayTeam);
        currentLiveMatches =  fifaDashboardService.getAllLiveMatches();
        assertNotNull(startedMatchId);
        assertEquals(2, currentLiveMatches.size());
        String matchTwoKey = "spaincanada";
        assertTrue(currentLiveMatches.containsKey(matchTwoKey));

        Match matchTwo = currentLiveMatches.get(matchTwoKey);
        assertEquals("Spain", matchTwo.getHomeTeam());
        assertEquals("Canada", matchTwo.getAwayTeam());
        assertEquals(0, matchTwo.getHomeScore());
        assertEquals(0, matchTwo.getAwayScore());
    }

    @Test
    public void startMatchWithMixedCharacterInputTest() throws StartGameException {
        fifaDashboardService = new FifaDashboardService();
        String homeTeam = "bRaZIl";
        String awayTeam = "mEXicO";
        String startedMatchId = fifaDashboardService.startMatch(homeTeam, awayTeam);
        String matchOneKey = "brazilmexico";
        assertNotNull(startedMatchId);
        assertEquals(matchOneKey, startedMatchId);
        Map<String, Match> currentLiveMatches =  fifaDashboardService.getAllLiveMatches();
        assertEquals(1, currentLiveMatches.size());

        assertTrue(currentLiveMatches.containsKey(matchOneKey));

        Match matchOne = currentLiveMatches.get(matchOneKey);
        assertEquals("Brazil", matchOne.getHomeTeam());
        assertEquals("Mexico", matchOne.getAwayTeam());
        assertEquals(0, matchOne.getHomeScore());
        assertEquals(0, matchOne.getAwayScore());
    }

    @Test
    public void startMatchWithEmptyInputTest() {
        fifaDashboardService = new FifaDashboardService();
        String homeTeam = "";
        String awayTeam = "Mexico";
        assertExceptionTestResults(homeTeam, awayTeam);
    }

    @Test
    public void startMatchWithNullInputTest() {
        fifaDashboardService = new FifaDashboardService();
        String homeTeam = null;
        String awayTeam = "Mexico";
        assertExceptionTestResults(homeTeam, awayTeam);
    }

    @Test
    public void startMatchWithWhiteSpaceInputTest() {
        fifaDashboardService = new FifaDashboardService();
        String homeTeam = "   ";
        String awayTeam = "Mexico";
        assertExceptionTestResults(homeTeam, awayTeam);
    }

    @Test
    public void updateMatchTest() throws StartGameException, UpdateGameException {

        fifaDashboardService = new FifaDashboardService();
        String startedMatchId = createSingleMatch(fifaDashboardService);
        String updateScoreResp = fifaDashboardService.updateScore(startedMatchId, 0,1);
        Map<String, Match> currentLiveMatches =  fifaDashboardService.getAllLiveMatches();
        assertEquals(startedMatchId, updateScoreResp);
        assertNotNull(currentLiveMatches);
        assertEquals(0,currentLiveMatches.get(updateScoreResp).getHomeScore());
        assertEquals(1,currentLiveMatches.get(updateScoreResp).getAwayScore());
    }

    @Test
    public void updateMatchWithInvalidMatchIdTest() throws StartGameException {
        fifaDashboardService = new FifaDashboardService();
        String startMatchResp = createSingleMatch(fifaDashboardService);
        String invalidMatchId = "abcd";
        UpdateGameException exceptionThrown = assertThrows(UpdateGameException.class,
                () -> fifaDashboardService.updateScore(invalidMatchId,0,1),
                "Custom exception expected");
        assertEquals("Trying to update a match which is not present. Please provide valid match",
                exceptionThrown.getMessage());

    }

    @Test
    public void updateScoreWhenNoLiveMatchTest() {
        fifaDashboardService = new FifaDashboardService();
        String invalidMatchId = "abcd";
        UpdateGameException exceptionThrown = assertThrows(UpdateGameException.class,
                () -> fifaDashboardService.updateScore(invalidMatchId,0,1),
                "Custom exception expected");
        assertEquals("Trying to update a match which is not present. Please provide valid match",
                exceptionThrown.getMessage());
    }

    @Test
    public void finishMatchTest() throws StartGameException, FinishGameException {
        fifaDashboardService =  new FifaDashboardService();
        String startedMatchId = createSingleMatch(fifaDashboardService);
        String finishedGameId = fifaDashboardService.finishMatch(startedMatchId);
        Map<String, Match> currentLiveMatches =  fifaDashboardService.getAllLiveMatches();
        assertNotNull(finishedGameId);
        assertEquals(startedMatchId, finishedGameId);
        assertFalse(currentLiveMatches.containsKey(startedMatchId));
        assertNull(currentLiveMatches.get(startedMatchId));
    }

    @Test
    public void finishIncorrectMatchTest() throws StartGameException {
        fifaDashboardService =  new FifaDashboardService();
        String startedMatchId = createSingleMatch(fifaDashboardService);
        String nonExistingMatchId = "abcd";
        FinishGameException exceptionThrown = assertThrows(FinishGameException.class,
                () -> fifaDashboardService.finishMatch(nonExistingMatchId),
                "Custom exception expected");
        assertEquals("The match you are trying to finish doesn't exist in the dashboard",
                exceptionThrown.getMessage());
    }

    @Test
    public void finishAMatchWhenNoLiveMatchTest() {
        fifaDashboardService =  new FifaDashboardService();
        String nonLiveMatchId = "brazilmexico";
        FinishGameException exceptionThrown = assertThrows(FinishGameException.class,
                () -> fifaDashboardService.finishMatch(nonLiveMatchId),
                "Custom exception expected");
        assertEquals("The match you are trying to finish doesn't exist in the dashboard",
                exceptionThrown.getMessage());

    }

    @Test
    public void getMatchesOverviewTest() throws GetMatchesOverviewException, UpdateGameException, StartGameException {
        fifaDashboardService =  new FifaDashboardService();
        createABatchOfMatches(fifaDashboardService);
        List<Match> liveMatches = fifaDashboardService.getLiveMatchesOverview();
        assertEquals("Uruguay",liveMatches.get(0).getHomeTeam());
        assertEquals("Italy",liveMatches.get(0).getAwayTeam());
        assertEquals(6,liveMatches.get(0).getHomeScore());
        assertEquals(6, liveMatches.get(0).getAwayScore());

        assertEquals("Spain",liveMatches.get(1).getHomeTeam());
        assertEquals("Brazil",liveMatches.get(1).getAwayTeam());

        assertEquals("Mexico",liveMatches.get(2).getHomeTeam());
        assertEquals("Canada",liveMatches.get(2).getAwayTeam());

        assertEquals("Argentina",liveMatches.get(3).getHomeTeam());
        assertEquals("Australia",liveMatches.get(3).getAwayTeam());

        assertEquals("Germany",liveMatches.get(4).getHomeTeam());
        assertEquals("France",liveMatches.get(4).getAwayTeam());

    }
    @Test
    public void getMatchOverviewWhenNoLiveMatchTest() {
        fifaDashboardService =  new FifaDashboardService();
        GetMatchesOverviewException exceptionThrown = assertThrows(GetMatchesOverviewException.class,
                () -> fifaDashboardService.getLiveMatchesOverview(),
                "Custom exception expected");
        assertEquals("No Matches present in the dashboard now",
                exceptionThrown.getMessage());
    }

    @Test
    public void testConcurrencySafety() throws GetMatchesOverviewException, InterruptedException, StartGameException {

        fifaDashboardService =  new FifaDashboardService();
        fifaDashboardService.startMatch("AAA","BBB");
        fifaDashboardService.startMatch("CCC","DDD");
        fifaDashboardService.startMatch("EEE","FFF");

        Thread t1 = new Thread(() -> {
            try {
                fifaDashboardService.startMatch("GGG","HHH");
                fifaDashboardService.startMatch("III","JJJ");
                fifaDashboardService.startMatch("KKK","LLL");
                fifaDashboardService.startMatch("MMM","NNN");
            } catch (StartGameException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t2 = new Thread(() -> {

            try {
                fifaDashboardService.finishMatch("aaabbb");
                fifaDashboardService.finishMatch("cccddd");
                fifaDashboardService.finishMatch("eeefff");
            } catch (FinishGameException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();

        Thread.sleep(4000);
        List<Match> matches = fifaDashboardService.getLiveMatchesOverview();
        assertEquals(4, matches.size());
    }

    private void createABatchOfMatches(GameDashboardService fifaDashboardService) throws StartGameException, UpdateGameException {
        String homeTeam = "Mexico";
        String awayTeam = "Canada";
        String startedMatchID = fifaDashboardService.startMatch(homeTeam, awayTeam);
        fifaDashboardService.updateScore(startedMatchID, 0,5);

        startedMatchID = fifaDashboardService.startMatch("Spain", "Brazil");
        fifaDashboardService.updateScore(startedMatchID, 10,2);

        startedMatchID = fifaDashboardService.startMatch("Germany", "France");
        fifaDashboardService.updateScore(startedMatchID, 2,2);

        startedMatchID = fifaDashboardService.startMatch("Uruguay", "Italy");
        fifaDashboardService.updateScore(startedMatchID, 6,6);

        startedMatchID = fifaDashboardService.startMatch("Argentina", "Australia");
        fifaDashboardService.updateScore(startedMatchID, 3,1);
    }

    private String createSingleMatch(GameDashboardService fifaDashboardService) throws StartGameException {
        String homeTeam = "Brazil";
        String awayTeam = "Mexico";
        return fifaDashboardService.startMatch(homeTeam, awayTeam);
    }

    private void assertExceptionTestResults(String homeTeam, String awayTeam) {
        StartGameException exceptionThrown = assertThrows(StartGameException.class,
                () -> fifaDashboardService.startMatch(homeTeam, awayTeam),
                "Custom exception expected");
        assertEquals("Can't start the game.Incorrect Team details provided",
                exceptionThrown.getMessage());
    }

}
