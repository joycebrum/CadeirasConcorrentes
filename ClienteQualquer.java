public class ClienteQualquer extends Thread
{
	int id;
	Assento assento;
	Gerente gerente;
	
	public ClienteQualquer(int id, Gerente gerente)
	{
		this.gerente = gerente;
		this.id = id;
	}
	
	public void run()
	{
		gerente.visualiza(id);
		
		gerente.alocaLivre(assento, id);
		
		gerente.visualiza(id);
	}

}
