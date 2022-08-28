import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ClienteRe {
    public static void main(String[] args) throws InterruptedException {

        Socket conexion = null;
        for (;;)
            try {
                conexion = new Socket("localhost", 50000);
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                salida.writeInt(123);
                salida.writeDouble(1234567890.1234567890);
                salida.write("hola".getBytes());

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
                conexion.close();
                break;
                } catch (Exception e) {
                    Thread.sleep(100);
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
