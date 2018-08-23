package application.battleships.Controllers;

import application.battleships.Models.GameModel;
import application.battleships.Models.PlayerModel;
import application.battleships.Services.GameService;
import application.battleships.Services.PlayerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerControllerTest {
    @InjectMocks
    private PlayerController playerController;

    @Mock
    private PlayerService playerService;

    @Mock
    private GameService gameService;

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
}
