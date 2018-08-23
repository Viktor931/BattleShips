package application.battleships.Repsoitories;

import application.battleships.Models.GameModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameModelRepository extends CrudRepository<GameModel, Long> {
    List<GameModel> findAll();
}
