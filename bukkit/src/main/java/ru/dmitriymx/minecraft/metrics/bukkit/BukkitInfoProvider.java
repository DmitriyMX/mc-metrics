package ru.dmitriymx.minecraft.metrics.bukkit;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Server;
import ru.dmitriymx.minecraft.metrics.MinecraftInfoProvider;

import java.lang.reflect.Field;

@RequiredArgsConstructor
public class BukkitInfoProvider implements MinecraftInfoProvider {

    private final Server bukkitServer;

    @Override
    public int playersOnline() {
        return bukkitServer.getOnlinePlayers().size();
    }

    @Override
    @SneakyThrows
    public double tps() {
        Field consoleField = bukkitServer.getClass().getDeclaredField("console");
        consoleField.setAccessible(true);
        Object minecraftServer = consoleField.get(bukkitServer);
        Field recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
        recentTps.setAccessible(true);

        double tps = ((double[]) recentTps.get(minecraftServer))[0];
        return Math.min(tps, 20.0d);
    }
}
