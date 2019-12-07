package com.smrkn.announcer;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AnnouncerPlugin extends JavaPlugin {
    // Keep a reference to our scheduled task so we may cancel it in onDisable
    private BukkitTask announcer;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Explicitly declare a default to save a lookup
        String formattedHeader = translateOrNull(getConfig().getString("formatting.header", null));
        // Explicitly declare a default to save a lookup
        String formattedFooter = translateOrNull(getConfig().getString("formatting.footer", null));
        /*
         * Streaming only occurs when the Plugin state is set to "enabled" so we can afford to be fancy
         * as it's safe to assume people won't be spamming reloads
         */
        List<String> formattedMessages = getConfig().getStringList("messages").stream()
                .map(this::translateOrNull)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // There's not much point in leaving the plugin enabled if it's not doing anything
        if (formattedMessages.isEmpty()) {
            getLogger().severe(ChatColor.RED + "No messages have been defined in the configuration file, this plugin will be disabled.");
            getPluginLoader().disablePlugin(this);
            return;
        }

        /*
         * Explicitly declare a default to save a lookup
         * Standard architecture runs on 20 TPS, multiply by 20 to convert seconds to ticks
         */
        int interval = getConfig().getInt("interval", 10) * 20;
        this.announcer = new AnnouncerTask(formattedHeader, formattedFooter, formattedMessages)
                .runTaskTimer(this, interval, interval);
    }

    @Override
    public void onDisable() {
        // Graceful termination of our scheduled task, but only if the plugin successfully initialised
        if (this.announcer != null) {
            this.announcer.cancel();
            this.announcer = null;
        }
    }

    // Convenience method for shorthand lambda expression
    private String translateOrNull(String input) {
        return input == null ? null : ChatColor.translateAlternateColorCodes('&', input);
    }
}
