package main.java.com.archu.messengertcpip;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

import static main.java.com.archu.messengertcpip.Main.runMenu;

public class TCPListenerClient implements Runnable {

    private final Socket client;
    private boolean terminated = false;

    public TCPListenerClient(Socket client) {
        this.client = client;
    }

    public void start() {
        System.out.println("Uruchomnienie nasłuchiwania na serwer w nowym wątku");
        Thread thread = new Thread(this);
        thread.start();
    }

    public void terminate() {
        System.out.println("Wywołanie zakończenia nasłuchowania na klienta w nowym wątku");
        terminated = true;
    }

    @Override
    public void run() {
        try {
            if (client != null && client.isConnected()) {
                try (InputStream stream = client.getInputStream()) {
                    DataInputStream dataStream = new DataInputStream(stream);
                    while (!terminated) {
                        String line = dataStream.readUTF();
                        System.out.println(line);
                    }
                } catch (SocketException ex) {
                    System.err.print(ex.getLocalizedMessage());
                    System.out.println("Utracono połączenie z serwerem.\n Wybierz ponownie swoją rolę.");
                    runMenu();
                }
            }
        } catch (Exception e) {
            System.err.print(e.getLocalizedMessage());
        }
    }
}
