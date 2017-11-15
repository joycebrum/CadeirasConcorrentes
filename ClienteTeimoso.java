import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
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
	public void run()
	{
		gerente.visualiza(id);
		Assento tentativa = gerente.ListaAssentosLivres();
		
		System.out.println("abcd");
		if(gerente.alocaDado(tentativa,id))
		{
			assento = tentativa;
		}
		System.out.println("efgh");
		gerente.visualiza(id);
	}

}
