package dev.garvis.mcesbungee;

import dev.garvis.mcesbungee.KafkaManager;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;

import java.util.Map;
import java.util.HashMap;

public class Events implements Listener {

    private KafkaManager kafka;

    public Events(KafkaManager kafka) {
	this.kafka = kafka;
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
	Map<String, Object> e = new HashMap<String, Object>();
	e.put("eventType", "PLAYER_DISCONNECTED");
	e.put("playerName", event.getPlayer().getName());
	e.put("playerUUID", event.getPlayer().getUniqueId().toString());

	kafka.sendMessage(e);
    }
}
