package application.battleships.Models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerModelTest {
    private PlayerModel playerModel = new PlayerModel();

    @Test
    public void testEmail(){
        //given
        assertFalse("testEmail".equals(playerModel.getEmail()));
        //when
        playerModel.setEmail("testEmail");
        //then
        assertTrue("testEmail".equals(playerModel.getEmail()));
    }

    @Test
    public void testName(){
        //given
        assertFalse("testName".equals(playerModel.getName()));
        //when
        playerModel.setName("testName");
        //then
        assertTrue("testName".equals(playerModel.getName()));
    }

    @Test
    public void testId(){
        //given
        assertFalse(playerModel.getId() == -1);
        //when
        playerModel.setId(-1);
        //then
        assertTrue(playerModel.getId() == -1);
    }
}
