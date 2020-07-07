package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.managers.MessageTemplateManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand {
    public static void Root(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(MessageTemplateManager.title(9, "Command Helper"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit archive ..." + ChatColor.WHITE + " - " + ChatColor.GREEN + "Archive command");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit collection ..." + ChatColor.WHITE + " - " + ChatColor.GREEN + "Collection command");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit gui" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Open a GUI contains all archives");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit help" + ChatColor.WHITE + " - " + ChatColor.GREEN + "View command helper");
        sender.sendMessage(MessageTemplateManager.ending(15));
        sender.sendMessage("");
    }

    public static void Archive(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(MessageTemplateManager.title(13, "Command Helper > Archive"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit archive create <description> [worldName]" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Make a new archive");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit archive view <[archive id]|ALL>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "View archive details");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit archive rollback <archive id>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Rollback server game save");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit archive delete <archive id>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Delete an archive");
        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }

    public static void Collection(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(MessageTemplateManager.title(13, "Command Helper > Collection"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit archive create <name> <description>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Create a new collection");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit archive addCommit <collection id> <archive id>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Add a archive into a collection");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit archive removeCommit <collection id> <archive id>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Remove a archive from a collection");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit archive delete <archive id>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Delete a collection");
        sender.sendMessage(MessageTemplateManager.ending(18));
        sender.sendMessage("");
    }
}
