package dev.elijuh.fishing.animations.impl.sound.impl;

import dev.elijuh.fishing.animations.impl.sound.Note;
import dev.elijuh.fishing.animations.impl.sound.NoteAnimation;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author elijuh
 */
public class FishSellSound extends NoteAnimation {
    private static final Note[] notes = {
        new Note(10, Sound.NOTE_PLING),
        new Note(14, Sound.NOTE_PLING),
        new Note(17, Sound.NOTE_PLING),
        null,
        new Note(22, Sound.NOTE_PLING)
            .with(new Note(10, Sound.NOTE_PLING))
            .with(new Note(14, Sound.NOTE_PLING))
            .with(new Note(17, Sound.NOTE_PLING)),
        null,
        new Note(22, Sound.NOTE_PLING)
            .with(new Note(10, Sound.NOTE_PLING))
            .with(new Note(14, Sound.NOTE_PLING))
            .with(new Note(17, Sound.NOTE_PLING)),
    };

    public FishSellSound(Player p) {
        super(notes, 100, note -> note.play(p));
    }
}
