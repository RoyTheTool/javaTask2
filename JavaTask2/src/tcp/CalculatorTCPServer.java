package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorTCPServer {

	public static void main(String[] args) {
		int port = 9090;

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Server is listening on port " + port);

			Socket socket = serverSocket.accept();
			System.out.println("Client connected!");

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null) {

				if ("exit".equalsIgnoreCase(inputLine.trim())) {
					System.out.println("Client disconnected.");
					break;
				}

				System.out.println("Received: " + inputLine);

				try {
					String[] parts = inputLine.trim().split(" ");

					if (parts.length != 3) {
						throw new Exception("Error: Invalid expression");
					}

					double num1;
					double num2;
					String operator = parts[1];

					try {
						num1 = Double.parseDouble(parts[0]);
						num2 = Double.parseDouble(parts[2]);
					} catch (NumberFormatException e) {
						throw new Exception("Error: Invalid expression");
					}

					double result = 0;

					switch (operator) {
					case "+":
						result = num1 + num2;
						break;
					case "-":
						result = num1 - num2;
						break;
					case "*":
						result = num1 * num2;
						break;
					case "/":
						if (num2 == 0) {
							throw new ArithmeticException("Error: Division by zero");
						}
						result = num1 / num2;
						break;
					default:
						throw new Exception("Error: Invalid expression");
					}

//					send answer back to client
					out.println(result);

				} catch (ArithmeticException e) {
					out.println(e.getMessage());

				} catch (Exception e) {
					out.println(e.getMessage());
				}
			}

			socket.close();
			in.close();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}