package org.pokesplash.voteshop.config;

import com.google.gson.Gson;
import org.pokesplash.voteshop.VoteShop;
import org.pokesplash.voteshop.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Lang {
	private String title;
	private String fillerMaterial;
	private String backButtonMaterial;
	private String purchaseMaterial;
	private String cancelMaterial;

	public Lang() {
		title = "VoteShop";
		fillerMaterial = "minecraft:white_stained_glass_pane";
		backButtonMaterial = "minecraft:barrier";
		purchaseMaterial = "minecraft:lime_stained_glass_pane";
		cancelMaterial = "minecraft:red_stained_glass_pane";
	}

	public String getTitle() {
		return title;
	}
	public String getFillerMaterial() {
		return fillerMaterial;
	}
	public String getBackButtonMaterial() {
		return backButtonMaterial;
	}
	public String getPurchaseMaterial() {
		return purchaseMaterial;
	}
	public String getCancelMaterial() {
		return cancelMaterial;
	}

	/**
	 * Method to initialize the config.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(VoteShop.BASE_PATH, "lang.json",
				el -> {
					Gson gson = Utils.newGson();
					Lang lang = gson.fromJson(el, Lang.class);
					title = lang.getTitle();
					fillerMaterial = lang.getFillerMaterial();
					backButtonMaterial = lang.getBackButtonMaterial();
					purchaseMaterial = lang.getPurchaseMaterial();
					cancelMaterial = lang.getCancelMaterial();
				});

		if (!futureRead.join()) {
			VoteShop.LOGGER.info("No lang.json file found for " + VoteShop.MOD_ID + ". Attempting to generate one.");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(VoteShop.BASE_PATH, "lang.json", data);

			if (!futureWrite.join()) {
				VoteShop.LOGGER.fatal("Could not write lang.json for " + VoteShop.MOD_ID + ".");
			}
			return;
		}
		VoteShop.LOGGER.info(VoteShop.MOD_ID + " lang file read successfully.");
	}
}