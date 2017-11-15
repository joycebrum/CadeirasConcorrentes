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
	public synchronized void SaiLeitor()
	{
		leitores--;
		notifyAll();
	}
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
	public synchronized void SaiEscritor()
	{
		escritores--;
		notifyAll();
	}
}
