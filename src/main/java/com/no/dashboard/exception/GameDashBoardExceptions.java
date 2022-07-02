package com.no.dashboard.exception;

public class GameDashBoardExceptions {

    public static class StartGameException extends Exception{
        public StartGameException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class UpdateGameException extends Exception{
        public UpdateGameException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class FinishGameException extends Exception{
        public FinishGameException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class GetMatchesOverviewException extends Exception{
        public GetMatchesOverviewException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
