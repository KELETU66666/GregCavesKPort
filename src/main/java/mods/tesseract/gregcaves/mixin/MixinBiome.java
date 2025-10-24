package mods.tesseract.gregcaves.mixin;

import mods.tesseract.gregcaves.GregCaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Biome.class)
public class MixinBiome {

    @Inject(method = "generateBiomeTerrain", at = @At("RETURN"))
    private void smoothBedrock(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal, CallbackInfo ci) {
        if (GregCaves.smoothBedrock) {
            int localX = x & 15;
            int localZ = z & 15;

            for (int y = 1; y <= 5; y++) {
                IBlockState state = chunkPrimerIn.getBlockState(localX, y, localZ);

                if (state.getBlock() == Blocks.BEDROCK) {
                    chunkPrimerIn.setBlockState(localX, y, localZ, Blocks.STONE.getDefaultState());
                }
            }
        }
    }
}
