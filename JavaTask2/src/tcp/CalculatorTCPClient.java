package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CalculatorTCPClient {

	public static void main(String[] args) {
		String hostname = "localhost";
		int port = 9090;

		try (Socket socket = new Socket(hostname, port)) {
			System.out.println("Connected to calculator server!");

			// Create tools to communicate with the server
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Send to server
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Read from server

			Scanner scanner = new Scanner(System.in);
			String userInput;

			// user creating expressions
			while (true) {

				// get and send expression to server
				System.out.println("Enter expression (num op num) or 'close' to exit: ");
				userInput = scanner.nextLine();
				out.println(userInput);

				// user deciding to close app
				if ("close".equalsIgnoreCase(userInput.trim())) {
					System.out.println("client closed");
					break;
				}

//				print outcome
				String response = in.readLine();
				if (response != null) {
					System.out.println(userInput + " = " + response + "\n");
				}

			}

			scanner.close();

		} catch (IOException e) {
			System.out.println("I/O Error: " + e.getMessage());
		}
	}
}