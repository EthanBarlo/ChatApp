import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ServerData implements Serializable{
    private HashMap<String, ClientDetails> clientAccounts;
    private HashMap<String, Channel> channelDictionary = new HashMap<>() {{
        put("General", new Channel("General"));
    }};

    public Map<String, Channel> getChannelDictionary() {
        return channelDictionary;
    }
    public Map<String, ClientDetails> getClientAccounts() {
        return clientAccounts;
    }

    public ServerData(){
        LoadData();
    }

    public void SaveData() {
        try{
            File file = new File("clientData.dat");
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(clientAccounts);
            outputStream.close();
            fileOutputStream.close();

            file = new File("channelData.dat");
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(channelDictionary);
            outputStream.close();
            fileOutputStream.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }


    public void LoadData(){
        this.clientAccounts = LoadClientData();
        this.channelDictionary = LoadChannelData();
    }

    private HashMap<String, ClientDetails> LoadClientData(){
        try{
            FileInputStream fileInputStream = new FileInputStream("clientData.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (HashMap<String, ClientDetails>) objectInputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
            return new HashMap<>(){{
                put("Test", new ClientDetails("Test", "Test"));
            }};
        }
    }

    private HashMap<String, Channel> LoadChannelData(){
        try{
            FileInputStream fileInputStream = new FileInputStream("channelData.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (HashMap<String, Channel>) objectInputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
            return new HashMap<>(){{
                put("Global", new Channel("Global"));
            }};
        }
    }
}
