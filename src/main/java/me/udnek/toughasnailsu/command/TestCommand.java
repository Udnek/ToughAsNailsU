package me.udnek.toughasnailsu.command;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestCommand implements @Nullable CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        /*commandSender.sendMessage(String.valueOf(Tags.TANU_TEST.isTagged(Items.DRINKING_GLASS_BOTTLE)));
        commandSender.sendMessage(String.valueOf(Tags.TANU_TEST));
        commandSender.sendMessage(String.valueOf(Tags.TANU_TEST));
        commandSender.sendMessage(String.valueOf(Tags.TANU_TEST));*/
        return true;
    }
}
