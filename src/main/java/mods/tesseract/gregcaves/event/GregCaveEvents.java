package mods.tesseract.gregcaves.event;

import mods.tesseract.gregcaves.world.MapGenGregCaves;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GregCaveEvents {
    @SubscribeEvent
    public void mapGen(InitMapGenEvent e) {
        if (e.getType() == InitMapGenEvent.EventType.CAVE)
            e.setNewGen(new MapGenGregCaves());
    }
}
