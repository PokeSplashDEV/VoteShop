package org.pokesplash.voteshop.category;

import com.google.gson.Gson;
import org.pokesplash.voteshop.VoteShop;
import org.pokesplash.voteshop.category.configs.Category;
import org.pokesplash.voteshop.category.configs.Item;
import org.pokesplash.voteshop.util.Utils;

import java.io.File;
import java.util.ArrayList;

public abstract class CategoryProvider {
	private static ArrayList<Category> categories;

	public static ArrayList<Category> getCategories() {
		return categories;
	}

	public static void init() {
		String filePath = VoteShop.BASE_PATH + "categories/";

		categories  = new ArrayList<>();

		File dir = Utils.checkForDirectory(filePath);

		String[] list = dir.list();

		if (list.length == 0) {
			return;
		}

		for (String file : list) {
			Utils.readFileAsync(filePath, file, el -> {
				Gson gson = Utils.newGson();
				Category category = gson.fromJson(el, Category.class);

				boolean hasError = false;

				for (Item item : category.getItems()) {
					if (!item.getBuyType().equalsIgnoreCase("item") &&
					!item.getBuyType().equalsIgnoreCase("command")) {
						hasError = true;
					}
				}

				if (hasError) {
					VoteShop.LOGGER.error("Could not add category " + category.getName() +
							"to VoteShop as at least one of the items has an invalid buy type.");
				} else {
					categories.add(category);
				}
			});
		}
		VoteShop.LOGGER.info("VoteShop read " + categories.size() + " files.");
	}
}
