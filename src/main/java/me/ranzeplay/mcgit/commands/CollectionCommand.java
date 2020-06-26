package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.managers.ArchiveManager;
import me.ranzeplay.mcgit.managers.CollectionManager;
import me.ranzeplay.mcgit.managers.MessageTemplateManager;
import me.ranzeplay.mcgit.models.Archive;
import me.ranzeplay.mcgit.models.ArchivesCollection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CollectionCommand {
    public static void Do(String[] args, CommandSender sender) throws Exception {
        if (args.length > 1) {
            switch (args[1].toLowerCase()) {
                case "create":
                    create(args, sender);
                    break;
                case "addarchive":
                    addArchive(args, sender);
                    break;
                case "removearchive":
                    removeArchive(args, sender);
                    break;
                case "delete":
                    delete(args, sender);
                default:
                    break;
            }
        }
    }

    private static void create(String[] args, CommandSender sender) throws Exception {
        if (args.length > 3) {
            ArchivesCollection collection = CollectionManager.create(args[2], args[3]);
            ViewCommand.ViewCollection(sender, collection.getCollectionId().toString());
        }
    }

    private static void delete(String[] args, CommandSender sender) throws Exception {
        if (args.length > 2) {
            ArchivesCollection collection = CollectionManager.getSingal(args[2]);
            if (collection != null) {
                if (args.length > 3) {
                    if (args[3].equalsIgnoreCase("confirm")) {
                        sender.sendMessage(ChatColor.RED + "Processing...");
                        CollectionManager.delete(collection.getCollectionId().toString());
                        sender.sendMessage(ChatColor.GREEN + "The collection has been deleted!");
                    }
                } else {
                    sender.sendMessage("");
                    sender.sendMessage(MessageTemplateManager.title(10, "Request Confirm"));

                    sender.sendMessage(ChatColor.AQUA + "You are requesting to delete a collection, you need to confirm your action!");
                    ViewCommand.ViewCollection(sender, collection.getCollectionId().toString());
                    sender.sendMessage(ChatColor.AQUA + "Use \"/mcgit collection delete " + collection.getCollectionId().toString() + " confirm\" to confirm delete operation...");

                    sender.sendMessage(MessageTemplateManager.ending(15));
                    sender.sendMessage("");
                }
            }
        }
    }

    // Usage: /mcgit collection addArchive <collectionId> <archiveId>
    private static void addArchive(String[] args, CommandSender sender) throws Exception {
        if (args.length > 3) {
            ArchivesCollection collection = CollectionManager.getSingal(args[2]);
            Archive archive = ArchiveManager.getArchive(args[3]);
            if (collection != null && archive != null) {
                CollectionManager.addArchiveToCollection(collection.getCollectionId().toString(), archive.getArchiveId().toString());
            }
        }
    }

    // Usage: /mcgit collection removeArchive <collectionId> <archiveId>
    private static void removeArchive(String[] args, CommandSender sender) throws Exception {
        if (args.length > 3) {
            ArchivesCollection collection = CollectionManager.getSingal(args[2]);
            Archive archive = ArchiveManager.getArchive(args[3]);
            if (collection != null && archive != null) {
                CollectionManager.removeFromCollection(collection.getCollectionId().toString(), archive.getArchiveId().toString());
            }
        }
    }
}
