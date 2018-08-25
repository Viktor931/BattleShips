package application.battleships.Models;

import application.battleships.Exceptions.GameFinishedException;
import application.battleships.Exceptions.NotPlayersTurnException;
import org.junit.Test;

import javax.persistence.Id;

import java.util.ArrayList;
import java.util.Objects;

import static java.util.Objects.nonNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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

    @Test
    public void testGameInProgress(){
        //given
        gameModel.setStatus(0);//status in progress
        //when
        long winnersId = gameModel.getWinnersId();
        //then
        assertTrue(winnersId == -1);
    }

    @Test
    public void testGamePlayer1Won(){
        //given
        gameModel.setStatus(1);//status player 1 won
        gameModel.setPlayer1(playerModelMockWithId(1));
        //when
        long winnersId = gameModel.getWinnersId();
        //then
        assertTrue(winnersId == 1);
    }

    @Test
    public void testGamePlayer2Won(){
        //given
        gameModel.setStatus(2);//status player 2 won
        gameModel.setPlayer2(playerModelMockWithId(2));
        //when
        long winnersId = gameModel.getWinnersId();
        //then
        assertTrue(winnersId == 2);
    }

    @Test
    public void testPlayer1NotHitCoords(){
        //given
        assertFalse(nonNull(gameModel.getPlayer1NotHitShipCoords()));
        //when
        gameModel.setPlayer1Ships(new ArrayList<>());
        //then
        assertTrue(nonNull(gameModel.getPlayer1NotHitShipCoords()));
    }

    @Test
    public void testPlayer2NotHitCoords(){
        //given
        assertFalse(nonNull(gameModel.getPlayer2NotHitShipCoords()));
        //when
        gameModel.setPlayer2Ships(new ArrayList<>());
        //then
        assertTrue(nonNull(gameModel.getPlayer2NotHitShipCoords()));
    }

    @Test(expected = NotPlayersTurnException.class)
    public void testFireShotNotPlayers1Turn(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        //when
        gameModel.fireShot(2, 0, 0);
        //then
        fail();
    }

    @Test(expected = NotPlayersTurnException.class)
    public void testFireShotNotPlayers2Turn(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        gameModel.nextTurn();
        //when
        gameModel.fireShot(1, 0, 0);
        //then
        fail();
    }

    @Test
    public void testFireShotMISS(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        ArrayList<ArrayList<CoordinatesModel>> ships = new ArrayList<>();
        ships.add(createOneDimensionalShip(0, 0));
        gameModel.setPlayer2Ships(ships);
        //when
        String result = gameModel.fireShot(1, 1, 0);
        //then
        assertTrue("MISS".equals(result));
    }

    @Test
    public void testFireShotHIT(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        ArrayList<ArrayList<CoordinatesModel>> ships = new ArrayList<>();
        ships.add(createTwoDimensionalShip(0, 0, 1, 0));
        gameModel.setPlayer2Ships(ships);
        //when
        String result = gameModel.fireShot(1, 0, 0);
        //then
        assertTrue("HIT".equals(result));
    }

    private ArrayList<CoordinatesModel> createTwoDimensionalShip(int x1, int y1, int x2, int y2) {
        ArrayList<CoordinatesModel> ship = new ArrayList<>();
        ship.add(createCoordinates(x1, y1));
        ship.add(createCoordinates(x2, y2));
        return ship;
    }

    @Test
    public void testFireShotKILLAsPlayer2(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        gameModel.nextTurn();
        ArrayList<ArrayList<CoordinatesModel>> ships = new ArrayList<>();
        ships.add(createOneDimensionalShip(0, 0));
        gameModel.setPlayer1Ships(ships);
        gameModel.setPlayer2Ships(ships);
        //when
        String result = gameModel.fireShot(2, 0, 0);
        //then
        assertTrue("KILL".equals(result));
    }

    @Test
    public void testFireShotKILLAsPlayer1(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        ArrayList<ArrayList<CoordinatesModel>> ships = new ArrayList<>();
        ships.add(createOneDimensionalShip(0, 0));
        gameModel.setPlayer1Ships(ships);
        gameModel.setPlayer2Ships(ships);
        //when
        String result = gameModel.fireShot(1, 0, 0);
        //then
        assertTrue("KILL".equals(result));
    }

    private ArrayList<CoordinatesModel> createOneDimensionalShip(int x, int y) {
        ArrayList<CoordinatesModel> ship = new ArrayList<>();
        ship.add(createCoordinates(x, y));
        return ship;
    }

    private CoordinatesModel createCoordinates(int x, int y) {
        CoordinatesModel coordinatesModel = mock(CoordinatesModel.class);
        when(coordinatesModel.getX()).thenReturn(x);
        when(coordinatesModel.getY()).thenReturn(y);
        return coordinatesModel;
    }

    @Test
    public void testGetShotsFiredPlayer1(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        gameModel.setPlayer2Ships(new ArrayList<>());
        assertTrue(gameModel.getPlayer1ShotsFired().isEmpty());
        //when
        gameModel.fireShot(1, 0, 0);
        //then
        assertTrue(gameModel.getPlayer1ShotsFired().size() == 1);
    }

    @Test
    public void testGetShotsFiredPlayer2(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        gameModel.setPlayer1Ships(new ArrayList<>());
        gameModel.nextTurn();
        assertTrue(gameModel.getPlayer2ShotsFired().isEmpty());
        //when
        gameModel.fireShot(2, 0, 0);
        //then
        assertTrue(gameModel.getPlayer2ShotsFired().size() == 1);
    }

    @Test
    public void testTurnAiForPlayer1(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        gameModel.setPlayer1Ships(new ArrayList<>());
        //when
        gameModel.turnOnAiForPlayer1();
        //then
        assertTrue(gameModel.getPlayerOnTurnId() == 2);
    }

    @Test
    public void testTurnAiForPlayer2(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        gameModel.setPlayer2Ships(new ArrayList<>());
        gameModel.nextTurn();
        //when
        gameModel.turnOnAiForPlayer2();
        //then
        assertTrue(gameModel.getPlayerOnTurnId() == 1);
    }

    @Test
    public void testSettingBothPlayersToBots(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        gameModel.setPlayer1Ships(new ArrayList<>());
        gameModel.setPlayer2Ships(new ArrayList<>());
        //when
        gameModel.turnOnAiForPlayer1();
        gameModel.turnOnAiForPlayer2();
        //then
        assertTrue(gameModel.getWinnersId() != -1);
    }

    @Test
    public void testNextTurnWhenGameIsWon(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        gameModel.setStatus(1);//game is not in progress anymore
        long playerOnTurn = gameModel.getPlayerOnTurnId();
        //when
        gameModel.nextTurn();
        //then
        assertTrue(gameModel.getPlayerOnTurnId() == playerOnTurn);
    }

    @Test
    public void testAiPlayingForPlayer1(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        gameModel.setPlayer2Ships(new ArrayList<>());
        gameModel.turnOnAiForPlayer2();
        assertTrue(gameModel.getPlayerOnTurnId() == 1);
        //when
        gameModel.nextTurn();
        //then
        assertTrue(gameModel.getPlayerOnTurnId() == 1);
    }

    @Test
    public void testAiPlayingForPlayer2(){
        //given
        gameModel.setPlayer1(playerModelMockWithId(1));
        gameModel.setPlayer2(playerModelMockWithId(2));
        gameModel.setPlayer1Ships(new ArrayList<>());
        gameModel.turnOnAiForPlayer1();
        gameModel.nextTurn();
        assertTrue(gameModel.getPlayerOnTurnId() == 2);
        //when
        gameModel.nextTurn();
        //then
        assertTrue(gameModel.getPlayerOnTurnId() == 2);
    }
}
