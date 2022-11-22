import java.rmi.Naming;
import java.rmi.registry.Registry;

public class ServidorRMI {
    public static void main(String[] args) throws Exception {
        Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099);
        String url = "rmi://localhost/prueba";
        // System.out.println(url);
        ClaseRMI obj = new ClaseRMI();
        r.rebind(url, obj);
    }
}