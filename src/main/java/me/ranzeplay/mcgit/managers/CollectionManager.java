package me.ranzeplay.mcgit.managers;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.models.Commit;
import me.ranzeplay.mcgit.models.CommitsCollection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class CollectionManager {
    public static CommitsCollection create(String collectionName, String collectionDescription) throws IOException {
        CommitsCollection collection = new CommitsCollection(collectionName, collectionDescription);

        File newCollection = new File(Constants.CollectionsDirectory + "/" + collection.getCollectionId().toString() + ".yml");
        newCollection.createNewFile();

        YamlConfiguration yamlc = collection.saveToBukkitYmlFile();
        yamlc.save(newCollection);

        return collection;
    }

    public static void delete(String collectionId) throws Exception {
        CommitsCollection collection = getSingal(collectionId);
        if (collection != null) {
            File collectionFile = new File(Constants.CollectionsDirectory + "/" + collection.getCollectionId().toString() + ".yml");
            collectionFile.delete();
        }
    }

    public static ArrayList<CommitsCollection> getAll() throws ParseException {
        ArrayList<CommitsCollection> list = new ArrayList<>();
        File files = Constants.CollectionsDirectory;
        if (Objects.requireNonNull(files.listFiles()).length == 0) return list;
        for (File file : Objects.requireNonNull(Constants.CollectionsDirectory.listFiles())) {
            list.add(new CommitsCollection(null, null).getFromBukkitYmlFile(file));
        }

        return list;
    }

    public static CommitsCollection getSingal(String collectionId) throws ParseException {
        File collectionFile = new File(Constants.CollectionsDirectory + "/" + collectionId + ".yml");
        if (!collectionFile.exists()) {
            return null;
        }

        return new CommitsCollection(null, null).getFromBukkitYmlFile(collectionFile);
    }

    public static void addCommitToCollection(String collectionId, String commitId) throws Exception {
        CommitsCollection collection = getSingal(collectionId);
        Commit commit = CommitManager.getCommit(commitId);
        if (collection != null && commit != null) {
            ArrayList<Commit> commitArrayList = collection.getCommitsIncluded();
            commitArrayList.add(commit);
            collection.setCommitsIncluded(commitArrayList);
            YamlConfiguration yamlc = collection.saveToBukkitYmlFile();

            // Overwrite file (delete and recreate)
            File collectionFile = new File(Constants.CollectionsDirectory + "/" + collection.getCollectionId().toString() + ".yml");
            collectionFile.delete();
            collectionFile.createNewFile();
            yamlc.save(collectionFile);
        }
    }

    public static void removeFromCollection(String collectionId, String commitId) throws Exception {
        CommitsCollection collection = getSingal(collectionId);
        Commit commit = CommitManager.getCommit(commitId);
        if (collection != null && commit != null) {
            ArrayList<Commit> commitArrayList = collection.getCommitsIncluded();
            commitArrayList.remove(collection.getCommitsIncluded().indexOf(commit));
            collection.setCommitsIncluded(commitArrayList);
            YamlConfiguration yamlc = collection.saveToBukkitYmlFile();

            // Overwrite file (delete and recreate)
            File collectionFile = new File(Constants.CollectionsDirectory + "/" + collection.getCollectionId().toString() + ".yml");
            collectionFile.delete();
            collectionFile.createNewFile();
            yamlc.save(collectionFile);
        }
    }
}
