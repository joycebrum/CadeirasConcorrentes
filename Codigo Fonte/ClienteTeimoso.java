import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
/**
 * Esta classe e um dos tipos de thread o qual ira querer um assento especifico
 * <p>
 * chama os metodos da classe gerente
 *
 * @param  id campo que representa o id da thread
 * @param  assento campo para simbolizar o assento que o cliente pegou
 * @param  gerente campo utilizado para chamar os metodos necessarios para alocacao de assentos

 */

public class ClienteTeimoso extends Thread
{
	int id;
	Assento assento;
	Gerente gerente;
	public ClienteTeimoso(int id, Gerente gerente)
	{
		this.gerente = gerente;
		this.id = id;
	}
	/**
	 * Este metodo executa a thread, chamando todos os metodos que serao necessarios do gerente, neste caso o visualiza e o aloca dado.
	 */
	public void run()
	{
		gerente.visualiza(id);
		Assento tentativa = gerente.ListaAssentosLivres();
		
		if(gerente.alocaDado(tentativa,id))
		{
			assento = tentativa;
		}
		gerente.visualiza(id);
	}

}
