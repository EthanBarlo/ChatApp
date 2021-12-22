import java.util.Scanner;

public class ServerController {
    public static void main(String[] args) {
        int portNumber = 12345;
        ServerData serverData = new ServerData();
        Server server = new Server(portNumber, serverData);
        server.start();

        Scanner _Input = new Scanner(System.in);
        while (true) {
            String command = _Input.nextLine();
            switch (command) {
                case "stop" -> {
                    server.Stop();
                    System.exit(0);
                }
            }

        }

    }
}
