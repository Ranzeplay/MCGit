package me.ranzeplay.mcgit;

import java.io.File;
import java.text.SimpleDateFormat;

public class Constants {
    public static final File ArchivesDirectory = new File(Main.Instance.getDataFolder() + "/Archives");
    public static final File ProfileDirectory = new File(Main.Instance.getDataFolder() + "/Profile");

    // Formatting
    public static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    // Backup File Configurations
    public static final File ArchivesProfileDirectory = new File(Constants.ProfileDirectory + "/Archives/");
    public static final File CollectionsProfileDirectory = new File(Constants.ProfileDirectory + "/Collections/");

    // Other
    public static final File TempDirectory = new File(Main.Instance.getDataFolder() + "/Temp/");

    /**
     * Just used to tell players if it is going to rollback server save st the next server startup
     */
    public static boolean IsRollbackScheduled = false;
}
