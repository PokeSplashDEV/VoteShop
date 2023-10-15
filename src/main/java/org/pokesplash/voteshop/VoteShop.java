package org.pokesplash.voteshop;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pokesplash.voteshop.category.CategoryProvider;
import org.pokesplash.voteshop.command.CommandHandler;
import org.pokesplash.voteshop.config.Config;
import org.pokesplash.voteshop.config.Lang;
import org.pokesplash.voteshop.util.ImpactorUtils;

public class VoteShop implements ModInitializer {
	public static final String MOD_ID = "VoteShop";

	public static final String BASE_PATH = "/config/" + MOD_ID + "/";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Config config = new Config();
	public static final Lang lang = new Lang();

	/**
	 * Runs the mod initializer.
	 */
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
		ServerLifecycleEvents.SERVER_STARTED.register(el -> load());
	}

	public static void load() {
		config.init();
		lang.init();
		ImpactorUtils.setCurrency(config.getCurrencyName());
		CategoryProvider.init();
	}
}
