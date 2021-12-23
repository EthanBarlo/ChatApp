Folder Structure
- Client: Contains code for the client side of the messaging app
  - src/main/resources/com/example/client: Contains .fxml files
    - Styles: Contains the css stylesheets
    - Images: contains images used for .fxml files
  - src/main/java/com/example/client: Contains all the .java files
- Librarys: Contains the librarys required for the project
- Server: Contains code for the server side of the messaging app
- clientData: Contains files that are downloaded by the client, images or attachments
- serverData: Contains files that are downloaded by the server, images or attachments


Extension
- Persistance: The server saves the data when closed properly using the 'stop' command, the data is loaded when the server starts.
- Themes: The client has two theme options which the user can choose from.

Compile the Server
- javac -d out/production/ChatApp -cp Librarys/json-simple-1.1.16.jar Server/src/*.java 

Run the server
- java -cp "out/production/ChatApp;Librarys/json-simple-1.1.16.jar" ServerController

