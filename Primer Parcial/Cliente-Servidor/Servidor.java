import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Servidor {

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(50000);
            Socket conexion = servidor.accept();

            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());

            int n = entrada.readInt();
            System.out.println(n);

            double x = entrada.readDouble();
            System.out.println(x);

            byte[] buffer = new byte[4];
            read(entrada, buffer, 0, 4);
            System.out.println(new String(buffer, "UTF-8"));

            // Tiempo de inicio de recibir los 10000 numeros
            long start = System.currentTimeMillis();
            // Leer los 10000 numeros double
            for (int i = 0; i < 10000; i++) {
                double y = entrada.readDouble();
            }
            // Tiempo de finalizacion del proceso
            long end = System.currentTimeMillis() - start;

            System.out.println("El tiempo en s fue de: " + end + "ms");

            salida.write("HOLA".getBytes());

            byte[] a = new byte[5 * 8];
            read(entrada, a, 0, 5 * 8);
            ByteBuffer b = ByteBuffer.wrap(a);
            for (int i = 0; i < 5; i++)
                System.out.println(b.getDouble());

            // Tiempo de inicio de enviar los 10000 numeros
            long startB = System.currentTimeMillis();

            byte[] numbers = new byte[10000 * 8];
            read(entrada, numbers, 0, 10000 * 8);
            ByteBuffer nums = ByteBuffer.wrap(numbers);

            // Tiempo de finalizacion del proceso
            long endB = System.currentTimeMillis() - startB;
            System.out.println("El tiempo en s fue de: " + endB + "ms");

            conexion.close();
        } catch (Exception e) {

        }
    }

    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
        while (longitud > 0) {
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }

}
