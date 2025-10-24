package mods.tesseract.gregcaves;

import java.util.Map;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import org.spongepowered.asm.launch.MixinBootstrap;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.mixin.Mixins;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class GregCavesPlugin implements IFMLLoadingPlugin {

	public GregCavesPlugin() {
		MixinBootstrap.init();
		MixinExtrasBootstrap.init();
		Mixins.addConfiguration("mixins.gregcaves.vanilla.json");
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) { }

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}
