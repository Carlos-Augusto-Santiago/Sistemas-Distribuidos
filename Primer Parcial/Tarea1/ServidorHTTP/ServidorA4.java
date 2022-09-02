import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorA4 {
    static class Worker extends Thread {
        Socket conexion;

        Worker(Socket conexion) {
            this.conexion = conexion;
        }

        public void run() {
            try {
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                int num1 = entrada.readInt();
                int num2 = entrada.readInt();
                int num3 = entrada.readInt();

                String result = "";
                for (int i = num2; i <= num3; i++) {
                    result = divide(num1, i);
                    if(result == "DIVIDE"){
                        salida.writeBytes(result);
                        System.out.println(result);
                        break;
                    }
                    else{
                        salida.writeBytes(result);
                        System.out.println(result);
                    }
                }

                conexion.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket servidor = new ServerSocket(50003);
        for (;;) {
            Socket conexion = servidor.accept();
            Worker w = new Worker(conexion);
            w.start();
        }
    }

    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
        while (longitud > 0) {
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }

    static String divide(int n, int ni) {
        String result = "";

        if (n % ni == 0) {
            result = "DIVIDE";
        } else {
            result = "NO DIVIDE";
        }
        return result;
    }
}
