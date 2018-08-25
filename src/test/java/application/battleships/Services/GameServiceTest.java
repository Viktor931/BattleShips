package application.battleships.Services;

import application.battleships.Exceptions.GameFinishedException;
import application.battleships.Exceptions.InvalidSalvoRequestException;
import application.battleships.Exceptions.WrongGameIdException;
import application.battleships.Exceptions.WrongPlayerIdException;
import application.battleships.Models.CoordinatesModel;
import application.battleships.Models.GameModel;
import application.battleships.Models.PlayerModel;
import application.battleships.Repsoitories.GameModelRepository;
import application.battleships.Repsoitories.PlayerModelRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.*;

import static java.util.Objects.nonNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class GameServiceTest {
    @InjectMocks
    private GameService gameService = new GameService();

    @Mock
    private PlayerModelRepository playerModelRepository;

    @Mock
    private GameModelRepository gameModelRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateGame(){
        //given
        when(playerModelRepository.findById(anyLong())).thenReturn(Optional.of(mock(PlayerModel.class)));
        //when
        GameModel game = gameService.createGame(1, 2);
        //then
        verify(gameModelRepository, times(1)).save(any());
    }

    @Test (expected = WrongPlayerIdException.class)
    public void testInvalidPlayer1(){
        //given
        when(playerModelRepository.findById(1)).thenReturn(Optional.empty());
        when(playerModelRepository.findById(2)).thenReturn(Optional.of(mock(PlayerModel.class)));
        //when
        GameModel game = gameService.createGame(1, 2);
        //then
        fail();
    }

    @Test (expected = WrongPlayerIdException.class)
    public void testInvalidPlayer2(){
        //given
        when(playerModelRepository.findById(1)).thenReturn(Optional.of(mock(PlayerModel.class)));
        when(playerModelRepository.findById(2)).thenReturn(Optional.empty());
        //when
        GameModel game = gameService.createGame(1, 2);
        //then
        fail();
    }

    @Test
    public void testGetGameById(){
        //given
        GameModel gameModel = mock(GameModel.class);
        when(gameModelRepository.findById(1L)).thenReturn(Optional.of(gameModel));
        //when
        GameModel gameModelReturned = gameService.findGameById(1);
        //then
        assertTrue(gameModel == gameModelReturned);
    }

    @Test (expected = WrongGameIdException.class)
    public void testGetGameByInvalidId(){
        //given
        when(gameModelRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        gameService.findGameById(1);
        //then
        fail();
    }

    @Test
    public void testGetAllGamesForPlayer(){
        //given
        Mockito.doReturn(new ArrayList<>()).when(playerModelRepository).findAll();
        //when
        List<GameModel> result = gameService.getAllGamesForPlayer(1);
        //then
        assertTrue(result.size() == 0);
    }

    private GameModel createGameWithPlayerOfId(long playerId) {
        PlayerModel playerModel = mock(PlayerModel.class);
        when(playerModel.getId()).thenReturn(playerId);
        GameModel gameModel = mock(GameModel.class);
        when(gameModel.getPlayer1()).thenReturn(playerModel);
        return gameModel;
    }

    @Test (expected = InvalidSalvoRequestException.class)
    public void testFireShotsInvalidSalvoRequestForPlayer1(){
        //given
        GameModel gameModel = createGame(1, 2);
        when(gameModelRepository.findById(1L)).thenReturn(Optional.of(gameModel));
        //when
        Map<String, String> shotResults = gameService.fireShots(1, 1, new String[]{"1xA", "1xA"});
        //then
        fail();
    }

    @Test (expected = InvalidSalvoRequestException.class)
    public void testFireShotsAtNonExistentField(){
        //given
        GameModel gameModel = createGame(1, 2);
        when(gameModelRepository.findById(1L)).thenReturn(Optional.of(gameModel));
        //when
        Map<String, String> shotResults = gameService.fireShots(2, 1, new String[]{"0xZ"});
        //then
        fail();
    }

    @Test (expected = InvalidSalvoRequestException.class)
    public void testFireShotsWithInvalidSalvoSyntax(){
        //given
        GameModel gameModel = createGame(1, 2);
        when(gameModelRepository.findById(1L)).thenReturn(Optional.of(gameModel));
        //when
        Map<String, String> shotResults = gameService.fireShots(2, 1, new String[]{"1xAaa"});
        //then
        fail();
    }

    @Test (expected = InvalidSalvoRequestException.class)
    public void testFireShotsInvalidSalvoRequestForPlayer2(){
        //given
        GameModel gameModel = createGame(1, 2);
        when(gameModelRepository.findById(1L)).thenReturn(Optional.of(gameModel));
        //when
        Map<String, String> shotResults = gameService.fireShots(2, 1, new String[]{"1xA", "1xA"});
        //then
        fail();
    }

    @Test
    public void testFireShots(){
        //given
        GameModel gameModel = createGame(1, 2);
        when(gameModelRepository.findById(1L)).thenReturn(Optional.of(gameModel));
        //when
        Map<String, String> shotResults = gameService.fireShots(1, 1, new String[]{"1xA"});
        //then
        assertTrue("MISS".equals(shotResults.get("1xA")));
    }

    private GameModel createGame(long player1Id, long player2Id) {
        GameModel gameModel = mock(GameModel.class);
        PlayerModel player1 = mock(PlayerModel.class);
        PlayerModel player2 = mock(PlayerModel.class);
        when(player1.getId()).thenReturn(player1Id);
        when(player2.getId()).thenReturn(player2Id);
        when(gameModel.getPlayer1()).thenReturn(player1);
        when(gameModel.getPlayer2()).thenReturn(player2);
        ArrayList<ArrayList<CoordinatesModel>> notHitShips = new ArrayList<>();
        notHitShips.add(new ArrayList<>());
        when(gameModel.getPlayer1NotHitShipCoords()).thenReturn(notHitShips);
        when(gameModel.getPlayer2NotHitShipCoords()).thenReturn(notHitShips);
        when(gameModel.fireShot(1, 0, 0)).thenReturn("MISS");
        when(gameModel.getWinnersId()).thenReturn(-1L);
        return gameModel;
    }

    @Test (expected = WrongGameIdException.class)
    public void testFireShotsWithInvalidGameId(){
        //given
        when(gameModelRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        gameService.fireShots(1, 1, new String[]{"1xA"});
        //then
        fail();
    }

    @Test (expected = GameFinishedException.class)
    public void testGameFinishedException(){
        //given
        GameModel gameModel = createGame(1, 2);
        when(gameModelRepository.findById(1L)).thenReturn(Optional.of(gameModel));
        when(gameModel.getWinnersId()).thenReturn(1L);
        //when
        gameService.fireShots(1, 1, new String[]{"1xA"});
        //then
        fail();
    }

    @Test (expected = InvalidSalvoRequestException.class)
    public void testWrongShotSyntax(){
        //given
        GameModel gameModel = createGame(1, 2);
        when(gameModelRepository.findById(1L)).thenReturn(Optional.of(gameModel));
        when(gameModel.getWinnersId()).thenReturn(-1L);
        //when
        gameService.fireShots(1, 1, new String[]{"AxA"});
        //then
        fail();
    }

    @Test (expected = WrongGameIdException.class)
    public void testTunOnAiForInvalidGame(){
        //given
        when(gameModelRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        gameService.turnOnAutoPilot(1, 1);
        //then
        fail();
    }

    @Test (expected = WrongPlayerIdException.class)
    public void testTunOnAiForInvalidPlayer(){
        //given
        GameModel gameModel = createGame(1, 2);
        when(gameModelRepository.findById(1L)).thenReturn(Optional.of(gameModel));
        //when
        gameService.turnOnAutoPilot(11, 1);
        //then
        fail();
    }

    @Test
    public void testTunOnAiForPlayer1(){
        //given
        GameModel gameModel = createGame(1, 2);
        when(gameModelRepository.findById(1L)).thenReturn(Optional.of(gameModel));
        //when
        gameService.turnOnAutoPilot(1, 1);
        //then
        verify(gameModelRepository).save(gameModel);
    }

    @Test
    public void testTunOnAiForPlayer2(){
        //given
        GameModel gameModel = createGame(1, 2);
        when(gameModelRepository.findById(1L)).thenReturn(Optional.of(gameModel));
        //when
        gameService.turnOnAutoPilot(2, 1);
        //then
        verify(gameModelRepository).save(gameModel);
    }
}
