package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.managers.ArchiveManager;
import me.ranzeplay.mcgit.managers.CollectionManager;
import me.ranzeplay.mcgit.models.Archive;
import me.ranzeplay.mcgit.models.ArchivesCollection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CommandCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        ArrayList<String> availableChoices = new ArrayList<>();

        ArrayList<String> s = new ArrayList<>();
        for (String arg : args) {
            if (!arg.isEmpty()) {
                s.add(arg);
            }
        }

        if (command.isRegistered()) {
            if (commandSender instanceof Player) {
                if (command.getName().equalsIgnoreCase("mcgit") || command.getAliases().contains(label)) {
                    if (s.size() == 0) {
                        availableChoices.add("gui");
                        availableChoices.add("archive");
                        availableChoices.add("collection");
                        availableChoices.add("view");
                        availableChoices.add("rollback");
                        availableChoices.add("delete");
                    } else if (s.size() == 1) {
                        if (s.get(0).equalsIgnoreCase("view")) {
                            availableChoices.add("archives");
                            availableChoices.add("archive");
                            availableChoices.add("collections");
                            availableChoices.add("collection");
                        } else if (s.get(0).equalsIgnoreCase("rollback") || s.get(0).equalsIgnoreCase("delete")) {
                            availableChoices = getAllArchivesId();

                            if (s.get(0).equalsIgnoreCase("rollback")) {
                                availableChoices.add("abort");
                            }
                        } else if (s.get(0).equalsIgnoreCase("collection")) {
                            availableChoices.add("create");
                            availableChoices.add("delete");
                            availableChoices.add("addArchive");
                            availableChoices.add("removeArchive");
                        }
                    } else if (s.size() == 2) {
                        if (s.get(0).equalsIgnoreCase("view")) {
                            if (s.get(1).equalsIgnoreCase("archive")) {
                                availableChoices = getAllArchivesId();
                            }
                            if (s.get(1).equalsIgnoreCase("collection")) {
                                availableChoices = getAllCollectionsId();
                            }
                        } else if (s.get(0).equalsIgnoreCase("collection")) {
                            if (s.get(1).equalsIgnoreCase("addArchive") || s.get(1).equalsIgnoreCase("removeArchiveArchive") || s.get(1).equalsIgnoreCase("delete")) {
                                availableChoices = getAllCollectionsId();
                            }
                        }
                    } else if (s.size() == 3) {
                        if (s.get(0).equalsIgnoreCase("archive")) {
                            availableChoices = getAllWorldsName();
                        } else if (s.get(0).equalsIgnoreCase("collection")) {
                            if ((s.get(1).equalsIgnoreCase("addArchive"))) {
                                availableChoices = getAllArchivesId();
                            } else if ((s.get(1).equalsIgnoreCase("removeArchive"))) {
                                try {
                                    ArchivesCollection collection = CollectionManager.getSingal(s.get(2));
                                    ArrayList<String> finalAvailableChoices = availableChoices;
                                    collection.getArchivessIncluded().stream().forEach(c -> {
                                        finalAvailableChoices.add(c.getArchiveId().toString());
                                    });
                                    availableChoices = finalAvailableChoices;
                                } catch (ParseException e) {
                                    // e.printStackTrace();
                                }

                            }
                        }
                    }
                }
            }
        }

        return availableChoices;
    }

    private ArrayList<String> getAllArchivesId() {
        ArrayList<String> availableChoices = new ArrayList<>();
        try {
            ArrayList<Archive> archives = ArchiveManager.getAllArchives();
            for (Archive archive : archives) {
                availableChoices.add(archive.getArchiveId().toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return availableChoices;
    }

    private ArrayList<String> getAllCollectionsId() {
        ArrayList<String> availableChoices = new ArrayList<>();
        try {
            ArrayList<ArchivesCollection> collections = CollectionManager.getAll();
            for (ArchivesCollection collection : collections) {
                availableChoices.add(collection.getCollectionId().toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return availableChoices;
    }

    private ArrayList<String> getAllWorldsName() {
        ArrayList<String> availableChoices = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            if (!(world.getName().endsWith("_nether") || world.getName().endsWith("_the_end"))) {
                availableChoices.add(world.getName());
            }
        }

        return availableChoices;
    }
}
