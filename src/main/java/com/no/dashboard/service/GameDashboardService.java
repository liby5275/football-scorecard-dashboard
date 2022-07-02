package com.no.dashboard.service;

import com.no.dashboard.exception.GameDashBoardExceptions.*;
import com.no.dashboard.model.Match;

import java.util.Map;

public interface GameDashboardService {

    String startMatch(String homeTeam, String awayTeam) throws StartGameException;

    String updateScore(String matchId, int homeScore, int awayScore) throws UpdateGameException;

    Map<String, Match> getAllLiveMatches();
}
