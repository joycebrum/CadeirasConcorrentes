import java.util.*;
import java.util.concurrent.Semaphore;
/**
 * Classe responsavel por gerenciar todos os pedidos das Threads,
 * coordena e controla o acesso a determinadas variaveis e chamadas de metodos 
 * utilizando lock , semaforo e monitores a fim de evitar condicoes de corrida.
 * <p>
 * Os metodos sao publicos mas nao sincronizacao, ja que a sincronizacao e garantida pelas
 * estruturas auxiliares 
 *
 * @param  assentosLivreslist  campo publico contendo o ArrayList com os assentos livres
 * @param  totalAssentos  campo publico inteiro com o numero total de assentos do sistema
 * @param  fila  campo publico do tipo Queue<String> que representa o buffer do log de saida
 * @param  lockbuffer  campo publico Lock que protege o acesso ao buffer
 * @param  monitorlivres  campo publico monitor do tipo Monitor_Escritor_esconly
 * @param  vAssentos  campo publico vetor de Assento contendo todos os assentos do sistema
 * @param  monitor  campo publico monitor do tipo Leitor_Escritor 
 * @param  semaforo  campo privado semaforo para controlar quantidade de elementos no buffer
 */
public class Gerente
{
	public ArrayList<Assento> assentosLivreslist;
	public int totalAssentos;
	public Queue<String> fila;
	public Lock lockbuffer;
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
	}
	/**
	 * Funcao para escolher um assento aleatorio dentre todos os assentos livres
	 * <p>
	 * Retorna um Assento caso tenha ou null caso a lista esteja vazia. Para selecionar o assento usa
	 * o metodo shuffle da classe Collections para embaralhar a lista e pega sempre o primeiro elemento
	 * @return      Um Assento aleatorio caso tenha na lista ou null caso esta esteja vazia
	 */
	public Assento ListaAssentosLivres()
	{
		Assento temp = null;
		monitor.EntraLeitor();//espera ate que nenhuma thread esteja modificando o vetor
		monitorlivres.EntraEscritor();//pede acesso para modificar a lista
		
		Collections.shuffle(assentosLivreslist);
		if(assentosLivreslist.size()>0)
		{
			temp = assentosLivreslist.get(0);
		}
		monitorlivres.SaiEscritor();
		monitor.SaiLeitor();
		return temp;
	}
	/**
	 * Este metodo e responsavel por visualizar o estado dos assentos, colocando esta informacao
	 * no buffer numa area protegida pelo lockbuffer.
	 * <p>
	 * Para poder acessar o vetor de assentos pede acesso pelo metodo EntraLeitor() do monitor (da classe Leitor_Escritor)
	 *
	 * @param  id  codigo de identificacao da thread que chamou o metodo
	 */
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
	/**
	 * Este metodo e responsavel por tentar alocar um assento aleatorio para a thread de id passado como parametro.
	 * <p>
	 * Enquanto assentosLivreslist nao estiver vazia ele chama ListaAssentosLivres para conseguir um metodo aleatorio
	 * para tentar efetivar compra. 
	 * <p>
	 * Se por acaso ele for null a thread encerra sua execucao pois a lista esta vazia, caso contrario pede acesso
	 * ao monitor (da classe Leitor_Escritor) como Escritor e tenta efetivar a compra. Se o efetivaACompra retonar true, 
	 * libera o monitor (SaiEscritor()), e encerra o loop, retornando o assento que foi efetivado, se o efetivaACompra retornar 
	 * false ele continua no loop. Em seguida verifica novamente se a lista es vazia numa area protegia pelo monitor como Leitor e pelo
	 * monitorlivres (da classe Leitor_Escritor_esconly)
	 * @param  assento  Assento a ser inicializado no metodo
	 * @param  id  codigo de identificacao da thread que chamou o metodo
	 * @return      retorna o Assento que foi efetivado no metodo ou null caso nao tenha conseguido efetivar nenhum acesso
	 */
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
				/*monitor.EntraLeitor();
				monitorlivres.EntraEscritor();
					
				Collections.shuffle(assentosLivreslist);
				tentativa = assentosLivreslist.get(0);
				
				monitorlivres.SaiEscritor();
				monitor.SaiLeitor();*/
				tentativa = ListaAssentosLivres();
			
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
	/**
	 * Este metodo e responsavel por tentar alocar um assento especifico passado pela thread de id passado como parametro.
	 * <p>
	 * Verifica se o assento nao e nulo, se for, retorna false, se nao, pede acesso como Escritor ao monitor
	 * (da classe Leitor_Escritor), tenta efetivar a compra do assento passado. se o efetivaACompra retornar true, a funcao retorna
	 * true, caso o contrario, retorna false.
	 * @param  assento  Assento que deseja ser efetivado
	 * @param  id  codigo de identificacao da thread que chamou o metodo
	 * @return      retorna true caso tenha conseguido efetivar o assento e false caso nao
	 */
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
	/**
	 * Este metodo e responsavel por tentar desalocar um assento para a thread de id passado como parametro.
	 * <p>
	 * Pede acesso como Escritor ao monitor (da Classe Leitor_Escritor), tenta liberar o assento passando o id como parametro 
	 * libera o monitor chamando SaiEscritor().
	 * @param  assento  Assento a ser liberado no metodo
	 * @param  id  codigo de identificacao da thread que chamou o metodo
	 */
	public void libera(Assento assento, int id)//assento ja inicializado
	{
		
		monitor.EntraEscritor();
		assento.Libera(id);
		
		monitor.SaiEscritor();		
	}
}
