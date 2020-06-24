package me.ranzeplay.mcgit;

import java.io.File;
import java.text.SimpleDateFormat;

public class Constants {
    public static final File BackupsDirectory = new File(Main.Instance.getDataFolder() + "/Backups");
    public static final File ConfigDirectory = new File(Main.Instance.getDataFolder() + "/Config");

    // Formatting
    public static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    // Backup File Configurations
    public static final File CommitsDirectory = new File(Constants.ConfigDirectory + "/Commits");
    public static final File CollectionsDirectory = new File(Constants.ConfigDirectory + "/Collections");

    /**
     * Just used to tell players if it is going to rollback server save st the next server startup
     */
    public static boolean IsScheduled = false;
}
