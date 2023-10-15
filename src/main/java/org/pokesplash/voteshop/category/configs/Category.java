package org.pokesplash.voteshop.category.configs;

import java.util.ArrayList;

public class Category {
	private String name;
	private String item;
	private ArrayList<String> description;
	private ArrayList<Item> items;

	public String getName() {
		return name;
	}

	public String getItem() {
		return item;
	}

	public ArrayList<String> getDescription() {
		return description;
	}

	public ArrayList<Item> getItems() {
		return items;
	}
}
