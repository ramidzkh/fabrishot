package me.ramidzkh.fabrishot.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.systems.RenderSystem;
import me.ramidzkh.fabrishot.Fabrishot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {

    @Unique
    private static boolean wasLastFrameInCapture;

    @WrapWithCondition(method = "flipFrame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSwapBuffers(J)V"), remap = false)
    private static boolean glfwSwapBuffers(long window) {
        if (Fabrishot.isInCapture()) {
            wasLastFrameInCapture = true;
            return false;
        }

        // skip a single black frame
        if (wasLastFrameInCapture) {
            wasLastFrameInCapture = false;
            return false;
        }

        return true;
    }
}
