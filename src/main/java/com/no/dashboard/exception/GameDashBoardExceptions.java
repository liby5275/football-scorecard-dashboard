package com.no.dashboard.exception;

public class GameDashBoardExceptions {

    public static class StartGameException extends Exception{
        public StartGameException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
