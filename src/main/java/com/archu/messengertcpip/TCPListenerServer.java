package main.java.com.archu.messengertcpip;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

import static main.java.com.archu.messengertcpip.Main.runMenu;

public class TCPListenerServer implements Runnable {

    private final Socket client;
    private boolean terminated = false;

    public TCPListenerServer(Socket client) {
        this.client = client;
    }

    public void start() {
        System.out.println("Uruchomnienie nasłuchiwania na klienta w nowym wątku");
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
                    System.out.println("Utracono połączenie z klientem.\n Wybierz ponownie swoją rolę.");
                    ClientList.getList().remove(client);
                    runMenu();
                }
            }
        } catch (Exception e) {
            System.err.print(e.getLocalizedMessage());
            ClientList.getList().remove(client);
        }
    }
}
