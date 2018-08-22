package application.battleships.Services;


import application.battleships.Exceptions.WrongPlayerIdException;
import application.battleships.Exceptions.WrongPlayerNameException;
import application.battleships.Models.PlayerModel;
import application.battleships.Repsoitories.PlayerModelRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlayerServiceTest {
    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerModelRepository playerModelRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPlayerWithCorrectName() {
        //given
        Optional<PlayerModel> playerModelOptional = playerModelWithName("testName");
        when(playerModelRepository.findByEmail("testEmail")).thenReturn(playerModelOptional);
        //when
        PlayerModel playerModel = playerService.getPlayer("testName", "testEmail");
        //then
        assertTrue("testName".equals(playerModel.getName()));
    }

    @Test(expected = WrongPlayerNameException.class)
    public void testGetPlayerWithWrongName() {
        //given
        Optional<PlayerModel> playerModelOptional = playerModelWithName("testName");
        when(playerModelRepository.findByEmail("testEmail")).thenReturn(playerModelOptional);
        //when
        PlayerModel playerModel = playerService.getPlayer("wrongName", "testEmail");
        //then
        fail();
    }

    private Optional<PlayerModel> playerModelWithName(String name) {
        PlayerModel playerModel = mock(PlayerModel.class);
        when(playerModel.getName()).thenReturn(name);
        return Optional.of(playerModel);
    }

    @Test
    public void testGetPlayerCreation() {
        //given
        when(playerModelRepository.findByEmail("testEmail")).thenReturn(Optional.empty());
        //when
        playerService.getPlayer("testName", "testEmail");
        //then
        verify(playerModelRepository, times(1)).save(any());
    }

    @Test
    public void testGetPlayerById() {
        //given
        Optional<PlayerModel> playerModelOptional = playerModelWithName("testName");
        when(playerModelRepository.findById(1)).thenReturn(playerModelOptional);
        //when
        PlayerModel playerModel = playerService.getPlayerById(1);
        //then
        assertTrue(playerModelOptional.get() == playerModel);
    }

    @Test(expected = WrongPlayerIdException.class)
    public void testGetPlayerByInvalidId() {
        //given
        when(playerModelRepository.findById(1)).thenReturn(Optional.empty());
        //when
        playerService.getPlayerById(1);
        //then
        fail();
    }
}
