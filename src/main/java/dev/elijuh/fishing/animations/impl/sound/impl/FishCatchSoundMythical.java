package dev.elijuh.fishing.animations.impl.sound.impl;

import dev.elijuh.fishing.animations.impl.sound.Note;
import dev.elijuh.fishing.animations.impl.sound.NoteAnimation;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author elijuh
 */
public class FishCatchSoundMythical extends NoteAnimation {
    private static final Note[] notes = {
        new Note(16, Sound.ORB_PICKUP).with(new Note(13, Sound.NOTE_BASS)),
        new Note(18, Sound.ORB_PICKUP).with(new Note(15, Sound.NOTE_BASS)),
        new Note(20, Sound.ORB_PICKUP).with(new Note(17, Sound.NOTE_BASS)),
        new Note(21, Sound.ORB_PICKUP).with(new Note(18, Sound.NOTE_BASS)),
        new Note(23, Sound.ORB_PICKUP).with(new Note(20, Sound.NOTE_BASS)),
        null,
        null,
        new Note(16, Sound.ORB_PICKUP)
            .with(new Note(13, Sound.NOTE_BASS))
            .with(new Note(20, Sound.ORB_PICKUP))
            .with(new Note(23, Sound.ORB_PICKUP))
            .with(new Note(4, Sound.ORB_PICKUP))
            .with(new Note(1, Sound.NOTE_BASS))
            .with(new Note(8, Sound.ORB_PICKUP))
            .with(new Note(11, Sound.ORB_PICKUP)),
    };

    public FishCatchSoundMythical(Player p) {
        super(notes, 100, note -> note.play(p));
    }
}
