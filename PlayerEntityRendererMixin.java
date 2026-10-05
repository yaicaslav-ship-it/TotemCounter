package com.example.totemcounter.mixin;

import com.example.totemcounter.client.TotemCounterClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V",
            at = @At("RETURN"))
    private void renderTotemCount(AbstractClientPlayerEntity entity, Text text, MatrixStack matrices,
                                 VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci) {
        Integer totems = TotemCounterClient.TOTEM_COUNTS.get(entity.getId());
        if (totems == null) return;

        matrices.push();
        matrices.translate(0.0F, 0.3F, 0.0F);

        Formatting color = totems > 0 ? Formatting.GOLD : Formatting.GRAY;
        Text label = Text.literal("✦ Тотемы: " + totems).formatted(color);

        this.renderLabelIfPresent(entity, label, matrices, vertexConsumers, light, tickDelta);
        matrices.pop();
    }
}
