package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class QuoteUDPServer {

	public static void main(String[] args) {
		// רשימת הציטוטים שלך
		String[] quotes = { "hey user", "hey roy", "hey shalev", "game over", "start game" };
		int port = 8080;

		try {

			DatagramSocket socket = new DatagramSocket(port);
			System.out.println("Server is running on port 8080...");

			byte[] receiveBuffer = new byte[1024];
			byte[] sendBuffer;

			while (true) {
				// waiting for client message and reading the message
				DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
				socket.receive(receivePacket);

				String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
				System.out.println("Request received : " + clientMessage);

				// saving client's crucial details to send beck to him (port, address)
				InetAddress clientAddress = receivePacket.getAddress();
				int clientPort = receivePacket.getPort();

				// building three cases for each potentially behavior of the app
				try {
					// 1. user sends exit
					if (clientMessage.trim().equalsIgnoreCase("exit")) {
						break;
					}

					// 2. user send GET
					String response;

					if (clientMessage.trim().equalsIgnoreCase("GET")) {
						Random random = new Random();
						int randomIndex = random.nextInt(quotes.length);
						response = quotes[randomIndex];
					} else {
						// 3. user sends something else = exceptionז
						throw new Exception("Unknown command. Please send 'GET' or 'exit'.");
					}

					// send quote to client
					sendBuffer = response.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress,
							clientPort);
					socket.send(sendPacket);

				} catch (Exception e) {
					// sending to client the exception
					String errorMessage = "Error: " + e.getMessage();
					sendBuffer = errorMessage.getBytes();
					DatagramPacket errorPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress,
							clientPort);
					socket.send(errorPacket);
				}
			}
			socket.close();
			System.out.println("Server shutting down. ");

		} catch (Exception e) {
			// תופס שגיאות כלליות של השרת (למשל אם הפורט תפוס)
			e.printStackTrace();
		}
	}
}