package me.ranzeplay.mcgit.managers;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;
import me.ranzeplay.mcgit.managers.zip.ZipManager;
import me.ranzeplay.mcgit.models.Archive;

import java.io.File;

public class BackupManager {
    /**
     * Will not check if the archiveId is valid
     *
     * @param archiveId Must be valid
     * @throws Exception When unzip operation fail
     */
    public static void Rollback(String archiveId) throws Exception {
        Archive archive = new Archive(null, null, null).getFromBukkitYmlFile(new File(Constants.ArchivesProfileDirectory + "/" + archiveId + ".yml"));
        if (Main.Instance.getConfig().getBoolean("compressNetherWorldByDefault")) {
            ZipManager.replaceWorldFromBackup(archive.getWorldName().replaceAll("_nether", "").replaceAll("_the_end", "") + "_nether",
                    archive.getArchiveId().toString().replace("-", ""));
        }
        if (Main.Instance.getConfig().getBoolean("compressTheEndWorldByDefault")) {
            ZipManager.replaceWorldFromBackup(archive.getWorldName().replaceAll("_nether", "").replaceAll("_the_end", "") + "_the_end",
                    archive.getArchiveId().toString().replace("-", ""));
        }

        ZipManager.replaceWorldFromBackup(archive.getWorldName().replaceAll("_nether", "").replaceAll("_the_end", ""), archiveId.replace("-", ""));
    }

    public static void Schedule(String archiveId) {
        Main.Instance.getConfig().set("nextRollback", archiveId);
        Constants.IsRollbackScheduled = true;
        Main.Instance.saveConfig();
    }

    /**
     * To abort rollback operation if it was scheduled
     *
     * @return Has been cancelled(true) or not scheduled(false)
     */
    public static boolean Abort() {
        if (Constants.IsRollbackScheduled) {
            Main.Instance.getConfig().set("nextRollback", "unset");
            Main.Instance.saveConfig();

            Constants.IsRollbackScheduled = false;
            return true;
        }

        return false;
    }
}
