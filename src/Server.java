import java.io.*;
import java.net.*;
import java.util.*;

public class Server
{
	ArrayList<PrintWriter> acceptclient;
	
	public class ClientAccept implements Runnable
	{
		BufferedReader reader;
		Socket sock;
		
		public ClientAccept(Socket clientSocket)
		{
			try
			{
				sock=clientSocket;
				InputStreamReader read=new InputStreamReader(sock.getInputStream());
				reader=new BufferedReader(read);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}

		public void run()
		{
			String message;
			try
			{
				while((message = reader.readLine())!= null)
				{
					System.out.println("read" + message);
					toEveryone(message);
				}	
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}


		
	}

	public static void main(String args[])
	{
		new Server().go();
	}

	public void go()
	{
		acceptclient=new ArrayList<PrintWriter>();
		try
		{
			ServerSocket socket=new ServerSocket(5000);
			
			while(true)
			{
				Socket clientSock=socket.accept();
				PrintWriter writer=new PrintWriter(clientSock.getOutputStream());
				acceptclient.add(writer);

				Thread t=new Thread(new ClientAccept(clientSock));
				t.start();
				System.out.print("Connection Established");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void toEveryone(String message)
	{
		Iterator<PrintWriter> it = acceptclient.iterator();
		while(it.hasNext())
		{
			try
			{
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

}