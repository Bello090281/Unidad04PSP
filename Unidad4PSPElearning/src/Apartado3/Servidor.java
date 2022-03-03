package Apartado3;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mario Bello García
 * @version Actividad 4.3. A partir del ejercicio anterior crea un servidor que una vez iniciada sesión a través de un nombre de usuario y contraseña específico (por ejemplo javier / secreta) el sistema permita Ver el contenido del directorio actual, mostrar el contenido de un determinado archivo y salir.
            Para realizar el ejercicio primero debes crear un diagrama de estados que muestre el funcionamiento del servidor.
 * @fecha 26/02/2022
 */
public class Servidor extends Thread {

    Socket skCliente;
    static final int Puerto = 1500;
    static int numero_Cliente = 0;
    String comando;
    File archivo = null;

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
            entrada = new DataInputStream(skCliente.getInputStream());
            DataOutputStream salida = new DataOutputStream(skCliente.getOutputStream());
            int estado = 1;
            do {
                switch (estado) {

                    case 1:
                        salida.writeUTF("Introduce comando (ls/get/exit)");
                        comando = entrada.readUTF();

                        if (comando.equals("ls")) {
                           estado=2;
                        } else if (comando.equals("get")) {
                            // Voy al estado 3 para mostrar el fichero
                            estado = 3;
                            
                        } else if(comando.equals("exit")){
                            //Salgo de bucle y cierro la conexion con el cliente
                            estado = 0;
                        }else{
                            //En caso de no introducir un comando correcto vuelvo al menu principal
                        estado=1;
                        }
                        break;
                    case 2:
                        salida.writeUTF("Introduce directorio a ver");
                            String directorio = entrada.readUTF();
                            // Muestro el directorio
                            System.out.println("\tEl cliente quiere ver el contenido del directorio " + directorio);
                            // Muestro el directorio

                            File f = new File(directorio);
                            File[] ficheros = f.listFiles();
                            try {
                                salida.writeUTF(String.valueOf(ficheros.length));//Numero de elementos
                            } catch (IOException ex) {
                                System.out.println(ex.getMessage());
                            }
                            for (int x = 0; x < ficheros.length; x++) {
                                try {
                                    salida.writeUTF(ficheros[x].getName());
                                } catch (IOException ex) {
                                    System.out.println(ex.getMessage());
                                }
                            }

                            estado = 1;
                            break;
                       
                      
                    case 3://voy a mostrar el archivo
                        String nombreArchivo = entrada.readUTF();

                        if (checar(nombreArchivo) == true) {

                            salida.writeBoolean(true);
                            salida.writeUTF("SI existe el archivo:" + nombreArchivo + " en el servidor");
                            System.out.println("El cliente quiere ver el archivo " + nombreArchivo);

                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(nombreArchivo));
                            int leer;

                            while ((leer = bis.read()) != -1) {

                                salida.writeByte(leer);//Mandamos el byte del archivo

                            }
                            System.out.println("Los bytes se han mandado al cliente correctamente");
                            bis.close();

                            salida.writeBoolean(true);//TENGO QUE ESCRIBIR ESTA LÍNEA PARA QUE FUNCIONE EL PROGRAMA
                            System.out.println("Cliente cerrado");
                            skCliente.close();
                            break;

                        }
                            //En caso de no existir el archivo vuelvo al menu principal
                        if (checar(nombreArchivo) == false) {
                            salida.writeBoolean(false);
                            salida.writeUTF("NO existe el archivo:" + nombreArchivo + " en el servidor");
                            System.out.println("Se envio respuesta al cliente");
                            estado=1;
                            break;
                        }
                    
                        

                }
            } while (estado!=0);
            //Salimos y dejamos marcados la opcion elegida por el cliente en el servidor
 System.out.println("Cliente cerrado");
                        skCliente.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
//Comprobamos si el archivo exixte o no
    boolean checar(String nombre) {
        archivo = new File(nombre);
        if (archivo.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
