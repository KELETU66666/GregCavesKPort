package mods.tesseract.gregcaves.mixin;

import net.minecraft.world.biome.BiomeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeDecorator.class)
public class MixinBiomeDecorator {

    @Shadow
    public boolean generateFalls;

    @Inject(method = "decorate", at = @At("HEAD"))
    private void disableGenerateFalls(CallbackInfo ci) {
        this.generateFalls = false;
    }
}
