import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Cliente {
    public static void main(String[] args) {
        Socket conexion = null;
        for (;;)
            try {
                conexion = new Socket("localhost", 50000);
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                salida.writeInt(123);
                salida.writeDouble(1234567890.1234567890);
                salida.write("hola".getBytes());

                // Tiempo de inicio de enviar los 10000 numeros
                long start = System.currentTimeMillis();
                // Enviar 10000 numeros con write Double
                for (double i = 1.0; i <= 10000.0; i += 1.0) {
                    salida.writeDouble(i);
                }
                // Tiempo de finalizacion del proceso
                long end = System.currentTimeMillis() - start;

                System.out.println("El tiempo en s fue de: " + end + "ms");
                byte[] buffer = new byte[4];
                read(entrada, buffer, 0, 4);
                System.out.println(new String(buffer, "UTF-8"));

                ByteBuffer b = ByteBuffer.allocate(5 * 8);
                b.putDouble(1.1);
                b.putDouble(1.2);
                b.putDouble(1.3);
                b.putDouble(1.4);
                b.putDouble(1.5);
                byte[] a = b.array();
                salida.write(a);

                // Tiempo de inicio de enviar los 10000 numeros
                long startB = System.currentTimeMillis();
                // Envio de 10000 double con ByteBuffer
                ByteBuffer nums = ByteBuffer.allocate(10000 * 8);
                for (double i = 1.0; i < 10000.0; i += 1.0) {
                    nums.putDouble(i);
                }
                byte[] numbers = nums.array();
                salida.write(numbers);
                // Tiempo de finalizacion del proceso
                long endB = System.currentTimeMillis() - startB;
                System.out.println("El tiempo en s fue de: " + endB + "ms");

                Thread.sleep(1000);
                conexion.close();
                break;
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
