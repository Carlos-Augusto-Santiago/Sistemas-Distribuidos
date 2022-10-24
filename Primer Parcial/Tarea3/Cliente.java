import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    static int N = 2;
    static float[][] A = new float[N][N];
    static float[][] B = new float[N][N];
    static boolean reintentar = true;

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
    // puerto inicial para los hilos
    static int PORT_START = 21864;

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

    static class Worker extends Thread {

        Socket socket;
        int num;

        Worker(Socket socket, int num) {
            this.socket = socket;
            this.num = num;
        }

        public void run() {
            try {
                // iniciamos el hilo en el puerto asignado
                DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // enviar las matrices B1-B4
                FloatBuffer buffer = FloatBuffer.allocate(N);
                for (int i = 0; i < B1.length; i++) {
                    buffer.put(B1[i]);
                }
                float[][] Bs = buffer.array();

                // se checa en que hilo esta, si es el 1 manda A1 y A2
                // si es el dos manda A3 y A4
                // if (num == 1) {
                // System.out.println("Enviando A1, A2 y B1-B4 al hilo 1");
                // salida.println(A1);
                // salida.println(A2);
                // salida.flush();

                // // recibir las matrices C1-C8
                // for (int i = 0; i < 8; i++) {
                // C_values[i] = Float.parseFloat(entrada.readLine());
                // }
                // } else if (num == 2) {
                // System.out.println("Enviando A3, A4 y B1-B4 al hilo 2");
                // salida.println(A3);
                // salida.println(A4);
                // salida.flush();
                // // recibir las matrices C9-C16
                // for (int i = 8; i < 16; i++) {
                // C_values[i] = Float.parseFloat(entrada.readLine());
                // }
                // }

                reintentar = false;

            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Socket conexion = null;
        // asignacion de las matrices usado la funcion Matrices
        Matrices();
        // ciclo de reconexiones
        while (reintentar) {
            Worker[] hilos;
            try {
                conexion = new Socket("localhost", 8080);
                // iniciar conexion
                System.out.println("Conexion recibida");
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(conexion.getInputStream()));

                // asegurando que se abrieron los sockets
                System.out.println("Esperando la confirmacion del servidor...");
                entrada.readLine();
                System.out.println("Confirmacion completada");

                // Enviar las matrices e iniciar su Hilo
                System.out.println("Iniciando hilos para lectura de las matrices");
                hilos = new Worker[2];
                for (int i = 0; i < 2; i++) {
                    try {
                        Worker w = new Worker(new Socket("localhost", PORT_START + i), i + 1);
                        w.start();
                        hilos[i] = w;
                    } catch (UnknownHostException ex) {
                        Logger
                                .getLogger(Cliente.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("Listo");

                System.out.println("Esperando a que terminen de enviarse los archivos");
                // Cerramos los hilos
                for (int i = 0; i < 2; i++) {
                    hilos[i].join();
                }

                // Cerrar el socket
                conexion.close();
                System.out.println("Proceso terminado");
            } catch (InterruptedException | IOException ex) {
                Logger
                        .getLogger(Cliente.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
}
