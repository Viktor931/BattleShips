package application.battleships.Controllers;

import application.battleships.Models.PlayerModel;
import application.battleships.Services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/player")
    public ResponseEntity<Map<String, Object>> player(@RequestParam(name="name") String name, @RequestParam(name="email") String email){
        PlayerModel playerModel = playerService.getPlayer(name, email);
        Map<String, Object> json = new HashMap<>();
        json.put("name", playerModel.getName());
        json.put("email", playerModel.getEmail());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/player/player-" + playerModel.getId());

        return new ResponseEntity<Map<String, Object>>(json, headers, HttpStatus.CREATED);
    }
}
