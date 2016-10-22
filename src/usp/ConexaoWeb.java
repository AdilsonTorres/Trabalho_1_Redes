package usp;

import java.net.*;
import java.io.*;
import java.util.*;

public class ConexaoWeb {
  Socket socket; 					//socket que vai tratar com o cliente.
  static String arqi = "index.html"; 	//se nao for passado um arquivo, o servidor fornecera a pagina index.html

  //coloque aqui o construtor
  

  //metodo TrataConexao, aqui serao trocadas informacoes com o Browser...

  public void TrataConexao() {
    String metodo=""; 				//String que vai guardar o metodo HTTP requerido
    String ct; 					//String que guarda o tipo de arquivo: text/html;image/gif....
    String versao = ""; 			//String que guarda a versao do Protocolo.
    File arquivo; 				//Objeto para os arquivos que vao ser enviados. 
    String NomeArq; 				//String para o nome do arquivo.
    String raiz = "."; 				//String para o diretorio raiz.
	String inicio;				//String para guardar o inicio da linha
	String senha_user="";		//String para armazenar o nome e a senha do usuario
	Date now = new Date();
		
    try {

		//Coloque aqui o acesso aos streams do socket!
		
		//Coloque aqui o procedimento para ler toda a mensagem do cliente. Imprima na tela com System.out.println()!
		
			/* Para a segunda parte, ignore para a primeira!
  			// Enviar o arquivo
			try {
				
			
			
			//Crie aqui o objeto do tipo File
			
			//agora faca a leitura do arquivo.

			//Mande aqui a mensagem para o cliente.
			
			}
			//este catch e para o caso do arquivo nao existir. Mande para o browser uma mensagem de not found, e um texto html!
			catch(IOException e)
			{
				
			}   
			*/
		
	}
	catch(IOException e)
	{
	}
	
    //Fecha o socket.
    try {
      socket.close();
    }
    catch(IOException e) {
    }
  }

//Funcao que retorna o tipo do arquivo.

  public String TipoArquivo(String nome) {
    if(nome.endsWith(".html") || nome.endsWith(".htm")) return "text/html";
    else if(nome.endsWith(".txt") || nome.endsWith(".java")) return "text/plain";
    else if(nome.endsWith(".gif")) return "image/gif";
    else if(nome.endsWith(".class"))  return "application/octed-stream";
    else if( nome.endsWith(".jpg") || nome.endsWith(".jpeg") ) return "image/jpeg";
    else return "text/plain";
  }
}
		










