import java.io.*;
import java.net.*;
import java.io.IOException;

public class Server{
    public static void main(String[] args){
        
            try{
                // Create a server and listen to port 7896
                System.out.println("Server Running");
                @SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket(7896);
                Socket clientSocket = serverSocket.accept();
                Socket clientSocket1 = serverSocket.accept();
                System.out.println("Client is connected!");
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                PrintWriter out1 = new PrintWriter(clientSocket1.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedReader in1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
                while(true)
                {
                    String s = in.readLine();
                    if (!s.isEmpty()){
                        System.out.println(s);
                        out1.println(s);
                    }
                    String s1 = in1.readLine();
                    if (!s1.isEmpty()){
                        System.out.println(s1);
                        out.println(s1);
                    }
                }
                
            } catch (IOException ex){
                System.out.println("Server failed!");
            }
    }
}