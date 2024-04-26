package me.ramidzkh.fabrishot.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import me.ramidzkh.fabrishot.Fabrishot;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {

    @Unique
    private static boolean wasLastFrameInCapture;

    @Redirect(method = "flipFrame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSwapBuffers(J)V"), remap = false)
    private static void glfwSwapBuffers(long window) {
        if (Fabrishot.isInCapture()) {
            wasLastFrameInCapture = true;
            return;
        }

        // skip a single black frame
        if (wasLastFrameInCapture) {
            wasLastFrameInCapture = false;
            return;
        }

        GLFW.glfwSwapBuffers(window);
    }
}
