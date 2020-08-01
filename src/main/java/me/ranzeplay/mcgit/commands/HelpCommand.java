package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.managers.MessageTemplateManager;
import org.bukkit.command.CommandSender;

public class HelpCommand {
    public static void Root(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(MessageTemplateManager.title(9, "Command Helper"));
        sender.sendMessage(MessageTemplateManager.commandHelper("/mcgit archive ...", "Archive command"));
        sender.sendMessage(MessageTemplateManager.commandHelper("/mcgit collection ...", "Collection command"));
        sender.sendMessage(MessageTemplateManager.commandHelper("/mcgit gui", "Open a GUI contains all archives"));
        sender.sendMessage(MessageTemplateManager.commandHelper("/mcgit help", "View command helper"));
        sender.sendMessage(MessageTemplateManager.ending(15));
        sender.sendMessage("");
    }

    public static void Archive(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(MessageTemplateManager.title(13, "Command Helper > Archive"));
        sender.sendMessage(MessageTemplateManager.commandHelper("/mcgit archive create <description> [worldName]", "Make a new archive"));
        sender.sendMessage(MessageTemplateManager.commandHelper("/mcgit archive view <[archive id]|ALL>", "View archive details"));
        sender.sendMessage(MessageTemplateManager.commandHelper("/mcgit archive rollback <archive id>", "Rollback server game save"));
        sender.sendMessage(MessageTemplateManager.commandHelper("/mcgit archive delete <archive id>", "Delete an archive"));
        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }

    public static void Collection(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(MessageTemplateManager.title(13, "Command Helper > Collection"));
        sender.sendMessage(MessageTemplateManager.commandHelper("/mcgit collecton create <name> <description>", "Create a new collection"));
        sender.sendMessage(MessageTemplateManager.commandHelper("/mcgit collecton addCommit <collection id> <archive id>", "Add a archive into a collection"));
        sender.sendMessage(MessageTemplateManager.commandHelper("/mcgit collecton removeCommit <collection id> <archive id>", "Remove a archive from a collection"));
        sender.sendMessage(MessageTemplateManager.commandHelper("/mcgit collecton delete <archive id>", "Delete a collection"));
        sender.sendMessage(MessageTemplateManager.ending(18));
        sender.sendMessage("");
    }
}
