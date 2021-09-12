package ru.dmitriymx.minecraft.metrics.sponge;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.plugin.PluginContainer;
import ru.dmitriymx.minecraft.config.ConfigProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@RequiredArgsConstructor
public class SpongeConfigProvider implements ConfigProvider {

    private final PluginContainer plugin;
    private final Path pluginDataDirPath;

    @Override
    @SneakyThrows
    public Config get() {
        Optional<Asset> optionalAsset = plugin.getAsset("config-default.conf");
        if (!optionalAsset.isPresent()) {
            throw new RuntimeException("Where is 'config-default.conf'!!??");
        }

        Config defaultConfig = ConfigFactory.parseString(optionalAsset.get().readString());
        Config config;
        Path userConfigPath = pluginDataDirPath.resolve("config.conf");
        if (Files.exists(userConfigPath)) {
            Config userConfig = ConfigFactory.parseFile(userConfigPath.toFile());
            config = userConfig.withFallback(defaultConfig).resolve();
        } else {
            config = defaultConfig.resolve();
        }

        return config;
    }
}
