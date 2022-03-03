package Apartado2;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.ProcessHandle.current;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * @author Mario Bello Garcia
 * @version 4.2.Modifica el ejercicio 2 de la unidad 3 para el servidor permita trabajar de forma concurrente con varios clientes.
 * @fecha 27/02/2022
 */
public class cliente extends Thread {

    //variables del puerto..
    public final static int puerto = 1500;
    //variables del host...
    public final static String servidor = "localhost";
    //variable del tamaño del fichero maximo...
    public final static int tamFichero = 50000;
    //variable del archivo que se creara con los bytes recibidos desde el sservidor...
    public final static String fichero = "C:\\Users\\BELLO\\Desktop\\copia.txt";

    public static void main(String[] args) throws IOException {

        Socket socket = null;
        boolean existeFichero;
        int current = 0;

        socket = new Socket(servidor, puerto);
        System.out.println("Conectando con el servidor...");

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        Scanner sca = new Scanner(System.in);

        do {

            System.out.println("Escribe el nombre del fichero");
            String nombreFichero = sca.nextLine();
            dos.writeUTF(nombreFichero);//MANDAMOS EL NOMBRE DEL FICHERO
            existeFichero = dis.readBoolean();

            if (!existeFichero) {
                System.out.println("EL FICHERO NO EXISTE, ESCRIBA DE NUEVO");
            }

        } while (existeFichero == false);

        //Array con los bytes que recibimos.
        byte[] miArray = new byte[tamFichero];
        InputStream is = socket.getInputStream();
        FileOutputStream fos = new FileOutputStream(fichero);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int bytesRead = is.read(miArray, 0, miArray.length);
        current = bytesRead;
        do {
            bytesRead
                    = is.read(miArray, current, (miArray.length - current));
            if (bytesRead >= 0) {
                current += bytesRead;
            }

        } while (bytesRead > -1);

        bos.write(miArray, 0, current);
        bos.flush();
       
        BufferedReader br = new BufferedReader(new FileReader(fichero));
        //Muestro el archivo por pantalla
        String linea;
        System.out.println("\n"
                + "El archivo descargado contiene:\n"
                + "");
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
        //Mensaje con el nombre del archivo copiado y su tamaño
        System.out.println("\n"
                + "Archivo descargado desde el serevidor: " + fichero + " con " + current + " bytes read.");
    }
}
