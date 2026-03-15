/*
 * MIT License
 *
 * Copyright (c) 2025 Ramid Khan
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

package me.ramidzkh.fabrishot.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.platform.Window;
import me.ramidzkh.fabrishot.Fabrishot;
import me.ramidzkh.fabrishot.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Window.class, priority = 2000)
public class WindowMixin {

    @ModifyReturnValue(method = {"getScreenWidth", "getGuiScaledWidth"}, at = @At("RETURN"))
    private int scaleWidth(int original) {
        return Fabrishot.isInCapture() ? Config.CAPTURE_WIDTH : original;
    }

    @ModifyReturnValue(method = {"getScreenHeight", "getGuiScaledHeight"}, at = @At("RETURN"))
    private int scaleHeight(int original) {
        return Fabrishot.isInCapture() ? Config.CAPTURE_HEIGHT : original;
    }

    // todo: fix gui scaling (or is that needed anymore?)
    //  @Inject(method = "getScaleFactor", at = @At("RETURN"), cancellable = true)
    //  private void scaleScale(CallbackInfoReturnable<Double> cir) {
    //  }
}
