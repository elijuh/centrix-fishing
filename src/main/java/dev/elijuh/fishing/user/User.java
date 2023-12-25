package dev.elijuh.fishing.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author elijuh
 */
@Getter
@RequiredArgsConstructor
public class User {
    private final Player player;
    private final UserData userData;

    public UUID uuid() {
        return player.getUniqueId();
    }

    public int getTokens() {
        return userData.getStatistic("tokens");
    }
}
