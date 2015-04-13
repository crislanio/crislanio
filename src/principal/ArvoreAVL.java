package principal;

import java.awt.dnd.InvalidDnDOperationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.naming.directory.InvalidAttributesException;


public class ArvoreAVL {
	
	public static ArrayList<Integer> numeros = new ArrayList<Integer>();

	public static No inserir(No aux, int num) throws InvalidAttributesException {
		No novo;
		
		if(consultar(aux, num, false))
			throw new InvalidAttributesException();

		if (aux == null) {
			novo = new No();
			novo.num = num;
			novo.altd = 0;
			novo.alte = 0;
			novo.esq = null;
			novo.dir = null;
			aux = novo;
		} else if (num < aux.num) {
			aux.esq = inserir(aux.esq, num);
			if (aux.esq.altd > aux.esq.alte) {
				aux.alte = aux.esq.altd + 1;
			} else {
				aux.alte = aux.esq.alte + 1;
			}
			aux = balanceamento(aux);

		} else {
			aux.dir = inserir(aux.dir, num);
			if (aux.dir.altd > aux.dir.alte) {
				aux.altd = aux.dir.altd + 1;
			} else {
				aux.altd = aux.dir.alte + 1;
			}

			aux = balanceamento(aux);

		}
		return aux;
	}

	public static No balanceamento(No aux) {
		int d, df;
		d = aux.altd - aux.alte;

		if (d == 2) {
			df = aux.dir.altd - aux.dir.alte;
			if (df >= 0) {
				aux = rotacao_esquerda(aux);
			} else {
				aux.dir = rotacao_direita(aux.dir);
				aux = rotacao_esquerda(aux);
			}

		} else if (d == -2) {
			df = aux.esq.altd - aux.esq.alte;

			if (df <= 0) {
				aux = rotacao_direita(aux);
			} else {
				aux.esq = rotacao_esquerda(aux.esq);
				aux = rotacao_direita(aux);
			}
		}
		
		return aux;
	}

	public static No rotacao_esquerda(No aux) {

		No aux1, aux2;
		aux1 = aux.dir;
		aux2 = aux1.esq;
		aux.dir = aux2;
		aux1.esq = aux;

		if (aux.dir == null) {
			aux.altd = 0;
		} else if (aux.dir.alte > aux.dir.altd) {
			aux.altd = aux.dir.alte + 1;
		} else {
			aux.altd = aux.dir.altd + 1;
		}

		if (aux1.esq.alte > aux1.esq.altd) {
			aux1.alte = aux1.esq.alte + 1;
		} else {
			aux1.alte = aux1.esq.altd + 1;
		}
		return aux1;
	}

	public static No rotacao_direita(No aux) {

		No aux1, aux2;
		aux1 = aux.esq;
		aux2 = aux1.dir;
		aux.esq = aux2;
		aux1.dir = aux;

		if (aux.esq == null) {
			aux.alte = 0;
		} else if (aux.esq.alte > aux.esq.altd) {
			aux.alte = aux.esq.alte + 1;
		} else {
			aux.alte = aux.esq.altd + 1;
		}

		if (aux1.dir.alte > aux1.dir.altd) {
			aux1.altd = aux1.dir.alte + 1;
		} else {
			aux1.altd = aux1.dir.altd + 1;
		}
		return aux1;
	}
	
	/*
	 * Pega todos os valores da arvore e colocar no arraylist numeros
	 */
	public static void pegarValores(No no) {
	     if(no != null) {
	    	 pegarValores(no.esq);
	    	 numeros.add(no.num);
	    	 pegarValores(no.dir);
	     }
	 }
	
	public static No excluir(No aux, int num) throws InvalidDnDOperationException{
		
		if(aux == null)
			throw new InvalidDnDOperationException();
		
        No p, p2;
        if (aux.num == num) {
            if (aux.esq == aux.dir) {
                return null;
            } else if (aux.esq == null) {
                return aux.dir;
            } else if (aux.dir == null) {
                return aux.esq;
            } else {
                p2 = aux.dir;
                p = aux.dir;
                while (p.esq != null) {
                    p = p.esq;
                }
                p.esq = aux.esq;
                return p2;
            }
        } else if (aux.num < num) {
            aux.dir = excluir(aux.dir, num);
        } else {
            aux.esq = excluir(aux.esq, num);
        }
        return aux;
    }
 
    public static No atualizar(No aux) {
        if (aux != null) {
            aux.esq = atualizar(aux.esq);
            if (aux.esq == null) {
                aux.alte = 0;
            } else if (aux.esq.alte > aux.esq.altd) {
                aux.alte = aux.esq.alte + 1;
            } else {
                aux.alte = aux.esq.altd + 1;
            }
            aux.dir = atualizar(aux.dir);
            if (aux.dir == null) {
                aux.alte = 0;
            } else if (aux.dir.alte > aux.dir.altd) {
                aux.altd = aux.dir.alte + 1;
            } else {
                aux.altd = aux.dir.altd + 1;
            }
            aux = balanceamento(aux);
        }
        return aux;
    }
 
    public static boolean consultar(No aux, int num, boolean loc) {
        if (aux != null && loc == false) {
            if (aux.num == num) {
                loc = true;
            } else if (num < aux.num) {
                loc = consultar(aux.esq, num, loc);
            } else {
                loc = consultar(aux.dir, num, loc);
            }
        }
        return loc;
    }
	
	public static void savetofile(No no, String fnome) throws IOException {
        if (no==null) return;
        LinkedList<No> fila = new LinkedList<No>();
        fila.add(no);
        
        File file = new File(fnome);
        if (!file.exists()) {
			file.createNewFile();
		}
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
        
        
        while (!fila.isEmpty()) {
        	No q = fila.element();
        	bw.write(q.num + " ");
            if (q.esq != null) {
            	fila.add(q.esq);
            	bw.write(" [" + q.esq.num + "] ");
            } else
                bw.write(" [] ");
            if (q.dir != null) {
            	fila.add(q.dir);
            	bw.write(" [" + q.dir.num + "] ");
            } else
            	bw.write(" [] ");
            bw.write("\n");
            fila.pop();
        }
        bw.close();
    }

}
