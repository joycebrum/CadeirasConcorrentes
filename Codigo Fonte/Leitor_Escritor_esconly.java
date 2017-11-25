/**
 * Esta classe se comporta como um monitor a fim de proteger determinadas secoes do codigo
 * dividindo as classes entre leitoras e escritoras.
 * <p>
 * Este monitor da prioridade para escrita e nao permite escritas simultaneas (apenas um escritor
 * pode escrever por vez)
 *
 * @param  leitores campo privado: numeros de leitores lendo
 * @param  escritores campo privado: numero de escritores escrevendo
 * @param  escesp campo privado: numero de escritores esperando
 */
public class Leitor_Escritor_esconly
{
	private int leitores;
	private int escritores;	
	private int escesp;
	public Leitor_Escritor_esconly()
	{
		escesp = 0;
		leitores = 0;
		escritores = 0;
	}
	/**
	 * Metodo responsavel por conceder acesso a um Leitor. Caso tenha alguma thread
	 * escrevendo ou algum escritor esperando, a mesma tera que esperar ate
	 * que receba um sinal para acordar
	 */
	public synchronized void EntraLeitor()
	{
		try
		{
			while(this.escritores > 0 || this.escesp>0)
			{
				wait();
			}
			this.leitores++;
		}
		catch(InterruptedException e)
		{}	
	}
	/**
	 * Metodo responsavel por avisar que um determinado leitor nao esta mais lendo. 
	 * Reduz o numero de leitores e notifica todas as threads que estiverem dormindo
	 */
	public synchronized void SaiLeitor()
	{
		leitores--;
		notifyAll();
	}
	/**
	 * Metodo responsavel por conceder acesso a um Escritor. Caso tenha alguma thread
	 * lendo ou alguma thread escrevendo, a mesma tera que esperar ate
	 * que receba um sinal para acordar
	 */
	public synchronized void EntraEscritor() 
	{
		try
		{
			escesp++;
			while(this.leitores>0 || this.escritores>0)
			{
				wait();
			}
			escesp--;
			escritores++;
		}
		catch(InterruptedException e)
		{}
	}
	/**
	 * Metodo responsavel por avisar que um determinado escritor nao esta mais escrevendo. 
	 * Reduz o numero de escritores e notifica todas as threads que estiverem dormindo
	 */
	public synchronized void SaiEscritor()
	{
		escritores--;
		notifyAll();
	}
}
