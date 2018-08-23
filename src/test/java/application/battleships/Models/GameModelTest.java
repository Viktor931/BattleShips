package application.battleships.Models;

import org.junit.Test;

import javax.persistence.Id;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameModelTest {
    private GameModel gameModel = new GameModel();

    @Test
    public void testId(){
        //given
        assertFalse(gameModel.getId() == 1);
        //when
        gameModel.setId(1);
        //then
        assertTrue(gameModel.getId() == 1);
    }

    @Test
    public void testPlayer1(){
        //given
        PlayerModel playerModel = mock(PlayerModel.class);
        assertFalse(gameModel.getPlayer1() == playerModel);
        //when
        gameModel.setPlayer1(playerModel);
        //then
        assertTrue(gameModel.getPlayer1() == playerModel);
    }

    @Test
    public void testPlayer2(){
        //given
        PlayerModel playerModel = mock(PlayerModel.class);
        assertFalse(gameModel.getPlayer2() == playerModel);
        //when
        gameModel.setPlayer2(playerModel);
        //then
        assertTrue(gameModel.getPlayer2() == playerModel);
    }

    @Test
    public void testPlayer1Ships(){
        //given
        ArrayList ships = new ArrayList();
        assertFalse(gameModel.getPlayer1Ships() == ships);
        //when
        gameModel.setPlayer1Ships(ships);
        //then
        assertTrue(gameModel.getPlayer1Ships() == ships);
    }

    @Test
    public void testPlayer2Ships(){
        //given
        ArrayList ships = new ArrayList();
        assertFalse(gameModel.getPlayer2Ships() == ships);
        //when
        gameModel.setPlayer2Ships(ships);
        //then
        assertTrue(gameModel.getPlayer2Ships() == ships);
    }

    @Test
    public void testPlayerTurns(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        assertFalse(gameModel.getPlayerOnTurnId() == 2);
        //when
        gameModel.nextTurn();
        //then
        assertTrue(gameModel.getPlayerOnTurnId() == 2);
    }

    private PlayerModel playerModelMockWithId(long id) {
        PlayerModel playerModel = mock(PlayerModel.class);
        when(playerModel.getId()).thenReturn(id);
        return playerModel;
    }
}
