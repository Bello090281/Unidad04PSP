    package Apartado3;

import static Apartado2.cliente.fichero;
import static Apartado2.cliente.tamFichero;
import java.awt.Desktop;
import java.io.*;
import static java.lang.ProcessHandle.current;
import java.net.*;
import java.util.Date;
import java.util.Scanner;
import static java.util.concurrent.ThreadLocalRandom.current;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mario Bello García
 * @version Actividad 4.3. A partir del ejercicio anterior crea un servidor que una vez iniciada sesión a través de un nombre de usuario y contraseña específico (por ejemplo javier / secreta) el sistema permita Ver el contenido del directorio actual, mostrar el contenido de un determinado archivo y salir.
            Para realizar el ejercicio primero debes crear un diagrama de estados que muestre el funcionamiento del servidor.
 * @fecha 26/02/2022
 */
class Cliente {

    static final String HOST = "localhost";
    static final int Puerto = 1500;
    Scanner teclado = new Scanner(System.in);

    public Cliente() throws IOException {

        String datos = new String();

        // Me conecto al puerto
        Socket sCliente = new Socket(HOST, Puerto);

        // Creo los flujos de entrada y salida
        DataInputStream entrada = new DataInputStream(sCliente.getInputStream());
        DataOutputStream salida = new DataOutputStream(sCliente.getOutputStream());

        // CUERPO DEL ALGORITMO
        boolean correcto = false;
        do {
//Creamos una sesion de inicio con un usuario y una contraseña(Mario/bello) en caso de introducirlo mal se solicita de nuevo
            System.out.println("Escribe el nombre del usuario");
            String nombre = teclado.nextLine();

            if (nombre.equals("Mario")) {
                System.out.println("Escribe la contraseña del usuario");
                String contraseña = teclado.nextLine();
                if (contraseña.equals("bello")) {
                    correcto = true;
                    System.out.println("Bienvenido a una nueva sesion " + nombre);
                } else {
                    System.out.println("La contraseña no es correcta");
                }

            } else {
                correcto = false;
                System.out.println("El nombre no es correcto");
            }

        } while (correcto != true);

        while (true) {
            //Menu principal
            System.out.println("Introduzca uno de los siguienes comando ls, get, exit\n"
                    + " ls.............ver contenido del directorio.\n"
                    + " get............mostrar el contenido de un archivo.\n"
                    + " exit...........Salir.");

            String mensajeRecibido = entrada.readUTF(); //Leemos respuesta del servidor...
            System.out.println(mensajeRecibido);
            String comando = teclado.nextLine();
            salida.writeUTF(comando);
 
            /**
             * Comando ls
             */
            if (comando.equals("ls")) {
                 String mensaje = entrada.readUTF();
                 System.out.println(mensaje);
                 String introducirdirectorio = teclado.nextLine();
                 salida.writeUTF(introducirdirectorio);
                int numeroFicheros = Integer.parseInt(entrada.readUTF());

                for (int i = 0; i < numeroFicheros; i++) {
                    System.out.println(entrada.readUTF());
                }

            }
            /**
             * Comando get
             */
            else if (comando.equals("get")) {
                String servidor;
                System.out.println("Teclee el nombre del archivo:");
                String archivo = teclado.nextLine();
                salida.writeUTF(archivo);
                boolean respuesta = entrada.readBoolean();

                if (respuesta == true) {
                    servidor = entrada.readUTF();
                    System.out.println(servidor);

                    int tamaño = 5000;
                    byte[] miArray = new byte[tamFichero];
                    InputStream is = sCliente.getInputStream();
                    FileOutputStream fos = new FileOutputStream(fichero);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int bytesRead = is.read(miArray, 0, miArray.length);
                    int current = bytesRead;
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
                    break;
                } else {
                    servidor = entrada.readUTF();
                    System.out.println(servidor);
                }
            }
            else if (comando.equals("exit")) {
                 
            }
            System.out.println("El comando no es correcto, introducelo de nuevo");
        }

    }

    /**
     * Comando exit
     */
    public static void main(String[] arg) {
        try {
            new Cliente();
        } catch (IOException ex) {

        }
    }
}


