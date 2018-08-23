package application.battleships.Services;

import application.battleships.Exceptions.WrongGameIdException;
import application.battleships.Exceptions.WrongPlayerIdException;
import application.battleships.Models.GameModel;
import application.battleships.Models.PlayerModel;
import application.battleships.Repsoitories.GameModelRepository;
import application.battleships.Repsoitories.PlayerModelRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Objects;
import java.util.Optional;

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
}
