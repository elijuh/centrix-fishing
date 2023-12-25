package dev.elijuh.fishing.utils;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Logger;

/**
 * @author elijuh
 */
@RequiredArgsConstructor
public enum Library {
    HIKARI_CP(
        "com.zaxxer.hikari.HikariDataSource",
        "HikariCP",
        "4.0.3",
        "com/zaxxer/"
    ),
    SLF4J_API(
        "org.slf4j.Logger",
        "slf4j-api",
        "1.7.30",
        "org/slf4j/"
    ),
    ;

    private final String className, artifactId, version, repository;

    private static final Logger logger = Logger.getLogger("Libraries");
    private static final File FOLDER;
    private static final Method ADD_URL;
    private static final URLClassLoader CLASS_LOADER;

    static {
        FOLDER = new File(JavaPlugin.getProvidingPlugin(Library.class).getDataFolder(), "libraries");
        if (!FOLDER.exists()) {
            if (FOLDER.mkdirs()) {
                logger.info("Created folder: " + FOLDER.getAbsolutePath());
            }
        }
        Method a;
        try {
            a = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            a.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            a = null;
        }
        ADD_URL = a;
        CLASS_LOADER = (URLClassLoader) Library.class.getClassLoader();
    }

    public boolean isLoaded() {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public void load() {
        if (isLoaded()) return;
        File file = new File(FOLDER, artifactId + "-" + version + ".jar");
        if (!file.exists()) {
            download(file);
        }
        try {
            ADD_URL.invoke(CLASS_LOADER, file.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Loaded library: " + artifactId + "-" + version + ".jar");
    }

    public void download(File file) {
        try {
            String urlString = "https://repo1.maven.org/maven2/" + repository + String.format("%1$s/%2$s/%1$s-%2$s.jar", artifactId, version);
            final URL url = new URL(urlString);
            final URLConnection connection = url.openConnection();

            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");

            logger.info("Downloading library: " + urlString);
            try (InputStream input = connection.getInputStream();
                 ReadableByteChannel channel = Channels.newChannel(input);
                 FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
