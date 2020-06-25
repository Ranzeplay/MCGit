package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.managers.CollectionManager;
import me.ranzeplay.mcgit.managers.CommitManager;
import me.ranzeplay.mcgit.managers.MessageTemplateManager;
import me.ranzeplay.mcgit.models.Commit;
import me.ranzeplay.mcgit.models.CommitsCollection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CollectionCommand {
    public static void Do(String[] args, CommandSender sender) throws Exception {
        if (args.length > 1) {
            switch (args[1].toLowerCase()) {
                case "create":
                    create(args, sender);
                    break;
                case "addcommit":
                    addCommit(args, sender);
                    break;
                case "removecommit":
                    removeCommit(args, sender);
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
            CommitsCollection collection = CollectionManager.create(args[2], args[3]);
            ViewCommand.ViewCollection(sender, collection.getCollectionId().toString());
        }
    }

    private static void delete(String[] args, CommandSender sender) throws Exception {
        if (args.length > 2) {
            CommitsCollection collection = CollectionManager.getSingal(args[2]);
            if (collection != null) {
                if (args.length > 3) {
                    if (args[3].equalsIgnoreCase("confirm")) {
                        sender.sendMessage(ChatColor.RED + "Processing...");
                        CollectionManager.delete(collection.getCollectionId().toString());
                        sender.sendMessage(ChatColor.GREEN + "CommitsCollection has been deleted!");
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

    // Usage: /mcgit collection addCommit <collectionId> <commitId>
    private static void addCommit(String[] args, CommandSender sender) throws Exception {
        if (args.length > 3) {
            CommitsCollection collection = CollectionManager.getSingal(args[2]);
            Commit commit = CommitManager.getCommit(args[3]);
            if (collection != null && commit != null) {
                CollectionManager.addCommitToCollection(collection.getCollectionId().toString(), commit.getCommitId().toString());
            }
        }
    }

    // Usage: /mcgit collection removeCommit <collectionId> <commitId>
    private static void removeCommit(String[] args, CommandSender sender) throws Exception {
        if (args.length > 3) {
            CommitsCollection collection = CollectionManager.getSingal(args[2]);
            Commit commit = CommitManager.getCommit(args[3]);
            if (collection != null && commit != null) {
                CollectionManager.removeFromCollection(collection.getCollectionId().toString(), commit.getCommitId().toString());
            }
        }
    }
}
