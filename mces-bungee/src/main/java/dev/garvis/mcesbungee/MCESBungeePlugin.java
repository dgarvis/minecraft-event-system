package dev.garvis.mcesbungee;

import dev.garvis.mcesbungee.KafkaManager;
import dev.garvis.mcesbungee.Events;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.File;

public class MCESBungeePlugin extends Plugin {

    private KafkaManager kafka = new KafkaManager();
    
    @Override
    public void onEnable() {
	try {
	    makeConfig();
	} catch (IOException e) {
	    getLogger().warning("Could not create default config.");
	}

	Configuration config;
	try {
	    config = ConfigurationProvider.getProvider(YamlConfiguration.class)
		.load(new File(getDataFolder(), "config.yml"));
	} catch (IOException e) {
	    getLogger().warning("Could not load config.");
	    return;
	}

	String kafkaServer = config.getString("kafkaServer");
	String serverName = config.getString("serverName");

	if (kafka.connect(kafkaServer, serverName)) // using server name as client id.
	    getLogger().info("Connected to Kafka");
	else
	    getLogger().warning("Not connected to kafka, check plugin config.");

	getProxy().getPluginManager().registerListener(this, new Events(kafka));
	
	getLogger().info("onEnable");
    }

    public void makeConfig() throws IOException {
	// Create plugin config folder if it doesn't exist
	if (!getDataFolder().exists()) {
	    getLogger().info("Created config folder: " + getDataFolder().mkdir());
	}
	
	File configFile = new File(getDataFolder(), "config.yml");

	// Copy default config if it doesn't exist
	if (!configFile.exists()) {
	    FileOutputStream outputStream = new FileOutputStream(configFile); // Throws IOException
	    InputStream in = getResourceAsStream("config.yml"); // This file must exist in the jar resources folder
	    in.transferTo(outputStream); // Throws IOException
	}
    }
}
