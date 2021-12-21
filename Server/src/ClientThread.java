import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.Socket;
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
            Thread.sleep(100);
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void HandleClientRequest() throws IOException, InterruptedIOException{
        String jsonRequest;
        while((jsonRequest = _FromClient.readLine()) != null){
            JSONObject json = (JSONObject) JSONValue.parse(jsonRequest);
            Request request;
            // TODO: 20/12/2021 Switch to set the request type

            Response response;
        }
    }

}
