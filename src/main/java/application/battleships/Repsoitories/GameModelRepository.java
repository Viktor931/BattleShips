package application.battleships.Repsoitories;

import application.battleships.Models.GameModel;
import org.springframework.data.repository.CrudRepository;

public interface GameModelRepository extends CrudRepository<GameModel, Long> {
}
