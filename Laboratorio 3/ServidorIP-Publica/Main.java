import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    int puerto = 1234;
    public static void main(String [] array)    
    {
        Main proxy = new Main();
        proxy.inicia();
    }
    
    public void inicia(){
        while(true){
           try {
			ServerSocket s = new ServerSocket(puerto);
				System.out.println("Esperando conexion del navegador...");
				Socket entrante = s.accept();
				ProcesoNavegador pNavegador= new ProcesoNavegador(entrante);
				pNavegador.run();
				s.close();
				} catch (Exception e) {
			System.out.println(e.getMessage());
			}
        }           
    }
}
