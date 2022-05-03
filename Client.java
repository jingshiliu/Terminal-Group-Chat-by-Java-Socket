import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String username;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket socket;

    public Client(String proxy, int port, String name){
        username = name;
        try {
            socket = new Socket(proxy, port);
            System.out.println("Connection established");

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(){
        while( socket.isConnected()){
            try {
                bufferedWriter.write(username + ": " + new Scanner(System.in).nextLine());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void listenMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try {
                        System.out.println(bufferedReader.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        System.out.print("Enter your username: ");
        String username = new Scanner(System.in).nextLine();
        Client client = new Client("localhost", 1234, username);
        client.listenMessage();
        client.sendMessage();
    }

    
}
