package me.ranzeplay.mcgit.models;

import me.ranzeplay.mcgit.Constants;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Archive {
    private UUID archiveId;
    private String description;
    private Date createTime;
    private UUID playerUUID;
    private String worldName;

    public Archive(String description, Player player, World world) {
        if (player == null) {
            this.playerUUID = UUID.randomUUID();
        } else {
            this.playerUUID = player.getUniqueId();
        }

        if (world == null) {
            worldName = "";
        } else {
            this.worldName = world.getName();
        }

        this.archiveId = UUID.randomUUID();
        this.description = description;
        this.createTime = new Date();
    }

    public UUID getArchiveId() {
        return archiveId;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Player getPlayer() {
        return Bukkit.getOfflinePlayer(this.playerUUID).getPlayer();
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public String getWorldName() {
        return this.worldName;
    }


    public YamlConfiguration saveToBukkitYmlFile() {
        YamlConfiguration yamlc = new YamlConfiguration();

        yamlc.set("id", this.getArchiveId().toString());
        yamlc.set("description", this.getDescription());
        yamlc.set("time", Constants.DateFormat.format(new Date()));
        yamlc.set("player", this.getPlayer().getUniqueId().toString());
        yamlc.set("world", this.getWorld().getName());

        return yamlc;
    }

    public Archive getFromBukkitYmlFile(File ymlFile) throws ParseException {
        YamlConfiguration filec = YamlConfiguration.loadConfiguration(ymlFile);
        this.archiveId = UUID.fromString(Objects.requireNonNull(filec.getString("id")));
        this.description = filec.getString("description");
        this.createTime = Constants.DateFormat.parse(filec.getString("time"));
        this.playerUUID = UUID.fromString(Objects.requireNonNull(filec.getString("player")));
        this.worldName = filec.getString("world");

        return this;
    }
}
