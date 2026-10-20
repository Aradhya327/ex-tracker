import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Experiment 4 - Client side of the TCP socket demo.
 *
 * Connects to the server on localhost:5000, sends one line, prints the reply,
 * then closes the connection.
 *
 * Run server first, then client. See README.md.
 */
public class client {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {

        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter output =
                     new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input =
                     new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to server " + HOST + ":" + PORT);

            output.println("Hello Server");

            String reply = input.readLine();
            System.out.println("Server says: " + reply);

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
            System.out.println("Is the server running? Start server.java first.");
        }
    }
}
