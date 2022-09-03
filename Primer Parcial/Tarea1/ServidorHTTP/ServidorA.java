/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorHTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laxelott
 */
public class ServidorA {

	static class Worker extends Thread {

		Socket conexion;

		Worker(Socket conexion) {
			this.conexion = conexion;
		}

		@Override
		public void run() {
			try {
				PrintWriter salida = new PrintWriter(conexion.getOutputStream());
				BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
				salida.flush();

				String stringEntrada;
				System.out.println("1");
				while (entrada.ready()) {
					System.out.println("1");
					stringEntrada = entrada.readLine();
					System.out.println("1");
					String[] entradas = stringEntrada.split(",");
					System.out.println("1");

					int num1 = Integer.valueOf(entradas[0]);
					int num2 = Integer.valueOf(entradas[1]);
					int num3 = Integer.valueOf(entradas[2]);

					String result = "";
					for (int i = num2; i <= num3; i++) {
						result = divide(num1, i);
						if ("DIVIDE".equals(result)) {
							break;
						}
					}
					salida.println(result);
					salida.flush();
					System.out.println(result);
				}

				conexion.close();
			} catch (NumberFormatException | IOException ex) {
				Logger.getLogger(ServidorA.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		int puerto;
		Scanner datos = new Scanner(System.in);

		System.out.print("Inserte el puerto: ");
		puerto = datos.nextInt();

		ServerSocket servidor = new ServerSocket(puerto);
		for (;;) {
			Socket conexion = servidor.accept();
			Worker w = new Worker(conexion);
			w.start();
		}
	}

	static String divide(int n, int ni) {
		String result;

		if (n % ni == 0) {
			result = "DIVIDE";
		} else {
			result = "NO DIVIDE";
		}
		return result;
	}
}
