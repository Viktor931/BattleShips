package application.battleships.Models;

import application.battleships.Exceptions.GameFinishedException;
import application.battleships.Exceptions.NotPlayersTurnException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Entity
public class GameModel {
    private static int IN_PROGRESS = 0;
    private static int PLAYER1_WON = 1;
    private static int PLAYER2_WON = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private PlayerModel player1;
    @ManyToOne
    private PlayerModel player2;
    @Column(columnDefinition = "TEXT")
    private ArrayList<ArrayList<CoordinatesModel>> player1Ships;
    @Column(columnDefinition = "TEXT")
    private ArrayList<ArrayList<CoordinatesModel>> player2Ships;
    @Column(columnDefinition = "TEXT")
    private ArrayList<ArrayList<CoordinatesModel>> player1NotHitShipCoords;
    @Column(columnDefinition = "TEXT")
    private ArrayList<ArrayList<CoordinatesModel>> player2NotHitShipCoords;
    @Column(columnDefinition = "TEXT")
    private ArrayList<CoordinatesModel> player1ShotsFired = new ArrayList<>();
    @Column(columnDefinition = "TEXT")
    private ArrayList<CoordinatesModel> player2ShotsFired = new ArrayList<>();
    private boolean isPlayerOnesTurn = true;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PlayerModel getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerModel player) {
        this.player1 = player;
    }

    public PlayerModel getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerModel player) {
        this.player2 = player;
    }

    public void setPlayer1Ships(ArrayList<ArrayList<CoordinatesModel>> player1Ships) {
        this.player1Ships = player1Ships;
        this.player1NotHitShipCoords = new ArrayList<>(player1Ships.stream().map(list -> new ArrayList<>(list)).collect(Collectors.toList()));
    }

    public ArrayList<ArrayList<CoordinatesModel>> getPlayer1Ships() {
        return player1Ships;
    }

    public void setPlayer2Ships(ArrayList<ArrayList<CoordinatesModel>> player2Ships) {
        this.player2Ships = player2Ships;
        this.player2NotHitShipCoords = new ArrayList<>(player2Ships.stream().map(list -> new ArrayList<>(list)).collect(Collectors.toList()));
    }

    public ArrayList<ArrayList<CoordinatesModel>> getPlayer2Ships() {
        return player2Ships;
    }

    public long getPlayerOnTurnId() {
        return isPlayerOnesTurn ? player1.getId() : player2.getId();
    }

    public void nextTurn(){
        isPlayerOnesTurn = !isPlayerOnesTurn;
    }

    public long getWinnersId(){
        if(status == IN_PROGRESS){
            return -1;
        }
        if(status == PLAYER1_WON){
            return player1.getId();
        }
        return player2.getId();
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<ArrayList<CoordinatesModel>> getPlayer1NotHitShipCoords() {
        return player1NotHitShipCoords;
    }

    public ArrayList<ArrayList<CoordinatesModel>> getPlayer2NotHitShipCoords() {
        return player2NotHitShipCoords;
    }

    public ArrayList<CoordinatesModel> getPlayer1ShotsFired() {
        return player1ShotsFired;
    }

    public ArrayList<CoordinatesModel> getPlayer2ShotsFired() {
        return player2ShotsFired;
    }

    public String fireShot(long playerId, int x, int y){
        if(status != IN_PROGRESS){
            throw new GameFinishedException();
        }
        if(playerId == player1.getId()){
            if(!isPlayerOnesTurn){
                throw new NotPlayersTurnException();
            }
            player1ShotsFired.add(new CoordinatesModel(x, y));
            return fireShot(player2NotHitShipCoords, x, y);
        } else {
            if(isPlayerOnesTurn){
                throw new NotPlayersTurnException();
            }
            player2ShotsFired.add(new CoordinatesModel(x, y));
            return  fireShot(player1NotHitShipCoords, x, y);
        }
    }

    private String fireShot(ArrayList<ArrayList<CoordinatesModel>> notHitShipCoords, int x, int y){
        for(ArrayList<CoordinatesModel> ship : notHitShipCoords){
            for(CoordinatesModel coordinates : ship){
                if(coordinates.getX() == x && coordinates.getY() == y){
                    if(ship.size() == 1){
                        notHitShipCoords.remove(ship);
                        if(notHitShipCoords.isEmpty()){
                            setWinner();
                        }
                        return "KILL";
                    }
                    ship.remove(coordinates);
                    return "HIT";
                }
            }
        }
        return "MISS";
    }

    private void setWinner() {
        if(player1NotHitShipCoords.isEmpty()){
            status = PLAYER2_WON;
        }
        if(player2NotHitShipCoords.isEmpty()){
            status = PLAYER1_WON;
        }
    }
}
