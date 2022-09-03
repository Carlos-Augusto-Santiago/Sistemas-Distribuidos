/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorHTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laxelott
 */
public class TCPWorker extends Thread {

	Socket conexion;
	Boolean divide;
	int n, limiteInferior, limiteSuperior;

	public TCPWorker(Socket conexion) {
		this.conexion = conexion;
	}

	public void setData(int n, int limiteInferior, int limiteSuperior) {
		this.n = n;
		this.limiteInferior = limiteInferior;
		this.limiteSuperior = limiteSuperior;
	}

	@Override
	public void run() {
		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			PrintWriter salida = new PrintWriter(conexion.getOutputStream());
			salida.flush();

			salida.println(n + "," + limiteInferior + "," + limiteSuperior);
			salida.flush();
			String respuesta = entrada.readLine();

			divide = "DIVIDE".matches(respuesta);
		} catch (IOException ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void close() {
		try {
			conexion.close();
		} catch (IOException ex) {
			Logger.getLogger(TCPWorker.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public Boolean isDivide() {
		return this.divide;
	}
}
