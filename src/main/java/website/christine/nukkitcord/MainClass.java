package website.christine.nukkitcord;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by Xena on 6/20/2017.
 */
public class MainClass extends PluginBase {
    @Override
    public void onLoad() {
        this.getLogger().info(TextFormat.WHITE + "Nukkitcord loaded");
    }

    @Override
    public void onEnable() {
        //Config reading and writing
        Config config = new Config(
                new File(this.getDataFolder(), "config.yml"),
                Config.YAML,
                //Default values (not necessary)
                new LinkedHashMap<String, Object>() {
                    {
                        put("discord-bot-key", "<discord bot api key>");
                        put("discord-relay-channel", "<channel id>"); //you can also put other standard objects!
                    }
                });
        this.getLogger().info(String.valueOf(config.get("discord-relay-channel")));
        //Don't forget to save it!
        config.save();

        this.getServer().getPluginManager().registerEvents(new EventListener(
                this,
                config.getString("discord-bot-key"),
                config.getString("discord-relay-channel")),
                this);
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.DARK_RED + "I've been disabled!");
    }
}

