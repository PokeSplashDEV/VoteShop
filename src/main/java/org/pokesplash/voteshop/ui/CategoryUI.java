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
import org.pokesplash.voteshop.VoteShop;
import org.pokesplash.voteshop.category.CategoryProvider;
import org.pokesplash.voteshop.category.configs.Category;
import org.pokesplash.voteshop.category.configs.Item;
import org.pokesplash.voteshop.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategoryUI {
	public Page getPage(Category category) {
		PlaceholderButton placeholderButton = new PlaceholderButton();

		List<Button> itemButtons = new ArrayList<>();
		for (Item item : category.getItems()) {
			Collection<String> lore = new ArrayList<>();
			lore.addAll(item.getDescription());
			lore.add("§aBuy: §e" + item.getBuy());


			Button button = GooeyButton.builder()
					.display(Utils.parseItemId(item.getMaterial()))
					.title(item.getName())
					.lore(lore)
					.onClick(el -> {
						UIManager.openUIForcefully(el.getPlayer(), new ItemUI().getPage(item, category));
					})
					.build();

			itemButtons.add(button);
		}

		GooeyButton filler = GooeyButton.builder()
				.display(Utils.parseItemId(VoteShop.lang.getFillerMaterial()))
				.title("")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.build();

		GooeyButton back = GooeyButton.builder()
				.display(Utils.parseItemId(VoteShop.lang.getBackButtonMaterial()))
				.title("§cBack")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.onClick(e -> {
					UIManager.openUIForcefully(e.getPlayer(), new MainUI().getPage());
				})
				.build();

		int rows = (int) Math.ceil((double) category.getItems().size() / 7) + 2;

		ChestTemplate template = ChestTemplate.builder(rows)
				.rectangle(1, 1, rows - 2, 7, placeholderButton)
				.fill(filler)
				.set(0, 0, back)
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, itemButtons, null);
		page.setTitle(category.getName());

		return page;
	}
}
