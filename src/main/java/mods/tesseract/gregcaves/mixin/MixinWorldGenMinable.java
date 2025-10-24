package mods.tesseract.gregcaves.mixin;

import mods.tesseract.gregcaves.GregCaves;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(WorldGenMinable.class)
public class MixinWorldGenMinable {
    @Inject(
        method = "generate(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onGenerate(World worldIn, Random rand, BlockPos position, CallbackInfoReturnable<Boolean> cir) {
        if (GregCaves.reduceOreGen) {
            int y = position.getY();
            int dimension = worldIn.provider.getDimension();

            if (y < GregCaves.reduceOreGenY && dimension == 0) {
                if (rand.nextFloat() >= GregCaves.reduceOreGenRate) {
                    cir.setReturnValue(false);
                    cir.cancel();
                }
            }
        }
    }
}
