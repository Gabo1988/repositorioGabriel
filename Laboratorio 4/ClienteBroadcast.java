package broadcast;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteBroadcast {
    
    public static void main(String [] array)    
    {
        try {
            ClienteBroadcast instancia = new ClienteBroadcast();
            instancia.iniciarConexion();
            instancia.inicia();
        } catch (IOException ex) {
            Logger.getLogger(ClienteBroadcast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void inicia(){
        while(true){
            try {
                int puerto = 1235;
                ServerSocket socketPrincipal = new ServerSocket(puerto);
                System.out.println("Esperando escaner conexion del servidor...");

                Socket socketEntrante = socketPrincipal.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(socketEntrante.getInputStream()));
                PrintWriter out = new PrintWriter(socketEntrante.getOutputStream(), true);
                
                String entrada = "";
                String comandoSalir = "Exit";

                while ((entrada = in.readLine()) != null) {
                  
                    switch(entrada){
                           
                        case "EscanearPuertos":
                            
                            out.println(obtenerProcesos());
                            break;
                                
                        default:
                            if (entrada.trim().equals(comandoSalir)){
                                return;
                            }
                            break;
                    }
                                  
                    
                }
                socketEntrante.close();
                socketPrincipal.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }           
    }
    
    public List<String> obtenerProcesos(){
        System.out.println("ObtenerPRocesos");
        List<String> proceso = new ArrayList<String>();
        
        EscanearProcesos escanearProcesos = new EscanearProcesos();
        proceso = escanearProcesos.listRunningProcesses();
        String result = "";
        // display the result 
        Iterator<String> it = proceso.iterator();
        int i = 0;
        while (it.hasNext()) {
            result += it.next() +"\n,";
            i++;
            if (i==10) {
                result += "\n";
                i = 0;
            }
        }
        //System.out.print(result);
        return proceso;
    }
    
    private String obtenerNombreMaquina(){
       
        InetAddress address=null;
        try {
            address = InetAddress.getLocalHost();
        } catch (Throwable ex) {
            //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        //System.out.println("Nombre de la Maquina: " + address);
        return "" + address;
    }
    
    private void iniciarConexion() throws IOException{
        System.out.println("Esperando conexion Servidor...");
        int puerto=1234;
        
        String ip="192.168.0.12";
        
        Socket cliente = null;
        outer:
        while(true){
            try{       
                
                cliente = new Socket(ip,puerto);
                BufferedReader entCliente=new BufferedReader(new InputStreamReader(cliente.getInputStream()));              
                String cadena= entCliente.readLine();
                System.out.println("Entrada: "+cadena);
                
                System.out.println("entra cadena");

                PrintWriter salCliente = new PrintWriter(cliente.getOutputStream()); 
                salCliente.println(obtenerNombreMaquina());            
                salCliente.close();
                
                entCliente.close();
                cliente.close();
                System.out.println("ANtes del outer");
                break outer;
             }
             catch(Exception e){
                 System.out.println(e.getMessage());
             }
        }
        
    }

}
