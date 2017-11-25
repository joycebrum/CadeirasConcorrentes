import java.util.*;
import java.util.concurrent.Semaphore;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
/**
 * Classe que contem o metodo statico main
 * <p>
 * Responsavel por inicializar todas as estruturas e classes, alem de iniciar as threads
 */
public class Main
{
	/**
	 * Metodo statico main que inicializa todas as estruturas e classes, instanciando-as dentro do proprio
	 * metodo
	 *
	 * @param  args  vetor de strings com as entradas do programa <nome do arquivo de saida> <numero de assentos>
	 */
	public static void main (String[] args) throws IOException
	{
		
		Assento[] vAssento;
		int numassentos;
		if(args.length<2)
		{
			System.out.printf("%d Entrada deve ser do tipo 'java <nomedoarquivodesaida> <numerodeassentos>'", args.length);
			return;
		}
		numassentos = Integer.parseInt(args[1]);//pega o numero de assentos passado como argumento
		String nomearquivo = args[0];
		FileWriter arq = new FileWriter(nomearquivo);
		PrintWriter gravarArq = new PrintWriter(arq);
		
		int clientesindecisos=(numassentos+50)/3;
		int clientesqualquer=(numassentos+50)/3;
		int clientesteimosos=(numassentos+50)/3;
		
		ClienteIndeciso[] vClientesIndecisos = new ClienteIndeciso[clientesindecisos];
		ClienteQualquer[] vClienteQualquer = new ClienteQualquer[clientesqualquer];
		ClienteTeimoso[] vClienteTeimoso = new ClienteTeimoso[clientesteimosos];
		Auxiliar auxiliar;
		
		Queue<String>filabuffer = new LinkedList<String>();
		Gerente gerente;
		Leitor_Escritor_esconly monitorlivres; //controla acesso a lista de assentos livres
		Lock lockbuffer;
		Lock lockordem;
		Leitor_Escritor monitor; //controla acesso ao vetor de assentos
		ArrayList<Assento> assentosLivreslist = new ArrayList<Assento>();
		Semaphore semaforo = new Semaphore(0);
		
		lockbuffer = new Lock();
		vAssento = new Assento[numassentos]; //instancia o vetor de assentos
		monitor = new Leitor_Escritor();
		monitorlivres = new Leitor_Escritor_esconly();
		lockordem = new Lock();
		
		for(int i = 0; i < numassentos; i++)//instancia cada assento passado separadamente
		{
			vAssento[i] = new Assento(monitorlivres, i,assentosLivreslist, vAssento,lockordem,semaforo,lockbuffer,numassentos,filabuffer);
			assentosLivreslist.add(vAssento[i]);
		}
		gerente = new Gerente(numassentos,filabuffer, lockbuffer,vAssento, monitor, assentosLivreslist, monitorlivres,semaforo);//instancia a classe gerente passando a fila 
		int id =1;
		auxiliar = new Auxiliar(filabuffer, lockbuffer, semaforo, gravarArq);
		auxiliar.start();
		
		for(int i =0; i< clientesteimosos; i++)
		{
			vClienteTeimoso[i] = new ClienteTeimoso(id,gerente);
			vClienteTeimoso[i].start();
			id++;
		}
		for(int i =0; i< clientesqualquer; i++)
		{
			vClienteQualquer[i] = new ClienteQualquer(id,gerente);
			vClienteQualquer[i].start();
			id++;
		}
		for(int i =0; i< clientesindecisos; i++)
		{
			vClientesIndecisos[i] = new ClienteIndeciso(id,gerente);
			vClientesIndecisos[i].start();
			id++;
		}
		
		//instancia as threads
		
		try
		{
			
			for(int i =0; i< clientesteimosos; i++)
			{
				vClienteTeimoso[i].join();
			}
			for(int i =0; i< clientesqualquer; i++)
			{
				vClienteQualquer[i].join();
			}
			for(int i =0; i< clientesindecisos; i++)
			{
				vClientesIndecisos[i].join();
			}
			auxiliar.acabou = true;
			semaforo.release();
			auxiliar.join();
			arq.close();
		}
		catch(InterruptedException e){}
	}
	
}
