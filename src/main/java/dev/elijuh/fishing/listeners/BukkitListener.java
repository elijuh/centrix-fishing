package dev.elijuh.fishing.listeners;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.fish.Bait;
import dev.elijuh.fishing.fish.Fish;
import dev.elijuh.fishing.utils.Text;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author elijuh
 */
public class BukkitListener implements Listener {

    @EventHandler
    public void on(PlayerPickupItemEvent e) {
        Item item = e.getItem();
        UUID fisher = fishedBy.asMap().get(item);
        if (fisher == null) return;

        if (!fisher.equals(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        } else {
            fishedBy.asMap().remove(item);
        }
    }

    private final Cache<Item, UUID> fishedBy = CacheBuilder.newBuilder()
        .expireAfterWrite(20, TimeUnit.SECONDS)
        .build();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(PlayerFishEvent e) {
        Player p = e.getPlayer();
        if (!Core.getTestingWhitelist().contains(p.getName())) return;

        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH && e.getCaught() instanceof Item) {
            Fish fish = Core.i().getFishService().randomCatch(Bait.NONE);

            Item item = (Item) e.getCaught();
            item.setItemStack(fish.getItem());
            item.setCustomName(item.getItemStack().getItemMeta().getDisplayName());
            item.setCustomNameVisible(true);
            fishedBy.put(item, p.getUniqueId());
            fish.getType().getRarity().playCatchSound(p);
            p.sendMessage(Text.prefixed("&7You have caught a " +
                fish.getType().getRarity().getColor() + fish.getType().getDisplay() +
                " &7(&f" + Text.formatGrams(fish.getWeight()) + "&7)"
            ));
        }
    }
}
