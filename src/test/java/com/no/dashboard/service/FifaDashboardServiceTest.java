package com.no.dashboard.service;

import com.no.dashboard.exception.GameDashBoardExceptions;
import com.no.dashboard.model.Match;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import static com.no.dashboard.exception.GameDashBoardExceptions.*;

public class FifaDashboardServiceTest {

    FifaDashboardService fifaDashboardService =  new FifaDashboardService();

    @Test
    public void startMatchWithValidInputTest() throws StartGameException {
        String homeTeam = "Brazil";
        String awayTeam = "Mexico";
        Map<String, Match> result = fifaDashboardService.startMatch(homeTeam, awayTeam);
        assertNotNull(result);
        assertEquals(1, result.size());
        String matchOneKey = "brazilmexico";
        assertTrue(result.containsKey(matchOneKey));

        Match matchOne = result.get(matchOneKey);
        assertEquals(homeTeam, matchOne.getHomeTeam());
        assertEquals(awayTeam, matchOne.getAwayTeam());
        assertEquals(0, matchOne.getHomeScore());
        assertEquals(0, matchOne.getAwayScore());

        //more matches with lowercase and upperCase mixed in the team names;

        homeTeam = "SpAiN";
        awayTeam = "cANadA";
        result = fifaDashboardService.startMatch(homeTeam, awayTeam);

        assertNotNull(result);
        assertEquals(2, result.size());
        String matchTwoKey = "spaincanada";
        assertTrue(result.containsKey(matchTwoKey));

        Match matchTwo = result.get(matchTwoKey);
        assertEquals(homeTeam, matchTwo.getHomeTeam());
        assertEquals(awayTeam, matchTwo.getAwayTeam());
        assertEquals(0, matchTwo.getHomeScore());
        assertEquals(0, matchTwo.getAwayScore());
    }
}
