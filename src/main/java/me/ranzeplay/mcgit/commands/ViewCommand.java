package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.managers.ArchiveManager;
import me.ranzeplay.mcgit.managers.CollectionManager;
import me.ranzeplay.mcgit.managers.MessageTemplateManager;
import me.ranzeplay.mcgit.models.Archive;
import me.ranzeplay.mcgit.models.ArchivesCollection;
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
                case "archives":
                    ViewArchives(sender);
                    break;
                case "archive":
                    if (args.length > 2) {
                        ViewArchive(sender, args[2]);
                    } else {
                        sender.sendMessage("Usage: /mcgit view archive <archiveId>");
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
                    sender.sendMessage("Usage: /mcgit view <archive|archives>");
            }
        } else {
            HelpCommand.View(sender);
        }
    }

    public static void ViewArchive(CommandSender sender, String archiveId) throws ParseException {
        File archiveFile = new File(Constants.ArchivesProfileDirectory + "/" + archiveId + ".yml");
        if (!archiveFile.exists()) {
            sender.sendMessage(ChatColor.AQUA + "Archive Not Found");
            return;
        }

        Archive archive = new Archive(null, null, null).getFromBukkitYmlFile(archiveFile);

        sender.sendMessage("");
        // sender.sendMessage("----------[MCGit : Archive Details]----------");
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

        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }

    private static void ViewArchives(CommandSender sender) throws ParseException {
        ArrayList<Archive> existingArchives = ArchiveManager.getAllArchives();
        existingArchives = reverseArrayList(existingArchives);

        sender.sendMessage("");
        // sender.sendMessage("---------[MCGit : Existing archives]---------");
        sender.sendMessage(MessageTemplateManager.title(10, "Existing Archives"));
        for (Archive archive : existingArchives) {
            TextComponent detailsMessage = new TextComponent();
            detailsMessage.setText(ChatColor.GREEN + archive.getDescription() + " " + ChatColor.YELLOW + Constants.DateFormat.format(archive.getCreateTime()));
            detailsMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mcgit view archive " + archive.getArchiveId().toString()));
            sender.spigot().sendMessage(detailsMessage);
        }
        if (existingArchives.size() == 0) {
            sender.sendMessage(ChatColor.AQUA + "Nothing to show");
        }

        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }

    public static void ViewCollection(CommandSender sender, String collectionId) throws ParseException {
        File collectionFile = new File(Constants.CollectionsProfileDirectory + "/" + collectionId + ".yml");
        if (!collectionFile.exists()) {
            sender.sendMessage(ChatColor.AQUA + "Collection Not Found");
            return;
        }

        ArchivesCollection collection = new ArchivesCollection(null, null).getFromBukkitYmlFile(collectionFile);

        sender.sendMessage(MessageTemplateManager.title(11, "ArchivesCollection Details"));
        sender.sendMessage(ChatColor.YELLOW + "ArchivesCollection Id: " + ChatColor.GREEN + collection.getCollectionId());
        sender.sendMessage(ChatColor.YELLOW + "Name: " + ChatColor.GREEN + collection.getName());
        sender.sendMessage(ChatColor.YELLOW + "Description: " + ChatColor.GREEN + collection.getDescription());
        sender.sendMessage(ChatColor.YELLOW + "Archives in collection: " + ChatColor.GREEN + collection.getArchivessIncluded().size());
        sender.sendMessage(MessageTemplateManager.ending(23));
    }

    private static void ViewCollections(CommandSender sender) throws ParseException {
        ArrayList<ArchivesCollection> existingCollections = CollectionManager.getAll();
        existingCollections = reverseArrayList(existingCollections);

        sender.sendMessage("");
        // sender.sendMessage("---------[MCGit : Existing archives]---------");
        sender.sendMessage(MessageTemplateManager.title(11, "Existing Collections"));
        for (ArchivesCollection collection : existingCollections) {
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
