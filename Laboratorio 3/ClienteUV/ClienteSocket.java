package cliente;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClienteSocket
{
   public static void main(String [] array) throws InterruptedException 
    {
        int puerto=9191;
        String ip="127.0.0.1";
        while(true){
        	 try
             {           
                 Socket cliente = new Socket(ip,puerto);
                 BufferedReader entCliente=new BufferedReader(new InputStreamReader(cliente.getInputStream()));              
                 String cadena= entCliente.readLine();
                 System.out.println(cadena);
                 String[] datos = cadena.split(",");
                 String get= datos[0];
                 get=get.substring(2);
                 String host= datos[1];
                 String port= datos[2];
                 port=port.replace(" ","");
                 
                 int puertohttp=Integer.parseInt(port);
                 PrintWriter salCliente = new PrintWriter(cliente.getOutputStream()); 
                 ClienteSocket proceso = new ClienteSocket();
                 String resul= proceso.ConectaCliente(get,host,puertohttp);
                 salCliente.println(resul);            
                 salCliente.close();
                 entCliente.close();
                
             }
             catch(Exception e)
             {
                 System.out.println(e.getMessage());
             }
        	 finally {
        		 Thread.sleep(1000);
        	 }
        }
       
        

    }
 
   public String ConectaCliente(String get,String host, int puerto) throws IOException{
	 
	  String hostname = host;
      int port = puerto;
      String resul="";
      Socket socket = null;
      PrintWriter writer = null;
      BufferedReader reader = null;

      try {
          socket = new Socket(hostname, port);
          
          writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
          writer.println(get);
          writer.println("Host: " + hostname);
          writer.println("Accept: */*");
          writer.println("User-Agent: Java"); // Be honest.
          writer.println(""); // Important, else the server will expect that there's more into the request.
          writer.flush();
          try{
       	 
          	reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
              for (String line; (line = reader.readLine()) != null;) {
            	  resul+=line+"\n";
              }
          }
          catch(IOException logOrIgnore){
          	 if (writer != null) { writer.close(); }
          }
          
      } finally {
          if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {} 
          if (writer != null) { writer.close(); }
          if (socket != null) try { socket.close(); } catch (IOException logOrIgnore) {} 
      }
		
      return resul;
	}
    

}