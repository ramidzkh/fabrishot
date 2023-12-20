/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021, 2022, 2023 shedaniel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.ramidzkh.fabrishot.config;

import me.ramidzkh.fabrishot.MinecraftInterface;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class ScalingPresetEntry extends AbstractConfigListEntry<Unit> {

    private final List<ButtonWidget> children;
    private final int width;

    public ScalingPresetEntry(int myWidth, IntegerListEntry widthConfig, IntegerListEntry heightConfig) {
        super(Text.empty(), false);

        this.children = IntStream.rangeClosed(1, 4)
                .mapToObj(scaleFactor -> (ButtonWidget) new ButtonWidget(0, 0, 0, 20, Text.literal(scaleFactor + "x"), button -> {
                    int width = MinecraftInterface.getDisplayWidth() * scaleFactor;
                    int height = MinecraftInterface.getDisplayHeight() * scaleFactor;

                    widthConfig.setValue(Integer.toString(width));
                    heightConfig.setValue(Integer.toString(height));
                }, Supplier::get) {
                })
                .toList();
        this.width = myWidth;
    }

    @Override
    public Unit getValue() {
        return Unit.INSTANCE;
    }

    @Override
    public Optional<Unit> getDefaultValue() {
        return Optional.of(Unit.INSTANCE);
    }

    @Override
    public void save() {
    }

    @Override
    public void render(DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);

        int totalGap = (this.children.size() - 1) * 4;
        int childWidth = (this.width - totalGap) / this.children.size();

        for (int i = 0; i < children.size(); i++) {
            ButtonWidget child = children.get(i);
            child.active = this.isEditable();
            child.setX(x + entryWidth / 2 - this.width / 2 + i * (childWidth + 4));
            child.setY(y);
            child.setWidth(childWidth - 2);

            child.render(graphics, mouseX, mouseY, delta);
        }
    }

    @Override
    public List<? extends Element> children() {
        return children;
    }

    @Override
    public List<? extends Selectable> narratables() {
        return children;
    }
}
