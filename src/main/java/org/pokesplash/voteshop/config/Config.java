package org.pokesplash.voteshop.config;

import com.google.gson.Gson;
import org.pokesplash.voteshop.VoteShop;
import org.pokesplash.voteshop.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Config {
	private String currencyName;

	public Config() {
		currencyName = "dollar";
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(VoteShop.BASE_PATH,
				"config.json", el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					currencyName = cfg.getCurrencyName();
				});

		if (!futureRead.join()) {
			VoteShop.LOGGER.info("No config.json file found for " + VoteShop.MOD_ID + ". Attempting to generate " +
					"one");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(VoteShop.BASE_PATH,
					"config.json", data);

			if (!futureWrite.join()) {
				VoteShop.LOGGER.fatal("Could not write config for" + VoteShop.MOD_ID + " .");
			}
			return;
		}
		VoteShop.LOGGER.info(VoteShop.MOD_ID + " config file read successfully");
	}

	public String getCurrencyName() {
		return currencyName;
	}
}
