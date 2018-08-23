package application.battleships.Controllers;

import application.battleships.Models.GameModel;
import application.battleships.Models.PlayerModel;
import application.battleships.Services.GameService;
import application.battleships.Services.PlayerService;
import application.battleships.Util.GameDataFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameDataFormatter gameDataFormatter;

    @PostMapping("/player")
    public ResponseEntity<Map<String, Object>> player(@RequestParam(name = "name") String name, @RequestParam(name = "email") String email) {
        PlayerModel playerModel = playerService.getPlayer(name, email);
        Map<String, Object> json = createJsonForPlayerModel(playerModel);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/player/" + playerModel.getId());

        return new ResponseEntity<>(json, headers, HttpStatus.CREATED);
    }

    private Map<String, Object> createJsonForPlayerModel(PlayerModel playerModel) {
        Map<String, Object> json = new HashMap<>();
        json.put("name", playerModel.getName());
        json.put("email", playerModel.getEmail());
        return json;
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<Map<String, Object>> playerProfile(@PathVariable long id) {
        Map<String, Object> json = createJsonForPlayerModel(playerService.getPlayerById(id));
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping("/player/{opponentId}/game")
    public ResponseEntity<Map<String, Object>> createGame(@RequestParam(name = "player_id") long playerId, @PathVariable long opponentId) {
        GameModel game = gameService.createGame(playerId, opponentId);
        Map<String, Object> json = new HashMap<>();
        json.put("player_id", playerId);
        json.put("opponent_id", opponentId);
        json.put("game_id", game.getId());
        json.put("starting", game.getPlayer1().getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/game/" + game.getId());
        return new ResponseEntity<>(json, headers, HttpStatus.CREATED);
    }

    @GetMapping("/player/{playerId}/game/{gameId}")
    public ResponseEntity<Map<String, Object>> viewGame(@PathVariable long playerId, @PathVariable long gameId){
        playerService.getPlayerById(playerId);//validates that player exists
        GameModel game = gameService.findGameById(gameId);
        Map<String, Object> json = gameDataFormatter.format(playerId, game);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
