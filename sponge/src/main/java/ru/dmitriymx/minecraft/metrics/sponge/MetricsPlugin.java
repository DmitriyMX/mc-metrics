package ru.dmitriymx.minecraft.metrics.sponge;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import lombok.SneakyThrows;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import ru.dmitriymx.minecraft.config.ConfigProvider;
import ru.dmitriymx.minecraft.metrics.MetricsServer;

import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = "minecraft_metrics",
		name = "MinecraftMetrics",
		description = "Minecraft Prometheus Exporter",
		version = "1.0",
		authors = { "DmitriyMX" })
@SuppressWarnings("unused")
public class MetricsPlugin {

    @Inject
    private org.slf4j.Logger slf4jLogger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path pluginDataDirPath;

    @Inject
    private PluginContainer pluginContainer;

    private MetricsServer metricsServer;

    @Listener
    @SneakyThrows
    public void onGameStartedServerEvent(GameStartedServerEvent event) {
        if (Files.notExists(pluginDataDirPath)) {
            slf4jLogger.info("Create data dir: {}", pluginDataDirPath);
            Files.createDirectories(pluginDataDirPath);
        }

        ConfigProvider configProvider = new SpongeConfigProvider(pluginContainer, pluginDataDirPath);
        Config config = configProvider.get();

        SpongeLogger logger = new SpongeLogger(slf4jLogger);
        metricsServer = new MetricsServer(
            config.getString("server.host"),
            config.getInt("server.port"),
            config.getString("server.endpoint"),
            logger,
            new SpongeInfoProvider()
        );

        //noinspection HttpUrlsUsage
        logger.info("Start metrics server: http://{}:{}{}",
            config.getString("server.host"),
            config.getInt("server.port"),
            config.getString("server.endpoint"));
        metricsServer.start();
    }

    @Listener
    public void onGameStoppingServerEvent(GameStoppingServerEvent event) {
        metricsServer.stop();
    }
}
