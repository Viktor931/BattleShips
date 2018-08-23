package application.battleships.Exceptions;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class WrongPlayerIdExceptionTest {
    @Test
    public void testExceptionEmail(){
        //when
        WrongPlayerIdException exception = new WrongPlayerIdException("testId");
        //then
        assertTrue("testId".equals(exception.getPlayerId()));
    }
}
