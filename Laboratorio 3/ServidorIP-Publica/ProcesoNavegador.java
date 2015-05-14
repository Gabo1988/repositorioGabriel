import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ProcesoNavegador {

    private Socket socketNavegador;
    
    ProcesoNavegador(Socket sn)
    {
        socketNavegador = sn;
    }
    public void run(){
        System.out.println("Inicia SocketNavegador");
        try {
            BufferedReader entradaNavegador = new BufferedReader (new InputStreamReader(socketNavegador.getInputStream()));
            PrintWriter salCliente = new PrintWriter(new OutputStreamWriter(socketNavegador.getOutputStream(),"8859_1"),true);
            String cadena;
            String get="GET";
            String host="Host";
            int puerto=80;
            boolean bandera=false;
            do          
            {           
                cadena = entradaNavegador.readLine();
                if(cadena != null && cadena.length() != 0){
                    
                    if(cadena.contains(get)){
                        get = cadena;
                    }
                    else if(cadena.contains(host)){
                    cadena=cadena.replace(" ", "");
                    String[] array=cadena.split(":");
                    if(array.length==3){
                        host=array[1];
                        puerto=Integer.parseInt(array[2]);
                    }
                    else{
                        host=array[1];
                    }
                    bandera=true;
                    break;
                    }
                }                   
            }
            while (cadena != null && cadena.length() != 0);
            if(bandera){
                System.out.println("{{"+ get+" }}"+"{{"+ host+" }}"+ "{{"+ puerto+"}}");
                String respuesta =ConectaUV( get, host,  puerto);
                //String respuesta ="<H1>Hola Mundo</H1>";
                salCliente.println(respuesta);
            }
            
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        try {
            socketNavegador.close();
            System.out.println("Finaliza SocketNavegador");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    String ConectaUV(String get,String hostname, int port) throws   IOException{
        int puerto=9191;
        String respuestaCliente="";
        BufferedReader reader = null;
        ServerSocket serCliente = null;
        Socket sokCliente  = null;
        try 
        {
            serCliente = new ServerSocket(puerto);
            System.out.println("Esperando conexion cliente...");
            sokCliente = serCliente.accept();
            
            DataOutputStream envioCliente= new DataOutputStream(sokCliente.getOutputStream());
            
            envioCliente.writeUTF(get+","+hostname+","+port+" \n\r");
            
            BufferedReader entradaCliente = new BufferedReader (new InputStreamReader(sokCliente.getInputStream()));
            
            for (String line; (line = entradaCliente.readLine()) != null;) {
                
            	  respuestaCliente+=line+"\n";
              }
          envioCliente.close();
        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
        finally {
          if (serCliente != null) try { serCliente.close(); } catch (IOException logOrIgnore) {} 
          if (reader != null) { reader.close(); }
          if (sokCliente != null) try { sokCliente.close(); } catch (IOException logOrIgnore) {} 
      }
        
        return respuestaCliente;
    }
    
   
}