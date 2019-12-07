package com.smrkn.announcer;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

class AnnouncerTask extends BukkitRunnable {
    private final String formattedHeader;
    private final String formattedFooter;
    private final List<String> formattedMessages;
    private int messageIndex = 0;

    AnnouncerTask(String formattedHeader, String formattedFooter, List<String> formattedMessages) {
        this.formattedHeader = formattedHeader;
        this.formattedFooter = formattedFooter;
        this.formattedMessages = formattedMessages;
    }

    @Override
    public void run() {
        if (messageIndex == formattedMessages.size()) messageIndex = 0;
        String formattedMessage = formattedMessages.get(messageIndex++);
        if (formattedHeader != null) Bukkit.getServer().broadcastMessage(formattedHeader);
        Bukkit.getServer().broadcastMessage(formattedMessage);
        if (formattedFooter != null) Bukkit.getServer().broadcastMessage(formattedFooter);
    }
}
