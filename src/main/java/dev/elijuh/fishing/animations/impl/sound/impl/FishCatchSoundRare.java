package dev.elijuh.fishing.animations.impl.sound.impl;

import dev.elijuh.fishing.animations.impl.sound.Note;
import dev.elijuh.fishing.animations.impl.sound.NoteAnimation;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author elijuh
 */
public class FishCatchSoundRare extends NoteAnimation {
    private static final Note[] notes = {
        new Note(13, Sound.ORB_PICKUP),
        new Note(15, Sound.ORB_PICKUP),
        new Note(17, Sound.ORB_PICKUP),
        new Note(18, Sound.ORB_PICKUP),
        new Note(20, Sound.ORB_PICKUP),
        new Note(13, Sound.ORB_PICKUP)
            .with(new Note(17, Sound.ORB_PICKUP))
            .with(new Note(20, Sound.ORB_PICKUP)),
    };

    public FishCatchSoundRare(Player p) {
        super(notes, 100, note -> note.play(p));
    }
}
