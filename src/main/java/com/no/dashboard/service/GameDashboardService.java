package com.no.dashboard.service;

import com.no.dashboard.exception.GameDashBoardExceptions;
import com.no.dashboard.model.Match;

import java.util.Map;

public interface GameDashboardService {

    public Map<String, Match> startMatch(String homeTeam, String awayTeam) throws GameDashBoardExceptions.StartGameException;
}
