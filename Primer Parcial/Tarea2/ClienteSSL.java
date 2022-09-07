import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import javax.net.ssl.SSLSocketFactory;

public class ClienteSSL {

  public static void main(String[] args) {
    System.setProperty("javax.net.ssl.trustStore", "keystore_cliente.jks");
    System.setProperty("javax.net.ssl.trustStorePassword", "123456");
    for (;;) try {
      //Creacion del servidor seguro con reintentos de conexion
      SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
      Socket conexion = cliente.createSocket("localhost", 50000);
      DataOutputStream salida = new DataOutputStream(
        conexion.getOutputStream()
      );
      DataInputStream entrada = new DataInputStream(conexion.getInputStream());
      //Envio de datos
      //Se envia un valor de prueba
      salida.writeDouble(123456789.123456789);
      //Lectura del archivo sin ruta
      Scanner scanner = new Scanner(System.in); 
      //Nombre del archivo
      System.out.println("Ingresa el nombre de los archivo (sin ruta), separar cada nombre con comas: ");
      String names = scanner.nextLine();
      String[] values = names.split("\\,");
      System.out.println(Arrays.toString(values));

      // Comprobar que esta el archivo en el disco duro
      File archivo = new File("ClienteSSL.class");
      if (archivo.exists()) {
        System.out.println("wuu");
      }
      scanner.close();
      //Guardar el archivo en el disco
      //Recibir mensaje "OK" si todo se ejecutó con orden
      Thread.sleep(1000);
      conexion.close();
      break;
    } catch (Exception e) {

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
