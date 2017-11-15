import java.util.*;
import java.util.concurrent.Semaphore;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
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
	
	public void run()
	{
		gravarArq.println("Arquivo de Saída\n");
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
			System.out.println("saiu");
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
			System.out.println("acabou");
		}
	
		catch(Exception e){/*ai oh peguei*/}
	}
}

