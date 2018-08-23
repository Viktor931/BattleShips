package application.battleships.Util;

import application.battleships.Exceptions.WrongPlayerIdException;
import application.battleships.Exceptions.WrongPlayerNameException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler(WrongPlayerNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Map<String, Object> processWrongPlayerNameException(WrongPlayerNameException ex) {
        Map<String, Object> json = new HashMap<>();
        json.put("error-code", "error.username-already-taken");
        json.put("error-arg", ex.getEmail());
        return json;
    }

    @ExceptionHandler(WrongPlayerIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, Object> processWrongPlayerIdException(WrongPlayerIdException ex) {
        Map<String, Object> json = new HashMap<>();
        json.put("error-code", "error.unknown-user-id");
        json.put("error-arg", ex.getPlayerId());
        return json;
    }
}
