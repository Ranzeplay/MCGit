package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;
import me.ranzeplay.mcgit.managers.ArchiveManager;
import me.ranzeplay.mcgit.managers.BackupManager;
import me.ranzeplay.mcgit.managers.CollectionManager;
import me.ranzeplay.mcgit.managers.MessageTemplateManager;
import me.ranzeplay.mcgit.managers.zip.ZipManager;
import me.ranzeplay.mcgit.models.Archive;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ArchiveCommand {
    public static void Do(String[] args, CommandSender sender) throws Exception {
        if (args.length > 2) {
            switch (args[1].toLowerCase()) {
                case "create":
                    create(args, sender);
                    return;
                case "view":
                    view(args, sender);
                    return;
                case "delete":
                    delete(args, sender);
                    return;
                case "rollback":
                    rollback(args, sender);
                    return;
                default:
                    break;
            }
        }

        HelpCommand.Archive(sender);
    }

    private static void create(String[] args, CommandSender sender) throws IOException {
        Player execPlayer = (Player) sender;
        World targetWorld;
        switch (args.length) {
            case 3:
                targetWorld = execPlayer.getWorld();
                break;
            case 4:
                targetWorld = Main.Instance.getServer().getWorld(args[3].replaceAll("_nether", "").replaceAll("_the_end", ""));
                break;
            default:
                HelpCommand.Archive(sender);
                return;
        }

        long operationStartTime = System.nanoTime();

        Archive archive = ArchiveManager.makeArhive(args[2], execPlayer, targetWorld);
        if (targetWorld == null) {
            sender.sendMessage(ChatColor.RED + "Base world (overworld, DIM0) not found");
            return;
        }

        // Turn off Auto-Save to prevent the exception like "file is busy"
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-off");

        // Run Save-All command on server console to save all worlds
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
        Bukkit.savePlayers();

        CompletableFuture.runAsync(() -> {
            if (Main.Instance.getConfig().getBoolean("compressNetherWorldByDefault")) {
                World netherWorld = Bukkit.getWorld(targetWorld.getName() + "_nether");
                try {
                    ZipManager.zipWorld(Objects.requireNonNull(netherWorld).getName(), archive.getArchiveId().toString().replace("-", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (Main.Instance.getConfig().getBoolean("compressTheEndWorldByDefault")) {
                World theEndWorld = Bukkit.getWorld(targetWorld.getName() + "_the_end");
                try {
                    ZipManager.zipWorld(Objects.requireNonNull(theEndWorld).getName(), archive.getArchiveId().toString().replace("-", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                ZipManager.zipWorld(targetWorld.getName(), archive.getArchiveId().toString().replace("-", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).whenComplete((Void t, Throwable u) -> {

            long operationCompleteTime = System.nanoTime();

            sender.sendMessage("");
            sender.sendMessage(MessageTemplateManager.title(10, "Archive Created"));

            sender.sendMessage(ChatColor.GREEN + "Archive " + ChatColor.YELLOW + archive.getArchiveId().toString() + ChatColor.GREEN + " created successfully!");
            sender.sendMessage(ChatColor.GREEN + "Description: " + archive.getDescription());
            sender.sendMessage(ChatColor.GREEN + "Size: " + ChatColor.YELLOW + String.format("%.4f", ArchiveManager.GetArchiveTotalSize(archive.getArchiveId().toString()) / 1024 / 1024) + "MB");
            sender.sendMessage(ChatColor.GREEN + "Time elapsed: " + ChatColor.YELLOW + String.format("%.4f", (double) (operationCompleteTime - operationStartTime) / 1000 / 1000 / 1000) + " seconds");

            sender.sendMessage(MessageTemplateManager.ending(15));
            sender.sendMessage("");
        });

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-on");
    }

    private static void view(String[] args, CommandSender sender) throws ParseException {
        if (args.length > 2) {
            if (args[2].equalsIgnoreCase("ALL")) {
                ArrayList<Archive> existingArchives = ArchiveManager.getAllArchives();
                // existingArchives = reverseArrayList(existingArchives);

                sender.sendMessage("");
                sender.sendMessage(MessageTemplateManager.title(10, "Existing Archives"));
                for (Archive archive : existingArchives) {
                    TextComponent detailsMessage = new TextComponent();
                    detailsMessage.setText(ChatColor.GREEN + archive.getDescription() + " " + ChatColor.YELLOW + Constants.DateFormat.format(archive.getCreateTime()));
                    detailsMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mcgit archive view" + archive.getArchiveId().toString()));
                    sender.spigot().sendMessage(detailsMessage);
                }
                if (existingArchives.size() == 0) {
                    sender.sendMessage(ChatColor.AQUA + "Nothing to show");
                }
            } else {
                File archiveFile = new File(Constants.ArchivesProfileDirectory + "/" + args[2] + ".yml");
                if (!archiveFile.exists()) {
                    sender.sendMessage(ChatColor.AQUA + "Archive Not Found");
                    return;
                }

                Archive archive = new Archive(null, null, null).getFromBukkitYmlFile(archiveFile);

                sender.sendMessage("");
                sender.sendMessage(MessageTemplateManager.title(12, "Archive Details"));
                sender.sendMessage(ChatColor.YELLOW + "Archive Id: " + ChatColor.GREEN + archive.getArchiveId());
                sender.sendMessage(ChatColor.YELLOW + "Description: " + ChatColor.GREEN + archive.getDescription());
                sender.sendMessage(ChatColor.YELLOW + "Create time: " + ChatColor.GREEN + archive.getCreateTime());
                sender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.GREEN + archive.getWorld().getName());
                sender.sendMessage(ChatColor.YELLOW + "Operator: " + ChatColor.GREEN + archive.getPlayer().getName() + " (" + archive.getPlayer().getUniqueId() + ")");
                sender.sendMessage(ChatColor.YELLOW + "Size: " + String.format("%.4f", ArchiveManager.GetArchiveTotalSize(archive.getArchiveId().toString()) / 1024 / 1024) + "MB");

                TextComponent actionsMessage = new TextComponent();
                actionsMessage.setText(ChatColor.RED + "[Rollback]");
                actionsMessage.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mcgit rollback " + archive.getArchiveId().toString()));
                sender.spigot().sendMessage(actionsMessage);

            }

            sender.sendMessage(MessageTemplateManager.ending(17));
            sender.sendMessage("");
        }
    }

    private static void delete(String[] args, CommandSender sender) {
        if (args.length > 2) {
            String archiveId = args[2];
            if (args.length == 3) {
                sender.sendMessage("");
                sender.sendMessage(MessageTemplateManager.title(10, "Request Confirm"));

                sender.sendMessage(ChatColor.AQUA + "You are requesting to delete a archive and its file, you need to confirm your action!");
                ((Player) sender).performCommand("mcgit archive view" + archiveId);
                sender.sendMessage(ChatColor.AQUA + "Use \"/mcgit delete " + archiveId + " confirm\" to confirm delete operation...");

                sender.sendMessage(MessageTemplateManager.ending(15));
                sender.sendMessage("");
            } else if (args.length == 4 && args[3].equalsIgnoreCase("confirm")) {
                sender.sendMessage(ChatColor.RED + "Operation started...");

                long operationStartTime = System.nanoTime();
                CompletableFuture.runAsync(() -> {
                    // Remove the archive which is going to delete in collection
                    try {
                        CollectionManager.getAll().forEach(collection -> {
                            collection.getArchivessIncluded().removeIf(archive -> archive.getArchiveId().toString().equals(archiveId));
                            try {
                                collection.saveToBukkitYmlFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ZipManager.deleteDirectory(Constants.ArchivesDirectory + "/" + archiveId.replace("-", ""));
                    new File(Constants.ArchivesProfileDirectory + "/" + archiveId + ".yml").delete();
                }).whenComplete((Void t, Throwable u) -> {
                    long operationFinishTime = System.nanoTime();
                    sender.sendMessage(ChatColor.AQUA + "Operation completed in " + String.format("%.4f", (double) (operationFinishTime - operationStartTime) / 1000 / 1000 / 1000) + " second(s)");
                });
            }
        }
    }

    private static void rollback(String[] args, CommandSender sender) throws ParseException {
        if (args.length > 2) {
            String archiveId = args[2];
            if (args.length == 4 && args[3].equalsIgnoreCase("confirm")) {
                // Schedule a rollback
                File archiveFile = new File(Constants.ArchivesProfileDirectory + "/" + archiveId + ".yml");
                if (!archiveFile.exists()) {
                    sender.sendMessage(ChatColor.AQUA + "Archive Not Found");
                    return;
                }

                Archive archive = ArchiveManager.getArchive(archiveId);
                if (archive == null) {
                    sender.sendMessage(ChatColor.RED + "Cannot read Archive file normally, it might be damaged");
                    sender.sendMessage(ChatColor.RED + "Operation failed...");
                    return;
                }

                for (Player targetPlayer : Main.Instance.getServer().getOnlinePlayers()) {
                    targetPlayer.sendMessage("");
                    // targetPlayer.sendMessage("-----[MCGit : Rollback Operation Summary]-----");
                    targetPlayer.sendMessage(MessageTemplateManager.title(7, "Rollback Operation Summary"));

                    TextComponent text = new TextComponent();
                    text.setText(ChatColor.YELLOW + "Target archiveId: " + ChatColor.GREEN + archiveId);
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mcgit view archive " + archiveId));
                    targetPlayer.spigot().sendMessage(text);

                    targetPlayer.sendMessage(ChatColor.YELLOW + "Triggered by: " + ChatColor.GREEN + sender.getName());
                    targetPlayer.sendMessage(ChatColor.AQUA + "Rollback will be start on next server startup automatically");

                    TextComponent actionsMessage = new TextComponent();
                    actionsMessage.setText(ChatColor.YELLOW + "Use " + ChatColor.GREEN + "/mcgit rollback abort" + ChatColor.YELLOW + " to abort rollback");
                    actionsMessage.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mcgit rollback abort"));
                    targetPlayer.spigot().sendMessage(actionsMessage);

                    targetPlayer.sendMessage(MessageTemplateManager.ending(15));
                    targetPlayer.sendMessage("");
                }

                BackupManager.Schedule(archiveId);
            } else {
                if (args[2].equalsIgnoreCase("abort")) {
                    // Abort backup schedule
                    if (BackupManager.Abort()) {
                        for (Player targetPlayer : Main.Instance.getServer().getOnlinePlayers()) {
                            targetPlayer.sendMessage(ChatColor.YELLOW + "Rollback has been aborted by " + ChatColor.GREEN + sender.getName());
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Nothing was scheduled");
                    }
                } else {
                    // Request the player to confirm the action
                    sender.sendMessage("");
                    sender.sendMessage(MessageTemplateManager.title(10, "Request Confirm"));

                    sender.sendMessage(net.md_5.bungee.api.ChatColor.AQUA + "You are requesting to rollback the server, you need to confirm your action!");
                    ((Player) sender).performCommand("mcgit archive view" + archiveId);
                    sender.sendMessage(net.md_5.bungee.api.ChatColor.AQUA + "Use \"/mcgit rollback " + archiveId + " confirm\" to confirm rollback operation...");

                    sender.sendMessage(MessageTemplateManager.ending(15));
                    sender.sendMessage("");
                }
            }
        }
    }
}
