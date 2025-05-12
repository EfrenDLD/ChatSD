package server.handler;

import java.io.*;
import java.net.Socket;
import java.util.Set;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private Set<ClientHandler> clients;

    public ClientHandler(Socket socket, Set<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            username = in.readLine();
            broadcast("[Servidor]: " + username + " se ha unido.");

            String msg;
            while ((msg = in.readLine()) != null) {
                broadcast(username + ": " + msg);
            }
        } catch (IOException e) {
            System.err.println("Usuario desconectado: " + username);
        } finally {
            try {
                clients.remove(this);
                broadcast("[Servidor]: " + username + " ha salido.");
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private void broadcast(String msg) {
        synchronized (clients) {
            for (ClientHandler c : clients) {
                c.out.println(msg);
            }
        }
    }
}
