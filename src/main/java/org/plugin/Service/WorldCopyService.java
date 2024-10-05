package org.plugin.Service;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class WorldCopyService {
    private final JavaPlugin plugin;

    public WorldCopyService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean copyWorldFromResources(String worldName) {
        Path worldFolder = plugin.getServer().getWorldContainer().toPath().resolve(worldName);
        if (Files.exists(worldFolder) && !Files.isDirectory(worldFolder)) {
            plugin.getLogger().warning("Файл с именем '" + worldName + "' уже существует и не является папкой. Удаляем файл.");
            try {
                Files.delete(worldFolder);
            } catch (IOException e) {
                plugin.getLogger().severe("Ошибка при удалении файла: " + e.getMessage());
                return false;
            }
        }

        try {
            plugin.getLogger().info("Начало копирования мира '" + worldName + "'");
            copyDirectoryFromResources(worldName, worldFolder);
            plugin.getLogger().info("Мир '" + worldName + "' успешно скопирован");
            return true;
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при копировании мира из ресурсов: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void copyDirectoryFromResources(String resourcePath, Path destination) throws IOException {
        try (JarFile jarFile = new JarFile(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replace("%20", " "))) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith(resourcePath + "/")) {
                    Path newPath = destination.resolve(entry.getName().substring(resourcePath.length() + 1));
                    if (entry.isDirectory()) {
                        Files.createDirectories(newPath);
                    } else {
                        try (InputStream is = jarFile.getInputStream(entry)) {
                            Files.createDirectories(newPath.getParent());
                            Files.copy(is, newPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }
        }
    }
}
