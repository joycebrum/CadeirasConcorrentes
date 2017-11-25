/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gdsm
 */
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.*;



public class NewMain {

    /**
     * @param args the command line arguments
     */
     
    
    public static void main(String[] args) 
    {
        LeLog loguinho = new LeLog();
        int quantAsento;
        
        if(args.length<2)
        {
			System.out.printf("%d Entrada deve ser do tipo 'java <nomedoarquivo> <numerodeassentos>'", args.length);
			return;
		}
        
        quantAsento=Integer.parseInt(args[1]);
        String nomearq = args[0]; 
        int n = 0;
        //ArrayList<int[]> v = new ArrayList<>();
        int[] bla = new int[quantAsento];
        for(int i=0;i<quantAsento;i++)
        {
			bla[i]=0;
		} 
        loguinho.inicializaBuffer(quantAsento);
        
        processaLog(bla, loguinho, n, quantAsento, nomearq);
        
        
        
        
    }
    
    public static void processaLog(int bla[], LeLog l, int n, int quantAsento, String nomearq)
    {
        
        try{
            FileReader arq = new FileReader(nomearq);
            BufferedReader lerArq = new BufferedReader(arq);
            
            String linha = lerArq.readLine();
            
            //variáveis que serão utilizadas para construir o arrayList de teste
            //elas irão armazenar dados do .txt e aplicar a operação que precisar
            
           
            while(linha!=null)
            {		
				int contTemp = 0;
				int opType = 0;
				int tid=0;
				int pos = 0;
				int[] vetor = new int[quantAsento]; //ler
				int i = 0;
				char aChar = ' ';
				char simbolo = '.';
				int cont = 0;
				while(simbolo!='[')
				{
					
					while(true)
					{
						simbolo=linha.charAt(contTemp);
						contTemp++;
						if(simbolo==',')
							break;
						if(simbolo==aChar)
							continue;
						if(simbolo=='[')
							break;
							
						int valor = Character.getNumericValue(simbolo);
						if(cont==0)
						{
							opType=valor;
							continue;
						}
						else if(cont==1)
						{
							tid=tid*10+valor;
							continue;
						}
						else if(cont==2)
						{
							pos=pos*10+valor;
							continue;
						}
					}
					
					cont++;
				}
				cont=0;
				contTemp++;
				while(true)
				{
					
					simbolo=linha.charAt(contTemp);
					contTemp++;
					if(simbolo==']')
						break;
					if(simbolo==aChar)
					{
						cont++;
						continue;
					}
					int valor = Character.getNumericValue(simbolo);
					
					vetor[cont]=10*vetor[cont]+valor;
				}
				
				bla=vetor;
				/*if(n>519)
				{
					System.out.printf("optype="+opType+" tid="+tid);
				}*/
				
				if(opType==1)
				{
					if(!l.op1(tid, bla, n))
					{
						return;
					}
					n++;
				}
				else if(opType==4)
				{
					if(!l.op4(tid, pos, bla, n))
					{
						return;
					}
					n++;
				}
				else
				{
					if(!l.op2(tid, pos, bla, n))
					{
						return;
					}
                    n++;
				}
				
				
				linha = lerArq.readLine();
			}
			System.out.println("Todas as operacoes foram corretamente executadas\n");
        }
        
        catch(IOException e){
        System.err.printf("");
        }
        
        //tam 21 ou 24
        //codigo, id, numIdentificação, vetor assentos 
        //1 visualiza
        //2 alocaLivre
        //3 alocaDado
        //4 libera
    
        
    }
    
    
}
