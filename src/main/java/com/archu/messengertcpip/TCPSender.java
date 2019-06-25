package main.java.com.archu.messengertcpip;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPSender {

    public static void sendData(Socket client, String data) {
        if (client != null && client.isConnected()) {
            try {
                OutputStream stream = client.getOutputStream();
                DataOutputStream output = new DataOutputStream(stream);
                output.writeUTF("Server: " + data);
                output.flush();
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
    }
}
