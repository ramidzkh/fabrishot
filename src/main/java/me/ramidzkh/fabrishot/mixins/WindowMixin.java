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

import me.ramidzkh.fabrishot.Fabrishot;
import me.ramidzkh.fabrishot.MinecraftInterface;
import me.ramidzkh.fabrishot.config.Config;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Window.class)
public class WindowMixin {

    @Inject(method = "getWidth", at = @At("RETURN"), cancellable = true)
    private void scaleWidth(CallbackInfoReturnable<Integer> cir) {
        if (Fabrishot.isInCapture()) {
            cir.setReturnValue(Config.CAPTURE_WIDTH);
        }
    }

    @Inject(method = "getHeight", at = @At("RETURN"), cancellable = true)
    private void scaleHeight(CallbackInfoReturnable<Integer> cir) {
        if (Fabrishot.isInCapture()) {
            cir.setReturnValue(Config.CAPTURE_HEIGHT);
        }
    }

    // todo: fix gui scaling (or is that needed anymore?)
    //  @Inject(method = "getScaleFactor", at = @At("RETURN"), cancellable = true)
    //  private void scaleScale(CallbackInfoReturnable<Double> cir) {
    //  }

    @Inject(method = {"onFramebufferSizeChanged", "updateFramebufferSize"}, at = @At("RETURN"))
    private void onResize(CallbackInfo ci) {
        MinecraftInterface.refresh();
    }
}
