package Apartado2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * 
 * @author Mario Bello Garcia
 * @version 4.2.Modifica el ejercicio 2 de la unidad 3 para el servidor permita trabajar de forma concurrente con varios clientes.
 * @fecha 27/02/2022
 */
class Servidor extends Thread {

    private static int Puerto = 1500;
    public static String fichero = "mario.txt";
    Socket skCliente;
    private static int numero_Cliente = 0;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean existeFichero;

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
             try {
           
                 DataInputStream dis = new DataInputStream(skCliente.getInputStream());
                 DataOutputStream dos = new DataOutputStream(skCliente.getOutputStream());
 
            do {
 
                fichero = dis.readUTF();//RECIBIMOS LA RUTA DEL FICHERO
                File archivo = new File(fichero);
 
                if(archivo.exists()) {
 
                    dos.writeBoolean(true);//MANDAMOS QUE EXISTE EL FICHERO
                    existeFichero = true;
                }else {
                    dos.writeBoolean(false);
                    existeFichero = false;
                }
 
 
 
            }while(existeFichero == false);
 
 
            //ahora deberemos de ir mandando los bytes del archivo
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fichero));
            int leer;
 
            while((leer = bis.read()) != -1) {
 
             
                dos.writeByte(leer);//Mandamos el byte del archivo
 
            }
            System.out.println("Los bytes se han mandado al cliente correctamente");
            bis.close();
 
            dos.writeBoolean(true);//TENGO QUE ESCRIBIR ESTA LÍNEA PARA QUE FUNCIONE EL PROGRAMA
                 System.out.println("Cliente cerrado");
            skCliente.close();
 
 
 
 
        } catch (IOException ex) {
 
            System.out.println(ex.getMessage());
        }
 
    }
}

