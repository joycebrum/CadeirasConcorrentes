/**
 * Esta classe e um dos tipos de thread o qual ira querer qualquer assento disponivel e depois desistira dele
 * <p>
 * chama os metodos da classe gerente
 *
 * @param  id campo que representa o id da thread
 * @param  assento campo para simbolizar o assento que o cliente pegou
 * @param  gerente campo utilizado para chamar os metodos necessarios para alocacao de assentos

 */

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
	
	/**
	 * Este metodo executa a thread, chamando todos os metodos que serao necessarios do gerente, neste caso o visualiza e o aloca livre e libera.
	 */
	
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
