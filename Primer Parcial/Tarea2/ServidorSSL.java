import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLServerSocketFactory;

public class ServidorSSL {

	static class Worker extends Thread {

		Socket conexion;

		Worker(Socket conexion) {
			this.conexion = conexion;
		}

		public void run() {
			try {
				DataOutputStream salida = new DataOutputStream(
						conexion.getOutputStream());
				DataInputStream entrada = new DataInputStream(
						conexion.getInputStream());

				double x = entrada.readDouble();
				System.out.println(x);
				conexion.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("javax.net.ssl.keyStore", "keystore_servidor.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "1234567");
		SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		ServerSocket socket_servidor = socket_factory.createServerSocket(8080);
		for (;;) {
			Socket conexion = socket_servidor.accept();
			Worker w = new Worker(conexion);
			w.start();
		}
	}

	static void read(DataInputStream f, byte[] b, int posicion, int longitud)
			throws Exception {
		while (longitud > 0) {
			int n = f.read(b, posicion, longitud);
			posicion += n;
			longitud -= n;
		}
	}
}
