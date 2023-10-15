package org.pokesplash.voteshop.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.voteshop.VoteShop;
import org.pokesplash.voteshop.category.CategoryProvider;
import org.pokesplash.voteshop.category.configs.Category;
import org.pokesplash.voteshop.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainUI {
	public Page getPage() {
		PlaceholderButton placeholderButton = new PlaceholderButton();

		List<Button> categoryButtons = new ArrayList<>();
		for (Category category : CategoryProvider.getCategories()) {
			Button button = GooeyButton.builder()
					.display(Utils.parseItemId(category.getItem()))
					.title(category.getName())
					.lore(category.getDescription())
					.onClick(el -> {
						ServerPlayerEntity player = el.getPlayer();

						UIManager.openUIForcefully(player, new CategoryUI().getPage(category));
					})
					.build();

			categoryButtons.add(button);
		}

		GooeyButton filler = GooeyButton.builder()
			.display(Utils.parseItemId(VoteShop.lang.getFillerMaterial()))
			.title("")
			.lore(new ArrayList<>())
			.hideFlags(FlagType.All)
			.build();

		int rows = (int) Math.ceil((double) CategoryProvider.getCategories().size() / 7) + 2;

		ChestTemplate template = ChestTemplate.builder(rows)
				.rectangle(1, 1, rows - 2, 7, placeholderButton)
				.fill(filler)
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, categoryButtons, null);
		page.setTitle(VoteShop.lang.getTitle());

		return page;
	}
}
