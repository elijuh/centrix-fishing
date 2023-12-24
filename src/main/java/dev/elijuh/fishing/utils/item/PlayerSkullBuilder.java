package dev.elijuh.fishing.utils.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.elijuh.fishing.utils.ReflectionUtil;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

/**
 * @author elijuh
 */
public class PlayerSkullBuilder extends ItemBuilder implements Cloneable {

    private PlayerSkullBuilder() {
        super(Material.SKULL_ITEM);
        super.dura(3);
    }

    public static PlayerSkullBuilder create() {
        return new PlayerSkullBuilder();
    }

    public PlayerSkullBuilder textureUrl(String id) {
        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(id.getBytes()), null);
        SkullMeta skullMeta = (SkullMeta) getMeta();

        String value = "{\"textures\":{\"SKIN\":{\"url\":\"https://textures.minecraft.net/texture/" + id + "\"}}}";
        profile.getProperties().put("textures", new Property("textures", Base64.getEncoder().encodeToString(value.getBytes())));

        try {
            SKULL_META_PROFILE_FIELD.set(skullMeta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public PlayerSkullBuilder clone() {
        return (PlayerSkullBuilder) super.clone();
    }

    private static final Field SKULL_META_PROFILE_FIELD;

    static {
        Class<?> cmsClass = ReflectionUtil.getCBClass("inventory.CraftMetaSkull");
        SKULL_META_PROFILE_FIELD = ReflectionUtil.getField(cmsClass, "profile");
        if (SKULL_META_PROFILE_FIELD == null) throw new RuntimeException("profile field not found");
    }
}
