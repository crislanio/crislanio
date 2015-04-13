package principal;

import java.util.ArrayList;
import java.util.LinkedList;

public class ExibirArvore {
	
	public static ArrayList<Integer> sequencia = new ArrayList<Integer>();
	
	public static void exibirpreOrdem(No no) {
	     if(no != null) {
	    	 sequencia.add(no.num);
	    	 exibirpreOrdem(no.esq);
	    	 exibirpreOrdem(no.dir);
	     }
	 }
	
	public static void exibirOrdemSimetrica(No no) {
	     if(no != null) {
	    	 exibirOrdemSimetrica(no.esq);
	    	 sequencia.add(no.num);
	    	 exibirOrdemSimetrica(no.dir);
	     }
	 }
	
	public static void exibirpposOrdem(No no) {
	     if(no != null) {
	    	 exibirpposOrdem(no.esq);
	    	 exibirpposOrdem(no.dir);
	    	 sequencia.add(no.num);
	     }
	 }
	
	
	public static void exibirNossaOrdem(No no) {
        if (no==null) return;
        LinkedList<No> fila = new LinkedList<No>();
        fila.add(no);
        
        while (!fila.isEmpty()) {
        	No q = fila.element();
            sequencia.add(q.num);
        	if (q.esq != null) {
            	fila.add(q.esq);
            } 
            if (q.dir != null) {
            	fila.add(q.dir);
            }
            fila.pop();
        }
    }

}
