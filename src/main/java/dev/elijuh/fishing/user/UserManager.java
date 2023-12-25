package dev.elijuh.fishing.user;

import dev.elijuh.fishing.Core;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author elijuh
 */
@Getter
public class UserManager implements Listener {
    private final Map<UUID, User> users = new ConcurrentHashMap<>();
    private final Map<UUID, UserData> userDataCache = new HashMap<>();

    public UserManager() {
        Bukkit.getPluginManager().registerEvents(this, Core.i());

        CompletableFuture.allOf(Bukkit.getOnlinePlayers().stream().map(p ->
            Core.i().getStorage().getDao().getUserData(p.getUniqueId()).thenAccept(fetched ->
                users.put(p.getUniqueId(), new User(p, fetched))
            )
        ).toArray(CompletableFuture[]::new)).join();

        Bukkit.getScheduler().runTaskTimerAsynchronously(Core.i(), () ->
            users.values().forEach(this::save), 6000L, 6000L
        );
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on(AsyncPlayerPreLoginEvent e) {
        if (!Core.getTestingWhitelist().contains(e.getName())) return;
        if (e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;

        UserData fetched = Core.i().getStorage().getDao().getUserData(e.getUniqueId()).join();
        userDataCache.put(e.getUniqueId(), fetched == null ? new UserData() : fetched);
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        if (!Core.getTestingWhitelist().contains(e.getPlayer().getName())) return;
        Player p = e.getPlayer();
        UserData data = userDataCache.remove(p.getUniqueId());
        if (data == null) {
            p.kickPlayer(ChatColor.RED + "An error occured while loading data, please try again.");
        } else {
            users.put(p.getUniqueId(), new User(p, data));
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        User user = users.remove(e.getPlayer().getUniqueId());
        if (user != null) {
            save(user);
        }
    }

    public CompletableFuture<Void> save(User user) {
        return CompletableFuture.allOf(
            Core.i().getStorage().getDao().save(user.uuid(), user.getUserData()),
            Core.i().getStorage().getDao().saveName(user.uuid(), user.getPlayer().getName())
        );
    }

    public User getUser(Player player) {
        return users.get(player.getUniqueId());
    }

    public void shutdown() {
        CompletableFuture.allOf(users.values().stream().map(this::save).toArray(CompletableFuture[]::new)).join();
    }
}
