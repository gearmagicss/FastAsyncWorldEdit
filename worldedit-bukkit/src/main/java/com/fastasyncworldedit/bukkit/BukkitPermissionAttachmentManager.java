package com.fastasyncworldedit.bukkit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitPermissionAttachmentManager {

    private final WorldEditPlugin plugin;
    private final Map<Player, PermissionAttachment> attachments = new ConcurrentHashMap<>();

    public BukkitPermissionAttachmentManager(WorldEditPlugin plugin) {
        this.plugin = plugin;
    }

    public PermissionAttachment getOrAddAttachment(@Nullable final Player p) {
        if (p == null) {
            return null;
        }
        return attachments.computeIfAbsent(p, k -> k.addAttachment(plugin));
    }

    public void removeAttachment(@Nullable final Player p) {
        if (p == null) {
            return;
        }
        PermissionAttachment attach = attachments.remove(p);
        if (attach != null) {
            p.removeAttachment(attach);
        }
    }

}
