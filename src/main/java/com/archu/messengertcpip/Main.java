package main.java.com.archu.messengertcpip;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    private static final IPAddressValidator IPAV = new IPAddressValidator();

    public static void main(String[] args) {

        System.out.println("Uruchomienie programu.\n##########\n");
        runMenu();
    }

    public static void runMenu() {
        boolean choice = true;
        while (choice) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Połączyć z serwerem (rola klienta) - 1\nZacząć nasłuchiwanie na innego klienta (rola serwera) - 2");
            try {
                int roleOption = scanner.nextInt();
                switch (roleOption) {
                    case 1: {
                        choice = false;
                        runClient();
                        break;
                    }
                    case 2: {
                        choice = false;
                        runServer();
                        break;
                    }
                    default: {
                        System.out.println("Wybierz liczbę 1 albo 2");
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Wybierz liczbę");
            }
        }
    }

    public static void runClient() {
        System.out.println("Uruchamianie klienta");
        String address;
        try {

            do {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Podaj adres serwera w postaci: X.X.X.X, liczby od 0 do 255");
                address = scanner.next();
            } while (!IPAV.validate(address));
            int port = setPort();
            InetAddress addr = InetAddress.getByName(address);
            TCPClient client = new TCPClient(addr, port);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    public static void runServer() {
        System.out.println("Uruchamienie serwera");
        String address;
        try {
            do {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Podaj adres IP aby zacząć nasłuchiwaniew postaci: X.X.X.X, liczby od 0 do 255"); // w cmd netstat -a porty i adres "ESTABLISHED"
                address = scanner.next();
            } while (!IPAV.validate(address));
            int port = setPort();
            TCPServer server = new TCPServer(port, address);
            server.Start();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
        while (true) {
            Scanner scanner = new Scanner(System.in);
            ClientList list = ClientList.getList();
            try {
                Socket client = list.get(0);

                if (client != null && client.isConnected()) {
                    String data = scanner.nextLine();
                    TCPSender.sendData(client, data);
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    public static int setPort() {
        int port = -1;
        do {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Podaj numer portu: ");
                if (scanner.hasNextInt()) {
                    port = scanner.nextInt();
                }
            } catch (Exception ex) {
                System.out.println("Wpisz liczbę.");
            }
        } while (port <= 0 || port > 65535);
        return port;
    }
}
