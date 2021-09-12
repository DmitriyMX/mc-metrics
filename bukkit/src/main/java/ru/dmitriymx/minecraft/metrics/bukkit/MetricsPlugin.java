package ru.dmitriymx.minecraft.metrics.bukkit;

import com.typesafe.config.Config;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dmitriymx.minecraft.config.ConfigProvider;
import ru.dmitriymx.minecraft.metrics.MetricsServer;

import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("unused")
public class MetricsPlugin extends JavaPlugin {

    private MetricsServer metricsServer;

    @Override
    @SneakyThrows
    public void onEnable() {
        Path pluginDataDirPath = getDataFolder().toPath();
        if (Files.notExists(pluginDataDirPath)) {
            getLogger().info("Create data dir: " + pluginDataDirPath);
            Files.createDirectories(pluginDataDirPath);
        }

        ConfigProvider configProvider = new BukkitConfigProvider(this);
        Config config = configProvider.get();

        BukkitLogger logger = new BukkitLogger(getLogger());
        metricsServer = new MetricsServer(
            config.getString("server.host"),
            config.getInt("server.port"),
            config.getString("server.endpoint"),
            logger,
            new BukkitInfoProvider(getServer())
        );

        //noinspection HttpUrlsUsage
        logger.info("Start metrics server: http://{}:{}{}",
            config.getString("server.host"),
            config.getInt("server.port"),
            config.getString("server.endpoint"));
        metricsServer.start();
    }

    @Override
    @SneakyThrows
    public void onDisable() {
        metricsServer.stop();
    }
}
