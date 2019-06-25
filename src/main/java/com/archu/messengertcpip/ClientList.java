
package main.java.com.archu.messengertcpip;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ClientList {

    private final List<Socket> clientList = new ArrayList<>();

    private static final ClientList instance = new ClientList();

    private ClientList() {
    }

    public static synchronized ClientList getList() {
        return instance;
    }

    public synchronized void add(Socket client) {
        System.out.println("Dodanie nowego klienta" + client.getLocalSocketAddress().toString());
        clientList.add(client);
    }

    public synchronized void remove(Socket client) {
        System.out.println("Usuięcie klienta" + client.getLocalSocketAddress().toString());
        clientList.remove(client);
    }

    public synchronized int size() {
        return clientList.size();
    }

    public Socket get(int index) {
        if (index < clientList.size()) {
            return clientList.get(index);
        }

        return null;
    }
}
