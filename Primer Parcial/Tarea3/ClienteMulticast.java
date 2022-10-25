import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class ClienteMulticast {

    static int N = 2;
    static float[][] A = new float[N][N];
    static float[][] B = new float[N][N];

    // arreglos de A1-A4
    static float[][] A1 = new float[N][N];
    static float[][] A2 = new float[N][N];
    static float[][] A3 = new float[N][N];
    static float[][] A4 = new float[N][N];

    // arreglos de B1-B4
    static float[][] B1 = new float[N][N];
    static float[][] B2 = new float[N][N];
    static float[][] B3 = new float[N][N];
    static float[][] B4 = new float[N][N];

    // variable para la copia de la matriz B
    static float[][] B_copy = new float[N][N];

    // variable para los valores de C1-C16
    static float[] C_values = new float[16];

    public static void Matrices() {
        // inicializa las matrices A y B
        for (int i = 0; i < N; i++) {
            // almacenar los valores de A en cada A1-A4
            if (i == N / 4) {
                // A1
                A1 = A.clone();
            } else if (i == N / 2) {
                // A2
                int x = N / 2;
                for (int y = N / 4; y < x; y++) {
                    for (int j = 0; j < N; j++) {
                        A2[y][j] = A[y][j];
                    }
                }
            } else if (i == 3 * N / 4) {
                // A3
                int x = 3 * N / 4;
                for (int y = N / 2; y < x; y++) {
                    for (int j = 0; j < N; j++) {
                        A3[y][j] = A[y][j];
                    }
                }
            }
            // ciclo para llenar A y B
            for (int j = 0; j < N; j++) {
                A[i][j] = i + 3 * j;
                B[i][j] = 2 * i - j;
                // copia de la matriz B
                B_copy[i][j] = B[i][j];
            }
            System.out.println("");
            if (i == N - 1) {
                // A4
                int x = N;
                for (int y = 3 * N / 4; y < x; y++) {
                    for (int j = 0; j < N; j++) {
                        A4[y][j] = A[y][j];
                    }
                }
            }
        }

        // trasponer la matriz B
        for (int i = 0; i < N; i++) {
            // B traspuesta
            for (int j = 0; j < i; j++) {
                float x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }

        for (int i = 0; i < N; i++) {
            // almacenar los valores de B en cada B1-B4
            if (i == N / 4) {
                B1 = B.clone();
            } else if (i == N / 2) {
                int x = N / 2;
                for (int y = N / 4; y < x; y++) {
                    for (int j = 0; j < N; j++) {
                        B2[y][j] = B[y][j];
                    }
                }
            } else if (i == 3 * N / 4) {
                int x = 3 * N / 4;
                for (int y = N / 2; y < x; y++) {
                    for (int j = 0; j < N; j++) {
                        B3[y][j] = B[y][j];
                    }
                }
            }
            if (i == N - 1) {
                int x = N;
                for (int y = 3 * N / 4; y < x; y++) {
                    for (int j = 0; j < N; j++) {
                        B4[y][j] = B[y][j];
                    }
                }
            }
        }
    }

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
        MulticastSocket socket = new MulticastSocket(50000);
        InetSocketAddress grupo = new InetSocketAddress(InetAddress.getByName("230.0.0.0"), 50000);
        NetworkInterface netInter = NetworkInterface.getByName("em1");
        socket.joinGroup(grupo, netInter);

        // enviamos el valor de N
        ByteBuffer N_value = ByteBuffer.allocate(4);
        N_value.putInt(N);
        envia_mensaje(N_value.array(), "230.0.0.0", 50000);

        // Enviar los valores de A1 y A2
        // ByteBuffer a1 = ByteBuffer.allocate(N * 8);
        // ByteBuffer a2 = ByteBuffer.allocate(N * 8);
        // for (int i = 0; i < N; i++) {
        // for (int j = 0; j < N; j++) {
        // a1.putFloat(A1[i][j]);
        // a2.putFloat(A2[i][j]);
        // }
        // }

        // byte[] a = recibe_mensaje(socket, 4);
        // System.out.println(new String(a, "UTF-8"));

        // byte[] buffer = recibe_mensaje(socket, 5 * 8);
        // ByteBuffer b = ByteBuffer.wrap(buffer);
        // for (int i = 0; i < 5; i++)
        // System.out.println(b.getDouble());

        socket.leaveGroup(grupo, netInter);
        socket.close();
    }

}