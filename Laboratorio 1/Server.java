import java.net.*;
import java.util.*;
import java.io.*;

public class Server{

	public static void main(String args[]){
		try {
            Server server= new Server();
            server.principal();

		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
    
    public void principal(){
        try{
        
            int puerto = 5000;
            
            ServerSocket s = new ServerSocket(puerto);
            
            String comandoSalir = "Exit";
            String entrada = "";
            System.out.println("Servidor iniciado en el puerto " + puerto + "...");
            
            while(true){
                Socket s1 = s.accept();
                System.out.println("Aceptando conexion...");
                PrintWriter out = new PrintWriter(s1.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s1.getInputStream()));
                
                while ((entrada = in.readLine()) != null) {
                    //System.out.println(entrada);
                    //String pathDesktop = System.getProperty("user.home") + "\\Desktop\\";
                    
                    switch(entrada){
                        case "GET NombreUsuario":
                            out.println(nombreUsuario());
                            break;
                        case "GET NombreMaquina":
                            out.println(nombreMaquina());
                            break;
                        case "GET DireccionMac":
                            out.println("DireccionMac "+direccionMac());
                            break;
                        case "GET FechaServidor":
                            out.println("Fecha Y hora: " + new Date());
                            break;
                        case "GET UnidadesDisco":
                            out.println("Unidades "+unidades());
                            break;
                            
                        default:
                            if (entrada.trim().equals(comandoSalir)){
                                return;
                            }else{
                                out.println("AYUDA: Se puede consultar datos como NombreUsuario, NombreMaquina, DireccionMac, FechaServidor, UnidadesDisco");
                            }
                            
                            
                            break;
                    }
                    
                    
                    
                    
                } 
                s1.close();
                
            }
            
        }catch(Throwable e){
            e.printStackTrace();
        }
        
    }
    
    public String nombreUsuario(){
        String nombreUsuario = "";
        try{
            nombreUsuario = System.getProperty("user.name");
        }catch(Throwable e){
            e.printStackTrace();
        }
        return "Nombre Usuario "+nombreUsuario;
    }
    
    public String nombreMaquina()throws UnknownHostException, SocketException, NumberFormatException{
        String nombreMaquina = "";
        InetAddress Address = InetAddress.getLocalHost();
        try{
            nombreMaquina = Address.toString();
        }catch(Throwable e){
            e.printStackTrace();
        }
        return "Nombre Maquina "+nombreMaquina;
    }
    
    public StringBuilder direccionMac()throws UnknownHostException, SocketException, NumberFormatException{
        StringBuilder dirMac= new StringBuilder();
        try{
            NetworkInterface a = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            //Obtenemos su MAC Address, pero nos devuelve un array de bytes
            //Por lo que hay que convertirlos a Hexadecimal
            byte[] b = a.getHardwareAddress();
            String[] macAddres = new String[6];
            for (int i = 0; i < b.length; i++) {
                //Tratamos los valores que devuelven < 0 normalmente son el "3 y 5 par"
                if (b[i] < 0) {
                    //Convertimos el byte a Hexadecimal con la clase Integer
                    String tmp = Integer.toHexString(b[i]);
                    //Los numeros que son menores a cero al momento de convertirlo a string nos devuelven una cadena de este tipo ffffffAA por lo que unicamente tomamos los ultimos 2 caracteres que son lo que buscamos. y obtenemos esos ultimos caracteres con substring
                    macAddres [i]= (tmp.substring(tmp.length() - 2).toUpperCase());
                    continue;
                }else{
                    String aux = Integer.toHexString(b[i]);
                    if(aux.length() < 2){
                        macAddres [i]= ("0"+Integer.toHexString(b[i]));
                    }else{
                        macAddres [i]= (Integer.toHexString(b[i]));
                    }
                }
            }
            
            for(int x = 0;x < macAddres.length ;x++){
                
                if(x<macAddres.length-1){
                    dirMac.append(macAddres [x] + "-");
                }else{
                    dirMac.append(macAddres [x]);
                }
            }
            
        }catch(Throwable e){
            e.printStackTrace();
        }
        return dirMac;
    }

    public StringBuilder unidades()throws UnknownHostException, SocketException, NumberFormatException{
        StringBuilder unidades= new StringBuilder();
        
        try{
            File[] roots = File.listRoots();
            for (int i=0; i<roots.length; i++){                
                unidades.append(roots[i]);
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
        return unidades;
    }
    
}
