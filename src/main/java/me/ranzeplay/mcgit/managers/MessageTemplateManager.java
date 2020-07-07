package me.ranzeplay.mcgit.managers;

import org.bukkit.ChatColor;

import java.util.Arrays;

public class MessageTemplateManager {
    public static String title(int dividerLength, String subTitle) {
        char[] divider = new char[dividerLength];
        Arrays.fill(divider, '=');

        return ChatColor.BOLD + new String(divider) +
                "[ " + ChatColor.RESET + ChatColor.GREEN + "MCGit" + ChatColor.YELLOW + " : " + ChatColor.AQUA + subTitle + ChatColor.RESET + ChatColor.BOLD + " ]" +
                new String(divider);
    }

    public static String ending(int dividerLength) {
        char[] divider = new char[dividerLength];
        Arrays.fill(divider, '=');

        return ChatColor.BOLD + new String(divider) + " END " + new String(divider);
    }

    public static String commandHelper(String commandContent, String commandDescription) {
        return ChatColor.YELLOW + commandContent + ChatColor.WHITE + " - " + ChatColor.GREEN + commandDescription;
    }

    public static String commandExecutedWithErrors() {
        return ChatColor.RED + "Command executed with error(s)";
    }
}
