package dev.newty.fabrishotplus.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClothConfigBridge implements ConfigScreenFactory<Screen> {
    @Override
    public Screen create(Screen parent) {
        ConfigBuilder builder = new me.ramidzkh.fabrishot.config.ClothConfigBridge().create(parent);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("fabrishotplus.config.category"));

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("fabrishotplus.config.show_nametags"), PlusConfig.SHOW_NAMETAGS)
                .setDefaultValue(false)
                .setSaveConsumer(b -> PlusConfig.SHOW_NAMETAGS = b)
                .build());

        return builder.build();
    }
}
