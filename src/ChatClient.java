import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient
{
	Socket sock;
	PrintWriter writer;
	BufferedReader reader;
	JTextArea text;
	JTextField field;
	
	public static void main(String args[])
	{
		ChatClient chat = new ChatClient();
		chat.go();
	}

	
	public void go()
	{
		JFrame frame=new JFrame("Chat Application");
		JPanel panel=new JPanel();
		text=new JTextArea(15,50);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setEditable(false);
		JScrollPane scroller=new JScrollPane(text);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS) ;
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		field=new JTextField(20);
		JButton button=new JButton("Send");
		button.addActionListener(new ButtonListener());
		panel.add(scroller);
		panel.add(field);
		panel.add(button);
		setNetworking();
		
		Thread thread=new Thread(new Reader());
		thread.start();

		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.setSize(400,500);
		frame.setVisible(true);
	}

	private void setNetworking()
	{
		try
		{
			sock=new Socket("127.0.0.1", 5000);
			InputStreamReader streamReader=new InputStreamReader(sock.getInputStream());
			reader=new BufferedReader(streamReader);
			writer=new PrintWriter(sock.getOutputStream());	
			System.out.println("Request Send");					
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	
	public class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ev){
		try{
			writer.println(field.getText());
			writer.flush();
		}catch(Exception ex){
			ex.printStackTrace();
		}

		field.setText(" ");
		field.requestFocus();
		}		
		
	}
	
	public class Reader implements Runnable
	{
		public void run()
		{
			String message;
			try
			{
				while((message=reader.readLine()) != null)
				{
					System.out.println("read" + message);
					text.append(message + "\n");
				}			
			}catch(Exception ex){
				ex.printStackTrace();
			}			
		}
	}

}