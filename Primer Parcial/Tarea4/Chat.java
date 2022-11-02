import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class Chat {
    static String IP = "239.10.10.1";
    static int PUERTO = 10000;

    static void envia_mensaje_multicast(byte[] buffer, String ip, int puerto) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), puerto));
        socket.close();
    }

    // se recibe una longitud de mensaje para crear un buffer con ese espacio y
    // poder recibir el mensaje
    static byte[] recibe_mensaje_multicast(MulticastSocket socket, int longitud_mensaje) throws IOException {
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }

    static class Worker extends Thread {
        public void run() {
            try {
                InetAddress ip = InetAddress.getByName(IP);
                MulticastSocket socket = new MulticastSocket(PUERTO);
                socket.joinGroup(ip);
                // ciclo para recibir los mensajes enviados
                while (true) {
                    // se almacena el mensaje recibido
                    byte[] mensaje = recibe_mensaje_multicast(socket, 100);
                    // se imprime el mensaje con este formato para que acepte
                    System.out.println(new String(mensaje, "IBM850"));
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // Recibir el nombre de usuario
        if (args.length < 1) {
            System.err.println("Debe pasar el nombre");
            System.exit(1);
        }
        // iniciar hilo
        new Worker().start();
        String nombre = args[0];
        Scanner entrada = new Scanner(System.in);
        String mensaje = "";
        while (true) {
            System.out.println("Ingrese el mensaje:");
            mensaje = nombre + ":- " + entrada.nextLine();
            byte[] buffer = mensaje.getBytes();
            envia_mensaje_multicast(buffer, IP, PUERTO);
        }
    }
}