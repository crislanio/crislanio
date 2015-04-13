package principal;

import java.io.IOException;

import javax.naming.directory.InvalidAttributesException;

public class Controlador {

	public static void main(String[] args) throws InterruptedException, IOException {
		No a = null;	
		try {
			a = ArvoreAVL.inserir(a, 1);
			a = ArvoreAVL.inserir(a, 2);
			a = ArvoreAVL.inserir(a, 3);
			a = ArvoreAVL.inserir(a, 4);
			a = ArvoreAVL.inserir(a, 5);
			a = ArvoreAVL.inserir(a, 6);
			a = ArvoreAVL.inserir(a, 7);
			a = ArvoreAVL.inserir(a, 8);
			a = ArvoreAVL.inserir(a, 9);
			a = ArvoreAVL.inserir(a, 10);
		} catch (InvalidAttributesException e) {
		}
		JanelaPrincipal jp = new JanelaPrincipal(a);
		jp.setVisible(true);
	}

}
