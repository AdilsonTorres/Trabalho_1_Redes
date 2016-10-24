package usp;

import java.net.*;
import java.io.*;
import java.util.*;

public class ConexaoWeb extends Thread
{
	Socket socket; 					    //socket que vai tratar com o cliente.
	static String arqi = "index.html"; 	//se nao for passado um arquivo, o servidor fornecera a pagina index.html
	DataOutputStream log;               //objeto para arquivo de log
	
	//coloque aqui o construtor
	public ConexaoWeb(Socket socket)
	{
		this.socket = socket;
		try
		{
			log = new DataOutputStream(new FileOutputStream("WebLog.txt", true));
		}
		catch (Exception e) {
			System.out.println("Erro ao inicializar arquivo de log");
			System.out.println(e.getMessage());
		}
	}
	
	//metodo TrataConexao, aqui serao trocadas informacoes com o Browser...

	public void run() 
	{
	    String metodo=""; 		   			//String que vai guardar o metodo HTTP requerido
	    String ct; 				   			//String que guarda o tipo de arquivo: text/html;image/gif....
	    String versao = ""; 	   			//String que guarda a versao do Protocolo.
	    File arquivo; 		 	   			//Objeto para os arquivos que vao ser enviados. 
	    String NomeArq; 		   			//String para o nome do arquivo.
	    String raiz = "root"; 	   			//String para o diretorio raiz.
	    String host = "host0";     			//String para host padrao.
	    String datapath = "";      			//String para o caminho final do arquivo.
		String inicio;			   			//String para guardar o inicio da linha
		//String senha_user = "";	  			//String para armazenar o nome e a senha do usuario
		String primeiraLinha = ""; 			//String para armazenar a primeira linha do cabeçalho 
		String linha;                      	//String para leitura de cabeçalho
		String ip;                 			//String para id do cliente
		String tipo_codificacao = "";
		String senha_user_codificado = ""; 	//String para armazenar o nome e a senha codificado
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
				// pega o ip do cliente para salvar no log
				ip = socket.getInetAddress().getHostAddress();
				
				primeiraLinha = entrada.readLine();
				System.out.println("Primeira linha: " + primeiraLinha);
				StringTokenizer st = new StringTokenizer(primeiraLinha);
				metodo = st.nextToken();
				NomeArq = st.nextToken();
				versao = st.nextToken();
				
				// LOG
				log.writeBytes(ip + " - [" + now.toString() + "]\n");
				
				// Jogar para dentro do GET *****************
				if (NomeArq.endsWith("/")) NomeArq = NomeArq + arqi;
				ct = TipoArquivo(NomeArq);
				
				
				
				// Lê todo o cabeçalho
				while (!(linha = entrada.readLine()).equals(""))
				{
					st = new StringTokenizer(linha);
					String next = st.nextToken();
					System.out.println(linha);
					if (next.startsWith("Host"))
					{
						String host_received = st.nextToken();
						// Por padrão o Host é localhost:9090, mantendo assim o host padrão.
						if (host_received.startsWith("localhost"))
						{
							
						}else{
							host = host_received;
						}
						
						System.out.println("Host: " + host);
						System.out.println("Nome do arquivo: " + NomeArq);
					}
					if (next.startsWith("Authorization"))
					{
						tipo_codificacao = st.nextToken();
						senha_user_codificado = st.nextToken();
						
					}
				}
				
				if (metodo.equals("GET"))
				{
					System.out.println("Lendo arquivo");
					datapath = host + NomeArq;
					System.out.println("Datapath: " + datapath);
					
					//Área de acesso exclusivo com autenticação.
					if(datapath.startsWith("host0/secret/"))
					{
						// Caso não senha o usuário: alguem com a senha: senha não entra.
						// Assim como não tenha requisitado com Authorization.
						if(senha_user_codificado.equals("YWxndWVtOnNlbmhh"))
						{
							
						}else
						{
							saida.writeBytes("HTTP/1.1 401 Unauthorized\n");
							saida.writeBytes("Content-Type: text/html\n");
							saida.writeBytes("WWW-Authenticate: Basic realm=”System Administrator”");
							saida.writeBytes("\n");
							saida.close();							
						}
					}
					
					//Crie aqui o objeto do tipo File
					arquivo = new File(raiz,datapath.substring(0,datapath.length()));
					
					//agora faca a leitura do arquivo.
					FileInputStream fis = new FileInputStream(arquivo);
					byte[] dado = new byte[(int) arquivo.length()];
					fis.read(dado);
		
					//Mande aqui a mensagem para o cliente.
					saida.write(dado);
					
					fis.close();
					
					// LOG
					log.writeBytes("\"" + primeiraLinha + "\" 200 " + dado.length + "\n");
				}	
			}
			//este catch e para o caso do arquivo nao existir. Mande para o browser uma mensagem de not found, e um texto html!
			catch(IOException e)
			{
				System.out.println("Erro ao ler arquivo");
				System.out.println(e.getMessage());
				
				// LOG
				log.writeBytes("\"" + primeiraLinha + "\" 404 0 \n");
				
				saida.writeBytes("HTTP/1.1 404 Not Found\n");
				saida.writeBytes("Content-Type: text/html\n");
				saida.writeBytes("\n");
				saida.writeBytes("<html>Arquivo nao encontrado</html>");
				saida.close();
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