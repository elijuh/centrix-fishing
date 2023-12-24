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
    MYSQL_CONNECTOR_J(
        "mysql-connector-j",
        "8.0.33",
        "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/{v}/mysql-connector-j-{v}.jar"
    ),
    HIKARI_CP(
        "HikariCP",
        "4.0.3",
        "https://repo1.maven.org/maven2/com/zaxxer/HikariCP/{v}/HikariCP-{v}.jar"
    ),
    SLF4J_API(
        "slf4j-api",
        "1.7.30",
        "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/{v}/slf4j-api-{v}.jar"
    ),
    REFLECTIONS(
        "reflections",
        "0.10.2",
        "https://repo1.maven.org/maven2/org/reflections/reflections/{v}/reflections-{v}.jar"
    ),
    JAVASSIST(
        "javassist",
        "3.28.0-GA",
        "https://repo1.maven.org/maven2/org/javassist/javassist/{v}/javassist-{v}.jar"
    ),
    ;

    private final String artifactId;
    private final String version;
    private final String repository;

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

    public void load() {
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
            String urlString = repository.replace("{v}", version);
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
