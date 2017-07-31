package com.java_academy.logic.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.java_academy.logic.model.Cell;


public class Ship {

	private int mastAmount;
	private Map<Integer, Cell> indexes;
	
	public Ship(int mastAmount) {
		this.mastAmount = mastAmount;
		indexes = new HashMap<>();
	}

	public Integer getMastAmount() {
		return mastAmount;
	}
	
	public boolean isFourMast() {
		if(mastAmount == 4) {
			return true;
		}
		return false;
	}

	public Map<Integer, Cell> getIndexes() {
		return indexes;
	}

	public void setIndex(Integer index, Cell cellState) {
		indexes.put(index, cellState);
	}
	
	public boolean containsIndex(Integer index) {
		return indexes.containsKey(index);
	}

	public boolean isAlive() {
		for(Entry<Integer, Cell> cell: getIndexes().entrySet()) {
			if(cell.getValue().equals(Cell.SHIP_ALIVE)) {
				return true;
			}
		}
		return false;
	}

}
