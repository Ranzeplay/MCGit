package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;
import me.ranzeplay.mcgit.managers.GitManager;
import me.ranzeplay.mcgit.models.Commit;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.ParseException;

public class RollbackCommand {


    public static void Do(String[] args, CommandSender sender) throws ParseException, InterruptedException {
        if (args.length > 1) {
            if (args.length > 2 && args[2].equalsIgnoreCase("confirm")) {
                Process(sender, args[1]);
                return;
            }
            RequestConfirm(sender, args[1]);
        }
    }

    private static void RequestConfirm(CommandSender sender, String commitId) throws ParseException {
        sender.sendMessage(ChatColor.AQUA + "You are requesting to rollback the server, you need to confirm your action!");
        ViewCommand.ViewCommit(sender, commitId);
        sender.sendMessage(ChatColor.AQUA + "Use \"/mcgit rollback " + commitId + " confirm\" to confirm rollback operation...");
    }

    private static void Process(CommandSender sender, String commitId) throws ParseException, InterruptedException {
        File commitFile = new File(Constants.CommitsDirectory + "\\" + commitId + ".yml");
        if (!commitFile.exists()) {
            sender.sendMessage(ChatColor.AQUA + "Commit Not Found");
            return;
        }

        for (Player targetPlayer : Main.Instance.getServer().getOnlinePlayers()) {
            targetPlayer.sendMessage("");
            targetPlayer.sendMessage("-----[MCGit : Rollback Operation Summary]-----");

            TextComponent text = new TextComponent();
            text.setText(ChatColor.YELLOW + "Rollback point: " + ChatColor.GREEN + commitId);
            text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mcgit view commit " + commitId));
            targetPlayer.spigot().sendMessage(text);

            targetPlayer.sendMessage(ChatColor.YELLOW + "Triggered by: " + ChatColor.GREEN + sender.getName());
            targetPlayer.sendMessage("");
        }

        int timer = 10;
        for (int t = 10; t > 0; t--) {
            for (Player targetPlayer : Main.Instance.getServer().getOnlinePlayers()) {
                targetPlayer.sendMessage(ChatColor.RED + "Rollback operation will start in " + t + " second(s)");
            }

            Thread.sleep(1000);
        }

        for (Player player : Main.Instance.getServer().getOnlinePlayers()) {
            player.kickPlayer("Rollback operation in progress");
        }

        Commit commit = GitManager.getCommit(commitId);

        // Unload all chunks
    }
}