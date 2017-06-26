package website.christine.nukkitcord;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

/**
 * Created by Xena on 6/20/2017.
 */
public class EventListener implements Listener {
    private final MainClass mc;
    private DiscordAPI dis;
    private final String botChannel;

    public EventListener(MainClass main, String botToken, String bc, final String gameName) {
        this.mc = main;
        this.botChannel = bc;

        this.dis = Javacord.getApi(botToken, true);
        dis.setGame("Minecraft");

        this.dis.connect(new FutureCallback<DiscordAPI>() {
            public void onSuccess(DiscordAPI api) {
                api.setGame(gameName);
                mc.getLogger().info("discord login successful");
                // register listener
                api.registerListener(new MessageCreateListener() {
                    public void onMessageCreate(DiscordAPI api, Message message) {
                        // check the content of the message
                        if(message.getAuthor().getId().equalsIgnoreCase(dis.getYourself().getId())) {
                            return;
                        }

                        if(message.getChannelReceiver().getId().equalsIgnoreCase(botChannel)) {
                            String fmtMessage = String.format("[DIS] <%s> %s", message.getAuthor().getNickname(message.getChannelReceiver().getServer()), message.getContent());
                            mc.getServer().broadcastMessage(fmtMessage);
                        }
                    }
                });
            }

            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        this.dis.getChannelById(this.botChannel).sendMessage(String.format("**<%s>** %s", player.getName(), message));
    }
}
