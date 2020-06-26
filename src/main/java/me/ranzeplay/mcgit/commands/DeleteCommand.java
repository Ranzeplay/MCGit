package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;
import me.ranzeplay.mcgit.managers.CollectionManager;
import me.ranzeplay.mcgit.managers.MessageTemplateManager;
import me.ranzeplay.mcgit.managers.zip.ZipManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.CompletableFuture;

public class DeleteCommand {
    public static void Do(String[] args, CommandSender sender) throws ParseException {
        if (args.length > 1) {
            if (args.length > 2 && args[2].equalsIgnoreCase("confirm")) {
                Process(sender, args[1]);
                return;
            }
            RequestConfirm(sender, args[1]);
        } else {
            HelpCommand.Delete(sender);
        }
    }

    private static void RequestConfirm(CommandSender sender, String commitId) throws ParseException {
        sender.sendMessage("");
        sender.sendMessage(MessageTemplateManager.title(10, "Request Confirm"));

        sender.sendMessage(ChatColor.AQUA + "You are requesting to delete a commit and its file, you need to confirm your action!");
        ViewCommand.ViewCommit(sender, commitId);
        sender.sendMessage(ChatColor.AQUA + "Use \"/mcgit delete " + commitId + " confirm\" to confirm delete operation...");

        sender.sendMessage(MessageTemplateManager.ending(15));
        sender.sendMessage("");
    }

    private static void Process(CommandSender sender, String commitId) {
        sender.sendMessage(ChatColor.RED + "Process started...");

        long operationStartTime = System.nanoTime();
        CompletableFuture.runAsync(() -> {
            // Remove the commit which is going to delete in collection
            try {
                CollectionManager.getAll().forEach(collection -> {
                    collection.getCommitsIncluded().removeIf(commit -> commit.getCommitId().toString().equals(commitId));
                    try {
                        collection.saveToBukkitYmlFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ZipManager.deleteDirectory(Main.Instance.getDataFolder().getAbsolutePath() + "/Backups/" + commitId.replace("-", ""));
            new File(Constants.ConfigDirectory + "/Commits/" + commitId + ".yml").delete();
        }).whenComplete((Void t, Throwable u) -> {
            long operationFinishTime = System.nanoTime();
            sender.sendMessage(ChatColor.AQUA + "Operation completed in " + String.format("%.4f", (double) (operationFinishTime - operationStartTime) / 1000 / 1000 / 1000) + " second(s)");
        });
    }
}
