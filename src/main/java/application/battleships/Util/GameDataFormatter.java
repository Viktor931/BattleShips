package application.battleships.Util;

import application.battleships.Models.CoordinatesModel;
import application.battleships.Models.GameModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class GameDataFormatter {
    private final char NOT_HIT_SHIP = '#';
    private final char MISSED_SHOT = 'O';
    private final char HIT_SHIP = 'X';
    private final char EMPTY = '.';

    public Map<String, Object> format(long playerId, GameModel game) {
        Map<String, Object> result = new HashMap<>();
        result.put("game", getGameData(game));
        result.put("self", getSelfData(playerId, game));
        result.put("opponent", getOpponentData(playerId, game));
        return result;
    }

    private Map<String, Object> getOpponentData(long playerId, GameModel game) {
        Map<String, Object> opponentData = new HashMap<>();
        if(game.getPlayer1().getId() == playerId){
            opponentData.put("player_id", game.getPlayer2().getId());
            opponentData.put("board", createEnemyBoardData(game.getPlayer2Ships(), game.getPlayer1ShotsFired()));
        } else {
            opponentData.put("player_id", game.getPlayer1().getId());
            opponentData.put("board", createEnemyBoardData(game.getPlayer1Ships(), game.getPlayer2ShotsFired()));
        }
        return opponentData;
    }

    private String[] createEnemyBoardData(ArrayList<ArrayList<CoordinatesModel>> playerShips, ArrayList<CoordinatesModel> opponentShots) {
        String[] board = createFriendlyBoardData(playerShips, opponentShots);
        return Arrays.stream(board).map(row -> row.replaceAll(String.valueOf(NOT_HIT_SHIP), String.valueOf(EMPTY))).toArray(String[]::new);
    }

    private Map<String, Object> getSelfData(long playerId, GameModel game) {
        Map<String, Object> selfData = new HashMap<>();
        selfData.put("player_id", playerId);
        if(game.getPlayer1().getId() == playerId){
            selfData.put("board", createFriendlyBoardData(game.getPlayer1Ships(), game.getPlayer2ShotsFired()));
        } else {
            selfData.put("board", createFriendlyBoardData(game.getPlayer2Ships(), game.getPlayer1ShotsFired()));
        }
        return selfData;
    }

    private String[] createFriendlyBoardData(ArrayList<ArrayList<CoordinatesModel>> playerShips, ArrayList<CoordinatesModel> opponentShots) { //todo very shitty string manipulation
        String[] board = {"..........", "..........", "..........", "..........", "..........", "..........", "..........", "..........", "..........", ".........."};
        playerShips.stream()
                .flatMap(ship -> ship.stream())
                .forEach(coordinate -> board[coordinate.getY()] = board[coordinate.getY()].substring(0, coordinate.getX()) + NOT_HIT_SHIP + board[coordinate.getY()].substring(coordinate.getX() + 1));
        opponentShots.stream().forEach(shot -> {
            if(board[shot.getY()].charAt(shot.getX()) == NOT_HIT_SHIP){
                board[shot.getY()] = board[shot.getY()].substring(0, shot.getX()) + HIT_SHIP + board[shot.getY()].substring(shot.getX() + 1);
            }
            if(board[shot.getY()].charAt(shot.getX()) == EMPTY){
                board[shot.getY()] = board[shot.getY()].substring(0, shot.getX()) + MISSED_SHOT + board[shot.getY()].substring(shot.getX() + 1);
            }
        });
        return board;
    }

    private Map<String, Object> getGameData(GameModel gameModel) {
        Map<String, Object> gameData = new HashMap<>();
        if(gameModel.getWinnersId() != -1){
            gameData.put("won", gameModel.getWinnersId());
        } else {
            gameData.put("player_turn", gameModel.getPlayerOnTurnId());
        }
        return gameData;
    }
}
