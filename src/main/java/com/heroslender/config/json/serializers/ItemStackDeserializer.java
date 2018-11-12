package com.heroslender.config.json.serializers;

import com.google.gson.*;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Parse {@link ItemStack} to JSON
 *
 * @author DevSrSouza
 * @version 1.0
 * <p>
 * https://github.com/DevSrSouza/
 * You can find updates here https://gist.github.com/DevSrSouza
 */
public class ItemStackDeserializer implements JsonDeserializer<ItemStack> {
    private static final String[] BYPASS_CLASS = {"CraftMetaBlockState", "CraftMetaItem"
            /*Glowstone Support*/, "GlowMetaItem"};

    @Override
    public ItemStack deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (element.isJsonObject()) {
            JsonObject itemJson = element.getAsJsonObject();

            JsonElement typeElement = itemJson.get("type");
            JsonElement dataElement = itemJson.get("data");
            JsonElement amountElement = itemJson.get("amount");

            if (typeElement.isJsonPrimitive()) {

                String type = typeElement.getAsString();
                short data = dataElement != null ? dataElement.getAsShort() : 0;
                int amount = amountElement != null ? amountElement.getAsInt() : 1;

                ItemStack itemStack = new ItemStack(Material.getMaterial(type));
                itemStack.setDurability(data);
                itemStack.setAmount(amount);

                JsonElement itemMetaElement = itemJson.get("item-meta");
                if (itemMetaElement != null && itemMetaElement.isJsonObject()) {

                    ItemMeta meta = itemStack.getItemMeta();
                    JsonObject metaJson = itemMetaElement.getAsJsonObject();

                    JsonElement displaynameElement = metaJson.get("displayname");
                    JsonElement loreElement = metaJson.get("lore");
                    JsonElement enchants = metaJson.get("enchants");
                    JsonElement flagsElement = metaJson.get("flags");
                    if (displaynameElement != null && displaynameElement.isJsonPrimitive()) {
                        meta.setDisplayName(displaynameElement.getAsString());
                    }
                    if (loreElement != null && loreElement.isJsonArray()) {
                        JsonArray jarray = loreElement.getAsJsonArray();
                        List<String> lore = new ArrayList<>(jarray.size());
                        jarray.forEach(jsonElement -> {
                            if (jsonElement.isJsonPrimitive()) lore.add(jsonElement.getAsString());
                        });
                        meta.setLore(lore);
                    }
                    if (enchants != null && enchants.isJsonArray()) {
                        JsonArray jarray = enchants.getAsJsonArray();
                        jarray.forEach(jsonElement -> {
                            if (jsonElement.isJsonPrimitive()) {
                                String enchantString = jsonElement.getAsString();
                                if (enchantString.contains(":")) {
                                    try {
                                        String[] splitEnchant = enchantString.split(":");
                                        Enchantment enchantment = Enchantment.getByName(splitEnchant[0]);
                                        int level = Integer.parseInt(splitEnchant[1]);
                                        if (enchantment != null && level > 0) {
                                            meta.addEnchant(enchantment, level, true);
                                        }
                                    } catch (NumberFormatException ex) {
                                    }
                                }
                            }
                        });
                    }
                    if (flagsElement != null && flagsElement.isJsonArray()) {
                        JsonArray jarray = flagsElement.getAsJsonArray();
                        jarray.forEach(jsonElement -> {
                            if (jsonElement.isJsonPrimitive()) {
                                for (ItemFlag flag : ItemFlag.values()) {
                                    if (flag.name().equalsIgnoreCase(jsonElement.getAsString())) {
                                        meta.addItemFlags(flag);
                                        break;
                                    }
                                }
                            }
                        });
                    }
                    for (String clazz : BYPASS_CLASS) {
                        if (meta.getClass().getSimpleName().equals(clazz)) {
                            itemStack.setItemMeta(meta);
                            return itemStack;
                        }
                    }

                    JsonElement extrametaElement = metaJson.get("extra-meta");

                    if (extrametaElement != null
                            && extrametaElement.isJsonObject()) {
                        try {
                            JsonObject extraJson = extrametaElement.getAsJsonObject();
                            if (meta instanceof SkullMeta) {
                                JsonElement ownerElement = extraJson.get("owner");
                                if (ownerElement != null && ownerElement.isJsonPrimitive()) {
                                    SkullMeta smeta = (SkullMeta) meta;
                                    smeta.setOwner(ownerElement.getAsString());
                                }
                            } else if (meta instanceof BannerMeta) {
                                JsonElement baseColorElement = extraJson.get("base-color");
                                JsonElement patternsElement = extraJson.get("patterns");
                                BannerMeta bmeta = (BannerMeta) meta;
                                if (baseColorElement != null && baseColorElement.isJsonPrimitive()) {
                                    try {
                                        Optional<DyeColor> color = Arrays.stream(DyeColor.values())
                                                .filter(dyeColor -> dyeColor.name().equalsIgnoreCase(baseColorElement.getAsString()))
                                                .findFirst();
                                        color.ifPresent(bmeta::setBaseColor);
                                    } catch (NumberFormatException ex) {
                                    }
                                }
                                if (patternsElement != null && patternsElement.isJsonArray()) {
                                    JsonArray jarray = patternsElement.getAsJsonArray();
                                    List<Pattern> patterns = new ArrayList<>(jarray.size());
                                    jarray.forEach(jsonElement -> {
                                        String patternString = jsonElement.getAsString();
                                        if (patternString.contains(":")) {
                                            String[] splitPattern = patternString.split(":");
                                            Optional<DyeColor> color = Arrays.stream(DyeColor.values())
                                                    .filter(dyeColor -> dyeColor.name().equalsIgnoreCase(splitPattern[0]))
                                                    .findFirst();
                                            PatternType patternType = PatternType.getByIdentifier(splitPattern[1]);
                                            if (color.isPresent() && patternType != null) {
                                                patterns.add(new Pattern(color.get(), patternType));
                                            }
                                        }
                                    });
                                    if (!patterns.isEmpty()) bmeta.setPatterns(patterns);
                                }
                            } else if (meta instanceof EnchantmentStorageMeta) {
                                JsonElement storedEnchantsElement = extraJson.get("stored-enchants");
                                if (storedEnchantsElement != null && storedEnchantsElement.isJsonArray()) {
                                    EnchantmentStorageMeta esmeta = (EnchantmentStorageMeta) meta;
                                    JsonArray jarray = storedEnchantsElement.getAsJsonArray();
                                    jarray.forEach(jsonElement -> {
                                        if (jsonElement.isJsonPrimitive()) {
                                            String enchantString = jsonElement.getAsString();
                                            if (enchantString.contains(":")) {
                                                try {
                                                    String[] splitEnchant = enchantString.split(":");
                                                    Enchantment enchantment = Enchantment.getByName(splitEnchant[0]);
                                                    int level = Integer.parseInt(splitEnchant[1]);
                                                    if (enchantment != null && level > 0) {
                                                        esmeta.addStoredEnchant(enchantment, level, true);
                                                    }
                                                } catch (NumberFormatException ex) {
                                                }
                                            }
                                        }
                                    });
                                }
                            } else if (meta instanceof LeatherArmorMeta) {
                                JsonElement colorElement = extraJson.get("color");
                                if (colorElement != null && colorElement.isJsonPrimitive()) {
                                    LeatherArmorMeta lameta = (LeatherArmorMeta) meta;
                                    try {
                                        lameta.setColor(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                    } catch (NumberFormatException ex) {
                                    }
                                }
                            } else if (meta instanceof BookMeta) {
                                JsonElement titleElement = extraJson.get("title");
                                JsonElement authorElement = extraJson.get("author");
                                JsonElement pagesElement = extraJson.get("pages");

                                BookMeta bmeta = (BookMeta) meta;
                                if (titleElement != null && titleElement.isJsonPrimitive()) {
                                    bmeta.setTitle(titleElement.getAsString());
                                }
                                if (authorElement != null && authorElement.isJsonPrimitive()) {
                                    bmeta.setAuthor(authorElement.getAsString());
                                }
                                if (pagesElement != null && pagesElement.isJsonArray()) {
                                    JsonArray jarray = pagesElement.getAsJsonArray();
                                    List<String> pages = new ArrayList<>(jarray.size());
                                    jarray.forEach(jsonElement -> {
                                        if (jsonElement.isJsonPrimitive()) pages.add(jsonElement.getAsString());
                                    });
                                    bmeta.setPages(pages);
                                }

                            } else if (meta instanceof PotionMeta) {
                                JsonElement customEffectsElement = extraJson.get("custom-effects");
                                if (customEffectsElement != null && customEffectsElement.isJsonArray()) {
                                    PotionMeta pmeta = (PotionMeta) meta;
                                    JsonArray jarray = customEffectsElement.getAsJsonArray();
                                    jarray.forEach(jsonElement -> {
                                        if (jsonElement.isJsonPrimitive()) {
                                            String enchantString = jsonElement.getAsString();
                                            if (enchantString.contains(":")) {
                                                try {
                                                    String[] splitPotions = enchantString.split(":");
                                                    PotionEffectType potionType = PotionEffectType.getByName(splitPotions[0]);
                                                    int amplifier = Integer.parseInt(splitPotions[1]);
                                                    int duration = Integer.parseInt(splitPotions[2]) * 20;
                                                    if (potionType != null) {
                                                        pmeta.addCustomEffect(new PotionEffect(potionType, amplifier, duration), true);
                                                    }
                                                } catch (NumberFormatException ex) {
                                                }
                                            }
                                        }
                                    });
                                }
                            } else if (meta instanceof FireworkEffectMeta) {
                                JsonElement effectTypeElement = extraJson.get("type");
                                JsonElement flickerElement = extraJson.get("flicker");
                                JsonElement trailElement = extraJson.get("trail");
                                JsonElement colorsElement = extraJson.get("colors");
                                JsonElement fadeColorsElement = extraJson.get("fade-colors");

                                if (effectTypeElement != null && effectTypeElement.isJsonPrimitive()) {
                                    FireworkEffectMeta femeta = (FireworkEffectMeta) meta;

                                    FireworkEffect.Type effectType = FireworkEffect.Type.valueOf(effectTypeElement.getAsString());

                                    if (effectType != null) {
                                        List<Color> colors = new ArrayList<>();
                                        if (colorsElement != null && colorsElement.isJsonArray())
                                            colorsElement.getAsJsonArray().forEach(colorElement -> {
                                                if (colorElement.isJsonPrimitive())
                                                    colors.add(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                            });

                                        List<Color> fadeColors = new ArrayList<>();
                                        if (fadeColorsElement != null && fadeColorsElement.isJsonArray())
                                            fadeColorsElement.getAsJsonArray().forEach(colorElement -> {
                                                if (colorElement.isJsonPrimitive())
                                                    fadeColors.add(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                            });

                                        FireworkEffect.Builder builder = FireworkEffect.builder().with(effectType);

                                        if (flickerElement != null && flickerElement.isJsonPrimitive())
                                            builder.flicker(flickerElement.getAsBoolean());
                                        if (trailElement != null && trailElement.isJsonPrimitive())
                                            builder.trail(trailElement.getAsBoolean());

                                        if (!colors.isEmpty()) builder.withColor(colors);
                                        if (!fadeColors.isEmpty()) builder.withFade(fadeColors);

                                        femeta.setEffect(builder.build());
                                    }
                                }
                            } else if (meta instanceof FireworkMeta) {
                                FireworkMeta fmeta = (FireworkMeta) meta;

                                JsonElement effectArrayElement = extraJson.get("effects");
                                JsonElement powerElement = extraJson.get("power");

                                if (powerElement != null && powerElement.isJsonPrimitive()) {
                                    fmeta.setPower(powerElement.getAsInt());
                                }

                                if (effectArrayElement != null && effectArrayElement.isJsonArray()) {

                                    effectArrayElement.getAsJsonArray().forEach(jsonElement -> {
                                        if (jsonElement.isJsonObject()) {

                                            JsonObject jsonObject = jsonElement.getAsJsonObject();

                                            JsonElement effectTypeElement = jsonObject.get("type");
                                            JsonElement flickerElement = jsonObject.get("flicker");
                                            JsonElement trailElement = jsonObject.get("trail");
                                            JsonElement colorsElement = jsonObject.get("colors");
                                            JsonElement fadeColorsElement = jsonObject.get("fade-colors");

                                            if (effectTypeElement != null && effectTypeElement.isJsonPrimitive()) {

                                                FireworkEffect.Type effectType = FireworkEffect.Type.valueOf(effectTypeElement.getAsString());

                                                if (effectType != null) {
                                                    List<Color> colors = new ArrayList<>();
                                                    if (colorsElement != null && colorsElement.isJsonArray())
                                                        colorsElement.getAsJsonArray().forEach(colorElement -> {
                                                            if (colorElement.isJsonPrimitive())
                                                                colors.add(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                                        });

                                                    List<Color> fadeColors = new ArrayList<>();
                                                    if (fadeColorsElement != null && fadeColorsElement.isJsonArray())
                                                        fadeColorsElement.getAsJsonArray().forEach(colorElement -> {
                                                            if (colorElement.isJsonPrimitive())
                                                                fadeColors.add(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                                        });

                                                    FireworkEffect.Builder builder = FireworkEffect.builder().with(effectType);

                                                    if (flickerElement != null && flickerElement.isJsonPrimitive())
                                                        builder.flicker(flickerElement.getAsBoolean());
                                                    if (trailElement != null && trailElement.isJsonPrimitive())
                                                        builder.trail(trailElement.getAsBoolean());

                                                    if (!colors.isEmpty()) builder.withColor(colors);
                                                    if (!fadeColors.isEmpty()) builder.withFade(fadeColors);

                                                    fmeta.addEffect(builder.build());
                                                }
                                            }
                                        }
                                    });
                                }
                            } else if (meta instanceof MapMeta) {
                                MapMeta mmeta = (MapMeta) meta;

                                JsonElement scalingElement = extraJson.get("scaling");
                                if (scalingElement != null && scalingElement.isJsonPrimitive()) {
                                    mmeta.setScaling(scalingElement.getAsBoolean());
                                }

                                /* 1.11
                                JsonElement locationNameElement = extraJson.get("location-cmdName");
                                if(locationNameElement != null && locationNameElement.isJsonPrimitive()) {
                                    mmeta.setLocationName(locationNameElement.getAsString());
                                }
                                JsonElement colorElement = extraJson.get("color");
                                if(colorElement != null && colorElement.isJsonPrimitive()) {
                                    mmeta.setColor(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                }*/
                            }
                        } catch (Exception e) {
                            return null;
                        }
                    }
                    itemStack.setItemMeta(meta);
                }
                return itemStack;
            } else return null;
        } else return null;
    }
}
