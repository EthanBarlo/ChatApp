import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientDetails implements Serializable {
    private final String username;
    private final String password;

    private Map<String, Channel> subscribedChannels;
    private Map<String, Message> newMessages;

    public ClientDetails(String username, String password){
        this.username = username;
        this.password = password;
        subscribedChannels = new HashMap<>();
        newMessages = new HashMap<>();
    }

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public List<Channel> getSubscribedChannels() {return new ArrayList<>(subscribedChannels.values());}
    public Map<String, Message> getNewMessages() {return newMessages;}

    public void Subscribe(String channelName, Server server){
        Channel channel = server.getChannel(channelName);
        if(channel == null)
            return;
        subscribedChannels.put(channelName, channel);
        channel.Subscribe(this);
    }

    public void Unsubscribe(String channelName, Server server){
        Channel channel = subscribedChannels.get(channelName);
        if(channel == null)
            return;
        subscribedChannels.remove(channelName);
        channel.Unsubscribe(this);
    }
    // TODO: 21/12/2021 Add method to collect new messages, the channel will loop through and add it to each client.
    // TODO: This would mean that the user does not need to be logged in to keep track of messages;
}
