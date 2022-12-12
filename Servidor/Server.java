package Servidor;

import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Cliente.Client;

public class Server
{
    //declarando atributos
    private ServerSocket serverSocket; //responsible for listening for incoming conections and creating a socket objetc

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    //ignorar
    // para rodar o server no terminal powershell, colar isso:
    //& 'C:\Program Files\Eclipse Adoptium\jdk-17.0.5.8-hotspot\bin\java.exe' '-XX:+ShowCodeDetailsInExceptionMessages' '-cp' 'C:\Users\gusta\AppData\Roaming\Code\User\workspaceStorage\34d757e35716ed028d9827a826bf3583\redhat.java\jdt_ws\ProjetoChat_d3c58c9a\bin' 'Servidor.Server' 

    public static void main(String[] args) throws Exception
    {

        System.out.println("O servidor esta carregando o chat...");  
        System.out.println("Aguardando conexao...");
        System.out.println();
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
        
    }

    //to keep the server running
    public void startServer() throws Exception
    {
        try{
            while(!serverSocket.isClosed())
            {
                Socket socket = serverSocket.accept();
                Parceiro parceiro = new Parceiro(socket);
                parceiro.verificarParceiro();
                Thread thread = new Thread(parceiro);
                thread.start();//para comecar a execução da thread
                
                // Client client = new Client(socket, null);
                // client.listenFOrMessage();
                
            }
        }
        catch (IOException e) {

        }

    }


    public void closeServerSocket()
    {
        try{
            if(serverSocket != null)
            {
                serverSocket.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}