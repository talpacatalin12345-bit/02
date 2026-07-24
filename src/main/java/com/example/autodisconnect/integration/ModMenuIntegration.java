package com.example.autodisconnect.integration;

import com.example.autodisconnect.config.ModConfig;
import io.github.proactiveminds.modmenu.api.ConfigScreenFactory;
import io.github.proactiveminds.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.LiteralText;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ModConfig config = ModConfig.INSTANCE;
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(new LiteralText("Настройки AutoDisconnect"));

            ConfigCategory general = builder.getOrCreateCategory(new LiteralText("Основные"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startBooleanToggle(new LiteralText("Включить мод"), config.enabled)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.enabled = newValue)
                    .build());

            general.addEntry(entryBuilder.startIntSlider(new LiteralText("Радиус сканирования (блоки)"), config.radius, 10, 2048)
                    .setDefaultValue(512)
                    .setSaveConsumer(newValue -> config.radius = newValue)
                    .build());

            general.addEntry(entryBuilder.startBooleanToggle(new LiteralText("Проверять весь Tab-лист"), config.checkTabList)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> config.checkTabList = newValue)
                    .build());

            general.addEntry(entryBuilder.startStrList(new LiteralText("Белый список (Ники)"), config.whitelist)
                    .setDefaultValue(new java.util.ArrayList<>())
                    .setSaveConsumer(newValue -> config.whitelist = newValue)
                    .build());

            builder.setSavingRunnable(config::save);
            return builder.build();
        };
    }
}
