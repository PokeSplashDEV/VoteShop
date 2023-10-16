package org.pokesplash.voteshop.category.configs;

import java.util.ArrayList;

public class Item {
	private String name;
	private String material;
	private ArrayList<String> description;
	private double buy;
	private String buyType;
	private int quantity;
	private ArrayList<String> commands;

	public String getName() {
		return name;
	}

	public String getMaterial() {
		return material;
	}

	public ArrayList<String> getDescription() {
		return description;
	}

	public double getBuy() {
		return buy;
	}

	public String getBuyType() {
		return buyType;
	}

	public ArrayList<String> getCommands() {
		return commands;
	}

	public int getQuantity() {
		return quantity;
	}
}
