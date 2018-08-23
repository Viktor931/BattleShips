package application.battleships.Exceptions;

public class WrongGameIdException extends RuntimeException {
    private String gameId;
    public WrongGameIdException(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId(){
        return gameId;
    }
}
