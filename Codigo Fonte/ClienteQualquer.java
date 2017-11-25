/**
 * Esta classe e um dos tipos de thread o qual ira querer qualquer assento disponivel
 * <p>
 * chama os metodos da classe gerente
 *
 * @param  id campo que representa o id da thread
 * @param  assento campo para simbolizar o assento que o cliente pegou
 * @param  gerente campo utilizado para chamar os metodos necessarios para alocacao de assentos

 */

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
	/**
	 * Este metodo executa a thread, chamando todos os metodos que serao necessarios do gerente, neste caso o visualiza e o aloca livre.
	 */
	public void run()
	{
		gerente.visualiza(id);
		
		gerente.alocaLivre(assento, id);
		
		gerente.visualiza(id);
	}

}
