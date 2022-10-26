
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Matrices {
    // declaracion de N y las matrices
    static int N;
    static float A[][] = new float[N][N];
    static float B[][] = new float[N][N];
    static float C[][] = new float[N][N];

    public static void printM(float[][] m) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.println(m[i][j]);
            }
            System.out.println();
        }
    }

    static class Worker extends Thread {
        Socket conexion;

        Worker(Socket conexion) {
            this.conexion = conexion;
        }

        public void run() {
            try {
                // Streams de entrada y salida
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                // recibir el nodo
                int nodo = entrada.readInt();

                // envio de B1-B4
                for (int i = 0; i < N; i++) {
                    if (i == N / 4) {
                        for (int y = 0; y < N / 4; y++) {
                            for (int j = 0; j < N; j++) {
                                salida.writeFloat(B[y][j]);
                            }
                        }
                    } else if (i == N / 2) {
                        for (int y = N / 4; y < N / 2; y++) {
                            for (int j = 0; j < N; j++) {
                                salida.writeFloat(B[y][j]);
                            }
                        }
                    } else if (i == 3 * N / 4) {
                        for (int y = N / 2; y < 3 * N / 4; y++) {
                            for (int j = 0; j < N; j++) {
                                salida.writeFloat(B[y][j]);
                            }
                        }
                    }
                    if (i == N - 1) {
                        for (int y = 3 * N / 4; y < N; y++) {
                            for (int j = 0; j < N; j++) {
                                salida.writeFloat(B[y][j]);
                            }
                        }
                    }
                }

                // checamos el nodo correspondiente para hacer
                if (nodo == 1) {
                    // envio de A1
                    for (int i = 0; i < N; i++) {
                        if (i == N / 4) {
                            // A1
                            for (int y = 0; y < N / 4; y++) {
                                for (int j = 0; j < N; j++) {
                                    salida.writeFloat(A[y][j]);
                                }
                            }
                        } else if (i == N / 2) {
                            // A2
                            for (int y = N / 4; y < N / 2; y++) {
                                for (int j = 0; j < N; j++) {
                                    salida.writeFloat(A[y][j]);
                                }
                            }
                        }
                    }
                }
                salida.close();

            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        // le pedimos al usuario el valor de N, el numero de nodo y el ip
        Scanner values = new Scanner(System.in);
        System.out.println("Ingresa el valor de N: ");
        N = values.nextInt();
        System.out.print("Ingresa el nodo: ");
        int nodo = values.nextInt();
        System.out.print("Ingresa la ip: ");
        String ip = values.nextLine();
        System.out.println(N + " " + nodo + " " + ip);

        // checamos que nodo se ingreso
        if (nodo == 0) {
            // inicializamos las matrices
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    A[i][j] = i + 3 * j;
                    B[i][j] = 2 * i - j;
                    C[i][j] = 100;
                }
            }
            // si N = 12, imprimir la matriz A y B
            if (N == 12) {
                System.out.println("Matriz A");
                printM(A);
                System.out.println("\nMatriz B");
                printM(B);
            }
            // se traspone la matriz B
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < i; j++) {
                    float x = B[i][j];
                    B[i][j] = B[j][i];
                    B[j][i] = x;
                }
            }
            // incializacion del servidor y los hilos
            ServerSocket servidor = new ServerSocket(50000);
            // se crean 4 hilos, que son el numero de nodos
            Worker v[] = new Worker[4];
            for (int i = 0; i < v.length; i++) {
                Socket conexion = servidor.accept();
                v[i] = new Worker(conexion);
                v[i].start();
            }
            for (int i = 0; i < v.length; i++) {
                v[i].join();
            }

            if (N == 12) {
                System.out.println("\nC");
                printM(C);
            }

            checksum(C);
            servidor.close();
        } else if (nodo > 0 && nodo < 5) {
            Socket conexion = null;
            // conexion con reintentos
            for (;;) {
                try {
                    conexion = new Socket(ip, 50000);
                } catch (Exception e) {
                    Thread.sleep(100);
                }
            }
            // Streams de entrada y salida
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            // Enviar el nodo
            salida.writeInt(nodo);
            float a[][] = new float[N][N];
            float b1[][] = new float[N / 4][N];
            float b2[][] = new float[N / 4][N];
            float b3[][] = new float[N / 4][N];
            float b4[][] = new float[N / 4][N];
            float c[][] = new float[N / 4][N / 4];

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    b[i][j] = entrada.readLong();
                }
            }
            for (int i = 0; i < N / 2; i++) {
                for (int j = 0; j < N; j++) {
                    a[i][j] = entrada.readLong();
                }
            }
            for (int i = 0; i < N / 4; i++) {
                for (int j = 0; j < N / 4; j++) {
                    for (int k = 0; k < N; k++) {
                        c[i][j] += a[i][k] * b[j][k];
                    }
                }
            }
            for (int i = 0; i < N / 4; i++) {
                for (int j = 0; j < N / 4; j++) {
                    salida.writeFloat(c[i][j]);
                }
            }

            salida.close();
            entrada.close();
            conexion.close();
        }

        values.close();
    }
}
