public class ClienteIndeciso extends Thread
{
	int id;
	Gerente gerente;
	Assento assento;
	public ClienteIndeciso(int id, Gerente gerente)
	{
		this.gerente = gerente;
		this.id = id;
		this.assento=null;
	}
	
	public void run()
	{
		
		gerente.visualiza(id);//lista nao sera mais necessario retornar
		
		assento = gerente.alocaLivre(assento, id);
		

		
		gerente.visualiza(id);
		
		if(assento!=null)
		{
			gerente.libera(assento, id);
		}
		
		gerente.visualiza(id);
	}

}
