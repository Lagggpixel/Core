package me.lagggpixel.core.modules.bazaar.impl;

import me.lagggpixel.core.modules.bazaar.interfaces.Bazaar;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarCategory;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarConfigIndexer;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarSubItem;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SkyblockBazaarConfigIndexer implements BazaarConfigIndexer {

    private final Bazaar bazaar;
    private final File items;

    public SkyblockBazaarConfigIndexer(Bazaar bazaar, File items) {
        this.bazaar = bazaar;
        this.items = items;
    }

    @Override
    public Pair<List<BazaarCategory>, List<BazaarSubItem>> index() throws Bazaar.BazaarIOException, Bazaar.BazaarItemNotFoundException {
        if (!items.exists()) throw new Bazaar.BazaarIOException("Bazaar items file " + items.getAbsolutePath() + " does not exist!");

        JSONObject parser;

        try {
            parser = (JSONObject) new JSONParser().parse(new BufferedReader(new InputStreamReader(Files.newInputStream(items.toPath()), StandardCharsets.UTF_8)));
        } catch (IOException | ParseException ex) {
            throw new Bazaar.BazaarIOException("Could not parse bazaar items file " + items.getAbsolutePath() + "!");
        }

        JSONObject bz = parser.get("bazaar") instanceof JSONObject ? (JSONObject) parser.get("bazaar") : null;

        if (bz == null) throw new Bazaar.BazaarIOException("Could not parse bazaar items file " + items.getAbsolutePath() + "!");

        JSONArray categories = bz.get("categories") instanceof JSONArray ? (JSONArray) bz.get("categories") : null;

        if (categories == null) throw new Bazaar.BazaarIOException("Could not parse bazaar items file " + items.getAbsolutePath() + "!");

        List<BazaarConfigCategorySchema> categorySchemas = new ArrayList<>();

        for (Object category : categories) {
            JSONObject categoryObject = category instanceof JSONObject ? (JSONObject) category : null;

            if (categoryObject == null) {
                throw new Bazaar.BazaarIOException("Could not parse bazaar items file " + items.getAbsolutePath() + "!");
            }

            categorySchemas.add(this.indexCategory(bazaar, categoryObject));
        }

        List<BazaarCategory> bazaarCategories = new ArrayList<>();
        List<BazaarSubItem> rawItems = new ArrayList<>();

        for (BazaarConfigCategorySchema categorySchema : categorySchemas) {
            bazaarCategories.add(categorySchema.toBazaarEquivalent());

            for (BazaarConfigIndexer.BazaarConfigCategoryItemSchema itemSchema : categorySchema.items()) {
                for (BazaarConfigIndexer.BazaarConfigCategorySubItemSchema subItemSchema : itemSchema.subItems()) {
                    rawItems.add(subItemSchema.toBazaarEquivalent());
                }
            }
        }

        return Pair.of(bazaarCategories, rawItems);
    }

    @Override
    public BazaarConfigCategorySchema indexCategory(Bazaar bazaar, JSONObject category) {
        Component name = ChatUtils.stringToComponent((String) category.get("name"));
        Material icon = Material.valueOf(((String) category.get("icon")).toUpperCase());
        TextColor color = TextColor.fromHexString((String) category.get("color"));
        List<BazaarConfigCategoryItemSchema> items = new ArrayList<>();

        JSONArray categoryItems = (JSONArray) category.get("items");
        for (Object categoryItem : categoryItems) {
            items.add(this.indexCategoryItem(bazaar, (JSONObject) categoryItem));
        }

        return new BazaarConfigCategorySchema(name, icon, color, items);
    }

    @Override
    public BazaarConfigCategoryItemSchema indexCategoryItem(Bazaar bazaar, JSONObject categoryItem) {
        Component name = ChatUtils.stringToComponent((String) categoryItem.get("name"));
        long inventorySize = (long) categoryItem.get("inventorySize");
        List<BazaarConfigCategorySubItemSchema> subItems = new ArrayList<>();

        JSONArray subItemsArray = (JSONArray) categoryItem.get("subItems");
        for (Object subItem : subItemsArray) {
            subItems.add(this.indexCategorySubItem(bazaar, (JSONObject) subItem));
        }

        return new BazaarConfigCategoryItemSchema(name, (int) inventorySize, subItems);
    }

    @Override
    public BazaarConfigCategorySubItemSchema indexCategorySubItem(Bazaar bazaar, JSONObject categorySubItem) {
        String material = (String) categorySubItem.get("material");
        long slot = (long) categorySubItem.get("slot");

        return new BazaarConfigCategorySubItemSchema(material, (int) slot);
    }
}
