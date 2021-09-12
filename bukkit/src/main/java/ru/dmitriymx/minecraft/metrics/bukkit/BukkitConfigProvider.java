package ru.dmitriymx.minecraft.metrics.bukkit;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dmitriymx.minecraft.config.ConfigProvider;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class BukkitConfigProvider implements ConfigProvider {

    private final JavaPlugin plugin;

    @Override
    @SneakyThrows
    public Config get() {
        InputStream inputStream = plugin.getClass().getResourceAsStream("/assets/config-default.conf");
        if (inputStream == null) {
            throw new RuntimeException("Where is 'config-default.conf'!!??");
        }

        Reader reader = new InputStreamReader(inputStream);
        Config defaultConfig = ConfigFactory.parseReader(reader);
        Config config;
        Path userConfigPath = plugin.getDataFolder().toPath().resolve("config.conf");
        if (Files.exists(userConfigPath)) {
            BufferedReader reader1 = Files.newBufferedReader(userConfigPath);
            Config userConfig = ConfigFactory.parseReader(reader1);
            config = userConfig.withFallback(defaultConfig).resolve();
        } else {
            config = defaultConfig.resolve();
        }

        return config;
    }
}
