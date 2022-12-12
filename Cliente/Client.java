package Cliente;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
     
    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }
        catch (IOException e )
        {
            fecharPrograma(socket, bufferedReader, bufferedWriter);
        }
    }

    //ignorar
    //para rodar cliente no terminar powershell, entrar na pasta cliente e dar o comando java client.java
    //ou colar sem entrar na pasta: & 'C:\Program Files\Eclipse Adoptium\jdk-17.0.5.8-hotspot\bin\java.exe' '-XX:+ShowCodeDetailsInExceptionMessages' '-cp' 'C:\Users\gusta\AppData\Roaming\Code\User\workspaceStorage\34d757e35716ed028d9827a826bf3583\redhat.java\jdt_ws\ProjetoChat_d3c58c9a\bin' 'Cliente.Client'

    public static void main(String[] args) throws Exception {

        System.out.println("Conectando ao servidor...");

        try
        {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException in) {}

        System.out.println("Servidor conectado!");

        try
        {
            Thread.sleep(1 * 1000);
        } catch (InterruptedException in) {}

        System.out.println("");
        System.out.println("____Chat Room____");
        System.out.println("Bem-vindo ao chat, pegue seu cafe e fique a vontade!");
        System.out.println("");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o seu nome de usuario (mais de 3 caracteres): ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, username);
        client.listenFOrMessage();
        client.checarNome();

        try
        {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException in) {}

        System.out.println("Nome de usuario valido!");

        try
        {
            Thread.sleep(1 * 1000);
        } catch (InterruptedException in) {}

        System.out.println("Inicializando chat...");
        try
        {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException in) {}

        System.out.println("");
        System.out.println("Chat inicializado!");
        System.out.println("Você ja pode mandar mensagens!");

        client.sendMessages();
        
    }

    public void sendMessages(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected())
            {
                String messageTosend = scanner.nextLine();
                bufferedWriter.write(username + ": " + messageTosend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }
        catch (IOException e)
        {
            fecharPrograma(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenFOrMessage()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;

                while(socket.isConnected())
                {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    }
                    catch (IOException e)
                    {
                        fecharPrograma(socket, bufferedReader, bufferedWriter);

                    }
                }

            }
        }).start();
    }

    public void fecharPrograma(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
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

    public void checarNome ()
    {
        this.username = username;
        do {
            Scanner t = new Scanner(System.in);

            if (this.username.length() > 0 && this.username.length() <= 3) {
                System.out.println("Seu nickname deve ter mais de 3 caracteres. " + "Aguarde para digitar novamente.\n\r");

                try {
                    Thread.sleep(2 * 1000);
                
                    
                } catch (InterruptedException in) {}

                System.out.print("Digite seu nickname novamente: ");
                this.username = t.nextLine();
            }

            if (this.username.length() == 0) {
                System.out.println("Seu nickname não pode ser vazio. " + "Aguarde para digitar novamente.\n\r");

                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException in) {}

                System.out.print("Digite seu nickname novamente: ");
                this.username = t.nextLine(); 
            }

        } while (this.username.length() <= 3); 
    }

    
}