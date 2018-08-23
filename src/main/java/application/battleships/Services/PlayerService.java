package application.battleships.Services;

import application.battleships.Exceptions.WrongPlayerIdException;
import application.battleships.Exceptions.WrongPlayerNameException;
import application.battleships.Models.PlayerModel;
import application.battleships.Repsoitories.PlayerModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    private PlayerModelRepository playerModelRepository;

    public PlayerModel getPlayer(String name, String email) {
        Optional<PlayerModel> playerModelOptional = playerModelRepository.findByEmail(email);
        if(playerModelOptional.isPresent()){
            if(name.equals(playerModelOptional.get().getName())){
                return playerModelOptional.get();
            } else {
                throw new WrongPlayerNameException(email);
            }
        } else {
            PlayerModel playerModel = new PlayerModel();
            playerModel.setName(name);
            playerModel.setEmail(email);
            return playerModelRepository.save(playerModel);
        }
    }

    public PlayerModel getPlayerById(long id) {
        Optional<PlayerModel> playerModelOptional = playerModelRepository.findById(id);
        if(playerModelOptional.isPresent()){
            return playerModelOptional.get();
        }
        throw new WrongPlayerIdException(String.valueOf(id));
    }
}
