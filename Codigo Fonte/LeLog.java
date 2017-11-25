/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
/**
 *
 * @author gdsmattos
 */
public class LeLog {
    
	int[] buffer;
	int tamVetores;

	public LeLog()
	{
		tamVetores = 0;
	}

	boolean comparaVetores(int[] v1, int[] v2) {
		int i;
		for (i = 0; i < tamVetores; i++) {
			if (v1[i] != v2[i]) { return false; }
		}
		return true;
	}

	void inicializaBuffer(int size)
	{
		//buffer = (int*) malloc(sizeof(int) * size);
		buffer = new int[size];
		tamVetores = size;
		int i;
		for(i = 0; i < size; i++)
			buffer[i] = 0;
	}
	//vizu
	boolean op1(int tid, int vet[], int n) {
		if(!comparaVetores(buffer, vet))
		{
			System.out.println("Error in Operation #" + n);
			int i;
				System.out.print("[");
				for(i = 0; i < tamVetores; i++){
				System.out.print(" " + buffer[i] + " ");
				}
				System.out.println("]");
		    return false;
		}
		return true;
	}

	//alocaLivre ou alocaDado
	boolean op2(int tid, int pos, int vet[], int n) {
		if (buffer[pos] == 0) 
		{
			buffer[pos] = tid;
		}
		if(!comparaVetores(buffer, vet))
		{
			System.out.println("Error in Operation #" + n);
			int i;
			System.out.print("[");
			for(i = 0; i < tamVetores; i++)
				System.out.print(" " + buffer[i] + " ");
			System.out.println("]");
			return false;
		}
		return true;
	}

	//alocaDado
	/*void op3(int tid, int pos, int vet[], int n) {
		if (buffer[pos] == 0) {
			buffer[pos] = tid;
		}
		if(!comparaVetores(buffer, vet))
		{
			System.out.println("Error in Operation #" + n);
			int i;
			System.out.print("[");
			for(i = 0; i < tamVetores; i++)
				System.out.print(" " + buffer[i] + " ");
			System.out.println("]");
			return;
		}
	}*/
	//remove
	boolean op4(int tid, int pos, int vet[], int n) {
		if (buffer[pos] == tid) {
			buffer[pos] = 0;
		}
		if(!comparaVetores(buffer, vet))
		{
			System.out.println("Error in Operation #" + n);
			int i;
			System.out.print("[");
			for(i = 0; i < tamVetores; i++)
				System.out.print(" " + buffer[i] + " ");
			System.out.println("]");
			return false;
		}
		return true;
	}
    
}
