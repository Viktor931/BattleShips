package application.battleships.Repsoitories;

import application.battleships.Models.PlayerModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerModelRepository extends CrudRepository<PlayerModel, String> {
    Optional<PlayerModel> findByEmail(String email);
    Optional<PlayerModel> findById(long id);
}
