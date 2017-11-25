/**
 * Esta classe se comporta como um monitor a fim de proteger determinadas secoes do codigo
 * dividindo as classes entre leitoras e escritoras.
 * <p>
 * Este monitor da prioridade para leitura e permite escritas simultaneas (varios escritores
 * podem escrever ao mesmo tempo)
 *
 * @param  leitores campo privado: numeros de leitores lendo
 * @param  escritores campo privado: numero de escritores escrevendo
 * @param  leitesperando campo privado: numero de leitores esperando
 */
public class Leitor_Escritor
{
	private int leitores;
	private int escritores;
	private int leitesperando;
	public Leitor_Escritor()
	{
		this.leitores = 0;
		this.escritores = 0;
		this.leitesperando = 0;
	}
	/**
	 * Metodo responsavel por conceder acesso a um Leitor. Caso tenha alguma thread
	 * escrevendo, a mesma tera que esperar ate que receba um sinal para acordar
	 */
	public synchronized void EntraLeitor()
	{
		this.leitesperando++;
		try
		{
			while(this.escritores > 0)
			{
				wait();
			}
			this.leitesperando--;
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
		this.leitores--;
		notifyAll();
	}
	/**
	 * Metodo responsavel por conceder acesso a um Escritor. Caso tenha alguma thread
	 * lendo ou alguma thread leitora esperando, a mesma tera que esperar ate
	 * que receba um sinal para acordar
	 */
	public synchronized void EntraEscritor() //varias threads podem escrever ao mesmo tempo a classe Assento ja controla isto
	{
		try
		{
			while(this.leitores>0 || this.leitesperando>0)
			{
				wait();
			}
			this.escritores++;
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
		this.escritores--;
		notifyAll();
	}
}
