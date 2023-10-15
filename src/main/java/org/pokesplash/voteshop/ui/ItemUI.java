package org.pokesplash.voteshop.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.impactdev.impactor.api.economy.accounts.Account;
import net.impactdev.impactor.api.economy.transactions.EconomyTransaction;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.voteshop.VoteShop;
import org.pokesplash.voteshop.category.configs.Category;
import org.pokesplash.voteshop.category.configs.Item;
import org.pokesplash.voteshop.util.ImpactorUtils;
import org.pokesplash.voteshop.util.Utils;

import java.util.ArrayList;

public class ItemUI {
	public Page getPage(Item item, Category category) {
		GooeyButton itemButton = GooeyButton.builder()
				.display(Utils.parseItemId(item.getMaterial()))
				.title(item.getName())
				.lore(item.getDescription())
				.build();

		GooeyButton purchase = GooeyButton.builder()
				.display(Utils.parseItemId(VoteShop.lang.getPurchaseMaterial()))
				.title("§aPurchase")
				.onClick(e -> {
					// TODO purchase logic
					Account account = ImpactorUtils.getAccount(e.getPlayer().getUuid());
					EconomyTransaction transaction = ImpactorUtils.remove(account, item.getBuy());

					if (!transaction.successful()) {
						e.getPlayer().sendMessage(Text.literal(transaction.result().toString()));
						return;
					}

					if (!item.getBuyType().equalsIgnoreCase("item") &&
							!item.getBuyType().equalsIgnoreCase("command")) {
						e.getPlayer().sendMessage(Text.literal("Invalid buy type."));
						return;
					}

					if (item.getBuyType().equalsIgnoreCase("item")) {
						// Give Item
						e.getPlayer().getInventory().addPickBlock(Utils.parseItemId(item.getMaterial()));
					} else {
						// Run commands
						CommandDispatcher<ServerCommandSource> dispatcher =
								e.getPlayer().getServer().getCommandManager().getDispatcher();
						for (String command : item.getCommands()) {
							try {
								dispatcher.execute(Utils.formatPlaceholders(command, e.getPlayer().getName().getString()),
										e.getPlayer().getCommandSource());
							} catch (CommandSyntaxException ex) {
								throw new RuntimeException(ex);
							}
						}
					}
					UIManager.closeUI(e.getPlayer());
				})
				.build();

		GooeyButton cancel = GooeyButton.builder()
				.display(Utils.parseItemId(VoteShop.lang.getCancelMaterial()))
				.title("§cCancel")
				.onClick(e -> {
					UIManager.openUIForcefully(e.getPlayer(), new CategoryUI().getPage(category));
				})
				.build();

		GooeyButton filler = GooeyButton.builder()
				.display(Utils.parseItemId(VoteShop.lang.getFillerMaterial()))
				.title("")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.build();

		ChestTemplate template = ChestTemplate.builder(3)
				.fill(filler)
				.set(11, purchase)
				.set(13, itemButton)
				.set(15, cancel)
				.build();

		return GooeyPage.builder()
				.title(item.getName())
				.template(template)
				.build();
	}
}
