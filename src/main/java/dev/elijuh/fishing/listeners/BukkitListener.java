package dev.elijuh.fishing.listeners;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.fish.Bait;
import dev.elijuh.fishing.fish.Fish;
import dev.elijuh.fishing.rod.RodData;
import dev.elijuh.fishing.tasks.MoveItemToPlayerTask;
import dev.elijuh.fishing.user.User;
import dev.elijuh.fishing.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import xyz.tozymc.spigot.api.title.TitleApi;

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
        if (!Core.i().getMaintenanceManager().allows(p)) return;

        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH && e.getCaught() instanceof Item) {
            User user = Core.i().getUser(p);
            Item item = (Item) e.getCaught();
            new MoveItemToPlayerTask(p, item, 1).start();
            if (item.getItemStack().getType() != Material.RAW_FISH) {
                if (!item.getItemStack().getEnchantments().isEmpty()) {
                    user.getUserData().incrementStatistic("treasureCaught", 1);
                } else {
                    user.getUserData().incrementStatistic("junkCaught", 1);
                }
                return;
            }
            user.getUserData().incrementStatistic("fishCaught", 1);
            RodData rod = RodData.fromItem(p.getItemInHand());
            Fish fish = Core.i().getFishService().randomCatch(Bait.NONE, rod);

            int tokens = fish.getType().getRarity().getTokenValue();
            user.addTokens(tokens);

            TitleApi.sendActionbar(p, Text.color("&7+ &b" + tokens + "⛁ &8(&b" + Text.getFormat().format(user.getTokens()) + "&8)"));

            item.setItemStack(fish.getItem());
            item.setCustomName(item.getItemStack().getItemMeta().getDisplayName());
            item.setCustomNameVisible(true);
            fishedBy.put(item, p.getUniqueId());

            fish.getType().getRarity().playCatchSound(p);

            p.sendMessage(Text.prefixed("&7You have caught a " +
                fish.getType().getRarity().getColor() + fish.getType().getDisplay() +
                " &7(&f" + Text.formatGrams(fish.getWeight()) + "&7)"
            ));

            int heaviestCatch = user.getUserData().getStatistic("heaviestCatch");
            if (fish.getWeight() > heaviestCatch) {
                user.getUserData().setStatistic("heaviestCatch", fish.getWeight());
            }
        }
    }
}
