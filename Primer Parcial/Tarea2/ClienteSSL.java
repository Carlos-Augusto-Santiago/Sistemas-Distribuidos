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

	static class Worker {
		Socket conexion;

		Worker(Socket conexion) {
			this.conexion = conexion;
		}

		public void run() {
			try {

				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
	}

	public static void main(String[] args) {
		// Checar si existen valores en la linea de comandos
		if (args.length == 0) {
			System.out.println("No ingresaste nombres de archivos");
			System.exit(0);
		}
		// Mostrar los valores de la linea de comandos
		for (int counter = 0; counter < args.length; counter++) {
			Names.values.add(args[counter]);
		}

		Socket conexion = null;
		for (;;) {
			try {
				// Propiedades para el cliente con su keystore
				System.setProperty("javax.net.ssl.trustStore", "keystore_cliente.jks");
				System.setProperty("javax.net.ssl.trustStorePassword", "123456");
				// Creacion del servidor seguro con reintentos de conexion
				SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
				conexion = cliente.createSocket("localhost", 8080);
				DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
				DataInputStream entrada = new DataInputStream(conexion.getInputStream());
				// Envio de datos
				// Se envia un valor de prueba
				salida.writeDouble(123456789.123456789);

				// Comprobar que esta el archivo en el disco duro
				for (int i = 0; i < Names.values.size(); i++) {
					File archivo = new File(Names.values.get(i));
					if (archivo.exists() && archivo.isFile()) {
						System.out.println("El archivo existe");
					}
				}
				// Guardar el archivo en el disco
				// Recibir mensaje "OK" si todo se ejecutó con orden
				Thread.sleep(1000);
				conexion.close();
				break;
			} catch (Exception e) {
			}
		}
	}
}
