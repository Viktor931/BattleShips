package application.battleships.Models;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class GameModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private PlayerModel player1;
    @ManyToOne
    private PlayerModel player2;
    private ArrayList<ArrayList<CoordinatesModel>> player1Ships;
    private ArrayList<ArrayList<CoordinatesModel>> player2Ships;

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
}
