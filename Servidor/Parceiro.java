package Servidor;

import javax.imageio.IIOException;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Parceiro implements Runnable{
    public static ArrayList<Parceiro>parceiro= new ArrayList<>();//to keep track of our clients, to send messages to our client
    private Socket socket; //this will be the socket that will be passed from our Server Class, used to establised the connection
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter; //metodo para mandar dados para o servidor 
    private String username; 

    public Parceiro(Socket socket) throws Exception
    {
         
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//to send things
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //to read things
            this.username = bufferedReader.readLine();
			parceiro.add(this);
            // transmitirMensagem(username + " entrou no chat!!");
        }
        catch (IIOException e){
            fecharPrograma( socket, bufferedReader, bufferedWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //tudo que esta no metodo run, roda em uma thread diferente
    @Override
    public void run() // all we want to do in separate thread is listening to messages , because listening for messages is blocking operation , it means that
    {                 //the program will be stuck until the operation is completed , if we werent using multiple threads , our program would be stuck waiting for messages

        String messageFromClient;
        while(socket.isConnected())
        {
            try
            {
                messageFromClient = bufferedReader.readLine();
                transmitirMensagem(messageFromClient);
            }
            catch (IOException e)
            {
                fecharPrograma(socket, bufferedReader, bufferedWriter);
                break;
            }

        }

    }

    public void verificarParceiro()
    {
        System.out.println(username + " entrou no chat!!");
        // if(parceiro != null)
        // {
        //     System.out.println(username + " entrou no chat!!");
        // }
        // else
        // {
        //     parceiro.remove(this);
        //     transmitirMensagem(username + " deixou o chat!!");
        // }
    }

    public void transmitirMensagem(String messagetoSend) //transmitir mensagem 
        {
            for(Parceiro parceiro : parceiro){
                try
                {
                    if(!parceiro.username.equals(username));
                    parceiro.bufferedWriter.write(messagetoSend);
                    parceiro.bufferedWriter.newLine();
                    parceiro.bufferedWriter.flush();

                }
                catch (IOException e)
                {
                    fecharPrograma(socket, bufferedReader, bufferedWriter);
                }
            }
        }

        public void fecharPrograma(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
        {
            verificarParceiro();
            try{
                if(bufferedReader != null)
                {
                    bufferedReader.close();
                }
                if(bufferedWriter != null)
                    bufferedWriter.close();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
}
