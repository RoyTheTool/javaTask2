package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class QuoteUDPClient {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		DatagramSocket socket = null;
		int port = 8080;

		try {
			// creating client
			socket = new DatagramSocket();
			InetAddress serverAddress = InetAddress.getByName("localhost");

			while (true) {
//		get user's text
				System.out.print("Enter 'GET' for quote or 'exit' to quit: ");
				String sc = scanner.nextLine();

//				creating packet for user text and sending to server
				byte[] sendBuffer = sc.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, port);
				socket.send(sendPacket);

//				in case that user text is exit
				if (sc.trim().equalsIgnoreCase("exit")) {
					System.out.println("client finished");
					break;
				}

//				waiting for server to give us response
				byte[] receiveBuffer = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

				socket.receive(receivePacket);

				// converting resonsponse into string
				String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());

				// handling 2 cases of response quote or error
				if (serverResponse.startsWith("Error")) {
					System.out.println(serverResponse + "\n");
				} else {
					System.out.println("Quote received: " + serverResponse + "\n");
				}
			}

			socket.close();
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}