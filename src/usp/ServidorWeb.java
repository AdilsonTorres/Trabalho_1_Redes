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
		ConexaoWeb conexao;
		
		try 
		{	
			//crie aqui o socket Servidor!!!!!!!!(objeto = new ServerSocket(parametros))
			sw = new ServerSocket(porta);
			conexao = new ConexaoWeb(sw.accept());
			conexao.TrataConexao();
			
			while(true) 
			{
		
			}
		}
		catch(IOException e) 
		{
			System.err.println("Servidor foi abortardo");
			System.err.println(e.getMessage());
		}  
	}
}