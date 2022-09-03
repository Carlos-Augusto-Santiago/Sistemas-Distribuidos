import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

  static class HttpWorker extends Thread {

    Socket conexion;

    HttpWorker(Socket conexion) {
      this.conexion = conexion;
    }

    public void run() {
      try {
        BufferedReader entrada = new BufferedReader(
          new InputStreamReader(conexion.getInputStream())
        );
        PrintWriter salida = new PrintWriter(conexion.getOutputStream());

        // Leer la url
        String s = entrada.readLine();
        // Impresion del valor ingresado por el usuario en consola
        System.out.println(s);
		
		
        // Obtencion del numero a partir de la url
        String[] parts = s.split("=");
        String part2 = parts[1];
        // Separando el numero del HTTP1.1
        String[] number = part2.split(" ");
        String num = number[0];
		
        // Convertir el numero a entero
        int n = Integer.parseInt(num);
        // Obtencion del valor de m
        int m = n / 2;
		
        for (int i = 50000; i < 50004; i++) {
          Socket conexion = new Socket(InetAddress.getByName("localhost"), i);
          HttpWorker w = new HttpWorker(conexion);
          w.start();
        }

        // Mandando valores a los servidoresA
        salida.write(n);
        salida.write(2);
        salida.write(m);

        if (s.startsWith("GET /primo?numero=" + num)) {
          String respuesta = "<html><h1>Número primo</h1></html>";
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
      } catch (Exception e) {
        System.out.println(e.getMessage());
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
