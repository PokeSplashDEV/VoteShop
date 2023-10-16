package org.pokesplash.voteshop.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.voteshop.VoteShop;
import org.pokesplash.voteshop.category.configs.Category;
import org.pokesplash.voteshop.category.configs.Item;
import org.pokesplash.voteshop.util.ImpactorUtils;
import org.pokesplash.voteshop.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategoryUI {

	private final LinkedPageButton nextPage = LinkedPageButton.builder()
			.display(Utils.parseItemId(VoteShop.lang.getNextPageMaterial()))
			.title("§7Next Page")
			.linkType(LinkType.Next)
			.build();

	private final LinkedPageButton previousPage = LinkedPageButton.builder()
			.display(Utils.parseItemId(VoteShop.lang.getPreviousPageMaterial()))
			.title("§7Previous Page")
			.linkType(LinkType.Previous)
			.build();

	public Page getPage(Category category, ServerPlayerEntity player) {
		PlaceholderButton placeholderButton = new PlaceholderButton();

		List<Button> itemButtons = new ArrayList<>();
		for (Item item : category.getItems()) {
			Collection<String> lore = new ArrayList<>();
			lore.addAll(item.getDescription());
			lore.add("§aBuy: §e" + item.getBuy());
			lore.add("§6Current Balance: " + ImpactorUtils.getAccount(player.getUuid()).balanceAsync().join());


			Button button = GooeyButton.builder()
					.display(Utils.parseItemId(item.getMaterial(), item.getQuantity()))
					.title(item.getName())
					.lore(lore)
					.onClick(el -> {
						UIManager.openUIForcefully(el.getPlayer(), new ItemUI().getPage(item, category, el.getPlayer()));
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

		int rows = category.getItems().size() <= 28 ? (int) Math.ceil((double) category.getItems().size() / 7) + 2 : 6;

		ChestTemplate.Builder template = ChestTemplate.builder(rows)
				.rectangle(1, 1, rows - 2, 7, placeholderButton)
				.fill(filler)
				.set(0, 0, back);

		if (category.getItems().size() > 28) {
			template.set(45, previousPage);
			template.set(53, nextPage);
		}

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template.build(), itemButtons, null);
		page.setTitle(category.getName());

		return page;
	}
}
