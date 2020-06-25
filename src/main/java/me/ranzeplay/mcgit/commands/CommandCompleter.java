package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.managers.CollectionManager;
import me.ranzeplay.mcgit.managers.CommitManager;
import me.ranzeplay.mcgit.models.Commit;
import me.ranzeplay.mcgit.models.CommitsCollection;
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
                        availableChoices.add("commit");
                        availableChoices.add("collection");
                        availableChoices.add("view");
                        availableChoices.add("rollback");
                        availableChoices.add("delete");
                    } else if (s.size() == 1) {
                        if (s.get(0).equalsIgnoreCase("view")) {
                            availableChoices.add("commits");
                            availableChoices.add("commit");
                            availableChoices.add("collections");
                            availableChoices.add("collection");
                        } else if (s.get(0).equalsIgnoreCase("rollback") || s.get(0).equalsIgnoreCase("delete")) {
                            availableChoices = getAllCommitsId();

                            if (s.get(0).equalsIgnoreCase("rollback")) {
                                availableChoices.add("abort");
                            }
                        } else if (s.get(0).equalsIgnoreCase("collection")) {
                            availableChoices.add("create");
                            availableChoices.add("delete");
                            availableChoices.add("addCommit");
                            availableChoices.add("removeCommit");
                        }
                    } else if (s.size() == 2) {
                        if (s.get(0).equalsIgnoreCase("view")) {
                            if (s.get(1).equalsIgnoreCase("commit")) {
                                availableChoices = getAllCommitsId();
                            }
                            if (s.get(1).equalsIgnoreCase("collection")) {
                                availableChoices = getAllCollectionsId();
                            }
                        }
                    } else if (s.size() == 3) {
                        if (s.get(0).equalsIgnoreCase("commit")) {
                            availableChoices = getAllWorldsName();
                        } else if (s.get(0).equalsIgnoreCase("collection")) {
                            if (s.get(1).equalsIgnoreCase("addCommit") || s.get(1).equalsIgnoreCase("removeCommit")) {
                                availableChoices = getAllCommitsId();
                            }
                        }
                    }
                }
            }
        }

        return availableChoices;
    }

    private ArrayList<String> getAllCommitsId() {
        ArrayList<String> availableChoices = new ArrayList<>();
        try {
            ArrayList<Commit> commits = CommitManager.getAllCommits();
            for (Commit commit : commits) {
                availableChoices.add(commit.getCommitId().toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return availableChoices;
    }

    private ArrayList<String> getAllCollectionsId() {
        ArrayList<String> availableChoices = new ArrayList<>();
        try {
            ArrayList<CommitsCollection> collections = CollectionManager.getAll();
            for (CommitsCollection collection : collections) {
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
