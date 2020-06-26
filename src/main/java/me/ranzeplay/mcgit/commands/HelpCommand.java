package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.managers.MessageTemplateManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand {
    public static void Root(CommandSender sender) {
        sender.sendMessage("");
        // sender.sendMessage(ChatColor.AQUA + "---------[MCGit : Command Helper]---------");
        sender.sendMessage(MessageTemplateManager.title(9, "Command Helper"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit archive <description> [worldName]" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Archive a new backup");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit view <archives|archive>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "View all archives or a specific archive");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit rollback <archiveId>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Request a rollback operation");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit gui" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Open a GUI contains all archives");
        sender.sendMessage(MessageTemplateManager.ending(15));
        sender.sendMessage("");
    }

    public static void Archive(CommandSender sender) {
        sender.sendMessage("");
        // sender.sendMessage(ChatColor.AQUA + "---------[MCGit : Command Helper > Archive]---------");
        sender.sendMessage(MessageTemplateManager.title(13, "Command Helper > Archive"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit archive <description> [worldName]" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Make a new archive");
        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }

    public static void View(CommandSender sender) {
        sender.sendMessage("");
        // sender.sendMessage("---------[MCGit : Command Helper > View]---------");
        sender.sendMessage(MessageTemplateManager.title(13, "Command Helper > View"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit view archives" + ChatColor.WHITE + " - " + ChatColor.GREEN + "View all archives");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit view archive <archiveId>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "View a specific archive");
        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }

    public static void Rollback(CommandSender sender) {
        sender.sendMessage("");
        // sender.sendMessage("---------[MCGit : Command Helper]---------");
        sender.sendMessage(MessageTemplateManager.title(13, "Command Helper"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit rollback <archiveId>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Rollback to a specific archive");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit rollback abort" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Abort the rollback operation");
        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }

    public static void Delete(CommandSender sender) {
        sender.sendMessage("");
        // sender.sendMessage("---------[MCGit : Command Helper]---------");
        sender.sendMessage(MessageTemplateManager.title(13, "Command Helper"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit delete <archiveId>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Delete a specific archive");
        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }
}
