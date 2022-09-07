import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLSocketFactory;

public class ClienteSSL {

	static class Names {
		// Variable para recibir los nombres de los archivos
		static List<String> values = new ArrayList<String>();
	}

	static class Worker extends Thread {
		Socket conexion;

		Worker(Socket conexion) {
			this.conexion = conexion;
			for (int i = 0; i < Names.values.size(); i++) {
				
		}

		public void run() {
			try {

				DataOutputStream salida = new DataOutputStream(
						conexion.getOutputStream());
				DataInputStream entrada = new DataInputStream(conexion.getInputStream());
				// Envio de datos
				// Se envia un valor de prueba
				salida.writeDouble(123456789.123456789);

				// Comprobar que esta el archivo en el disco duro
				File archivo = new File(Names.values.get(0));
				if (archivo.exists()) {
					System.out.println("wuu");
				}
				// Guardar el archivo en el disco
				// Recibir mensaje "OK" si todo se ejecutó con orden
				Thread.sleep(1000);
				conexion.close();
			} catch (Exception e) {
			}
		}
	}

	public static void main(String[] args) {
		// Checar si existen valores en la linea de comandos
		if (args.length == 0)
			System.exit(0);
		// Mostrar los valores de la linea de comandos
		for (int counter = 0; counter < args.length; counter++) {
			Names.values.add(args[counter]);
		}

		// Propiedades para el cliente con su keystore
		System.setProperty("javax.net.ssl.trustStore", "keystore_cliente.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");

		for (;;) {
			// Creacion del servidor seguro con reintentos de conexion
			SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
			Socket conexion;
			try {
				conexion = cliente.createSocket("localhost", 50000);
				Worker w = new Worker(conexion);
				w.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
