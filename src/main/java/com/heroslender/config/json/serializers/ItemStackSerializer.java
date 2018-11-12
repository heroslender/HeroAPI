package com.heroslender.config.json.serializers;

import com.google.gson.*;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.lang.reflect.Type;

/**
 * Parse {@link ItemStack} to JSON
 *
 * @author DevSrSouza
 * @version 1.0
 * <p>
 * https://github.com/DevSrSouza/
 * You can find updates here https://gist.github.com/DevSrSouza
 */
public class ItemStackSerializer implements JsonSerializer<ItemStack> {
    private static final String[] BYPASS_CLASS = {"CraftMetaBlockState", "CraftMetaItem"
            /*Glowstone Support*/, "GlowMetaItem"};

    @Override
    public JsonElement serialize(ItemStack itemStack, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject itemJson = new JsonObject();

        itemJson.addProperty("type", itemStack.getType().name());
        if (itemStack.getDurability() > 0) itemJson.addProperty("data", itemStack.getDurability());
        if (itemStack.getAmount() != 1) itemJson.addProperty("amount", itemStack.getAmount());


        if (itemStack.hasItemMeta()) {
            JsonObject metaJson = new JsonObject();

            ItemMeta meta = itemStack.getItemMeta();


            if (meta.hasDisplayName()) {
                metaJson.addProperty("displayname", meta.getDisplayName());
            }
            if (meta.hasLore()) {
                JsonArray lore = new JsonArray();
                meta.getLore().forEach(str -> lore.add(new JsonPrimitive(str)));
                metaJson.add("lore", lore);
            }
            if (meta.hasEnchants()) {
                JsonArray enchants = new JsonArray();
                meta.getEnchants().forEach((enchantment, integer) -> {
                    enchants.add(new JsonPrimitive(enchantment.getName() + ":" + integer));
                });
                metaJson.add("enchants", enchants);
            }
            if (!meta.getItemFlags().isEmpty()) {
                JsonArray flags = new JsonArray();
                meta.getItemFlags().stream().map(ItemFlag::name).forEach(str -> flags.add(new JsonPrimitive(str)));
                metaJson.add("flags", flags);
            }

            for (String clazz : BYPASS_CLASS) {
                if (meta.getClass().getSimpleName().equals(clazz)) {
                    itemJson.add("item-meta", metaJson);
                    return context.serialize(itemJson);
                }
            }

            if (meta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) meta;
                if (skullMeta.hasOwner()) {
                    JsonObject extraMeta = new JsonObject();
                    extraMeta.addProperty("owner", skullMeta.getOwner());
                    metaJson.add("extra-meta", extraMeta);
                }
            } else if (meta instanceof BannerMeta) {
                BannerMeta bannerMeta = (BannerMeta) meta;
                JsonObject extraMeta = new JsonObject();
                extraMeta.addProperty("base-color", bannerMeta.getBaseColor().name());

                if (bannerMeta.numberOfPatterns() > 0) {
                    JsonArray patterns = new JsonArray();
                    bannerMeta.getPatterns()
                            .stream()
                            .map(pattern ->
                                    pattern.getColor().name() + ":" + pattern.getPattern().getIdentifier())
                            .forEach(str -> patterns.add(new JsonPrimitive(str)));
                    extraMeta.add("patterns", patterns);
                }

                metaJson.add("extra-meta", extraMeta);
            } else if (meta instanceof EnchantmentStorageMeta) {
                EnchantmentStorageMeta esmeta = (EnchantmentStorageMeta) meta;
                if (esmeta.hasStoredEnchants()) {
                    JsonObject extraMeta = new JsonObject();
                    JsonArray storedEnchants = new JsonArray();
                    esmeta.getStoredEnchants().forEach((enchantment, integer) -> {
                        storedEnchants.add(new JsonPrimitive(enchantment.getName() + ":" + integer));
                    });
                    extraMeta.add("stored-enchants", storedEnchants);
                    metaJson.add("extra-meta", extraMeta);
                }
            } else if (meta instanceof LeatherArmorMeta) {
                LeatherArmorMeta lameta = (LeatherArmorMeta) meta;
                JsonObject extraMeta = new JsonObject();
                extraMeta.addProperty("color", Integer.toHexString(lameta.getColor().asRGB()));
                metaJson.add("extra-meta", extraMeta);
            } else if (meta instanceof BookMeta) {
                BookMeta bmeta = (BookMeta) meta;
                if (bmeta.hasAuthor() || bmeta.hasPages() || bmeta.hasTitle()) {
                    JsonObject extraMeta = new JsonObject();
                    if (bmeta.hasTitle()) {
                        extraMeta.addProperty("title", bmeta.getTitle());
                    }
                    if (bmeta.hasAuthor()) {
                        extraMeta.addProperty("author", bmeta.getAuthor());
                    }
                    if (bmeta.hasPages()) {
                        JsonArray pages = new JsonArray();
                        bmeta.getPages().forEach(str -> pages.add(new JsonPrimitive(str)));
                        extraMeta.add("pages", pages);
                    }
                    metaJson.add("extra-meta", extraMeta);
                }
            } else if (meta instanceof PotionMeta) {
                PotionMeta pmeta = (PotionMeta) meta;
                if (pmeta.hasCustomEffects()) {
                    JsonObject extraMeta = new JsonObject();

                    JsonArray customEffects = new JsonArray();
                    pmeta.getCustomEffects().forEach(potionEffect -> {
                        customEffects.add(new JsonPrimitive(potionEffect.getType().getName()
                                + ":" + potionEffect.getAmplifier()
                                + ":" + potionEffect.getDuration() / 20));
                    });
                    extraMeta.add("custom-effects", customEffects);

                    metaJson.add("extra-meta", extraMeta);
                }
            } else if (meta instanceof FireworkEffectMeta) {
                FireworkEffectMeta femeta = (FireworkEffectMeta) meta;
                if (femeta.hasEffect()) {
                    FireworkEffect effect = femeta.getEffect();
                    JsonObject extraMeta = new JsonObject();

                    extraMeta.addProperty("type", effect.getType().name());
                    if (effect.hasFlicker()) extraMeta.addProperty("flicker", true);
                    if (effect.hasTrail()) extraMeta.addProperty("trail", true);

                    if (!effect.getColors().isEmpty()) {
                        JsonArray colors = new JsonArray();
                        effect.getColors().forEach(color ->
                                colors.add(new JsonPrimitive(Integer.toHexString(color.asRGB()))));
                        extraMeta.add("colors", colors);
                    }

                    if (!effect.getFadeColors().isEmpty()) {
                        JsonArray fadeColors = new JsonArray();
                        effect.getFadeColors().forEach(color ->
                                fadeColors.add(new JsonPrimitive(Integer.toHexString(color.asRGB()))));
                        extraMeta.add("fade-colors", fadeColors);
                    }

                    metaJson.add("extra-meta", extraMeta);
                }
            } else if (meta instanceof FireworkMeta) {
                FireworkMeta fmeta = (FireworkMeta) meta;

                JsonObject extraMeta = new JsonObject();

                extraMeta.addProperty("power", fmeta.getPower());

                if (fmeta.hasEffects()) {
                    JsonArray effects = new JsonArray();
                    fmeta.getEffects().forEach(effect -> {
                        JsonObject jsonObject = new JsonObject();

                        jsonObject.addProperty("type", effect.getType().name());
                        if (effect.hasFlicker()) jsonObject.addProperty("flicker", true);
                        if (effect.hasTrail()) jsonObject.addProperty("trail", true);

                        if (!effect.getColors().isEmpty()) {
                            JsonArray colors = new JsonArray();
                            effect.getColors().forEach(color ->
                                    colors.add(new JsonPrimitive(Integer.toHexString(color.asRGB()))));
                            jsonObject.add("colors", colors);
                        }

                        if (!effect.getFadeColors().isEmpty()) {
                            JsonArray fadeColors = new JsonArray();
                            effect.getFadeColors().forEach(color ->
                                    fadeColors.add(new JsonPrimitive(Integer.toHexString(color.asRGB()))));
                            jsonObject.add("fade-colors", fadeColors);
                        }

                        effects.add(jsonObject);
                    });
                    extraMeta.add("effects", effects);
                }
                metaJson.add("extra-meta", extraMeta);
            } else if (meta instanceof MapMeta) {
                MapMeta mmeta = (MapMeta) meta;
                JsonObject extraMeta = new JsonObject();

                /* 1.11
                if(mmeta.hasLocationName()) {
                    extraMeta.addProperty("location-cmdName", mmeta.getLocationName());
                }
                if(mmeta.hasColor()) {
                    extraMeta.addProperty("color", Integer.toHexString(mmeta.getColor().asRGB()));
                }*/
                extraMeta.addProperty("scaling", mmeta.isScaling());

                metaJson.add("extra-meta", extraMeta);
            }

            itemJson.add("item-meta", metaJson);
        }
        return context.serialize(itemJson);
    }
}
