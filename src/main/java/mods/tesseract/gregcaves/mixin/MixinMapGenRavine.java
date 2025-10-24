package mods.tesseract.gregcaves.mixin;

import mods.tesseract.gregcaves.GregCaves;
import net.minecraft.world.gen.MapGenRavine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MapGenRavine.class)
public class MixinMapGenRavine {

    @ModifyConstant(
        method = "digBlock",
        constant = @Constant(intValue = 10)
    )
    private int modifyLavaLevel(int original) {
        return GregCaves.caveLavaLevel;
    }
}
