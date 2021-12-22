import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread{
    private final int portNumber;
    private final ServerData data;
    private final Map<String, Channel> _channels;
    private final Map<String, ClientDetails> _clientData;

    private ServerSocket socket;
    private ArrayList<ClientThread> connectedClients = new ArrayList<>();


    public Server(int portNumber, ServerData data){
        this.portNumber = portNumber;
        this.data = data;
        this._channels = data.getChannelDictionary();
        this._clientData = data.getClientAccounts();
    }

    @Override
    public void run(){
        try{
            socket = new ServerSocket(portNumber);
            System.out.println("Server started -");
            System.out.println("Address: " + socket.getInetAddress());
            System.out.println("port: " + portNumber);
            while(true){
                Socket clientSocket = socket.accept();
                if(HandleClientLogin(clientSocket))
                    System.out.println("New client connected. ID - " + clientSocket.getInetAddress());
                else
                    clientSocket.close();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean HandleClientLogin(Socket clientSocket) throws IOException{
        BufferedReader _FromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter _ToClient = new PrintWriter(clientSocket.getOutputStream(), true);

        String rawInput = null;
        boolean waitingForRequest = true;
        while(waitingForRequest){
            rawInput = _FromClient.readLine();
            if(rawInput != null)
                waitingForRequest = false;
        }
        System.out.println("REQUEST - " + rawInput);

        JSONObject json = (JSONObject) JSONValue.parse(rawInput);
        if(!json.get("_class").toString().equals("OpenRequest")){
            _ToClient.println(new ErrorResponse("Invalid Request, Expecting an OpenRequest").toJSONString());
            return false;
        }

        ClientDetails clientDetails = VerifyLoginDetails(json);
        if(clientDetails == null){
            _ToClient.println(new ErrorResponse("User credentials were invalid.").toJSONString());
            return false;
        }
        clientDetails.SetServer(this);

        ClientThread clientThread = new ClientThread(this, clientSocket, clientDetails);
        connectedClients.add(clientThread);
        clientThread.start();
        _ToClient.println(new SuccessResponse().toJSONString());
        return true;
    }

    private ClientDetails VerifyLoginDetails(JSONObject json){
        OpenRequest request = OpenRequest.fromJSON(json);
        String username = request.getUsername();
        String password = request.getPassword();

        ClientDetails clientDetails = _clientData.get(username);
        if(clientDetails == null){
            clientDetails = new ClientDetails(username, password);
            _clientData.put(username, clientDetails);
        }
        if(!clientDetails.getPassword().equals(password)){
            return null;
        }
        return clientDetails;
    }


    public void RemoveClient(ClientThread client){
        connectedClients.remove(client);
    }

    // Channel Methods
    public List<String> getChannelNames(){
        return new ArrayList<String>(_channels.keySet());
    }
    public Channel getChannel(String channelName){
        return _channels.get(channelName);
    }
    public Channel createChannel(String channelName){
        Channel newChannel = new Channel(channelName);
        _channels.put(channelName, newChannel);
        return newChannel;
    }

    public void Stop(){
        data.SaveData();
        try{
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
