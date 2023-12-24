package dev.elijuh.fishing.animations.impl.sound;

import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author elijuh
 */
@Getter
public class Note {
    private final float pitch, volume;
    private final Sound sound;
    private final List<Note> extra = new ArrayList<>();

    public Note(int pitch) {
        this(pitch, Sound.NOTE_PLING);
    }

    public Note(int pitch, Sound sound) {
        this(pitch, 1f, sound);
    }

    public Note(int pitch, float volume, Sound sound) {
        this.pitch = (float) Math.pow(2, (-12 + pitch) / 12f);
        this.volume = volume;
        this.sound = sound;
    }

    public Note with(Note other) {
        extra.add(other);
        return this;
    }

    public void play(Player player) {
        player.playSound(player.getLocation(), sound, 1f, pitch);
        for (Note note : extra) {
            note.play(player);
        }
    }
}
