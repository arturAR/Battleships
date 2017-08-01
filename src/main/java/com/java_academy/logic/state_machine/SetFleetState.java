package com.java_academy.logic.state_machine;

import com.java_academy.logic.model.MessageObject;
import com.java_academy.logic.model.Player;
import com.java_academy.logic.model.Players;
import com.java_academy.logic.state_machine.core.GameState;

import java.util.function.Consumer;

/**
 * @author Bartlomiej Janik
 * @since 7/31/2017
 */
public class SetFleetState implements GameState {

    @Override
    public void display(Consumer<MessageObject> displayConsumer) {

    }

    @Override
    public GameState changeState(String message) {
        //TODO handling user input about fleet settings
        createBoardWithFleet(Players.FIRST_PLAYER.getPlayer());
        createBoardWithFleet(Players.SECOND_PLAYER.getPlayer());

        return new PlayerActionState(Players.FIRST_PLAYER);
    }

    private void createBoardWithFleet(Player player) {
        //TODO SHIPYARD USER
        player.createFleet();
    }

    @Override
    public boolean isEndingState() {
        return false;
    }
}