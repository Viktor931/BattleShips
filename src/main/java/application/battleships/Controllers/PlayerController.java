package application.battleships.Controllers;

import application.battleships.Models.PlayerModel;
import application.battleships.Services.PlayerService;
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

    @PostMapping("/player")
    public ResponseEntity<Map<String, Object>> player(@RequestParam(name="name") String name, @RequestParam(name="email") String email){
        PlayerModel playerModel = playerService.getPlayer(name, email);
        Map<String, Object> json = createJsonForPlayerModel(playerModel);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/player/player-" + playerModel.getId());

        return new ResponseEntity<>(json, headers, HttpStatus.CREATED);
    }

    private Map<String, Object> createJsonForPlayerModel(PlayerModel playerModel) {
        Map<String, Object> json = new HashMap<>();
        json.put("name", playerModel.getName());
        json.put("email", playerModel.getEmail());
        return json;
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<Map<String, Object>> playerProfile(@PathVariable long id){
        Map<String, Object> json = createJsonForPlayerModel(playerService.getPlayerById(id));
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
