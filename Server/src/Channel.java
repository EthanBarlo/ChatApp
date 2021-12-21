import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Channel implements Serializable {
    private List<Message> messages;
    private List<ClientDetails> SubscribedClients;
    private final String name;

    public Channel(String name){
        this.name = name;
        messages = new ArrayList<>();
        SubscribedClients = new ArrayList<>();
        messages.add(new Message("SYSTEM", System.currentTimeMillis() / 1000L, "Created new channel with the name: " + name));
    }

    public String getName() {return name;}
    public List<Message> getMessages() {return messages;}

    public int getMessagesLength() {return messages.size();}
    public List<Message> getMessagesAfter(int index){return messages.subList(index, messages.size());}

    public void Subscribe(ClientDetails client) {SubscribedClients.add(client);}
    public void Unsubscribe(ClientDetails client) {SubscribedClients.remove(client);}
}
