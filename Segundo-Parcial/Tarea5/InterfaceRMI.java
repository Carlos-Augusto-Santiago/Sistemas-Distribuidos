import java.rmi.*;

public interface InterfaceRMI extends Remote {
    public double[][] multiplica_matrices(double[][] A, double[][] B, int N, int M) throws RemoteException;
}