package application.battleships.Models;

import javax.persistence.*;
import java.util.ArrayList;

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
    private boolean isPlayerOnesTurn = true;
    private int status = 0;


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
    }

    public ArrayList<ArrayList<CoordinatesModel>> getPlayer1Ships() {
        return player1Ships;
    }

    public void setPlayer2Ships(ArrayList<ArrayList<CoordinatesModel>> player2Ships) {
        this.player2Ships = player2Ships;
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

//    public long getWinnersId(){
//        if(status == IN_PROGRESS){
//            return -1;
//        }
//        if(status == PLAYER1_WON){
//            return player1.getId();
//        }
//        return player2.getId();
//    }
}
