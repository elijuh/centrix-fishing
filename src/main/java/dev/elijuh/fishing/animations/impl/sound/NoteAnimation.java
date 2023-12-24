package dev.elijuh.fishing.animations.impl.sound;

import dev.elijuh.fishing.animations.TimedAnimation;

import java.util.function.Consumer;

/**
 * @author elijuh
 */
public abstract class NoteAnimation extends TimedAnimation<Note> {

    public NoteAnimation(Note[] phases, long interval, Consumer<Note> consumer) {
        super(phases, interval, consumer);
    }
}
