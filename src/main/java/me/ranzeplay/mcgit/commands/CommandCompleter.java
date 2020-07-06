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

        ArrayList<String> param = new ArrayList<>();
        for (String arg : args) {
            if (!arg.isEmpty()) {
                param.add(arg);
            }
        }

        if (command.isRegistered()) {
            if (commandSender instanceof Player) {
                if (command.getName().equalsIgnoreCase("mcgit") || command.getAliases().contains(label)) {
                    // mcgit [fill]
                    if (param.size() == 0) {
                        availableChoices.add("archive");
                        availableChoices.add("collection");
                        availableChoices.add("gui");
                    }

                    // mcgit <archive|collection> [fill]
                    if (param.size() == 1) {
                        // mcgit archive [fill]
                        if (param.get(0).equalsIgnoreCase("archive")) {
                            availableChoices.add("create");
                            availableChoices.add("view");
                            availableChoices.add("rollback");
                            availableChoices.add("delete");
                        }

                        // mcgit collection [fill]
                        if (param.get(0).equalsIgnoreCase("collection")) {
                            availableChoices.add("create");
                            availableChoices.add("view");
                            availableChoices.add("addCommit");
                            availableChoices.add("removeCommit");
                            availableChoices.add("delete");
                        }
                    }

                    // mcgit <valid> <valid> [fill]
                    if (param.size() == 2) {
                        // mcgit archive <valid> [fill]
                        if (param.get(0).equalsIgnoreCase("archive")) {
                            // mcgit archive view [fill]
                            if (param.get(1).equalsIgnoreCase("view")) {
                                availableChoices = getAllArchivesId();
                                availableChoices.add("ALL");
                            }

                            // mcgit archive rollback [fill]
                            if (param.get(1).equalsIgnoreCase("rollback")) {
                                availableChoices = getAllArchivesId();
                                availableChoices.add("ABORT");
                            }

                            // mcgit archive delete [fill]
                            if (param.get(1).equalsIgnoreCase("delete")) {
                                availableChoices = getAllArchivesId();
                            }
                        }

                        // mcgit collection <valid> [fill]
                        if (param.get(0).equalsIgnoreCase("collection")) {
                            // mcgit collection view [fill]
                            if (param.get(1).equalsIgnoreCase("view")) {
                                availableChoices = getAllCollectionsId();
                                availableChoices.add("ALL");
                            }

                            // mcgit collection addArchive [fill(collectionId + archiveId)]
                            if (param.get(1).equalsIgnoreCase("addCommit")) {
                                availableChoices = getAllArchivesId();
                            }

                            // mcgit collection removeArchive [fill(collectionId + archiveId[in collection])]
                            if (param.get(1).equalsIgnoreCase("removeCommit")) {
                                availableChoices = getAllCollectionsId();
                            }

                            // mcgit collection delete [fill]
                            if (param.get(1).equalsIgnoreCase("delete")) {
                                availableChoices = getAllCollectionsId();
                            }
                        }
                    }

                    // mcgit <valid> <valid> <valid> [fill]
                    if (param.size() == 3) {
                        // mcgit collection <valid> <valid> [fill]
                        if (param.get(0).equalsIgnoreCase("collection")) {
                            // mcgit collection addArchive <*collectionId> [fill]
                            if (param.get(1).equalsIgnoreCase("addArchive")) {
                                availableChoices = getAllArchivesId();
                            }

                            // mcgit collection removeArchive <*collectionId> [fill]
                            if (param.get(1).equalsIgnoreCase("removeArchive")) {
                                availableChoices = getAllArchivesId();
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
