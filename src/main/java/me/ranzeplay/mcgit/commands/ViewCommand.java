package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.managers.CollectionManager;
import me.ranzeplay.mcgit.managers.CommitManager;
import me.ranzeplay.mcgit.managers.MessageTemplateManager;
import me.ranzeplay.mcgit.models.Commit;
import me.ranzeplay.mcgit.models.CommitsCollection;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;


public class ViewCommand {
    public static void Do(String[] args, CommandSender sender) throws ParseException {
        if (args.length > 1) {
            switch (args[1].toLowerCase()) {
                case "tree":
                    break;
                case "commits":
                    ViewCommits(sender);
                    break;
                case "commit":
                    if (args.length > 2) {
                        ViewCommit(sender, args[2]);
                    } else {
                        sender.sendMessage("Usage: /mcgit view commit <commitId>");
                    }
                    break;
                case "collections":
                    ViewCollections(sender);
                    break;
                case "collection":
                    if (args.length > 2) {
                        ViewCollection(sender, args[2]);
                    } else {
                        sender.sendMessage("Usage: /mcgit view collection <collectionId>");
                    }
                    break;
                default:
                    sender.sendMessage("Usage: /mcgit view <commit|commits>");
            }
        } else {
            HelpCommand.View(sender);
        }
    }

    public static void ViewCommit(CommandSender sender, String commitId) throws ParseException {
        File commitFile = new File(Constants.CommitsDirectory + "/" + commitId + ".yml");
        if (!commitFile.exists()) {
            sender.sendMessage(ChatColor.AQUA + "Commit Not Found");
            return;
        }

        Commit commit = new Commit(null, null, null).getFromBukkitYmlFile(commitFile);

        sender.sendMessage("");
        // sender.sendMessage("----------[MCGit : Commit Details]----------");
        sender.sendMessage(MessageTemplateManager.title(12, "Commit Details"));
        sender.sendMessage(ChatColor.YELLOW + "Commit Id: " + ChatColor.GREEN + commit.getCommitId());
        sender.sendMessage(ChatColor.YELLOW + "Description: " + ChatColor.GREEN + commit.getDescription());
        sender.sendMessage(ChatColor.YELLOW + "Commit Time: " + ChatColor.GREEN + commit.getCreateTime());
        sender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.GREEN + commit.getWorld().getName());
        sender.sendMessage(ChatColor.YELLOW + "Commit Player: " + ChatColor.GREEN + commit.getPlayer().getName() + " (" + commit.getPlayer().getUniqueId() + ")");
        sender.sendMessage(ChatColor.YELLOW + "Size: " + String.format("%.4f", CommitManager.GetCommitTotalSize(commit.getCommitId().toString()) / 1024 / 1024) + "MB");

        TextComponent actionsMessage = new TextComponent();
        actionsMessage.setText(ChatColor.RED + "[Rollback]");
        actionsMessage.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mcgit rollback " + commit.getCommitId().toString()));
        sender.spigot().sendMessage(actionsMessage);

        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }

    private static void ViewCommits(CommandSender sender) throws ParseException {
        ArrayList<Commit> existingCommits = CommitManager.getAllCommits();
        existingCommits = reverseArrayList(existingCommits);

        sender.sendMessage("");
        // sender.sendMessage("---------[MCGit : Existing Commits]---------");
        sender.sendMessage(MessageTemplateManager.title(10, "Existing Commits"));
        for (Commit commit : existingCommits) {
            TextComponent detailsMessage = new TextComponent();
            detailsMessage.setText(ChatColor.GREEN + commit.getDescription() + " " + ChatColor.YELLOW + Constants.DateFormat.format(commit.getCreateTime()));
            detailsMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mcgit view commit " + commit.getCommitId().toString()));
            sender.spigot().sendMessage(detailsMessage);
        }
        if (existingCommits.size() == 0) {
            sender.sendMessage(ChatColor.AQUA + "Nothing to show");
        }

        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }

    public static void ViewCollection(CommandSender sender, String collectionId) throws ParseException {
        File collectionFile = new File(Constants.CollectionsDirectory + "/" + collectionId + ".yml");
        if (!collectionFile.exists()) {
            sender.sendMessage(ChatColor.AQUA + "CommitsCollection Not Found");
            return;
        }

        CommitsCollection collection = new CommitsCollection(null, null).getFromBukkitYmlFile(collectionFile);

        sender.sendMessage(MessageTemplateManager.title(11, "CommitsCollection Details"));
        sender.sendMessage(ChatColor.YELLOW + "CommitsCollection Id: " + ChatColor.GREEN + collection.getCollectionId());
        sender.sendMessage(ChatColor.YELLOW + "Name: " + ChatColor.GREEN + collection.getName());
        sender.sendMessage(ChatColor.YELLOW + "Description: " + ChatColor.GREEN + collection.getDescription());
        sender.sendMessage(ChatColor.YELLOW + "Commits in collection: " + ChatColor.GREEN + collection.getCommitsIncluded().size());
        sender.sendMessage(MessageTemplateManager.ending(23));
    }

    private static void ViewCollections(CommandSender sender) throws ParseException {
        ArrayList<CommitsCollection> existingCollections = CollectionManager.getAll();
        existingCollections = reverseArrayList(existingCollections);

        sender.sendMessage("");
        // sender.sendMessage("---------[MCGit : Existing Commits]---------");
        sender.sendMessage(MessageTemplateManager.title(11, "Existing Collections"));
        for (CommitsCollection collection : existingCollections) {
            TextComponent detailsMessage = new TextComponent();
            detailsMessage.setText(ChatColor.GREEN + collection.getName() + " " + ChatColor.YELLOW + collection.getCollectionId().toString());
            detailsMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mcgit view collection " + collection.getCollectionId().toString()));
            sender.spigot().sendMessage(detailsMessage);
        }
        if (existingCollections.size() == 0) {
            sender.sendMessage(ChatColor.AQUA + "Nothing to show");
        }

        sender.sendMessage(MessageTemplateManager.ending(23));
        sender.sendMessage("");
    }

    private static <T> ArrayList<T> reverseArrayList(ArrayList<T> alist) {
        ArrayList<T> revArrayList = new ArrayList<>();
        for (int i = alist.size() - 1; i >= 0; i--) {
            revArrayList.add(alist.get(i));
        }

        return revArrayList;
    }
}
