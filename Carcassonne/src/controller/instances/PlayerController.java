package controller.instances;

import controller.Controller;
import controller.EventManager;
import controller.Player;

public class PlayerController implements Player, Controller {


    public PlayerController(){
        EventManager.getInstance().registerController(this);
    }

}
