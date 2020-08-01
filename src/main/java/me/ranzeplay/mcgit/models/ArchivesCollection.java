package me.ranzeplay.mcgit.models;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.managers.ArchiveManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ArchivesCollection {
    private UUID collectionId;
    private String name;
    private String description;
    private ArrayList<Archive> commitsIncluded;

    public ArchivesCollection(String name, String description) {
        this.collectionId = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.commitsIncluded = new ArrayList<>();
    }

    public UUID getCollectionId() {
        return collectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Archive> getArchivessIncluded() {
        return commitsIncluded;
    }

    public void setArchivesIncluded(ArrayList<Archive> commitsIncluded) {
        this.commitsIncluded = commitsIncluded;
    }

    public YamlConfiguration saveToBukkitYmlFile() throws IOException {
        YamlConfiguration yamlc = new YamlConfiguration();

        yamlc.set("id", this.getCollectionId().toString());
        yamlc.set("name", this.getName());
        yamlc.set("description", this.getDescription());

        // Only save commit id, to reduce size and flexible to use
        ArrayList<String> arr = new ArrayList<>();
        for (Archive archive : this.commitsIncluded) {
            arr.add(archive.getArchiveId().toString());
        }
        yamlc.set("commits", arr.toArray());

        File collectionFile = new File(Constants.CollectionsProfileDirectory + "/" + this.getCollectionId().toString() + ".yml");
        collectionFile.delete();
        collectionFile.createNewFile();
        yamlc.save(collectionFile);

        return yamlc;
    }

    public ArchivesCollection getFromBukkitYmlFile(File ymlFile) throws ParseException {
        YamlConfiguration filec = YamlConfiguration.loadConfiguration(ymlFile);
        this.collectionId = UUID.fromString(Objects.requireNonNull(filec.getString("id")));
        this.name = filec.getString("name");
        this.description = filec.getString("description");

        // Find each commit from the list
        this.commitsIncluded = new ArrayList<>();
        for (String s : filec.getStringList("commits")) {
            this.commitsIncluded.add(ArchiveManager.getArchive(s));
        }

        return this;
    }
}
