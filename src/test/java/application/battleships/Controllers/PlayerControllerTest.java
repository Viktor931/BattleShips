package application.battleships.Controllers;

import application.battleships.Models.GameModel;
import application.battleships.Models.PlayerModel;
import application.battleships.Services.GameService;
import application.battleships.Services.PlayerService;
import application.battleships.Util.GameDataFormatter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlayerControllerTest {
    @InjectMocks
    private PlayerController playerController;

    @Mock
    private PlayerService playerService;

    @Mock
    private GameService gameService;

    @Mock
    private GameDataFormatter gameDataFormatter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPlayer(){
        //given
        PlayerModel playerModel = playerModelWithNameAndEmail("testName", "testEmail");
        when(playerService.getPlayer("testName", "testEmail")).thenReturn(playerModel);
        //when
        ResponseEntity<Map<String, Object>> response = playerController.player("testName", "testEmail");
        //then
        assertTrue(response.getStatusCode().value() == 201);
        assertTrue(response.getBody().containsKey("name"));
        assertTrue(response.getBody().containsKey("email"));
    }

    private PlayerModel playerModelWithNameAndEmail(String name, String email) {
        PlayerModel playerModel = mock(PlayerModel.class);
        when(playerModel.getName()).thenReturn(name);
        when(playerModel.getEmail()).thenReturn(email);
        return playerModel;
    }

    @Test
    public void testPlayerProfile(){
        //given
        PlayerModel playerModel = playerModelWithNameAndEmail("testName", "testEmail");
        when(playerService.getPlayerById(1)).thenReturn(playerModel);
        //when
        ResponseEntity<Map<String, Object>> response = playerController.playerProfile(1);
        //then
        assertTrue("testName".equals(response.getBody().get("name")));
        assertTrue("testEmail".equals(response.getBody().get("email")));
    }

    @Test
    public void testCreateGame(){
        //given
        GameModel gameModel = createGameWithId1WithStartingPlayerWithId(1);
        when(gameService.createGame(1, 2)).thenReturn(gameModel);
        //when
        ResponseEntity<Map<String, Object>> response = playerController.createGame(1, 2);
        //then
        assertTrue("1".equals(response.getBody().get("player_id").toString()));
        assertTrue("2".equals(response.getBody().get("opponent_id").toString()));
        assertTrue("1".equals(response.getBody().get("game_id").toString()));
        assertTrue("1".equals(response.getBody().get("starting").toString()));
    }

    private GameModel createGameWithId1WithStartingPlayerWithId(long id) {
        GameModel gameModel = mock(GameModel.class);
        PlayerModel playerModel = mock(PlayerModel.class);
        when(playerModel.getId()).thenReturn(id);
        when(gameModel.getPlayer1()).thenReturn(playerModel);
        when(gameModel.getId()).thenReturn(1L);
        return gameModel;
    }

    @Test
    public void testViewGame(){
        //given
        Map<String, Object> json = new HashMap<>();
        GameModel game = mock(GameModel.class);
        when(gameService.findGameById(1)).thenReturn(game);
        when(gameDataFormatter.format(1, game)).thenReturn(json);
        //when
        ResponseEntity<Map<String, Object>> result = playerController.viewGame(1, 1);
        //then
        assertTrue(result.getBody() == json);
        assertTrue(result.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void testViewGames(){
        //given
        List<GameModel> games = createListOfGamesWithEveryStatus();
        when(gameService.getAllGamesForPlayer(1)).thenReturn(games);
        //when
        ResponseEntity<Map<String, Object>> result = playerController.viewGames(1);
        //then
        assertTrue(result.getStatusCode() == HttpStatus.OK);
        assertTrue(result.getBody().containsKey("games"));
    }

    private List<GameModel> createListOfGamesWithEveryStatus() {
        List<GameModel> listOfGames = new ArrayList<>();
        listOfGames.add(createGameWithPlayersAndWinnerId(1, 2, -1)); //status in progress
        listOfGames.add(createGameWithPlayersAndWinnerId(1, 2, 1)); //status player 1 won
        listOfGames.add(createGameWithPlayersAndWinnerId(1, 2, 2)); //status player 2 won
        return listOfGames;
    }

    private GameModel createGameWithPlayersAndWinnerId(long player1Id, long player2Id, long winnerId) {
        GameModel gameModel = mock(GameModel.class);
        PlayerModel playerModel1 = mock(PlayerModel.class);
        PlayerModel playerModel2 = mock(PlayerModel.class);
        when(playerModel1.getId()).thenReturn(player1Id);
        when(playerModel2.getId()).thenReturn(player2Id);
        when(gameModel.getPlayer1()).thenReturn(playerModel1);
        when(gameModel.getPlayer2()).thenReturn(playerModel2);
        when(gameModel.getWinnersId()).thenReturn(winnerId);
        return gameModel;
    }

    @Test
    public void testViewGamesWithNoGames(){
        //given
        when(gameService.getAllGamesForPlayer(1)).thenReturn(new ArrayList<>());
        //when
        ResponseEntity<Map<String, Object>> result = playerController.viewGames(1);
        //then
        assertTrue(result.getStatusCode() == HttpStatus.NO_CONTENT);
    }

    @Test
    public void testFireWinningShots(){
        //given
        when(gameService.findGameById(1)).thenReturn(mock(GameModel.class));
        //when
        ResponseEntity<Map<String, Object>> result = playerController.fireShots(1, 1, new String[]{"1xA"});
        //then
        assertTrue(result.getStatusCode() == HttpStatus.OK);
        assertTrue(((HashMap) result.getBody().get("game")).containsKey("won"));
    }

    @Test
    public void testFireShots(){
        //given
        GameModel gameModel = mock(GameModel.class);
        when(gameModel.getWinnersId()).thenReturn(-1L);
        when(gameService.findGameById(1)).thenReturn(gameModel);
        //when
        ResponseEntity<Map<String, Object>> result = playerController.fireShots(1, 1, new String[]{"1xA"});
        //then
        assertTrue(result.getStatusCode() == HttpStatus.OK);
        assertTrue(((HashMap) result.getBody().get("game")).containsKey("player_turn"));
    }

    @Test
    public void testTurnOnAutopilot(){
        //when
        ResponseEntity<Map<String, Object>> result = playerController.turnOnAutoPilot(1, 1);
        //then
        verify(gameService).turnOnAutoPilot(1, 1);
        assertTrue(result.getStatusCode() == HttpStatus.NO_CONTENT);
    }
}
