import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocketFactory;

public class ClienteSSL {

    static int PORT_START = 21864;
    static boolean reintentar = true;

    static class Worker extends Thread {

        Socket conexion;
        File fileName;

        public Worker(Socket conexion, File fileName) {
            this.conexion = conexion;
            this.fileName = fileName;
        }

        public void run() {
            try {
                PrintWriter salida = new PrintWriter(conexion.getOutputStream());
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(conexion.getInputStream()));

                // Enviar nombre del archivo al servidor
                salida.println(fileName.getName());
                salida.flush();
                System.out.println("Nombre del archivo: " + fileName.getName());

                // Enviar longitud del archivo
                String tam = fileName.length() + "";
                salida.println(tam);
                salida.flush();
                System.out.println("Tamaño del archivo" + tam);

                // Contenido del archivo
                Scanner input = new Scanner(fileName);
                String content = "";
                while (input.hasNextLine()) {
                    String line = input.nextLine().replace("\n", "<>");
                    content += line + "<>";
                }
                System.out.println("Enviando contenido: " + content);
                salida.println(content);
                salida.flush();
                input.close();

                // Recibir respuesta del servidor
                String response = entrada.readLine();
                System.out.println(response);
                reintentar = response == "ok";

                salida.flush();
            } catch (Exception e) {
            }
        }
    }

    public static boolean validate(File fileName) {
        // Validar que existe y que es archivo
        if (fileName.exists() && fileName.isFile()) {
            System.out.println("Se encontro el archivo: " + fileName);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        // Checar si existen valores en la linea de comandos
        if (args.length == 0) {
            System.out.println("No ingresaste nombres de archivos");
            System.exit(0);
        }

        // Variable para recibir los nombres de los archivos
        List<File> filesNames = new ArrayList<File>();
        File file;

        // Guardar los valores de la linea de comandos
        for (int counter = 0; counter < args.length; counter++) {
            file = new File(args[counter]);
            // Verificar que todos los archivos existan
            if (validate(file)) {
                filesNames.add(file);
            } else {
                System.out.println("Archivo no existe! " + args[counter]);
            }
        }

        // Obtenemos cuantos archivos se ingresan
        int num = filesNames.size();

        Socket conexion = null;
        while(reintentar) {
            // Propiedades para el cliente con su keystore
            System.setProperty("javax.net.ssl.trustStore", "keystore_cliente.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "123456");
            // Creacion del servidor seguro con reintentos de conexion
            SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
            Worker[] hilos;

            try {
                conexion = cliente.createSocket("localhost", 8080);
                PrintWriter salida = new PrintWriter(conexion.getOutputStream());
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(conexion.getInputStream()));

                // Enviar al servidor el número de archivos
                System.out.println(
                        "Enviando el numero de archivos al servidor: " + num);
                salida.println(num);
                salida.flush();

                // Asegurando que se abrieron los sockets
                System.out.println("Esperando la confirmacion del servidor...");
                entrada.readLine();
                System.out.println("Confirmacion completada");

                // Iterar cada archivo e iniciar su Hilo
                System.out.println("Iniciando hilos de lectura de archivo");
                hilos = new Worker[num];
                for (int i = 0; i < num; i++) {
                    try {
                        Worker w = new Worker(
                                cliente.createSocket("localhost", PORT_START + i),
                                filesNames.get(i));
                        w.start();
                        hilos[i] = w;
                    } catch (UnknownHostException ex) {
                        Logger
                                .getLogger(ClienteSSL.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("Listo");

                System.out.println("Esperando a que terminen de enviarse los archivos");
                // Cerramos los hilos
                for (int i = 0; i < num; i++) {
                    hilos[i].join();
                }

                // Cerrar el socket
                conexion.close();
                salida.close();
                System.out.println("Proceso terminado");
            } catch (InterruptedException | IOException ex) {
                Logger
                        .getLogger(ClienteSSL.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
}
