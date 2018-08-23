package application.battleships.Exceptions;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class WrongGameIdExceptionTest {
    @Test
    public void testExceptionEmail(){
        //when
        WrongGameIdException exception = new WrongGameIdException("testId");
        //then
        assertTrue("testId".equals(exception.getGameId()));
    }
}
