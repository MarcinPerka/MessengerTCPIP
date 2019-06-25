package main.java.com.archu.messengertcpip;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class TCPClient {

    private Socket client;
    private boolean terminated;

    private void connectToServer(InetAddress address, int port) throws Exception {
        System.out.println("Próba połączenia do serwera");
        client = new Socket(address, port);

        if (client.isConnected()) {
            System.out.println("Uruchomienie nasłuchiwania na serwer");
            TCPListenerClient listener = new TCPListenerClient(client);
            listener.start();

            Scanner scanner = new Scanner(System.in);

            try (OutputStream stream = client.getOutputStream()) {
                DataOutputStream output = new DataOutputStream(stream);

                while (!terminated) {
                    String data = scanner.nextLine();
                    output.writeUTF("Klient: " + data);
                    output.flush();
                }
                client.close();
            }
        }
    }

    public TCPClient(InetAddress address, int port) {
        try {
            connectToServer(address, port);
        } catch (IOException e) {
            try {
                if (client == null) {
                    System.out.println("Próba ponownego połączenia do serwera");
                    Thread.sleep(20000);
                    connectToServer(address, port);
                }
            } catch (Exception se) {
                System.err.println(e.getLocalizedMessage());
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
