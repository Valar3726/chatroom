package Server;


import java.util.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;


public class ServerThread extends Thread {
   

    HashMap<String, PrintWriter> clientList;
    ServerSocket serverSocket = null;
    int serverPort = 3726; 

    ServerThread() {
    	Thread server = new Thread(new RunningServer());
    	 server.start();
    }

    
    public class ClientHandler implements Runnable {
        Socket clientSocket;
        PrintWriter pw;
        BufferedReader br;
        String username;

        ClientHandler(Socket socket, PrintWriter pw) {
            try {
                this.pw = pw;
                username = "";
                this.clientSocket = socket;
                InputStreamReader clientbr = new InputStreamReader(socket.getInputStream());
                br = new BufferedReader(clientbr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            String message;
            String[] received_data;

            try {
                while(!Thread.currentThread().interrupted()) {
                    if ((message = br.readLine()) != null) {
                    	received_data = message.split(":");
                        if (received_data[2].equals("connect")) {
                            username = received_data[0];
                            if (clientList.containsKey(username)) {
                                pw.println(received_data[0] + ": :" + "fail");
                                pw.flush();
                                Thread.currentThread().interrupt();
                            } else {
                            	addClient(received_data[0], pw);
                                String users = getAllClients();
                                broadcastClient(message + ":" + users);
                            }
                        } else if (received_data[2].equals("whisper")) {
                        	whisperClient(username, received_data[3], message);
                        } else if (received_data[2].equals("broadcast")) {
                        	broadcastClient(message);
                        } else if (received_data[2].equals("disconnect")) {
                            username = "";
                            deleteClient(received_data[0]);
                            String users = getAllClients();
                            broadcastClient(message + ":" + users);
                            Thread.currentThread().interrupt();
                        } 
                    }
                }
            } catch (Exception e) {
            	deleteClient(username);
                e.printStackTrace();
            }
	    }
    }

    public class RunningServer implements Runnable {
        public void run() {
        	clientList = new HashMap<String, PrintWriter>();
            try {
                serverSocket = new ServerSocket(serverPort);

                while (!Thread.currentThread().interrupted()) {
				    Socket clientSock = serverSocket.accept();
				    PrintWriter pw = new PrintWriter(clientSock.getOutputStream(), true);

                    Thread clientHandler = new Thread(new ClientHandler(clientSock, pw));
                    clientHandler.start();
                   
                }
            } catch (Exception e) {
               
            }
        }
    }
    
    public void addClient (String name, PrintWriter pw) {
    	clientList.put(name, pw); 
    }
    
    public void deleteClient (String name) {
       
    	clientList.get(name).close();
    	clientList.remove(name);
  
    }
    
    public String getAllClients() {
        StringBuffer buf = new StringBuffer();
        for (String client : clientList.keySet()) {
            buf.append(client + " ");
        }

        return buf.toString().trim();
    }
    
    public void whisperClient(String sender, String receiver, String message) {
        PrintWriter pw = clientList.get(receiver);
        
        try {
            if(pw != null) {
            	
                pw.println(message);
                pw.flush();
                if( !sender.equals(receiver))
                {
                	pw = clientList.get(sender);
                	pw.println(message);
                	pw.flush();
                }
 
            } else {
                pw = clientList.get(sender);
                message = sender + ":receiver doesn't exist:whisper";
                pw.println(message);
                pw.flush();
                
            }
        } catch (Exception e) {
           
        }
    }

    public void broadcastClient(String message) {
	    for(PrintWriter pw : clientList.values()) {
            try {
               
                pw.println(message);
                pw.flush();
               
            } catch (Exception ex) {
               
            }
        } 
    }

    public static void main(String args[]) {
        ServerThread server = new ServerThread();
        }
}

