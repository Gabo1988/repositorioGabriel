import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente{
    public static void main(String[] args) {
      String servidor = "127.0.0.1";
      int puerto = 5000;
      try{
          Socket socket= null;
          BufferedReader in = null;
          PrintWriter out = null;
          System.out.println("Porfavor introduce una peticion para el servidor: ");
          Scanner inputReader = new Scanner(System.in);
          String comando = "";
          
          
          socket= new Socket (servidor,puerto);
          in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
          out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
              
          comando = inputReader.nextLine();
              
          out.println(comando);
          String line = "";
              
              
          while  ((line = in.readLine()) != null){
              System.out.println(line);
              break;
          }
          socket.close();
          
          
          /*do{
              socket= new Socket (servidor,puerto);
              in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
              out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
              
              comando = inputReader.nextLine();

              out.println(comando);
              String line = "";
        
          
              while  ((line = in.readLine()) != null){
                  System.out.println(line);
                  break;
              }
              socket.close();
          }while(comando!="-1");
          */
        
        }catch (IOException e){
       		System.out.println("Error en conexión!!!");
        }
      
    }
}



