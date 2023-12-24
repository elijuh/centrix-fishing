package dev.elijuh.fishing.animations.impl.sound.impl;

import dev.elijuh.fishing.animations.impl.sound.Note;
import dev.elijuh.fishing.animations.impl.sound.NoteAnimation;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author elijuh
 */
public class FishCatchSoundLegendary extends NoteAnimation {
    private static final Note[] notes = {
        new Note(15, Sound.ORB_PICKUP).with(new Note(12, Sound.NOTE_BASS)),
        new Note(17, Sound.ORB_PICKUP).with(new Note(14, Sound.NOTE_BASS)),
        new Note(19, Sound.ORB_PICKUP).with(new Note(16, Sound.NOTE_BASS)),
        new Note(20, Sound.ORB_PICKUP).with(new Note(17, Sound.NOTE_BASS)),
        new Note(22, Sound.ORB_PICKUP).with(new Note(19, Sound.NOTE_BASS)),
        null,
        null,
        new Note(15, Sound.ORB_PICKUP)
            .with(new Note(12, Sound.NOTE_BASS))
            .with(new Note(19, Sound.ORB_PICKUP))
            .with(new Note(22, Sound.ORB_PICKUP))
            .with(new Note(3, Sound.ORB_PICKUP))
            .with(new Note(0, Sound.NOTE_BASS))
            .with(new Note(7, Sound.ORB_PICKUP))
            .with(new Note(10, Sound.ORB_PICKUP)),
    };

    public FishCatchSoundLegendary(Player p) {
        super(notes, 100, note -> note.play(p));
    }
}
