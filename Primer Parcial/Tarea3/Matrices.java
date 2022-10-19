import java.util.Arrays;

class Matrices {

    static int N = 12;
    static float[][] A = new float[N][N];
    static float[][] B = new float[N][N];
    static float[][] C = new float[N][N];

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();

        // variable para la copia de la matriz B
        float[][] B_copy = new float[N][N];

        // arreglos de A1-A4
        float[][] A1 = new float[N][N];
        float[][] A2 = new float[N][N];
        float[][] A3 = new float[N][N];
        float[][] A4 = new float[N][N];

        // inicializa las matrices A y B

        for (int i = 0; i < N; i++) {

            // almacenar los valores en las otras matrices
            if (i == N / 4) {
                A1 = A.clone();
                System.out.println("");
                // mostrar valores del arreglo A1
                int x = N / 4;
                System.out.println("A");
                for (int y = 0; y < x; y++) {
                    for (int j = 0; j < N; j++) {
                        System.out.print(A1[y][j] + "   ");
                    }
                    System.out.println("");
                }
                System.out.println("");
            } else if (i == N / 2) {
                // mostrar valores del arreglo A1
                int x = N / 2;
                System.out.println("A2");
                for (int y = N / 4; y < x; y++) {
                    for (int j = 0; j < N; j++) {
                        A2[y][j] = A[y][j];
                        System.out.print(A2[y][j] + "   ");
                    }
                    System.out.println("");
                }
                System.out.println("");
            } else if (i == 3 * N / 4) {
                // mostrar valores del arreglo A1
                int x = 3 * N / 4;
                System.out.println("A3");
                for (int y = N / 2; y < x; y++) {
                    for (int j = 0; j < N; j++) {
                        A3[y][j] = A[y][j];
                        System.out.print(A3[y][j] + "   ");
                    }
                    System.out.println("");
                }
                System.out.println("");
            }
            System.out.println("Normal");
            for (int j = 0; j < N; j++) {
                A[i][j] = i + 3 * j;
                System.out.print(A[i][j] + "   ");
                B[i][j] = 2 * i - j;
                C[i][j] = 0;
            }
            System.out.println("");
            if (i == N - 1) {
                // mostrar valores del arreglo A1
                int x = N;
                System.out.println("A4");
                for (int y = 3 * N / 4; y < x; y++) {
                    for (int j = 0; j < N; j++) {
                        A4[y][j] = A[y][j];
                        System.out.print(A4[y][j] + "   ");
                    }
                    System.out.println("");
                }
                System.out.println("");
            }
        }
        // traspone la matriz B, la matriz traspuesta queda en B y copia de la matriz B
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                float x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }

        // multiplica la matriz A y la matriz B, el resultado queda en la matriz C
        // notar que los indices de la matriz B se han intercambiado

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                for (int k = 0; k < N; k++)
                    C[i][j] = A[i][k] * B[j][k];

        long t2 = System.currentTimeMillis();
        System.out.println("Tiempo: " + (t2 - t1) + "ms");
    }
}
