import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    static int PORT_START = 21864;

    static class Worker extends Thread {
        ServerSocket server;

        Worker(ServerSocket server) {
            this.server = server;
        }

        public void run() {
            try {
                Socket conexion = server.accept();

                PrintWriter salida = new PrintWriter(conexion.getOutputStream());
                BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

                // recibir las matrices B
                List<float[][]> B = new ArrayList<float[][]>();
                for (int i = 0; i < 6; i++) {
                    float value = Float.valueOf(entrada.readLine()).floatValue();
                    System.out.println("Valor: " + value);
                }
                salida.flush();
                salida.close();
                entrada.close();
                conexion.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        MulticastSocket s = new MulticastSocket();
        InetAddress group = InetAddress.getByName("230.0.0.0");
        Worker w;
        List<Worker> Workers = new ArrayList<Worker>();
        while (true) {
            Socket conexion = server.accept();
            try {
                PrintWriter salida = new PrintWriter(conexion.getOutputStream());
                BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                // iniciar los hilos con el puerto correspondiente
                int port = Integer.parseInt(entrada.readLine());
                System.out.println("Iniciando hilo en el puerto: " + (PORT_START));
                w = new Worker(new ServerSocket(PORT_START));
                w.start();
                Workers.add(w);
                salida.println("Ya quedó c:");
                salida.flush();
                System.out.println("Conectado!");

                System.out.println("Esperando a que se terminen de recibir las matrices...");
                for (int i = 0; i < Workers.size(); ++i) {
                    Workers.get(i).join();
                }
                System.out.println("Listo");

                salida.close();
                entrada.close();
                conexion.close();
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
