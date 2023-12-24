package dev.elijuh.fishing.storage;

/**
 * @author elijuh
 */
public interface Storage {

    void shutdown();

    Dao getDao();
}
