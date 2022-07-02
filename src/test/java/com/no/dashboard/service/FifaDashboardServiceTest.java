package com.no.dashboard.service;

import com.no.dashboard.exception.GameDashBoardExceptions;
import com.no.dashboard.model.Match;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static com.no.dashboard.exception.GameDashBoardExceptions.*;
import static org.junit.jupiter.api.Assertions.*;

public class FifaDashboardServiceTest {

    GameDashboardService fifaDashboardService;

    @Test
    public void startMatchWithValidInputTest() throws StartGameException {
        fifaDashboardService = new FifaDashboardService();
        String startMatchResp = createSingleMatch(fifaDashboardService);
        Map<String, Match> currentLiveMatches =  fifaDashboardService.getAllLiveMatches();
        String matchOneKey = "brazilmexico";
        assertNotNull(startMatchResp);
        assertEquals(matchOneKey, startMatchResp);
        assertEquals(1, currentLiveMatches.size());
        assertTrue(currentLiveMatches.containsKey(matchOneKey));

        Match matchOne = currentLiveMatches.get(matchOneKey);
        assertEquals("Brazil", matchOne.getHomeTeam());
        assertEquals("Mexico", matchOne.getAwayTeam());
        assertEquals(0, matchOne.getHomeScore());
        assertEquals(0, matchOne.getAwayScore());

        String homeTeam = "spain";
        String awayTeam = "canada";
        startMatchResp = fifaDashboardService.startMatch(homeTeam, awayTeam);
        currentLiveMatches =  fifaDashboardService.getAllLiveMatches();
        assertNotNull(startMatchResp);
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
        String startMathResp = fifaDashboardService.startMatch(homeTeam, awayTeam);
        String matchOneKey = "brazilmexico";
        assertNotNull(startMathResp);
        assertEquals(matchOneKey, startMathResp);
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
        String startMatchResp = createSingleMatch(fifaDashboardService);
        String updateScoreResp = fifaDashboardService.updateScore(startMatchResp, 0,1);
        Map<String, Match> currentLiveMatches =  fifaDashboardService.getAllLiveMatches();
        assertEquals(startMatchResp, updateScoreResp);
        assertNotNull(currentLiveMatches);
        assertEquals(0,currentLiveMatches.get(updateScoreResp).getHomeScore());
        assertEquals(1,currentLiveMatches.get(updateScoreResp).getAwayScore());
    }

    @Test
    public void UpdateMatchWithInvalidMatchIdTest() throws StartGameException {
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
    public void UpdateScoreWhenNoLiveMatchTest() {
        fifaDashboardService = new FifaDashboardService();
        String invalidMatchId = "abcd";
        UpdateGameException exceptionThrown = assertThrows(UpdateGameException.class,
                () -> fifaDashboardService.updateScore(invalidMatchId,0,1),
                "Custom exception expected");
        assertEquals("Trying to update a match which is not present. Please provide valid match",
                exceptionThrown.getMessage());
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
