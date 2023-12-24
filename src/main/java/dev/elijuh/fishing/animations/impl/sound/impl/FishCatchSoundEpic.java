package dev.elijuh.fishing.animations.impl.sound.impl;

import dev.elijuh.fishing.animations.impl.sound.Note;
import dev.elijuh.fishing.animations.impl.sound.NoteAnimation;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author elijuh
 */
public class FishCatchSoundEpic extends NoteAnimation {
    private static final Note[] notes = {
        new Note(14, Sound.ORB_PICKUP),
        new Note(16, Sound.ORB_PICKUP),
        new Note(18, Sound.ORB_PICKUP),
        new Note(19, Sound.ORB_PICKUP),
        new Note(21, Sound.ORB_PICKUP),
        new Note(14, Sound.ORB_PICKUP)
            .with(new Note(18, Sound.ORB_PICKUP))
            .with(new Note(21, Sound.ORB_PICKUP)),
    };

    public FishCatchSoundEpic(Player p) {
        super(notes, 100, note -> note.play(p));
    }
}