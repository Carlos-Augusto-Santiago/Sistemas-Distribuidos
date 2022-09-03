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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laxelott
 */
public class Servidor {

	static class HttpWorker extends Thread {

		Socket conexion;
		TCPWorker[] workers = new TCPWorker[4];

		public HttpWorker(Socket conexion) {
			this.conexion = conexion;
			for (int i = 0; i < 4; i++) {
				try {
					TCPWorker w = new TCPWorker(new Socket(InetAddress.getByName("localhost"), 50000 + i));
					workers[i] = w;
				} catch (UnknownHostException ex) {
					Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		@Override
		public void run() {
			try {

				BufferedReader entrada = new BufferedReader(
						new InputStreamReader(conexion.getInputStream()));
				PrintWriter salida = new PrintWriter(conexion.getOutputStream());

				// Leer la url
				String s = entrada.readLine();
				// Impresion del valor ingresado por el usuario en consola
				System.out.println(s);

				if (s.contains("primo")) {
					// Obtencion del numero a partir de la url
					String[] parts = s.split("=");
					String part2 = parts[1];
					// Separando el numero del HTTP1.1
					String[] number = part2.split(" ");
					String num = number[0];

					// Convertir el numero a entero
					int n = Integer.parseInt(num);
					// Obtencion del valor de m
					int m = (int) Math.floor(n / 4);

					// Mandando valores a los servidoresA
					for (int i = 0; i < 4; i++) {
						workers[i].setData(n, m * (i) + 2, m * (i + 1) + 2);
						workers[i].start();
						System.out.println("Worker " + i + " inició!");
					}

					workers[0].join();
					System.out.println("Worker 0 terminó!");
					workers[1].join();
					System.out.println("Worker 1 terminó!");
					workers[2].join();
					System.out.println("Worker 2 terminó!");
					workers[3].join();
					System.out.println("Worker 3 terminó!");

					Boolean divide = workers[0].isDivide() || workers[1].isDivide() || workers[2].isDivide()
							|| workers[3].isDivide();

					String respuesta = "<html><h1>" + (divide ? "Es primo" : "No es primo") + "</h1></html>";
					salida.println("HTTP/1.1 200 OK");
					salida.println("Content-type: text/html; charset=utf-8");
					salida.println("Content-length: " + respuesta.length());
					salida.println();
					salida.flush();
					salida.println(respuesta);
					salida.flush();
				} else {
					salida.println("HTTP/1.1 404 File Not Found");
					salida.flush();
				}

				System.out.println(conexion.getRemoteSocketAddress().toString());

				conexion.close();
			} catch (IOException | InterruptedException ex) {
				Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
	}

	public static void main(String[] args) throws Exception {
		ServerSocket servidor = new ServerSocket(8080);
		for (;;) {
			Socket conexion = servidor.accept();
			HttpWorker w = new HttpWorker(conexion);
			w.start();
		}
	}
}
