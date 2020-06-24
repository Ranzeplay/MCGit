package me.ranzeplay.mcgit.models;

import me.ranzeplay.mcgit.managers.CommitManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class CommitsCollection {
    private UUID collectionId;
    private String name;
    private String description;
    private ArrayList<Commit> commitsIncluded;

    public CommitsCollection(String name, String description) {
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

    public ArrayList<Commit> getCommitsIncluded() {
        return commitsIncluded;
    }

    public void setCommitsIncluded(ArrayList<Commit> commitsIncluded) {
        this.commitsIncluded = commitsIncluded;
    }

    public YamlConfiguration saveToBukkitYmlFile() {
        YamlConfiguration yamlc = new YamlConfiguration();

        yamlc.set("id", this.getCollectionId().toString());
        yamlc.set("name", this.getName());
        yamlc.set("description", this.getDescription());

        // Only save commit id, to reduce size and flexible to use
        ArrayList<String> arr = new ArrayList<>();
        for (Commit commit : this.commitsIncluded) {
            arr.add(commit.getCommitId().toString());
        }
        yamlc.set("commits", arr.toArray());

        return yamlc;
    }

    public CommitsCollection getFromBukkitYmlFile(File ymlFile) throws ParseException {
        YamlConfiguration filec = YamlConfiguration.loadConfiguration(ymlFile);
        this.collectionId = UUID.fromString(Objects.requireNonNull(filec.getString("id")));
        this.name = filec.getString("name");
        this.description = filec.getString("description");

        // Find each commit from the list
        this.commitsIncluded = new ArrayList<>();
        for (String s : filec.getStringList("commits")) {
            this.commitsIncluded.add(CommitManager.getCommit(s));
        }

        return this;
    }
}
