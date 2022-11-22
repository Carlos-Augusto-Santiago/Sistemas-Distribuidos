import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteRMI {
    static int N = 6;
    static int M = 5;
    static double A[][] = new double[N][M];
    static double B[][] = new double[N][M];
    static double C[][] = new double[N][M];

    // Funcion para separar las matrices
    public static double[][] separa_matriz(double[][] A, int inicio) {
        double[][] X = new double[N / 6][M];
        for (int i = 0; i < N / 6; i++) {
            for (int j = 0; j < M; j++) {
                X[i][j] = A[i + inicio][j];
            }
        }
        return X;
    }

    // Funcion para multiplicar matrices
    public static double[][] multiplica_matrices(double[][] A, double[][] B) {
        double[][] C = new double[N / 6][M / 6];
        for (int i = 0; i < N / 6; i++) {
            for (int j = 0; j < M / 6; j++) {
                for (int k = 0; k < N; k++) {
                    C[i][j] += A[i][k] * B[j][k];
                }
            }
        }
        return C;
    }

    // Funcion para ordenar los valores en la matriz C
    public static void acomoda_matriz(double[][] C, double[][] c, int renglon, int columna) {
        for (int i = 0; i < N / 6; i++) {
            for (int j = 0; j < M / 6; j++) {
                C[i + renglon][j + columna] = c[i][j];
            }
        }
    }

    public static void checksum(double[][] m) {
        double checksum = 0;
        for (double[] i : m) {
            for (double j : i) {
                checksum += j;
            }
        }
        System.out.println("Checksum: " + checksum);
    }

    public static void impMatriz(double[][] m) {
        for (double[] i : m) {
            for (double j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        // cambiar localhost por la ip del servidor
        Registry miRegistro = LocateRegistry.getRegistry("localhost", 1099);
        String url = "rmi://localhost/prueba";
        InterfaceRMI r = (InterfaceRMI) miRegistro.lookup(url);

        InterfaceRMI r1 = (InterfaceRMI) miRegistro.lookup(url);

        // Inicializando matrices
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                A[i][j] = 3 * i + 2 * j;
                B[i][j] = 2 * i - 3 * j;
                C[i][j] = 0;
            }
        }
        // Trasponer B
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < i; j++) {
                double x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }

        // Separando las matrices A
        double[][] A1 = separa_matriz(A, 0);
        double[][] A2 = separa_matriz(A, N / 6);
        double[][] A3 = separa_matriz(A, 1 * N / 3);
        double[][] A4 = separa_matriz(A, N / 2);
        double[][] A5 = separa_matriz(A, 2 * N / 3);
        double[][] A6 = separa_matriz(A, 5 * N / 6);

        // Separando las matrices B
        double[][] B1 = separa_matriz(A, 0);
        double[][] B2 = separa_matriz(A, N / 6);
        double[][] B3 = separa_matriz(A, 2 * N / 6);
        double[][] B4 = separa_matriz(A, N / 2);
        double[][] B5 = separa_matriz(A, 2 * N / 3);
        double[][] B6 = separa_matriz(A, 5 * N / 6);

        // Obteniendo los valores de C1-C12 localmente
        double[][] C1 = multiplica_matrices(A1, B1);
        double[][] C2 = multiplica_matrices(A1, B2);
        double[][] C3 = multiplica_matrices(A1, B3);
        double[][] C4 = multiplica_matrices(A1, B4);
        double[][] C5 = multiplica_matrices(A1, B5);
        double[][] C6 = multiplica_matrices(A1, B6);
        double[][] C7 = multiplica_matrices(A2, B1);
        double[][] C8 = multiplica_matrices(A2, B2);
        double[][] C9 = multiplica_matrices(A2, B3);
        double[][] C10 = multiplica_matrices(A2, B4);
        double[][] C11 = multiplica_matrices(A2, B5);
        double[][] C12 = multiplica_matrices(A2, B6);

        // Obteniendo los valores de C13-C24 rmi
        double[][] C13 = r.multiplica_matrices(A3, B1, N, M);
        double[][] C14 = r.multiplica_matrices(A3, B2, N, M);
        double[][] C15 = r.multiplica_matrices(A3, B3, N, M);
        double[][] C16 = r.multiplica_matrices(A3, B4, N, M);
        double[][] C17 = r.multiplica_matrices(A3, B5, N, M);
        double[][] C18 = r.multiplica_matrices(A3, B6, N, M);
        double[][] C19 = r.multiplica_matrices(A4, B1, N, M);
        double[][] C20 = r.multiplica_matrices(A4, B2, N, M);
        double[][] C21 = r.multiplica_matrices(A4, B3, N, M);
        double[][] C22 = r.multiplica_matrices(A4, B4, N, M);
        double[][] C23 = r.multiplica_matrices(A4, B5, N, M);
        double[][] C24 = r.multiplica_matrices(A4, B6, N, M);

        // Obteniendo los valores de C25-C36 rmi
        double[][] C25 = r.multiplica_matrices(A5, B1, N, M);
        double[][] C26 = r.multiplica_matrices(A5, B2, N, M);
        double[][] C27 = r.multiplica_matrices(A5, B3, N, M);
        double[][] C28 = r.multiplica_matrices(A5, B4, N, M);
        double[][] C29 = r.multiplica_matrices(A5, B5, N, M);
        double[][] C30 = r.multiplica_matrices(A5, B6, N, M);
        double[][] C31 = r.multiplica_matrices(A6, B1, N, M);
        double[][] C32 = r.multiplica_matrices(A6, B2, N, M);
        double[][] C33 = r.multiplica_matrices(A6, B3, N, M);
        double[][] C34 = r.multiplica_matrices(A6, B4, N, M);
        double[][] C35 = r.multiplica_matrices(A6, B5, N, M);
        double[][] C36 = r.multiplica_matrices(A6, B6, N, M);

        // Ordenando los valores en C
        // renglon 0
        acomoda_matriz(C, C1, 0, 0);
        acomoda_matriz(C, C2, 0, M / 6);
        acomoda_matriz(C, C3, 0, 1 * M / 3);
        acomoda_matriz(C, C4, 0, M / 2);
        acomoda_matriz(C, C5, 0, 2 * M / 3);
        acomoda_matriz(C, C6, 0, 5 * M / 6);
        // renglon 1
        acomoda_matriz(C, C7, N / 6, 0);
        acomoda_matriz(C, C8, N / 6, M / 6);
        acomoda_matriz(C, C9, N / 6, 1 * M / 3);
        acomoda_matriz(C, C10, N / 6, M / 2);
        acomoda_matriz(C, C11, N / 6, 2 * M / 3);
        acomoda_matriz(C, C12, N / 6, 5 * M / 6);
        // renglon 2
        acomoda_matriz(C, C13, N / 3, 0);
        acomoda_matriz(C, C14, N / 3, M / 6);
        acomoda_matriz(C, C15, N / 3, 1 * M / 3);
        acomoda_matriz(C, C16, N / 3, M / 2);
        acomoda_matriz(C, C17, N / 3, 2 * M / 3);
        acomoda_matriz(C, C18, N / 3, 5 * M / 6);
        // renglon 3
        acomoda_matriz(C, C19, N / 2, 0);
        acomoda_matriz(C, C20, N / 2, M / 6);
        acomoda_matriz(C, C21, N / 2, 1 * M / 3);
        acomoda_matriz(C, C22, N / 2, M / 2);
        acomoda_matriz(C, C23, N / 2, 2 * M / 3);
        acomoda_matriz(C, C24, N / 2, 5 * M / 6);
        // renglon 4
        acomoda_matriz(C, C25, 2 * N / 3, 0);
        acomoda_matriz(C, C26, 2 * N / 3, M / 6);
        acomoda_matriz(C, C27, 2 * N / 3, 1 * M / 3);
        acomoda_matriz(C, C28, 2 * N / 3, M / 2);
        acomoda_matriz(C, C29, 2 * N / 3, 2 * M / 3);
        acomoda_matriz(C, C30, 2 * N / 3, 5 * M / 6);
        // renglon 5
        acomoda_matriz(C, C31, 5 * N / 6, 0);
        acomoda_matriz(C, C32, 5 * N / 6, M / 6);
        acomoda_matriz(C, C33, 5 * N / 6, 1 * M / 3);
        acomoda_matriz(C, C34, 5 * N / 6, M / 2);
        acomoda_matriz(C, C35, 5 * N / 6, 2 * M / 3);
        acomoda_matriz(C, C36, 5 * N / 6, 5 * M / 6);

        // imprimir matriz
        if (N == 6 && M == 5) {
            System.out.println("Matriz A");
            impMatriz(A);
            System.out.println("Matriz B transpuesta");
            impMatriz(B);
            System.out.println("Matriz C");
            impMatriz(C);
        }
        // calcular y desplegar el checksum
        checksum(C);
    }
}