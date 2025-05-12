package client;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 9090;
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("🟢 Servidor de chat en ejecución en el puerto " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                clients.add(handler);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }

    static class ClientHandler extends Thread {
        private final Socket socket;
        private PrintWriter out;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                out = new PrintWriter(socket.getOutputStream(), true);
                username = in.readLine();
                broadcast("\u001B[32m🔵 " + username + " se ha unido al chat.\u001B[0m");

                String message;
                while ((message = in.readLine()) != null) {
                    broadcast(formatMessage(message));
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectado: " + username);
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
                clients.remove(this);
                broadcast("\u001B[31m🔴 " + username + " ha salido del chat.\u001B[0m");
            }
        }

        private void broadcast(String message) {
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    client.out.println(message);
                }
            }
        }

        private String formatMessage(String msg) {
            String color = "\u001B[36m"; // cian para usuario
            msg = msg.replace(":)", "\uD83D\uDE0A")
                    .replace(":(", "\uD83D\uDE1E")
                    .replace("<3", "\u2764\uFE0F");
            return color + "[" + username + "]: " + msg + "\u001B[0m";
        }
    }
}