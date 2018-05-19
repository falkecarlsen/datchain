package dk.aau.cs.a311c.datchain.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable {

    private Server server;
    private Socket client;
    private int localChainLength;

    public ClientHandler(Server server, Socket client, int localChainLength) {
        this.server = server;
        this.client = client;
        this.localChainLength = localChainLength;
    }

    @Override
    public void run() {

        try {
            //create reader and streams for client
            BufferedReader inClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            DataOutputStream toClient = new DataOutputStream(client.getOutputStream());

            //write response to output stream

            toClient.writeBytes("ServerChainLength: " + this.localChainLength + "\n");

            toClient.close();

            //if client response is "Equal chains", no transmissions are necessary
            if (inClient.readLine().equals("Equal chains")) System.out.println("Equal chains gotten from client");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

