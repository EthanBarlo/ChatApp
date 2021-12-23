import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Dictionary;
import java.util.Hashtable;

public class ClientThread extends Thread{
    private final Socket socket;
    private final Server server;

    private PrintWriter _ToClient;
    private BufferedReader _FromClient;

    private final ClientDetails details;

    public ClientThread(Server server, Socket clientSocket, ClientDetails details){
        this.server = server;
        this.socket = clientSocket;
        this.details = details;
        try{
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            _ToClient = new PrintWriter(outputStream, true);
            _FromClient = new BufferedReader(new InputStreamReader(inputStream));
        }catch(IOException ie){
            System.out.println(ie.getMessage());
            ie.printStackTrace();
        }
    }

    @Override
    public void run(){
        try{
            HandleClientRequest();
        }catch (SocketException e){
            System.out.println(details.getUsername() + ": Disconnected from the server.");
            server.RemoveClient(this);
            interrupt();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void HandleClientRequest() throws IOException, InterruptedIOException{
        String jsonRequest;
        while((jsonRequest = _FromClient.readLine()) != null){
            System.out.println(details.getUsername() + ": REQUEST - " + jsonRequest);

            JSONObject json = (JSONObject) JSONValue.parse(jsonRequest);
            Request request = null;
            switch (json.get("_class").toString()){
                case "GetRequest" -> { request = GetRequest.fromJSON(json); }
                case "OpenRequest" -> { request = OpenRequest.fromJSON(json); }
                case "PublishRequest" -> { request = PublishRequest.fromJSON(json); }
                case "SubscribeRequest" -> {request = SubscribeRequest.fromJSON(json); }
                case "UnsubscribeRequest" -> {request = UnsubscribeRequest.fromJSON(json);}
                case "GetChannelsRequest" -> { request = GetChannelsRequest.fromJSON(json); }
                case "CreateChannelRequest" -> { request = CreateChannelRequest.fromJSON(json); }
            }

            Response response;
            if(request == null)
                response = new ErrorResponse("Invalid Request!");
            else
                response = request.DoRequest(details);

            System.out.println(details.getUsername() + ": RESPONSE - " +response.toJSONString());
            _ToClient.println(response.toJSONString());
        }
    }

}
