package com.no.dashboard.service;

import com.no.dashboard.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static com.no.dashboard.exception.GameDashBoardExceptions.*;

public class FifaDashboardService implements GameDashboardService{

    Logger logger = LoggerFactory.getLogger(FifaDashboardService.class);

    private Map<String, Match> liveMatches = new ConcurrentHashMap<String, Match>() ;


    @Override
    public Map<String, Match> startMatch(String homeTeam, String awayTeam) throws StartGameException {
        logger.info("Starting the match between {} and {}", homeTeam, awayTeam);
        if(isValidTeams(homeTeam, awayTeam)){
            Match match = new Match(homeTeam,awayTeam, 0,0);
            String matchIdentifier = new StringBuilder(homeTeam).
                    append(awayTeam).toString().toLowerCase();
            liveMatches.put(matchIdentifier, match);
        } else {
            logger.error("Incorrect Team details provided");
            throw new StartGameException("Can't start the game.Incorrect Team details provided", null);
        }
        return liveMatches;

    }

    private boolean isValidTeams(String homeTeam, String awayTeam) {

        return null != homeTeam && null != awayTeam && !homeTeam.isBlank() && !awayTeam.isBlank();
    }


}
