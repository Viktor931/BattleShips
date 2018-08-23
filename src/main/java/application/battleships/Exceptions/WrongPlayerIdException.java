package application.battleships.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class WrongPlayerIdException extends RuntimeException {
    private String playerId;

    public WrongPlayerIdException(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }
}
