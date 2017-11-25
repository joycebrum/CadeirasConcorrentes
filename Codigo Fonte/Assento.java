import java.util.*;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Esta classe representa um assento unitario, com metodos para efetivacao da compra por um cliente,
 * e liberacao do assento
 * <p>
 * Os metodos sao publicos e sincronizados, pois na efetivacao apens um cliente podera realizar a compra,
 * e a liberacao necessita da sincronizacao, para evitar que uma thread tente efetivar uma compra
 * enquanto outra tenta liberar, no mesmo assento
 *
 * @param ocupado campo utilizado para dizer se a cadeira esta ocupada
 * @param idCliente campo utilizado para dizer o id da thread que comprou o assento
 * @param monitorlivres campo que simboliza o monitor que apenas uma thread pode escrever
 * @param numIdentificacao campo que representa a posicao do assento atual no bufer
 * @param assentosLivreslist campo do tipo ArrayList<Assento> que simboliza o vetor de assentos livres
 * @param lock campo que representa o lock que sera utilizado para que as operacoes sejam colocadas no bufer na ordem certa
 * @param lockbuffer campo que simboliza o lock utilizado sempre que for feita uma alteracao no buffer
 * @param semaforo campo que representa o semaforo utilizado para avisar para a classe auxiliar que ja existem produtos para serem consumidos
 * @param fila campo do tipo Queue<String> que simboliza o bufer com os objetos para serem impressos pela classe auxiliar gerando o log de saida
 * @param vAssento campo do tipo Assento[] que representa um array com todos os assentos
 * @param totalAssentos campo que representa o total de assentos
 */

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
	
	/**
	* Este metodo efetiva a compra de um assento por apenas uma thread, por ser sincronizado.
	 * <p>
	 * Alem disso tambem e responsavel pela escrita no bufer para que a classe auxiliar possa escrever no log de saida.
	 * Para garantir que as operacoes sao inseridas na ordem e no momento em que sao feitas, utiliza-se um lock ao mudar o estado 
	 * do assento de livre para ocupado.
	 * O metodo tambem atualiza a lista de assentos livres, removendo o assento corrente da mesma, numa area protegida pelo monitorlivres.
	 * Alem disso, o trecho de codigo que insere a string contendo as informacoes da operacao na fila e protegido pelo lockbuffer.
	 * 
	 * @param id campo que representa o id da thread que esta tentando efetivar a compra do assento
	 * @param codigo campo que representa o codigo da operacao realizada (2 - alocadaLivre, 3 - alocaDado)
	 * @return   um booleano dizendo se a compra foi efetivada ou nao

	*/
	
	public synchronized boolean efetivaACompra(int id, int codigo)
	{
		try
		{
			if(!ocupado)
			{
				lock.lock();
				if(ocupado)
				{
				
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
					return false;
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
				return false;
				
			}
		}
		catch(InterruptedException e){}
		
		
		return false;
	}
	
	/**
	* Este metodo libera uma cadeira por uma thread que tenha desistido de seu assento.
	 * <p>
	 * Alem disso tambem e responsavel pela escrita no bufer para que a classe auxiliar possa escrever no log de saida, garantindo 
	 * a ordem de escrita no buffer pelo lock compartilhado com os demais assentos, protegendo a escrita na fila com o lock buffer e 
	 * atualizando a lista de assentos livres, adicionando o Assento corrente na lista.
	 * 
	 * @param id campo que representa o id da thread que esta tentando efetivar a compra do assento
	 * @return   um booleano dizendo se a liberacao foi feita ou nao

	*/
	
	public synchronized boolean Libera(int id)
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
					return false;
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
