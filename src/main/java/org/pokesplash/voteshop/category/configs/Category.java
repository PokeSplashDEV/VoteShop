package org.pokesplash.voteshop.category.configs;

import java.util.ArrayList;

public class Category {
	private String name;
	private ArrayList<String> description;
	private ArrayList<Item> items;

	public String getName() {
		return name;
	}

	public ArrayList<String> getDescription() {
		return description;
	}

	public ArrayList<Item> getItems() {
		return items;
	}
}
