import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Experiment 4 - Server side of the TCP socket demo.
 *
 * Opens port 5000, waits for one client, prints whatever the client sends,
 * replies with a greeting, then closes the connection.
 *
 * Run server first, then client. See README.md.
 */
public class server {

    private static final int PORT = 5000;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server started on port " + PORT);
            System.out.println("Waiting for the client...");

            try (Socket socket = serverSocket.accept();
                 BufferedReader input =
                         new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter output =
                         new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println("Client connected: " + socket.getRemoteSocketAddress());

                String message = input.readLine();
                System.out.println("Client says: " + message);

                output.println("Hello client");

                System.out.println("Connection closed");
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
