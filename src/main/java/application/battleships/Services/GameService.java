package application.battleships.Services;

import application.battleships.Exceptions.GameFinishedException;
import application.battleships.Exceptions.InvalidSalvoRequestException;
import application.battleships.Exceptions.WrongGameIdException;
import application.battleships.Exceptions.WrongPlayerIdException;
import application.battleships.Models.CoordinatesModel;
import application.battleships.Models.GameModel;
import application.battleships.Models.PlayerModel;
import application.battleships.Repsoitories.GameModelRepository;
import application.battleships.Repsoitories.PlayerModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class GameService {
    @Autowired
    private PlayerModelRepository playerModelRepository;

    @Autowired
    private GameModelRepository gameModelRepository;

    private Random random = new Random();

    public GameModel createGame(long playerId, long opponentId) {
        GameModel game = new GameModel();
        populatePlayers(playerId, opponentId, game);
        populateShips(game);
        return gameModelRepository.save(game);
    }

    private void populateShips(GameModel game) {
        game.setPlayer1Ships(generateRandomShips());
        game.setPlayer2Ships(generateRandomShips());
    }

    private ArrayList<ArrayList<CoordinatesModel>> generateRandomShips() {
        int numberOfShipTypes = 5;
        List<CoordinatesModel> availableCoordinates = IntStream.range(0, 10)
                                                        .boxed()
                                                        .flatMap(this::generateCoordinatesForRow)
                                                        .collect(Collectors.toList());

        ArrayList<ArrayList<CoordinatesModel>> ships = new ArrayList<>();
        for(int shipSize = 1; shipSize < numberOfShipTypes; shipSize++){
            for(int numberOfShips = 0; numberOfShips < numberOfShipTypes - shipSize; numberOfShips++){
                ships.add(createShip(shipSize, availableCoordinates));
            }
        }
        return ships;
    }

    private ArrayList<CoordinatesModel> createShip(int shipSize, List<CoordinatesModel> availableCoordinates) {
        ArrayList<CoordinatesModel> ship = null;
        while(Objects.isNull(ship)){
            CoordinatesModel startingCoordinate = availableCoordinates.get(random.nextInt(availableCoordinates.size()));
            if(random.nextBoolean()){
                ship = tryToCreateHorizontalShip(shipSize, startingCoordinate, availableCoordinates);
            } else {
                ship = tryToCreateVerticalShip(shipSize, startingCoordinate, availableCoordinates);
            }
        }
        return ship;
    }

    private ArrayList<CoordinatesModel> tryToCreateHorizontalShip(int shipSize, CoordinatesModel startingCoordinate, List<CoordinatesModel> availableCoordinates) {
        List<CoordinatesModel> possibleCoordinates = availableCoordinates.stream()
                                                                    .filter(coord -> coord.getY() == startingCoordinate.getY())
                                                                    .collect(Collectors.toList());
        int startX = startingCoordinate.getX();
        int endX = startX + shipSize - 1;
        for(int i = 0; i < shipSize; i++){
            final int firstX = startX;
            final int lastX = endX;
            List<CoordinatesModel> shipCoords = possibleCoordinates.stream()
                                                                .filter(coord -> coord.getX() >= firstX && coord.getX() <= lastX)
                                                                .collect(Collectors.toList());
            if(shipCoords.size() == shipSize){
                List<CoordinatesModel> noLongerAvailable = availableCoordinates.stream()
                                                                            .filter(coord -> coord.getX() >= firstX - 1  && coord.getX() <= lastX + 1)
                                                                            .filter(coord -> coord.getY() >= startingCoordinate.getY() - 1  && coord.getY() <= startingCoordinate.getY() + 1)
                                                                            .collect(Collectors.toList());
                availableCoordinates.removeAll(noLongerAvailable);
                return new ArrayList<>(shipCoords);
            }
            startX--;
            endX--;
        }
        return null;
    }

    private ArrayList<CoordinatesModel> tryToCreateVerticalShip(int shipSize, CoordinatesModel startingCoordinate, List<CoordinatesModel> availableCoordinates) {
        List<CoordinatesModel> possibleCoordinates = availableCoordinates.stream()
                                                                    .filter(coord -> coord.getX() == startingCoordinate.getX())
                                                                    .collect(Collectors.toList());
        int startY = startingCoordinate.getY();
        int endY = startY + shipSize - 1;
        for(int i = 0; i < shipSize; i++){
            final int firstY = startY;
            final int lastY = endY;
            List<CoordinatesModel> shipCoords = possibleCoordinates.stream()
                                                                .filter(coord -> coord.getY() >= firstY && coord.getY() <= lastY)
                                                                .collect(Collectors.toList());
            if(shipCoords.size() == shipSize){
                List<CoordinatesModel> noLongerAvailable = availableCoordinates.stream()
                                                                            .filter(coord -> coord.getY() >= firstY - 1  && coord.getY() <= lastY + 1)
                                                                            .filter(coord -> coord.getX() >= startingCoordinate.getX() - 1  && coord.getX() <= startingCoordinate.getX() + 1)
                                                                            .collect(Collectors.toList());
                availableCoordinates.removeAll(noLongerAvailable);
                return new ArrayList<>(shipCoords);
            }
            startY--;
            endY--;
        }
        return null;
    }

    private Stream<CoordinatesModel> generateCoordinatesForRow(int y) {
        return IntStream.range(0, 10)
                .mapToObj(x -> createCoordinate(x, y));
    }

    private CoordinatesModel createCoordinate(int x, int y) {
        CoordinatesModel coordinates = new CoordinatesModel(x, y);
        return coordinates;
    }

    private void populatePlayers(long playerId, long opponentId, GameModel game) {
        Optional<PlayerModel> player1ModelOptional = playerModelRepository.findById(playerId);
        Optional<PlayerModel> player2ModelOptional = playerModelRepository.findById(opponentId);
        if(!player1ModelOptional.isPresent()){
            throw new WrongPlayerIdException(String.valueOf(playerId));
        }
        if(!player2ModelOptional.isPresent()){
            throw new WrongPlayerIdException(String.valueOf(opponentId));
        }
        boolean swap = random.nextBoolean();
        game.setPlayer1(swap ? player1ModelOptional.get() : player2ModelOptional.get());
        game.setPlayer2(swap ? player2ModelOptional.get() : player1ModelOptional.get());
    }

    public GameModel findGameById(long gameId) {
        Optional<GameModel> gameModelOptional = gameModelRepository.findById(gameId);
        if(gameModelOptional.isPresent()){
            return gameModelOptional.get();
        }
        throw new WrongGameIdException(String.valueOf(gameId));
    }


    public List<GameModel> getAllGamesForPlayer(long id) {
        return gameModelRepository.findAll().stream()
                            .filter(game -> game.getPlayer1().getId() == id || game.getPlayer2().getId() == id)
                            .collect(Collectors.toList());
    }

    public Map<String, String> fireShots(long playerId, long gameId, String[] shots) {
        Optional<GameModel> game = gameModelRepository.findById(gameId);
        if(!game.isPresent()){
            throw new WrongGameIdException(String.valueOf(gameId));
        }
        if(game.get().getWinnersId() != -1){
            throw new GameFinishedException();
        }
        if(game.get().getPlayer1().getId() == playerId && game.get().getPlayer1NotHitShipCoords().size() != shots.length){
            throw new InvalidSalvoRequestException();
        }
        if(game.get().getPlayer2().getId() == playerId && game.get().getPlayer2NotHitShipCoords().size() != shots.length){
            throw new InvalidSalvoRequestException();
        }
        Map<String, String> result = Arrays.stream(shots).collect(Collectors.toMap(shot -> shot, shot -> processShot(shot, playerId, game.get())));
        game.get().nextTurn();
        gameModelRepository.save(game.get());
        return result;
    }

    private String processShot(String shot, long playerId, GameModel game) {
        String[] coordinates = shot.split("x");
        if(coordinates.length != 2 || coordinates[1].length() != 1){
            throw new InvalidSalvoRequestException();
        }
        int x;
        try{
            x = Integer.valueOf(coordinates[0]) - 1;
        } catch (Exception e){
            throw new InvalidSalvoRequestException();
        }
        int y = coordinates[1].charAt(0) - 65;
        if(x < 0 || x > 9 || y < 0 || y > 9){
            throw new InvalidSalvoRequestException();
        }
        return game.fireShot(playerId, x, y);
    }

    public void turnOnAutoPilot(long playerId, long gameId) {
        Optional<GameModel> gameModel = gameModelRepository.findById(gameId);
        if(!gameModel.isPresent()){
            throw new WrongGameIdException(String.valueOf(gameId));
        }
        if(gameModel.get().getPlayer1().getId() == playerId){
            gameModel.get().turnOnAiForPlayer1();
            gameModelRepository.save(gameModel.get());
            return;
        }
        if(gameModel.get().getPlayer2().getId() == playerId){
            gameModel.get().turnOnAiForPlayer2();
            gameModelRepository.save(gameModel.get());
            return;
        }
        throw new WrongPlayerIdException(String.valueOf(playerId));
    }
}
