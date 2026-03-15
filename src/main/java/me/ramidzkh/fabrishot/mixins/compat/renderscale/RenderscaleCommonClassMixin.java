package me.ramidzkh.fabrishot.mixins.compat.renderscale;

import dev.zelo.renderscale.CommonClass;
import me.ramidzkh.fabrishot.Fabrishot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommonClass.class)
public class RenderscaleCommonClassMixin {
    @Inject(method = "setShouldScale", at = @At("HEAD"), cancellable = true)
    private void fabrishot$disableScalingDuringScreenshot(boolean shouldScale, CallbackInfo ci) {
        if (Fabrishot.isInCapture()) ci.cancel();
    }
}
