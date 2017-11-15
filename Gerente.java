import java.util.*;
import java.util.concurrent.Semaphore;
public class Gerente
{
	public ArrayList<Assento> assentosLivreslist;
	public int totalAssentos;
	public Queue<String> fila;
	public Lock lockbuffer;
	public Lock bla;
	public Leitor_Escritor_esconly monitorlivres; //diferente do outro monitor este nao permite multiplos escritores
	public Assento[] vAssentos;
	public Leitor_Escritor monitor;
	private Semaphore semaforo;
	
	public Gerente(int numassentos, Queue<String>filamain, Lock lockmain, Assento[] vAssentos, Leitor_Escritor monitor, 
				ArrayList<Assento> assentosLivreslist, Leitor_Escritor_esconly monitorlivres, Semaphore semaforo)
	{
		this.assentosLivreslist = assentosLivreslist;
		this.vAssentos = vAssentos;
		this.monitor = monitor;
		this.semaforo = semaforo;
		this.monitorlivres = monitorlivres;
		fila = filamain;
		totalAssentos = numassentos;
		lockbuffer = lockmain;
		bla=new Lock();
	}
	
	public Assento ListaAssentosLivres()
	{
		monitor.EntraLeitor();
		monitorlivres.EntraEscritor();
		
		Collections.shuffle(assentosLivreslist);
		Assento temp = assentosLivreslist.get(0);
		
		monitorlivres.SaiEscritor();
		monitor.SaiLeitor();
		return temp;
	}

	public void visualiza(int id)
	{		
		String buff;
		int i, temp = 0;
		
		
		buff = "1, " + id +", [ "; //codigo da operacao (1)
		
		try
		{
			monitor.EntraLeitor();
			for(i=0;i<totalAssentos;i++) 
			{
				buff = buff + vAssentos[i].idCliente + " ";
			}
			buff = buff + "]\n";
			lockbuffer.lock();
			fila.add(buff); //nao sei se eh push
			
			lockbuffer.unlock();
			monitor.SaiLeitor();
			semaforo.release();
		}
		catch (InterruptedException e)
		{
			System.out.println("Erro InterruptedException no libera do Gerente. Thread id = " + id);
		}
		
	}
	public Assento alocaLivre(Assento assento, int id)//assento ainda nao inicializado
	{
		String buff;
		ArrayList<Assento> arraytemp;
		Assento tentativa;
		
		
		//try
		//{
		
			monitor.EntraLeitor();//se tem alguma thread alocando cadeira a lista de assentos vai mudar
			monitorlivres.EntraLeitor();//nao tem nenhuma thread modificando o 
			
			boolean empty = assentosLivreslist.isEmpty();
			
			monitorlivres.SaiLeitor();
			monitor.SaiLeitor();
			
			int i;
			while(!empty)
			{
				monitor.EntraLeitor();
				monitorlivres.EntraEscritor();
					
				Collections.shuffle(assentosLivreslist);
				tentativa = assentosLivreslist.get(0);
				
				monitorlivres.SaiEscritor();
				monitor.SaiLeitor();
				
			
				if(tentativa==null)
				{
					break;
				}
				monitor.EntraEscritor();
				
				if(tentativa.efetivaACompra(id,2))
				{
					assento = tentativa;
					monitor.SaiEscritor();
					return tentativa;
				}
				monitor.SaiEscritor();
				
				monitor.EntraLeitor();
				monitorlivres.EntraLeitor();
				
				empty = assentosLivreslist.isEmpty();
				
				monitorlivres.SaiLeitor();
				monitor.SaiLeitor();
			}
			
			
		
			return null;
		//}
		/*catch(InterruptedException e)
		{
			bla.unlock();
		}*/
		//return null;
	}
	
	public boolean alocaDado(Assento assento, int id)//assento ja inicializado
	{
		boolean valret;
		
		if(assento == null)
		{
			return false;
		}
		monitor.EntraEscritor();
		valret = assento.efetivaACompra(id,3);
		monitor.SaiEscritor();		
		
		return valret;

	}
	public void libera(Assento assento, int id)//assento ja inicializado
	{
		
		monitor.EntraEscritor();
		assento.Libera(id);
		
		monitor.SaiEscritor();		
	}
}
