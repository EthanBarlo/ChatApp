import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientDetails implements Serializable {
    private final String username;
    private final String password;
    private Server server;
    private Map<String, Channel> subscribedChannels;
    private Map<String, List<Message>> newMessages;

    public ClientDetails(String username, String password){
        this.username = username;
        this.password = password;
        subscribedChannels = new HashMap<>();
        newMessages = new HashMap<>();
    }
    public void SetServer(Server server){ this.server = server; }

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public Server getServer() {return server;}
    public List<Channel> getSubscribedChannels() {return new ArrayList<>(subscribedChannels.values());}
    public List<String> getSubscribedChannelNames(){ return new ArrayList<>(subscribedChannels.keySet()); }
    public Map<String, List<Message>> getNewMessages() {return newMessages;}


    //region Subscribe Methods
    public void ChannelMessageUpdate(String channelName, Message message){newMessages.get(channelName).add(message);}
    public String Subscribe(String channelName){
        Channel channel = server.getChannel(channelName);
        if(channel == null)
            return "Channel does not exist!";

        subscribedChannels.put(channelName, channel);
        channel.Subscribe(this);
        return null;
    }

    public String Unsubscribe(String channelName){
        Channel channel = subscribedChannels.get(channelName);
        if(channel == null)
            return "Not subscribed to that channel.";

        subscribedChannels.remove(channelName);
        channel.Unsubscribe(this);
        return null;
    }
    //endregion


    public String PublishNewMessage(String channelName, Message message){
        Channel channel = subscribedChannels.get(channelName);
        if(channel == null)
            return "User is not subscribed to this channel!";
        channel.PublishMessage(message);
        return null;
    }

    public List<Message> GetMessagesAfter(String channelName, int index){
        List<Message> messages = null;
        Channel channel = subscribedChannels.get(channelName);
        messages = channel.getMessagesAfter(index);
        return messages;
    }
}
