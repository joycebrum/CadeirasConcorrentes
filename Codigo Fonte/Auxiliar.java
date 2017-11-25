import java.util.*;
import java.util.concurrent.Semaphore;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Esta classe e responsavel pela geracao do log de saida sendo filha da classe thread.
 * <p>
 * utiliza um semaforo para controlar a quantidade de elementos no buffer(fila) e um lock para proteger as modificacoes feitas nele
 * @param filabuffer campo do tipo Queue<String> que simboliza o bufer com os objetos para serem impressos pela classe auxiliar gerando o log de saida
 * @param acabou campo do tipo boleano que indica se o programa ja acabou
 * @param semaforo campo que simboliza um semforo para controlar a quantidade de elementos em bufer
 * @param gravarArq campo do tipo PrintWriter utilizado para esrever no arquivo
 */

public class Auxiliar extends Thread  //imprime o buffer
{
	public Queue<String> filabuffer;
	private Lock lockbuffer;
	public boolean acabou;
	private Semaphore semaforo;
	private PrintWriter gravarArq;
	//private Semaphore slotcheio; poder usar sem limite maximo
	
	
	public Auxiliar(Queue<String> filabuffer, Lock lockbuffer, Semaphore semaforo, PrintWriter gravarArq)
	{
		acabou = false;
		this.semaforo = semaforo;
		this.gravarArq = gravarArq;
		this.lockbuffer = lockbuffer;
		this.filabuffer = filabuffer;
	}
	
	/**
	* O metodo Run desta classe roda ate ser informado pela classe Main de que todas as threads ja terminaram o seu processamento.
	* Atraves do semaforo ela e acordada quando tem elementos no buffer a serem consumidos e volta a dormir quando nao tem, esperando
	* por mais elementos. Ao remover um elemento, utiliza o lockbuffer para proteger o acesso a fila.
	* Mesmo depois de ser informada que as threads terminaram, ela consome todos os elementos restantes na fila antes de encerrar seu
	* processamento.
	*/
	
	
	public void run()
	{
		//gravarArq.println("Arquivo de Saída\n");
		try
		{
			boolean vazia;
			while(!acabou)//enquanto ainda nao acabou ou ainda nao esta vazia 
			{
				semaforo.acquire();
				if(acabou)
				{
					break;
				}
				
				lockbuffer.lock();
				String temp = filabuffer.remove();
				lockbuffer.unlock();
				gravarArq.print(temp);
			}
			//quando executar este pedaco do codigo a thread gerente ja vai ter terminado
			lockbuffer.lock();
			vazia = filabuffer.isEmpty();
			lockbuffer.unlock();
			while(!vazia)
			{
				
				lockbuffer.lock();
				String temp = filabuffer.remove();
				vazia = filabuffer.isEmpty();
				lockbuffer.unlock();
				gravarArq.print(temp);
			}
		}
	
		catch(Exception e){}
	}
}

