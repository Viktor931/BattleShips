package application.battleships.Exceptions;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class WrongPlayerNameExceptionTest {
    @Test
    public void testExceptionEmail(){
        //when
        WrongPlayerNameException exception = new WrongPlayerNameException("testEmail");
        //then
        assertTrue("testEmail".equals(exception.getEmail()));
    }
}
