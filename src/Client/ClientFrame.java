package Client;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;





public class ClientFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	Socket serverSocket;
    BufferedReader br;
    PrintWriter pw;
	JTextField input_username;
	JTextField input_ipaddress;
	JTextField input_port;
	
	JLabel iplabel;
	JLabel portlabel;
	JLabel usernamelabel;
	
	String uname = "";

	JScrollPane contentscroll;
	JScrollPane userscroll;
	JTextField input;
	JButton send;
	JButton disconnect;
	JButton connect;
	JButton whisper;

	JTextArea chatWindow;
	JTextArea userWindow;
	
	static boolean isconnected = false;
	
	ClientFrame(){
		initComponents();
	}
	
	private void initComponents(){
		
		 setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		 setResizable(false);
		 
		 iplabel = new JLabel();
		 portlabel = new JLabel();
		 usernamelabel = new JLabel();
		 
		 iplabel.setText("ip");
		 portlabel.setText("port");
		 usernamelabel.setText("username");
		 
		 contentscroll = new JScrollPane();
		 userscroll = new JScrollPane();
		 
		 input_username = new JTextField();
		 input_ipaddress = new JTextField();
		 input_port = new JTextField();
		 input = new JTextField();
		 
		 connect = new JButton();
		 connect.setText("connect");
		 disconnect = new JButton();
		 disconnect.setText("disconnect");
		 send = new JButton();
		 send.setText("send");
		 whisper = new JButton();
		 whisper.setText("whisper");
		 
		 chatWindow = new JTextArea();
		 userWindow = new JTextArea();
		 	 
		 chatWindow.setColumns(40);
	     chatWindow.setRows(10);
	     chatWindow.setEditable(false);
	     contentscroll.setViewportView(chatWindow);
	     userWindow.setColumns(10);
	     userWindow.setRows(5);
	     userWindow.setEditable(false);
	     userscroll.setViewportView(userWindow);
	     input.setEditable(false);
		 
	     disconnect.addActionListener(this);
	     connect.addActionListener(this);
	     send.addActionListener(this);
	     whisper.addActionListener(this);
	     
	     setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	     setTitle("Client");
	     setResizable(false);
	     	     
	     GroupLayout layout = new GroupLayout(getContentPane());
	     getContentPane().setLayout(layout);
	     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                .addGroup(layout.createSequentialGroup()
	                        .addContainerGap()
	                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                                .addGroup(layout.createSequentialGroup()
	                                        .addContainerGap()
	                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
	                                                .addComponent(usernamelabel)         
	                                                .addComponent(input_username, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
	                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
	                                                .addComponent(iplabel)
	                                                .addComponent(input_ipaddress, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
	                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
	                                                .addComponent(portlabel)
	                                                .addComponent(input_port, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
	                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
	                                        		.addComponent(connect)
	                                        		.addComponent(disconnect))
	                                        .addContainerGap())
	                                .addGroup(layout.createSequentialGroup()
	                                    .addComponent(userscroll)
	                                    .addComponent(contentscroll))
	                                .addGroup(layout.createSequentialGroup()
	                                        .addComponent(input)
	                                        .addComponent(send)
	                                        .addComponent(whisper)))
	                        .addContainerGap())
	        );
	        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                .addGroup(layout.createSequentialGroup()
	                        .addContainerGap()
	                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                        		.addComponent(usernamelabel)
	                        		.addComponent(iplabel)
	                        		.addComponent(portlabel)
	                        		.addComponent(connect))
	                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
	                        		.addComponent(input_username)
	                        		.addComponent(input_ipaddress)
	                        		.addComponent(input_port)
	                        		.addComponent(disconnect)) 
	                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                        	.addComponent(userscroll, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
	                        	.addComponent(contentscroll, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE))
	                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                                .addComponent(input)
	                                .addComponent(send)
	                                .addComponent(whisper))
	                        .addContainerGap())
	       );
		 pack();
	 }
	 
	 public void actionPerformed(ActionEvent ae){
		 if (ae.getSource() == send) {
			 if ((input.getText()).equals("")) {
				 JOptionPane.showMessageDialog(this, "you cannot send empty message");
			 } else {
				 try{
					
					 String message = input.getText();
					 pw.println(uname + ":" + message + ":" + "broadcast");
					 pw.flush();
				 } catch (Exception e) {
					 chatWindow.append("send message unsuccessfully.\n");
				 }
				 input.setText("");
		         input.requestFocus();
			 }
		 } else if (ae.getSource() == connect) {
			 if(isconnected == true){
				 JOptionPane.showMessageDialog(this, "you have connected the server");
			 } else {
				 uname = input_username.getText();
				 if(uname.isEmpty()){
					 JOptionPane.showMessageDialog(this, "you can not use empty name");
				 } else {
					 String current_ip = input_ipaddress.getText();
					 String current_port = input_port.getText();
					 
					 if(current_ip.isEmpty()){
						 JOptionPane.showMessageDialog(this, "you can not use empty ipaddress");
					 } else if(current_port.isEmpty()){
						 JOptionPane.showMessageDialog(this, "you can not use empty port");
					 } else {
						 int port = Integer.valueOf(current_port);
						 
						 try{
							 serverSocket = new Socket(current_ip,port);
							 InputStreamReader is = new InputStreamReader(serverSocket.getInputStream());
							 br = new BufferedReader(is);
							 pw = new PrintWriter(serverSocket.getOutputStream());
							 pw.println(uname + ": :connect");
							 pw.flush();
							 isconnected = true;
							 input_username.setEditable(false);
					         input.setEditable(true);
						 } catch (Exception ex){
							 input_username.setEditable(true);
							 JOptionPane.showMessageDialog(this, "fail to connect server");
						 }
						 Thread clientHandler = new Thread(new ClientThread());
				         clientHandler.start();
					 }
				 }
			 }
			 
		 } else if (ae.getSource() == disconnect) {
			 
			 disconnect();
			 
		 } else if (ae.getSource() == whisper) {
			 try{
				 String message = input.getText();
				 String[] arraymessage = message.split(" ", 2);
				 if(arraymessage.length <= 1){
					 JOptionPane.showMessageDialog(this, "you must input as 'user  content'");
				 } else {
					 if(arraymessage[0].equals("")){
						 JOptionPane.showMessageDialog(this, "you must input as 'user  content'");
					 } else{
				
					 pw.println(uname + ":" + arraymessage[1] + ":" + "whisper:" + arraymessage[0]);
					 pw.flush();
					 }
				 }
			 } catch (Exception e) {
				 chatWindow.append("send message unsuccessfully.\n");
			 }
			 input.setText("");
	         input.requestFocus();
			 
		 }
	 }
	 public void disconnect()
	 {
		 try {
	            pw.println(uname + ": :disconnect");
	            pw.flush();
	        } catch (Exception e) {
	        	JOptionPane.showMessageDialog(this, "Failed to send Disconnect message.\n");
	        }
		 
		 try {
	            if (isconnected) {
	            	JOptionPane.showMessageDialog(this, "Disconnected.\n");
	                serverSocket.close();
	                isconnected = false;
	            } else
	            	JOptionPane.showMessageDialog(this, "You have been disconnected.\n");
	        } catch(Exception e) {
	        	JOptionPane.showMessageDialog(this, "Failed to disconnect.\n");
	        }
	        isconnected = false;
	        input_username.setEditable(true);
	 }
	
	 
	 public class ClientThread implements Runnable{
		public void run(){
			 String[] received_data;
			 String message;
			 
	         try{
	        	 while(!Thread.currentThread().interrupted()){
	        		 Date date = new Date();
	                 SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	                 String dateStr = format.format(date);
	        		 if (!serverSocket.isClosed() && (message = br.readLine()) != null){
	        			 received_data = message.split(":");
	        			 if (received_data[2].equals("broadcast")){
	        				 chatWindow.append(dateStr+":"+received_data[0] + ": " + received_data[1] + "\n");
	                       
	        			 } else if(received_data[2].equals("whisper")){
	        				 chatWindow.append(dateStr+":"+"(" + received_data[0] + "): " + received_data[1] + "\n");
	                        
	        			 } else if(received_data[2].equals("connect")){
	        				 chatWindow.removeAll();
	        				 AddUser(received_data[0], received_data[3]);
	        			 } else if(received_data[2].equals("disconnect")){
	        				 DeleteUser(received_data[0], received_data[3]);
	                         if (received_data[0].equals(uname))
	                                Thread.currentThread().interrupt();
	        			 } else if(received_data[2].equals("fail")){
	        				 chatWindow.append("The username is existed, you need change.\n");
	                         serverSocket.close();
	                         isconnected = false;
	                         input_username.setEditable(true);
	                         input.setEditable(false);
	        			 }
	        		 }
	        	 }
	         } catch(Exception e){
	        	 
	         }
		 }
	 }
	 
	 public void RenewUserList(String users)
	 {
		 userWindow.setText("");
		 for (String u : users.split(" ")) {
	            userWindow.append(u + "\n");
	        }
	 }
	 
	 public void DeleteUser(String username, String users)
	 {
		 chatWindow.append("\"" + username + "\" leaves the room.\n");
		 RenewUserList(users);
	 }
	 
	 public void AddUser(String username, String users)
	 {
		 chatWindow.append("\"" + username + "\" joins in the room.\n");
		 RenewUserList(users);
	 }
	 
	 public static void main(String args[]) {
		 ClientFrame client = new ClientFrame();
		 
		 client.addWindowListener(new WindowAdapter() {
	            
	            public void windowClosing(WindowEvent e) {
	            	if(isconnected)
	            	{
	            		client.disconnect();
	            	}
	                e.getWindow().dispose();
	            }
	        });
		 client.setVisible(true);
	 }
	
}
