package application.battleships.Controllers;

import application.battleships.Models.PlayerModel;
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
}
