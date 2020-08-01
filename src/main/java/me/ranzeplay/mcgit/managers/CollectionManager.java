package me.ranzeplay.mcgit.managers;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.models.Archive;
import me.ranzeplay.mcgit.models.ArchivesCollection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class CollectionManager {
    public static ArchivesCollection create(String collectionName, String collectionDescription) throws IOException {
        ArchivesCollection collection = new ArchivesCollection(collectionName, collectionDescription);

        File newCollection = new File(Constants.CollectionsProfileDirectory + "/" + collection.getCollectionId().toString() + ".yml");
        newCollection.createNewFile();

        YamlConfiguration yamlc = collection.saveToBukkitYmlFile();
        yamlc.save(newCollection);

        return collection;
    }

    public static void delete(String collectionId) throws Exception {
        ArchivesCollection collection = getSingal(collectionId);
        if (collection != null) {
            File collectionFile = new File(Constants.CollectionsProfileDirectory + "/" + collection.getCollectionId().toString() + ".yml");
            collectionFile.delete();
        }
    }

    public static ArrayList<ArchivesCollection> getAll() throws ParseException {
        ArrayList<ArchivesCollection> list = new ArrayList<>();
        File files = Constants.CollectionsProfileDirectory;
        if (Objects.requireNonNull(files.listFiles()).length == 0) return list;
        for (File file : Objects.requireNonNull(Constants.CollectionsProfileDirectory.listFiles())) {
            list.add(new ArchivesCollection(null, null).getFromBukkitYmlFile(file));
        }

        return list;
    }

    public static ArchivesCollection getSingal(String collectionId) throws ParseException {
        File collectionFile = new File(Constants.CollectionsProfileDirectory + "/" + collectionId + ".yml");
        if (!collectionFile.exists()) {
            return null;
        }

        return new ArchivesCollection(null, null).getFromBukkitYmlFile(collectionFile);
    }

    public static void addArchiveToCollection(String collectionId, String archiveId) throws Exception {
        ArchivesCollection collection = getSingal(collectionId);
        Archive archive = ArchiveManager.getArchive(archiveId);
        if (collection != null && archive != null) {
            ArrayList<Archive> archiveArrayList = collection.getArchivessIncluded();
            archiveArrayList.add(archive);
            collection.setArchivesIncluded(archiveArrayList);
            YamlConfiguration yamlc = collection.saveToBukkitYmlFile();

            // Overwrite file (delete and recreate)
            File collectionFile = new File(Constants.CollectionsProfileDirectory + "/" + collection.getCollectionId().toString() + ".yml");
            collectionFile.delete();
            collectionFile.createNewFile();
            yamlc.save(collectionFile);
        }
    }

    public static void removeFromCollection(String collectionId, String archiveId) throws Exception {
        ArchivesCollection collection = getSingal(collectionId);
        Archive archive = ArchiveManager.getArchive(archiveId);
        if (collection != null && archive != null) {
            ArrayList<Archive> archiveArrayList = collection.getArchivessIncluded();
            archiveArrayList.removeIf(c -> c.getArchiveId().equals(archive.getArchiveId()));
            collection.setArchivesIncluded(archiveArrayList);
            YamlConfiguration yamlc = collection.saveToBukkitYmlFile();

            // Overwrite file (delete and recreate)
            File collectionFile = new File(Constants.CollectionsProfileDirectory + "/" + collection.getCollectionId().toString() + ".yml");
            collectionFile.delete();
            collectionFile.createNewFile();
            yamlc.save(collectionFile);
        }
    }
}
