package me.ranzeplay.mcgit.managers;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.models.Archive;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class ArchiveManager {
    public static Archive makeArhive(String description, Player player, World world) throws IOException {
        Archive archive = new Archive(description, player, world);

        File newArchive = new File(Constants.ArchivesProfileDirectory + "/" + archive.getArchiveId().toString() + ".yml");
        newArchive.createNewFile();

        FileConfiguration filec = archive.saveToBukkitYmlFile();
        filec.save(newArchive);

        return archive;
    }

    public static ArrayList<Archive> getAllArchives() throws ParseException {
        ArrayList<Archive> list = new ArrayList<>();
        File archivesDirectory = Constants.ArchivesProfileDirectory;
        if (Objects.requireNonNull(archivesDirectory.listFiles()).length == 0) return list;
        for (File file : Objects.requireNonNull(archivesDirectory.listFiles())) {
            list.add(new Archive(null, null, null).getFromBukkitYmlFile(file));
        }

        return list;
    }

    public static Archive getArchive(String archiveId) throws ParseException {
        File archiveFile = new File(Constants.ArchivesProfileDirectory + "/" + archiveId + ".yml");
        if (!archiveFile.exists()) {
            return null;
        }

        return new Archive(null, null, null).getFromBukkitYmlFile(archiveFile);
    }

    public static double GetArchiveTotalSize(String archiveId) {
        double totalSize = 0;

        File archiveFile = new File(Constants.ArchivesDirectory + "/" + archiveId.replace("-", ""));
        for (File file : Objects.requireNonNull(archiveFile.listFiles())) {
            totalSize += file.length();
        }

        return totalSize;
    }
}
