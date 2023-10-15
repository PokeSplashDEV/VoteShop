package org.pokesplash.voteshop.category;

import com.google.gson.Gson;
import org.pokesplash.voteshop.VoteShop;
import org.pokesplash.voteshop.category.configs.Category;
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

		File dir = Utils.checkForDirectory(filePath);

		String[] list = dir.list();

		if (list.length == 0) {
			return;
		}

		for (String file : list) {
			categories  = new ArrayList<>();
			Utils.readFileAsync(filePath, file, el -> {
				Gson gson = Utils.newGson();
				Category category = gson.fromJson(el, Category.class);
				categories.add(category);
			});
		}
	}
}
