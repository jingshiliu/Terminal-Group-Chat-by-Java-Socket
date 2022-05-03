import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private int port;
    private BufferedReader bufferedReader;

    public Server(int port){
        this.port = port;
    }


    public void startServer(){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server ready to connect");

            while(! serverSocket.isClosed()){
                socket = serverSocket.accept();
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String username = bufferedReader.readLine();
                ClientHandler clientHandler = new ClientHandler(socket, username);

                System.out.println("A client is connected");
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Server server = new Server(1234);
        server.startServer();
    }
}
