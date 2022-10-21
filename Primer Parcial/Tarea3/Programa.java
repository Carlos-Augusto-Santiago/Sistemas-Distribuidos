import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Programa {

    static int N = 4;
    static float[][] A = new float[N][N];
    static float[][] B = new float[N][N];
    static float[][] C = new float[N][N];

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

    // puerto inicial para los hilos
    static int PORT_START = 50000;

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
                C[i][j] = 0;
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

        // multiplica la matriz A y la matriz B, el resultado queda en la matriz C
        // notar que los indices de la matriz B se han intercambiado

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    C[i][j] = A[i][k] * B[j][k];
                }
            }
        }
    }

    static class Worker extends Thread {

        int port;

        Worker(int port) {
            this.port = port;
        }

        public void run() {
            try {
                // iniciamos el hilo en el puerto asignado
                ServerSocket server = new ServerSocket(port);
                Socket conexion = server.accept();
                PrintWriter salida = new PrintWriter(conexion.getOutputStream());
                BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // inicializamos el servidor
        ServerSocket servidor = new ServerSocket(50000);
        // declarar la clase worker en w
        Worker w;
        // lista de workers para usar multi hilos
        List<Worker> fileWorkers = new ArrayList<Worker>();
        // asignacion de las matrices usado la funcion Matrices
        Matrices();
        // ciclo de reconexiones
        while (true) {
            // iniciar conexion
            Socket conexion = servidor.accept();
            System.out.println("Conexion recibida");
            try {
                PrintWriter salida = new PrintWriter(conexion.getOutputStream());
                System.out.println("Inicializando los 4 hilos de las computadoras");
                for (int i = 0; i < 4; i++) {
                    System.out.println("Iniciando hilo con puerto: " + (PORT_START + i));
                    w = new Worker(PORT_START + i);
                    w.start();
                    fileWorkers.add(w);
                }
                salida.println("Ya quedó c:");
                salida.flush();
                System.out.println("Conectado!");

                System.out.println("Esperando a que se terminen de ejecutar los hilos...");
                for (int i = 0; i < fileWorkers.size(); ++i) {
                    fileWorkers.get(i).join();
                }
                System.out.println("Listo");
            } catch (IOException e) {
                Logger.getLogger(Programa.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
