package application.battleships.Util;

import application.battleships.Models.CoordinatesModel;
import application.battleships.Models.GameModel;
import application.battleships.Models.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameDataFormatterTest {
    private GameDataFormatter gameDataFormatter = new GameDataFormatter();
    private GameModel gameModel;

    @Before
    public void setUpGameModel(){
        gameModel = mock(GameModel.class);
        PlayerModel playerModel1 = mock(PlayerModel.class);
        PlayerModel playerModel2 = mock(PlayerModel.class);
        when(playerModel1.getId()).thenReturn(1L);
        when(playerModel2.getId()).thenReturn(2L);
        when(gameModel.getPlayer1()).thenReturn(playerModel1);
        when(gameModel.getPlayer2()).thenReturn(playerModel2);
    }

    @Test
    public void testGameFormatterForPlayer1(){
        //given
        ArrayList<ArrayList<CoordinatesModel>> shipAtLocation00 = createShipsWithShipAt00();
        when(gameModel.getPlayer1Ships()).thenReturn(shipAtLocation00);
        when(gameModel.getPlayer2Ships()).thenReturn(shipAtLocation00);
        //when
        Map<String, Object> formattedOutput = gameDataFormatter.format(1, gameModel);
        //then
        assertTrue(nonNull(formattedOutput.get("self")));
    }

    @Test
    public void testGameFormatterForPlayer2(){
        //given
        ArrayList<ArrayList<CoordinatesModel>> shipAtLocation00 = createShipsWithShipAt00();
        when(gameModel.getPlayer1Ships()).thenReturn(shipAtLocation00);
        when(gameModel.getPlayer2Ships()).thenReturn(shipAtLocation00);
        ArrayList<CoordinatesModel> shotsAt00And01 = createShotsAt00And01();
        when(gameModel.getPlayer1ShotsFired()).thenReturn(shotsAt00And01);
        when(gameModel.getPlayer2ShotsFired()).thenReturn(shotsAt00And01);
        //when
        Map<String, Object> formattedOutput = gameDataFormatter.format(2, gameModel);
        //then
        assertTrue(nonNull(formattedOutput.get("opponent")));
    }

    private ArrayList<CoordinatesModel> createShotsAt00And01() {
        ArrayList<CoordinatesModel> shots = new ArrayList<>();
        shots.add(getCoordinatesModel(0, 0));
        shots.add(getCoordinatesModel(0, 1));
        return shots;
    }

    private ArrayList<ArrayList<CoordinatesModel>> createShipsWithShipAt00() {
        CoordinatesModel coordinatesModel = getCoordinatesModel(0, 0);
        ArrayList<ArrayList<CoordinatesModel>> result = new ArrayList<>();
        ArrayList<CoordinatesModel> ship = new ArrayList<>();
        ship.add(coordinatesModel);
        result.add(ship);
        return result;
    }

    private CoordinatesModel getCoordinatesModel(int x, int y) {
        CoordinatesModel coordinatesModel = mock(CoordinatesModel.class);
        when(coordinatesModel.getX()).thenReturn(x);
        when(coordinatesModel.getY()).thenReturn(y);
        return coordinatesModel;
    }

    @Test
    public void testGameFormatterForGameInProgress(){
        //given
        when(gameModel.getWinnersId()).thenReturn(-1L);
        //when
        Map<String, Object> formattedOutput = gameDataFormatter.format(2, gameModel);
        //then
        assertTrue(((HashMap) formattedOutput.get("game")).containsKey("player_turn"));
    }
}
