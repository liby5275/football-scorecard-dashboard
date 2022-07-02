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
    public String startMatch(String homeTeam, String awayTeam) throws StartGameException {
        logger.info("Starting the match between {} and {}", homeTeam, awayTeam);
        String matchIdentifier = null;
        if(isValidTeams(homeTeam, awayTeam)){
            Match match = new Match(capitalize(homeTeam),capitalize(awayTeam), 0,0);
            matchIdentifier = new StringBuilder(homeTeam).
                    append(awayTeam).toString().toLowerCase();
            this.liveMatches.put(matchIdentifier, match);
        } else {
            logger.error("Incorrect Team details provided");
            throw new StartGameException("Can't start the game.Incorrect Team details provided", null);
        }
        return matchIdentifier;

    }

    @Override
    public String updateScore(String matchId, int homeScore, int awayScore) throws UpdateGameException {
        logger.info("Updating the score of the match {}", matchId);
        if(liveMatches.isEmpty() || !liveMatches.containsKey(matchId)) {
            throw new UpdateGameException("Trying to update a match which is not present." +
                    " Please provide valid match", null);
        }else if(homeScore != 0 || awayScore !=0) { // if both away and home score to update is zero, no need to
            // update since its already there
            this.liveMatches.compute(matchId, (key,value) -> {
                value.setHomeScore(homeScore);
                value.setAwayScore(awayScore);
                return value;
            });
        }
        return matchId;
    }

    @Override
    public Map<String, Match> getAllLiveMatches() {
        logger.info("Fetching all the live matches present in the system");
        return this.liveMatches;
    }

    private boolean isValidTeams(String homeTeam, String awayTeam) {

        return null != homeTeam && null != awayTeam && !homeTeam.isBlank() && !awayTeam.isBlank();
    }

    private String capitalize(String input) {
        if(input.length() > 1){
            return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
        } else {
            return input.toUpperCase();
        }
    }


}
