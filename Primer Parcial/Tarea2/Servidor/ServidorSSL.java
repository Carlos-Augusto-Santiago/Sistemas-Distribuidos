import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLServerSocketFactory;

public class ServidorSSL {

	static int PORT_START = 21864;

	static class Worker extends Thread {

		Socket conexion;
		int num;
		int port;
		SSLServerSocketFactory factory;

		Worker(SSLServerSocketFactory socket_factory, int port) throws IOException {
			factory = socket_factory;
			this.port = port;
		}

		public void run() {
			try {
				ServerSocket socket_servidor = this.factory.createServerSocket(this.port);
				this.conexion = socket_servidor.accept();
			
				PrintWriter salida = new PrintWriter(this.conexion.getOutputStream());
				BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexion.getInputStream()));

				// Nombre del archivo
				String name = entrada.readLine();

				// Longitud del archivo
				String size = entrada.readLine();

				// Contenido del archivo
				String content = entrada.readLine();
				System.out.println(content);
				content = content.replace("<>", "\n");
				System.out.println(content);

				// Se crea el archivo
				File file = new File(name);
				boolean result = file.createNewFile();
				if (result) {
					file.createNewFile();
					// Se escribe el contenido en el archivo
					BufferedWriter bw = new BufferedWriter(new FileWriter(file));
					bw.write(content);
					bw.close();
					salida.println("ok");
				}
				else{
					salida.println("El servidor no pudo guardar el archivo: " + name);
				}

				salida.flush();
				salida.close();
				entrada.close();
				this.conexion.close();
				socket_servidor.close();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("javax.net.ssl.keyStore", "keystore_servidor.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "1234567");
		SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		ServerSocket socket_servidor = socket_factory.createServerSocket(8080);
		Worker w;
		List<Worker> fileWorkers = new ArrayList<Worker>();

		while(true) {
			Socket conexion = socket_servidor.accept();
			int num = 0;
			// Recibir el numero de archivos
			System.out.println("Conexion recibida!");
			try {
				PrintWriter salida = new PrintWriter(conexion.getOutputStream());
				BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

				System.out.println("Esperando el numero de archivos a recibir...");
				String numS = entrada.readLine();
				num = Integer.parseInt(numS);
				System.out.println("Numero de archivos: " + num);

				System.out.println("Iniciando hilos para escribir los archivos");
				for (int i = 0; i < num; ++i) {
					System.out.println("Iniciando hilo con puerto: " + (PORT_START + i));
					w = new Worker(socket_factory, PORT_START + i);
					w.start();
					fileWorkers.add(w);
				}
				salida.println("Ya quedó c:");
				salida.flush();
				System.out.println("Conectado!");

				System.out.println("Esperando a que se terminen de recibir los archivos...");
				for (int i = 0; i < fileWorkers.size(); ++i) {
					fileWorkers.get(i).join();
				}
				System.out.println("Listo");

				salida.close();
				entrada.close();
				conexion.close();
			} catch (IOException ex) {
				Logger.getLogger(ServidorSSL.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
