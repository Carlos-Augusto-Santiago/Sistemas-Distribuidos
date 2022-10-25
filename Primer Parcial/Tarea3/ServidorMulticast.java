import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class ServidorMulticast {

    static void envia_mensaje(byte[] buffer, String ip, int puerto) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress grupo = InetAddress.getByName(ip);
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, grupo, puerto);
        socket.send(paquete);
        socket.close();
    }

    static byte[] recibe_mensaje(MulticastSocket socket, int longitud_mensaje) throws IOException {
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        MulticastSocket socket = new MulticastSocket(50000);

        byte[] a = recibe_mensaje(socket, 4);
        System.out.println(new String(a, "UTF-8"));

        // byte[] buffer = recibe_mensaje(socket, 5 * 8);
        // ByteBuffer b = ByteBuffer.wrap(buffer);
        // for (int i = 0; i < 5; i++)
        // System.out.println(b.getDouble());

    }
}