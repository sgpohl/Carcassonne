package main;

import controller.Player;
import controller.instances.GameController;
import controller.instances.PlayerController;
import controller.instances.UIController;
import controller.instances.ais.NearestToPositionAI;
import logic.Position;
import model.GameField;
import model.GameFieldImpl;
import view.UI;

import java.util.Arrays;
import java.util.Collection;

public class StartGame {


    public static void main(String[] args) {

        UI ui = new UI();

      //  Player player = new PlayerController();
        Player ai = new NearestToPositionAI(new Position(0, 0));

        GameField field = new GameFieldImpl();
        ui.drawAll(field);

        Collection<Player> players = Arrays.asList(ai);
        GameController game = new GameController(field, players);
        UIController uiController = new UIController(ui);

        game.startGame();


    }


}
