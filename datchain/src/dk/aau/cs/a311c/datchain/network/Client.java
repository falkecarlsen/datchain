package dk.aau.cs.a311c.datchain.network;

import dk.aau.cs.a311c.datchain.Blockchain;

import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    private int port = 5000;
    private Blockchain chain;
    private int localChainLength;

    public Client(Blockchain chain) {
        this.chain = chain;
        this.localChainLength = this.chain.size();
    }

    public void run() throws IOException {
        //connect to server
        Socket client = new Socket("127.0.0.1", this.port);
        System.out.println("Client successfully connected to server" + client.getInetAddress());

        //setup reader and streams from server
        BufferedReader inServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
        DataOutputStream toServer = new DataOutputStream(client.getOutputStream());

        //receive chainLength from server and get any int
        String response = inServer.readLine();

        if (response == null) {
            System.out.println("ERROR: response from server is null!");
        }

        System.out.println("Response is: " + response);
        //replace all non-digit with empty
        int remoteChainLength = Integer.parseInt(response.replaceAll("[\\D]", ""));

        System.out.println("Remote chain has a length of: " + remoteChainLength);

    }

}
