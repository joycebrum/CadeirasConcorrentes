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
	public synchronized void SaiLeitor()
	{
		this.leitores--;
		notifyAll();
	}
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
	public synchronized void SaiEscritor()
	{
		this.escritores--;
		notifyAll();
	}
}
