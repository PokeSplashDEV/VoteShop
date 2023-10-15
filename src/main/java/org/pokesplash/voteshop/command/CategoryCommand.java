package org.pokesplash.voteshop.command;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.voteshop.VoteShop;
import org.pokesplash.voteshop.category.CategoryProvider;
import org.pokesplash.voteshop.category.configs.Category;
import org.pokesplash.voteshop.ui.CategoryUI;
import org.pokesplash.voteshop.util.LuckPermsUtils;

public class CategoryCommand {
	public CommandNode<ServerCommandSource> build() {
		return CommandManager.argument("category", StringArgumentType.greedyString())
				.suggests((ctx, builder) -> {
					for (Category category : CategoryProvider.getCategories()) {
						builder.suggest(category.getName());
					}
					return builder.buildFuture();
				})
				.executes(this::run)
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be ran by a player."));
			return 1;
		}

		String argument = StringArgumentType.getString(context, "category");

		for (Category category : CategoryProvider.getCategories()) {
			if (category.getName().equalsIgnoreCase(argument)) {
				UIManager.openUIForcefully(context.getSource().getPlayer(), new CategoryUI().getPage(category,
						context.getSource().getPlayer()));
				return 1;
			}
		}

		context.getSource().sendMessage(Text.literal("§cCould not find category " + argument));

		return 1;
	}
}
