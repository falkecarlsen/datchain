package dk.aau.cs.a311c.datchain.network;

import dk.aau.cs.a311c.datchain.Blockchain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int port = 5000;
    private ServerSocket server;
    private Blockchain chain;
    private int localChainLength;

    public Server(Blockchain chain) {
        this.chain = chain;
        this.localChainLength = this.chain.size();
    }

    public void run() throws IOException {

        //start try-with-resources routine for server
        try {
            //create new ServerSocket with this.port as argument
            server = new ServerSocket(this.port) {
                //create further method for shutting down server, when exiting program
                //TODO should be implemented into GUI shutdown routine
                protected void shutdown() {
                    try {
                        //close this ServerSocket
                        this.close();
                    } catch (IOException e) {
                        System.out.println("ERROR: Could not shut down server: " + e.getMessage());
                    }
                }
            };
        } catch (IOException e) {
            //catch exception thrown during creation of SocketServer
            System.out.println("ERROR: Could not create ServerSocket: " + e.getMessage());
        }


        //accept incoming connections to this server
        Socket client = server.accept();
        //debug sout
        System.out.println("Connected with client at IP: " + client.getInetAddress());


        //create new thread for handling incoming client
        new Thread(new ClientHandler(this, client, this.localChainLength));
    }
}
