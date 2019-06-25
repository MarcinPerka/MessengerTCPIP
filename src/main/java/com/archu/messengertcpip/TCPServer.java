package main.java.com.archu.messengertcpip;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable {

    private ServerSocket server;
    private boolean isTerminated = false;

    public TCPServer(int port, String address) {
        try {
            System.out.println(String.format("Utworzenie serwera na porcie: %d", port));
            InetAddress addr = InetAddress.getByName(address);
            this.server = new ServerSocket(port, 50, addr);
            System.out.println("Adres serwera:" + address);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    public void Start() {
        try {
            System.out.println("Uruchomienie nasłuchiwania na klientów w nowym wątku");
            Thread thread = new Thread(this);
            thread.start();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    public void Terminate() {
        isTerminated = true;
    }

    @Override
    public void run() {
        System.out.println("Uruchomiono nowy wątek serwera");
        try {
            System.out.println("Pobranie listy klientów");
            ClientList clientList = ClientList.getList();

            while (!isTerminated) {
                if (server != null) {
                    System.out.println("Oczekiwanie na klienta");
                    Socket client = server.accept();
                    System.out.println("Klient został połączony");
                    clientList.add(client);
                    System.out.println("Uruchomienie komunikacji");
                    TCPListenerServer listener = new TCPListenerServer(client);
                    listener.start();
                }
            }
            server.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
