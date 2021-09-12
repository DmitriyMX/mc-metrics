package ru.dmitriymx.minecraft.config;

import com.typesafe.config.Config;

public interface ConfigProvider {

    Config get();
}
