package ru.dmitriymx.minecraft.metrics.sponge;

import org.spongepowered.api.Sponge;
import ru.dmitriymx.minecraft.metrics.MinecraftInfoProvider;

public class SpongeInfoProvider implements MinecraftInfoProvider {

    @Override
    public int playersOnline() {
        return Sponge.getServer().getOnlinePlayers().size();
    }

    @Override
    public double tps() {
        return Sponge.getServer().getTicksPerSecond();
    }
}
