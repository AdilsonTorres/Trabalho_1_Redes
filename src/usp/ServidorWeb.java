package usp;

import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorWeb 
{
	public static void main(String[] args) 
	{
		int porta = 9090; //porta aonde o servidor vai aguardar por pedido de conexao.
		ServerSocket sw;  //Socket servidor
		Socket socket;
		ConexaoWeb conexao;
		
		try 
		{	
			while(true)
			{
				//crie aqui o socket Servidor!(objeto = new ServerSocket(parametros))
				sw = new ServerSocket(porta);
				socket = sw.accept(); //Aguardar um pedido de conexão.
				conexao = new ConexaoWeb(socket);
				conexao.start();
				
				// libera a porta
				sw.close();
			}
		}
		catch(IOException e) 
		{
			System.err.println("Servidor foi abortardo");
			System.err.println(e.getMessage());
		}  
	}
}