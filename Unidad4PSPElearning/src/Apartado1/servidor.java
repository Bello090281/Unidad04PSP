package Apartado1;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
*@autor Mario Bello García
*@version 4.1.Modifica el ejercicio 1 de la unidad 3 para el servidor permita trabajar de forma concurrente con varios clientes.
*@fecha 26/02/2022
*/
class Servidor extends Thread {

    Socket skCliente;
    static final int Puerto = 5000;
    DataOutputStream salida;
    DataInputStream entrada;
    String mensajeRecibido;
    static int numero_Cliente = 0;
    //mensajes para enviar...
    String mensaje1 = "El numero introducido es menor (<) que el numero oculto";
    String mensaje2 = "El numero introducido es mayor (>) que el numero oculto";
    String mensaje3 = "ok";

    public Servidor(Socket sCliente) {
        skCliente = sCliente;
    }

    public static void main(String[] arg) {
        try {
            // Inicio el servidor en el puerto
            ServerSocket skServidor = new ServerSocket(Puerto);
            System.out.println("Escucho el puerto " + Puerto);

            while (true) {
                // Se conecta un cliente
                Socket skCliente = skServidor.accept();
                numero_Cliente++;
                System.out.println("____________________________________________ Nuevo cliente conectado " + numero_Cliente + ".");

                // Atiendo al cliente mediante un thread
                new Servidor(skCliente).start();
            }
        } catch (Exception e) {;
        }
    }

    public void run() {

        DataInputStream entrada = null;
        try {
            // Creo los flujos de entrada y salida
            entrada = new DataInputStream(skCliente.getInputStream());
            DataOutputStream salida = new DataOutputStream(skCliente.getOutputStream());
            //numero aleatrio de 0a 100 para averiguar...
            int numeroOculto = (int) (Math.random() * 100 + 1);
            System.out.println("El numero oculto es " + numeroOculto);
            while (true) {

                int mensajeRecibido = entrada.readInt();//Leemos numero del cliente...
                System.out.println("El numero introducido es el " + mensajeRecibido);

                //sentencias if, para indicar si el numero es mayor o menor...
                if (numeroOculto > mensajeRecibido) {
                   salida.writeUTF("" + mensaje1);//enviamos mensaje...
                   
                } else if (numeroOculto < mensajeRecibido) {

                    salida.writeUTF("" + mensaje2);//enviamos mensaje...

                } else {

                    salida.writeUTF(mensaje3);//enviamos mensaje...
                break;
                
                }
        

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
