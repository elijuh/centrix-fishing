package dev.elijuh.fishing.animations;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.elijuh.fishing.Core;
import lombok.Getter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author elijuh
 */
public abstract class TimedAnimation<T> implements Runnable {
    @Getter
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
        .setNameFormat("timed-animation-thread-%d")
        .setUncaughtExceptionHandler((t, e) -> {
            Core.i().getLogger().warning("exception occured in thread " + t.getName());
            e.printStackTrace();
        }).build());

    private final T[] phases;
    private final Consumer<T> consumer;
    private final long interval;

    public TimedAnimation(T[] phases, long interval, Consumer<T> consumer) {
        if (phases.length == 0) {
            throw new IllegalArgumentException("phases cannot be empty");
        }
        this.phases = phases;
        this.consumer = consumer;
        this.interval = interval;
    }

    private int phase;

    @Override
    public void run() {
        if (phase >= phases.length) {
            cancel();
        } else {
            T stage = phases[phase++];
            if (stage != null) {
                consumer.accept(stage);
            }
        }
    }

    private ScheduledFuture<?> future;

    public void start() {
        future = executor.scheduleAtFixedRate(this, 0, interval, TimeUnit.MILLISECONDS);
    }

    public void cancel() {
        if (future == null) {
            throw new IllegalStateException("cannot cancel TimedAnimation that has not been started");
        }
        future.cancel(true);
    }
}

