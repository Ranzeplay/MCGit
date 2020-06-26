package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.gui.ArchivesPanel;
import me.ranzeplay.mcgit.managers.MessageTemplateManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.ParseException;

public class CommandExec implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.isRegistered()) {
            if (commandSender instanceof Player) {
                if (command.getName().equalsIgnoreCase("mcgit") || command.getAliases().contains(s)) {
                    if (args.length > 0) {
                        switch (args[0].toLowerCase()) {
                            case "archive":
                                try {
                                    ArchiveCommand.Do(args, commandSender);
                                } catch (Exception e) {
                                    commandSender.sendMessage(MessageTemplateManager.commandExecutedWithErrors());
                                    e.printStackTrace();
                                }
                                break;
                            case "collection":
                                try {
                                    CollectionCommand.Do(args, commandSender);
                                } catch (Exception e) {
                                    commandSender.sendMessage(MessageTemplateManager.commandExecutedWithErrors());
                                    e.printStackTrace();
                                }
                            case "view":
                                try {
                                    ViewCommand.Do(args, commandSender);
                                } catch (ParseException e) {
                                    commandSender.sendMessage(MessageTemplateManager.commandExecutedWithErrors());
                                    e.printStackTrace();
                                }
                                break;
                            case "rollback":
                                try {
                                    RollbackCommand.Do(args, commandSender);
                                } catch (Exception e) {
                                    commandSender.sendMessage(MessageTemplateManager.commandExecutedWithErrors());
                                    e.printStackTrace();
                                }
                                break;
                            case "delete":
                                try {
                                    DeleteCommand.Do(args, commandSender);
                                } catch (ParseException e) {
                                    commandSender.sendMessage(MessageTemplateManager.commandExecutedWithErrors());
                                    e.printStackTrace();
                                }
                                break;
                            case "gui":
                                ((Player) commandSender).openInventory(new ArchivesPanel().getInventory());
                                break;
                        }
                    } else {
                        HelpCommand.Root(commandSender);
                    }

                    return true;
                }
            }

            commandSender.sendMessage(ChatColor.RED + "The command can only be executed by a Player");

            return true;
        }

        return false;
    }
}
