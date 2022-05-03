import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private final String username;

    public ClientHandler(Socket socket, String username){
        this.socket = socket;
        this.username = username;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientHandlers.add(this);
        broadCastMessage(username + " join the chat!");
    }

    public void run(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg;
                while( socket.isConnected()){
                    try {
                        msg = bufferedReader.readLine();
                        broadCastMessage(msg);
                    } catch (IOException e) {
                        disconnect();
                    }
                }
            }
        }).start();
    }

    public void broadCastMessage(String msg){
        for(var clientHandler: clientHandlers){
            if(! clientHandler.username.equals(this.username)){
                try {
                    clientHandler.bufferedWriter.write(msg);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                } catch (IOException e) {
                    disconnect();
                }
            }
        }
    }

    private void disconnect(){
        try {
            socket.close();
            bufferedWriter.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
