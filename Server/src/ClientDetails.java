import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientDetails implements Serializable {
    private final String username;
    private final String password;

    private Map<String, Channel> subscribedChannels;
    private Map<String, Integer> lastReceivedMessage;

    public ClientDetails(String username, String password){
        this.username = username;
        this.password = password;
        subscribedChannels = new HashMap<>();
        lastReceivedMessage = new HashMap<>();
    }

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public List<Channel> getSubscribedChannels() {return new ArrayList<>(subscribedChannels.values());}
    public Map<String, Integer> getLastReceivedMessage() {return lastReceivedMessage;}

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
    // TODO: 21/12/2021 Add method to pull only the new messages from each subscribedChannel
}
