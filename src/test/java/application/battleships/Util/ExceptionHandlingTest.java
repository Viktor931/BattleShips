package application.battleships.Util;

import application.battleships.Exceptions.WrongGameIdException;
import application.battleships.Exceptions.WrongPlayerIdException;
import application.battleships.Exceptions.WrongPlayerNameException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ExceptionHandlingTest {
    private ExceptionHandling exceptionHandling = new ExceptionHandling();

    @Test
    public void testProcessWrongPlayerNameException(){
        //given
        WrongPlayerNameException exception = mock(WrongPlayerNameException.class);
        //when
        Map<String, Object> response = exceptionHandling.processWrongPlayerNameException(exception);
        //then
        assertTrue(response.containsKey("error-code"));
        assertTrue(response.containsKey("error-arg"));
    }

    @Test
    public void testProcessWrongPlayerIdException(){
        //given
        WrongPlayerIdException exception = mock(WrongPlayerIdException.class);
        //when
        Map<String, Object> response = exceptionHandling.processWrongPlayerIdException(exception);
        //then
        assertTrue(response.containsKey("error-code"));
        assertTrue(response.containsKey("error-arg"));
    }

    @Test
    public void testProcessWrongGameIdException(){
        //given
        WrongGameIdException exception = mock(WrongGameIdException.class);
        //when
        Map<String, Object> response = exceptionHandling.processWrongGameIdException(exception);
        //then
        assertTrue(response.containsKey("error-code"));
        assertTrue(response.containsKey("error-arg"));
    }
}