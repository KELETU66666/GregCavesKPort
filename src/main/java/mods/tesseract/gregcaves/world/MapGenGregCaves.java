package mods.tesseract.gregcaves.world;

import mods.tesseract.gregcaves.GregCaves;
import net.minecraft.block.BlockFalling;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class MapGenGregCaves extends MapGenCaves {
    private double[] caveNoise;
    private float[] biomeWeightTable;
    private NoiseCaveGenerator noiseCaves;
    public NoiseGeneratorOctaves noiseGen6;
    private NoiseGeneratorOctaves field_147431_j;
    private NoiseGeneratorOctaves field_147432_k;
    private NoiseGeneratorOctaves interpolationNoise;
    private double[] interpolationNoises;
    private double[] lowerInterpolatedNoises;
    private double[] upperInterpolatedNoises;
    private double[] depthNoises;

    @Override
    public void generate(World worldIn, int x, int z, ChunkPrimer primer) {
        if (this.world != worldIn) {
            this.caveNoise = new double[825];
            this.biomeWeightTable = new float[25];
            this.field_147431_j = new NoiseGeneratorOctaves(this.rand, 16);
            this.field_147432_k = new NoiseGeneratorOctaves(this.rand, 16);
            this.interpolationNoise = new NoiseGeneratorOctaves(this.rand, 8);
            this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
            this.noiseCaves = new NoiseCaveGenerator(this.rand);
            for (int j = -2; j <= 2; ++j) {
                for (int k = -2; k <= 2; ++k) {
                    float f = 10.0F / MathHelper.sqrt((float) (j * j + k * k) + 0.2F);
                    this.biomeWeightTable[j + 2 + (k + 2) * 5] = f;
                }
            }
        }
        this.world = worldIn;
        this.rand.setSeed(worldIn.getSeed());
        int k = this.range;
        long l = this.rand.nextLong();
        long i1 = this.rand.nextLong();
        BlockFalling.fallInstantly = true;

        for (int j1 = x - k; j1 <= x + k; ++j1) {
            for (int k1 = z - k; k1 <= z + k; ++k1) {
                long l1 = (long) j1 * l;
                long i2 = (long) k1 * i1;
                this.rand.setSeed(l1 ^ i2 ^ worldIn.getSeed());
                this.recursiveGenerate(worldIn, j1, k1, x, z, primer);
            }
        }
        this.generateNoiseCaves(x, z, primer);
        BlockFalling.fallInstantly = false;
    }

    private void generateNoiseCaves(int chunkX, int chunkZ, ChunkPrimer primer) {
        generateNoiseCavesNoise(chunkX, chunkZ);

        for (int noiseX = 0; noiseX < 4; ++noiseX) {
            int ix0 = noiseX * 5;
            int ix1 = (noiseX + 1) * 5;

            for (int noiseZ = 0; noiseZ < 4; ++noiseZ) {
                int ix0z0 = (ix0 + noiseZ) * 33;
                int ix0z1 = (ix0 + noiseZ + 1) * 33;
                int ix1z0 = (ix1 + noiseZ) * 33;
                int ix1z1 = (ix1 + noiseZ + 1) * 33;

                for (int noiseY = 0; noiseY < 32; ++noiseY) {
                    double x0z0 = this.caveNoise[ix0z0 + noiseY];
                    double x0z1 = this.caveNoise[ix0z1 + noiseY];
                    double x1z0 = this.caveNoise[ix1z0 + noiseY];
                    double x1z1 = this.caveNoise[ix1z1 + noiseY];
                    double x0z0Add = (this.caveNoise[ix0z0 + noiseY + 1] - x0z0) * 0.125D;
                    double x0z1Add = (this.caveNoise[ix0z1 + noiseY + 1] - x0z1) * 0.125D;
                    double x1z0Add = (this.caveNoise[ix1z0 + noiseY + 1] - x1z0) * 0.125D;
                    double x1z1Add = (this.caveNoise[ix1z1 + noiseY + 1] - x1z1) * 0.125D;

                    for (int pieceY = 0; pieceY < 8; ++pieceY) {
                        double z0 = x0z0;
                        double z1 = x0z1;
                        double z0Add = (x1z0 - x0z0) * 0.25D;
                        double z1Add = (x1z1 - x0z1) * 0.25D;

                        for (int pieceX = 0; pieceX < 4; ++pieceX) {
                            double densityAdd = (z1 - z0) * 0.25D;
                            double density = z0 - densityAdd;

                            for (int pieceZ = 0; pieceZ < 4; ++pieceZ) {
                                if ((density += densityAdd) < 0) {
                                    int realX = pieceX + noiseX * 4;
                                    int realY = noiseY * 8 + pieceY;
                                    int realZ = pieceZ + noiseZ * 4;

                                    if (GregCaves.smoothBedrock) {
                                        if (primer.getBlockState(realX, realY, realZ).getBlock() == Blocks.STONE) {
                                            if (realY > 5) {
                                                if (realY < GregCaves.caveLavaLevel) {
                                                    primer.setBlockState(realX, realY, realZ, Blocks.FLOWING_LAVA.getDefaultState());
                                                } else {
                                                    primer.setBlockState(realX, realY, realZ, Blocks.AIR.getDefaultState());
                                                }
                                            }
                                        }
                                    } else {
                                        if (realY > 0) {
                                            if (primer.getBlockState(realX, realY, realZ).getBlock() == Blocks.BEDROCK) {
                                                primer.setBlockState(realX, realY, realZ, Blocks.STONE.getDefaultState());
                                            } else {
                                                primer.setBlockState(realX, realY, realZ, Blocks.AIR.getDefaultState());
                                            }
                                        }
                                    }
                                }
                            }

                            z0 += z0Add;
                            z1 += z1Add;
                        }

                        x0z0 += x0z0Add;
                        x0z1 += x0z1Add;
                        x1z0 += x1z0Add;
                        x1z1 += x1z1Add;
                    }
                }
            }
        }
    }

    private void generateNoiseCavesNoise(int chunkX, int chunkZ) {
        int cx = chunkX * 4, cz = chunkZ * 4;
        this.depthNoises = this.noiseGen6.generateNoiseOctaves(this.depthNoises, cx, cz, 5, 5, 200.0D, 200.0D, 0.5D);
        this.interpolationNoises = this.interpolationNoise.generateNoiseOctaves(this.interpolationNoises, cx, 0, cz, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        this.lowerInterpolatedNoises = this.field_147431_j.generateNoiseOctaves(this.lowerInterpolatedNoises, cx, 0, cz, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        this.upperInterpolatedNoises = this.field_147432_k.generateNoiseOctaves(this.upperInterpolatedNoises, cx, 0, cz, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        Biome[] biomes = null;
        biomes = this.world.getBiomeProvider().getBiomesForGeneration(biomes, cx - 2, cz - 2, 10, 10);
        int i = 0, j = 0;

        for (int x = 0; x < 5; ++x) {
            for (int z = 0; z < 5; ++z) {
                float scale = 0.0F;
                float depth = 0.0F;
                float weight = 0.0F;
                double lowestScaledDepth = 0;

                Biome biome0 = biomes[x + 2 + (z + 2) * 10];
                // We iterate the entire area to ensure we're not anywhere even near an ocean
                for (int x1 = -2; x1 <= 2; ++x1) {
                    for (int z1 = -2; z1 <= 2; ++z1) {
                        Biome biome = biomes[x + x1 + 2 + (z + z1 + 2) * 10];
                        float depthHere = biome.getBaseHeight();
                        float scaleHere = biome.getHeightVariation();

                        float weightHere = this.biomeWeightTable[x1 + 2 + (z1 + 2) * 5] / (depthHere + 2.0F);

                        if (biome.getBaseHeight() > biome0.getBaseHeight()) {
                            weightHere /= 2.0F;
                        }

                        scale += scaleHere * weightHere;
                        depth += depthHere * weightHere;
                        weight += weightHere;
                        // Disable in oceans
                        lowestScaledDepth = Math.min(lowestScaledDepth, biome.getBaseHeight());
                    }
                }
                scale /= weight;
                depth /= weight;
                scale = scale * 0.9F + 0.1F;
                depth = (depth * 4.0F - 1.0F) / 8.0F;
                double depthNoise = this.depthNoises[j] / 8000;

                if (depthNoise < 0.0D) {
                    depthNoise = -depthNoise * 0.3D;
                }

                depthNoise = depthNoise * 3.0D - 2.0D;

                if (depthNoise < 0.0D) {
                    depthNoise /= 2.0D;

                    if (depthNoise < -1.0D) {
                        depthNoise = -1.0D;
                    }

                    depthNoise /= 1.4D;
                    depthNoise /= 2.0D;
                } else {
                    if (depthNoise > 1.0D) {
                        depthNoise = 1.0D;
                    }

                    depthNoise /= 8.0D;
                }

                ++j;
                double scaledDepth = depth;
                double scaledScale = scale;
                scaledDepth += depthNoise * 0.2D;
                scaledDepth = scaledDepth * 8.5D / 8.0D;
                double terrainHeight = 8.5D + scaledDepth * 4.0D;

                // Each unit of depth roughly corresponds to 16 blocks, but we use 20 for good measure
                // We start reduction at 56 instead of 64, the sea level, to give ourselves some more room.
                double startLevel = 56 + (lowestScaledDepth * 20);
                int sub = (int) (startLevel / 8);

                for (int y = 0; y < 33; y++) {
                    double falloff = ((double) y - terrainHeight) * 12.0D * 128.0D / 256.0D / scaledScale;

                    if (falloff < 0.0D) {
                        falloff *= 4.0D;
                    }

                    double lowerNoise = this.lowerInterpolatedNoises[i] / 512.0D;
                    double upperNoise = this.upperInterpolatedNoises[i] / 512.0D;
                    double interpolation = (this.interpolationNoises[i] / 10.0D + 1.0D) / 2.0D;
                    double noise = MathHelper.clampedLerp(lowerNoise, upperNoise, interpolation) - falloff;

                    // Scale down the last 3 layers
                    if (y > 29) {
                        double lerp = (float) (y - 29) / 3.0F;
                        noise = noise * (1.0D - lerp) + -10.0D * lerp;
                    }

                    double caveNoise = this.noiseCaves.sample(noise, y * 8, chunkZ * 16 + (z * 4), chunkX * 16 + (x * 4));

                    // Reduce so we don't break the surface
                    caveNoise = mods.tesseract.gregcaves.util.MathHelper.clampedLerp(caveNoise, (lowestScaledDepth * -30) + 20, (y - sub + 2) / 2.0);

                    this.caveNoise[i] = caveNoise;
                    i++;
                }
            }
        }
    }

    private boolean isExceptionBiome(Biome biome) {
        return biome == net.minecraft.init.Biomes.DESERT ||
               biome == net.minecraft.init.Biomes.BEACH ||
               biome == net.minecraft.init.Biomes.MUSHROOM_ISLAND;
    }

    protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop, net.minecraft.block.state.IBlockState state, net.minecraft.block.state.IBlockState up) {
        Biome biome = world.getBiome(new net.minecraft.util.math.BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
        net.minecraft.block.state.IBlockState top = (isExceptionBiome(biome) ? Blocks.GRASS.getDefaultState() : biome.topBlock);
        net.minecraft.block.state.IBlockState filler = (isExceptionBiome(biome) ? Blocks.DIRT.getDefaultState() : biome.fillerBlock);

        if (state.getBlock() == Blocks.STONE || state == filler || state == top) {
            if (y < GregCaves.caveLavaLevel - 1) {
                data.setBlockState(x, y, z, Blocks.FLOWING_LAVA.getDefaultState());
            } else {
                data.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
                if (foundTop && data.getBlockState(x, y + 1, z) == filler) {
                    data.setBlockState(x, y + 1, z, top);
                }
            }
        }
    }
}
