import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Socket conexion = null;
        for (;;)
            try {
                conexion = new Socket("localhost", 50000);
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                Scanner datos = new Scanner(System.in);
                System.out.println("Ingrese los 3 numeros a mandar al servidor");
                System.out.println("Número: ");
                int num1 = datos.nextInt();
                salida.writeInt(num1);

                System.out.println("Número inicial: ");                
                int num2 = datos.nextInt();
                salida.writeInt(num2);

                System.out.println("Número final: ");                
                int num3 = datos.nextInt();
                salida.writeInt(num3);

                int count = entrada.available();
                byte[] buffer = new byte[count];
                read(entrada, buffer, 0, count);
                System.out.println(new String(buffer, "UTF-8"));

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
