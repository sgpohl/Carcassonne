package main;

import controller.Player;
import controller.instances.GameController;
import controller.instances.PlayerController;
import controller.instances.UIController;
import controller.instances.ais.NearestToPositionAI;
import logic.GameFieldFactory;
import logic.Position;
import model.GameField;
import model.GameFieldImpl;
import view.UI;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class StartGame {


    public static void main(String[] args) {

        UI ui = new UI();

        Player player = new PlayerController(ui);
        Player ai = new NearestToPositionAI(new Position(0, 0));

        GameField field = GameFieldFactory.getSampleField(new Random(1), 100);
        ui.drawAll(field);

        Collection<Player> players = Arrays.asList(ai, player);
        GameController game = new GameController(field, players);
        UIController uiController = new UIController(ui);

        game.startGame();


    }


}
