package usp;

import java.net.*;
import java.io.*;
import java.util.*;

public class ConexaoWeb
{
	Socket socket; 					    //socket que vai tratar com o cliente.
	static String arqi = "index.html"; 	//se nao for passado um arquivo, o servidor fornecera a pagina index.html
	
	//coloque aqui o construtor
	public ConexaoWeb(Socket socket)
	{
		this.socket = socket;
	}
	
	//metodo TrataConexao, aqui serao trocadas informacoes com o Browser...

	public void TrataConexao() 
	{
	    String metodo=""; 		//String que vai guardar o metodo HTTP requerido
	    String ct; 				//String que guarda o tipo de arquivo: text/html;image/gif....
	    String versao = ""; 	//String que guarda a versao do Protocolo.
	    File arquivo; 			//Objeto para os arquivos que vao ser enviados. 
	    String NomeArq; 		//String para o nome do arquivo.
	    String raiz = "root"; 	//String para o diretorio raiz.
		String inicio;			//String para guardar o inicio da linha
		String senha_user = "";	//String para armazenar o nome e a senha do usuario
		String linha;           //String para leitura de cabeçalho
		Date now = new Date();
		
		try 
		{
			//Coloque aqui o acesso aos streams do socket!
			BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
			
			//Coloque aqui o procedimento para ler toda a mensagem do cliente. Imprima na tela com System.out.println()!
			/* ETAPA 1 - SERVIDOR SIMPLES
			while (!(linha = entrada.readLine()).equals(""))
			{
				System.out.println(linha);
			}
			
			saida.writeBytes("<html><h1>ISTO E UM TESTE</h1></html>");
			saida.close();
			*/
			
			// Para a segunda parte, ignore para a primeira!
  			// Enviar o arquivo
			try 
			{
				linha = entrada.readLine();
				System.out.println(linha);
				StringTokenizer st = new StringTokenizer(linha);
				metodo = st.nextToken();
				NomeArq = st.nextToken();
				versao = st.nextToken();
				if (NomeArq.endsWith("/")) NomeArq = NomeArq + arqi;
				ct = TipoArquivo(NomeArq);
				
				if (metodo.equals("GET"))
				{
					//Crie aqui o objeto do tipo File
					arquivo = new File(raiz,NomeArq.substring(1,NomeArq.length()));
					
					//agora faca a leitura do arquivo.
					FileInputStream fis = new FileInputStream(arquivo);
					byte[] dado = new byte[(int) arquivo.length()];
					fis.read(dado);
		
					//Mande aqui a mensagem para o cliente.
					saida.write(dado);
					
					fis.close();
				}	
			}
			//este catch e para o caso do arquivo nao existir. Mande para o browser uma mensagem de not found, e um texto html!
			catch(IOException e)
			{
				System.out.println("Erro no tratamento da requisição");
				System.out.println(e.getMessage());
			}   
		}
		catch(IOException e)
		{
			System.out.println("Erro ao receber requisição");
			System.out.println(e.getMessage());
		}
		
	    //Fecha o socket.
	    try 
	    {
	    	socket.close();
	    }
	    catch(IOException e) 
	    {
	    	System.out.println("Erro ao fechar socket");
	    	System.out.println(e.getMessage());
	    }
	}

	//Funcao que retorna o tipo do arquivo.
	public String TipoArquivo(String nome) 
	{
		if (nome.endsWith(".html") || nome.endsWith(".htm")) return "text/html";
		else if (nome.endsWith(".txt") || nome.endsWith(".java")) return "text/plain";
		else if (nome.endsWith(".gif")) return "image/gif";
		else if (nome.endsWith(".class"))  return "application/octed-stream";
		else if ( nome.endsWith(".jpg") || nome.endsWith(".jpeg") ) return "image/jpeg";
		else return "text/plain";
	}
}