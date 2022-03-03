package Apartado1;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
*@author Mario Bello García
*@version  4.1.Modifica el ejercicio 1 de la unidad 3 para el servidor permita trabajar de forma concurrente con varios clientes.
*@fecha 26/02/2022
*/
class Cliente {

    static final String HOST = "localhost";
    static final int Puerto = 5000;

    public Cliente() throws IOException {
        Scanner teclado=new Scanner(System.in);
        String datos = new String();
       
      
            // Me conecto al puerto
            Socket sCliente = new Socket(HOST, Puerto);

            // Creo los flujos de entrada y salida
            DataInputStream entrada = new DataInputStream(sCliente.getInputStream());
            DataOutputStream salida = new DataOutputStream(sCliente.getOutputStream());

            // CUERPO DEL ALGORITMO
            while(true){
                System.out.println("Introduce un numero, ayudate de las pistas entregadas");
                int numeroSeleccionado = teclado.nextInt();
                salida.writeInt(numeroSeleccionado);//enviamos numero...
                String mensajeRecibido = entrada.readUTF(); //Leemos respuesta del servidor...
                System.out.println(mensajeRecibido);
                
                
                //sentencia condicional en caso de el mensaje recibido sea ok, termina el juego y muestra mensaje ganador...
                if(mensajeRecibido.endsWith("ok")){
                    System.out.println(
                            "+++++++++++++++++++++++++++++++++++++++++++++++++++++ \n"
                          + "+++ Enhorabuena el numero introducido es correcto +++ \n"
                          + "+++++++++++++++++++++++++++++++++++++++++++++++++++++");
                   break;
                }
        } 
    }

    public static void main(String[] arg){
        try {
            new Cliente();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
