package application.battleships.Util;

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
}