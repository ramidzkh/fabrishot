package dev.newty.fabrishotplus.mixins;

import dev.newty.fabrishotplus.config.PlusConfig;
import me.ramidzkh.fabrishot.Fabrishot;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class RenderNametagMixin<T extends Entity, S extends EntityRenderState> {
    @Inject(at = @At("HEAD"), method = "renderLabelIfPresent", cancellable = true)
    private void doNotRender(S entityRenderState, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (!PlusConfig.SHOW_NAMETAGS && Fabrishot.isInCapture()) {
            ci.cancel();
        }
    }
}
