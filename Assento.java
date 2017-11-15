import java.util.*;
import java.util.*;
import java.util.concurrent.Semaphore;
public class Assento
{
	public boolean ocupado;
	public int idCliente;
	public Leitor_Escritor_esconly monitorlivres;
	public int numIdentificacao;
	public ArrayList<Assento> assentosLivreslist;
	private Lock lock;
	private Lock lockbuffer;
	private Semaphore semaforo;
	private Queue<String>fila;
	private Assento[] vAssento;
	private int totalAssentos;
	public Assento(Leitor_Escritor_esconly monitorlivres, int numid, ArrayList<Assento> assentosLivreslist, Assento[] vAssento,
					Lock lock, Semaphore semaforo, Lock lockbuffer, int totalAssentos, Queue<String>filabuffer)
	{
		this.fila = filabuffer;
		this.totalAssentos = totalAssentos;
		this.lockbuffer = lockbuffer;
		this.semaforo = semaforo;
		this.assentosLivreslist = assentosLivreslist;
		this.monitorlivres = monitorlivres;
		ocupado = false;
		idCliente = 0;
		numIdentificacao = numid;
		this.lock = lock;
		this.vAssento = vAssento;
	}
	
	public synchronized boolean efetivaACompra(int id, int codigo)
	{
		try
		{
			if(!ocupado)
			{
				lock.lock();
				if(ocupado)
				{
					lock.lock();
				
					String buff;
					
					buff = codigo + ", " + id + ", " + numIdentificacao + ", [ ";
					for(int i=0;i<totalAssentos;i++) 
					{
						buff = buff + vAssento[i].idCliente + " ";
					}
					
					buff = buff + "]\n";
					lockbuffer.lock();
					fila.add(buff); //nao sei se eh push
					lockbuffer.unlock();
					
					semaforo.release();
					
					lock.unlock();
				}
				monitorlivres.EntraEscritor();
				if(!assentosLivreslist.remove(this))
				{
					System.out.println("nao conseguiu remover\n");
				} //pretegido pelo monitorlivres
				monitorlivres.SaiEscritor();
				ocupado = true;
				this.idCliente = id;
				String buff;
				
				buff = codigo + ", " + id  + ", " + numIdentificacao + ", [ ";
				for(int i=0;i<totalAssentos;i++) 
				{
					buff = buff + vAssento[i].idCliente + " ";
				}
				
				buff = buff + "]\n";
				lockbuffer.lock();
				fila.add(buff); //nao sei se eh push
				lockbuffer.unlock();
				
				semaforo.release();
				
				lock.unlock();
				
				return true;
			}
			else
			{
				lock.lock();
				
				String buff;
				
				buff = codigo + ", " + id + ", " + numIdentificacao + ", [ ";
				for(int i=0;i<totalAssentos;i++) 
				{
					buff = buff + vAssento[i].idCliente + " ";
				}
				
				buff = buff + "]\n";
				lockbuffer.lock();
				fila.add(buff); //nao sei se eh push
				lockbuffer.unlock();
				
				semaforo.release();
				
				lock.unlock();
				
			}
		}
		catch(InterruptedException e){}
		
		
		return false;
	}
	
	public boolean Libera(int id)
	{
		try
		{
			if(id == idCliente)
			{		
				lock.lock();//nenhuma thread podera realizar qualquer outra operação ate que esta tenha de fato terminado
				monitorlivres.EntraEscritor();
				if(ocupado == false)
				{
					lock.unlock();
					monitorlivres.SaiEscritor();
				}
				assentosLivreslist.add(this);//adiciona o assento à lista
				idCliente = 0;
				ocupado = false;
				monitorlivres.SaiEscritor();
				
				String buff;
				
				buff = "4, " + id + ", " + numIdentificacao + ", [ ";
				for(int i=0;i<totalAssentos;i++) 
				{
					buff = buff + vAssento[i].idCliente + " ";
				}
				
				buff = buff + "]\n";
							
				lockbuffer.lock();
				fila.add(buff); //nao sei se eh push
				lockbuffer.unlock();
				
				semaforo.release();
				
				lock.unlock();
				return true;
			}
			System.out.println("Não conseguiu liberar\n");
			return false;
		}
		catch(InterruptedException e){}
		return false;
	}
}
