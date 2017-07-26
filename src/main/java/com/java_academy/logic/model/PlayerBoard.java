package com.java_academy.logic.model;

import com.java_academy.logic.Players;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class PlayerBoard {

    private Map<Integer, Cell> board;
    private Players player;

    public PlayerBoard(Players player) {
        this.player = player;
        board = new HashMap<>();
        IntStream.range(0, 100).forEach(i -> board.put(i, Cell.FREE));
    }

    public void fillWithShips(List<Ship> shipList) {
        IntStream.range(0, shipList.size()).forEach(i -> addShip(shipList.get(i)));
    }

    public void addShip(Ship ship) {
        List<Integer> shipPos = ship.getShipPosition();
        IntStream.range(0, shipPos.size()).forEach(j -> board.put(shipPos.get(j), Cell.SHIP));
    }

    public Map<Integer, Cell> getBoard() {
        return board;
    }

    public Players getPlayer() {
        return player;
    }
}
