package me.ranzeplay.mcgit.managers.config;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    public static void CreateProfile() throws IOException {
        if (!Constants.ProfileDirectory.exists()) Constants.ProfileDirectory.mkdirs();

        File commitsFile = Constants.ArchivesProfileDirectory;
        if (!commitsFile.exists()) {
            Main.Instance.getServer().getLogger().warning("Archives configuration file is not found. Creating...");
            commitsFile.createNewFile();
        }
    }
}
