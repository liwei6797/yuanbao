using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace HaiLiDrvDemo
{
    public class SocketServer
    {  
        private static byte[] result = new byte[1024];  
        private static int myProt = 8888;   //端口  
        static Socket serverSocket;
        private static Form1 frm = null;
        public static void Start(Form1 frm1)
        {  
            //服务器IP地址  
            //IPAddress ip = IPAddress.Parse("192.168.1.230");
            serverSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);  
            serverSocket.Bind(new IPEndPoint(IPAddress.Any, myProt));  //绑定IP地址：端口  
            serverSocket.Listen(10);    //设定最多10个排队连接请求  
            Console.WriteLine("启动监听{0}成功", serverSocket.LocalEndPoint.ToString());  
            //通过Clientsoket发送数据  
            Thread myThread = new Thread(ListenClientConnect);  
            myThread.Start();
            //Console.ReadLine();  
            frm = frm1;
        }  
  
        /// <summary>  
        /// 监听客户端连接  
        /// </summary>  
        private static void ListenClientConnect()
        {  
            while (true)  
            {  
                Socket clientSocket = serverSocket.Accept();
                frm.SetClientSocket(clientSocket);
                //clientSocket.Send(Encoding.ASCII.GetBytes("Server Say Hello"));
                Thread receiveThread = new Thread(ReceiveMessage);  
                receiveThread.Start(clientSocket);  
            }  
        }  
  
        /// <summary>  
        /// 接收消息  
        /// </summary>  
        /// <param name="clientSocket"></param>  
        private static void ReceiveMessage(object clientSocket)
        {  
            Socket myClientSocket = (Socket)clientSocket;  
            while (true)  
            {  
                try  
                {  
                    //通过clientSocket接收数据  
                    int receiveNumber = myClientSocket.Receive(result);
                    Console.WriteLine("接收客户端{0}消息{1}", myClientSocket.RemoteEndPoint.ToString(), Encoding.ASCII.GetString(result, 0, receiveNumber));  
                }  
                catch(Exception ex)
                {  
                    Console.WriteLine(ex.Message);  
                    myClientSocket.Shutdown(SocketShutdown.Both);  
                    myClientSocket.Close();
                    break;
                }  
            }  
        }  
    }    
}
