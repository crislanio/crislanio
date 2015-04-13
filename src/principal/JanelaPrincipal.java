package principal;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.naming.directory.InvalidAttributesException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.JList;
import javax.swing.AbstractListModel;

public class JanelaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private No arvore;
	
	private JPanel contentPane;
	private JTextField textField;
	private JLabel lblNewLabel;
	private JList<String> list;
	private boolean colorir;
	
	
	public JanelaPrincipal(No a) {
		this.arvore = a;
		colorir = false;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 594);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		list = new JList<String>();
		list.setModel(new AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
		panel.add(list, BorderLayout.SOUTH);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.NORTH);
		
		textField = new JTextField();
		panel_2.add(textField);
		textField.setColumns(10);
		
		JButton btnAdicionar = new JButton("Adicionar");
		panel_2.add(btnAdicionar);
		
		JButton btnNewButton = new JButton("Remover");
		panel_2.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Buscar");
		panel_2.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean achou = ArvoreAVL.consultar(arvore, Integer.parseInt(textField.getText()), false);
				if(achou){
					JOptionPane.showMessageDialog(null, "Valor existe.");
					colorir = true;
					atualizaSequencia(Integer.parseInt(textField.getText()));
				} else {
					if(arvore == null)
						JOptionPane.showMessageDialog(null, "A arvore está vazia.");
					else
						JOptionPane.showMessageDialog(null, "Valor não existe.");
				}
				atualizaImagem();
				
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				try{
					arvore = ArvoreAVL.excluir(arvore, Integer.parseInt(textField.getText()));
					No novaArvore = null;
					ArvoreAVL.pegarValores(arvore);
					for(Integer num : ArvoreAVL.numeros){
						try {
							novaArvore = ArvoreAVL.inserir(novaArvore, num); 
						} catch (InvalidAttributesException e) { }
					}
					arvore = novaArvore;
					ArvoreAVL.numeros.clear();
				} catch(Exception e){
					JOptionPane.showMessageDialog(null, "A arvore está vazia. Impossivel remover.");
				}
				
				atualizaImagem();
				atualizaSequencia(0);
			}
		});
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					arvore = ArvoreAVL.inserir(arvore, Integer.parseInt(textField.getText()));
				} catch (NumberFormatException | InvalidAttributesException e) {
					JOptionPane.showMessageDialog(null, "Esse valor já existe.");
				}
				atualizaImagem();
				atualizaSequencia(0);
			}
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.GRAY);
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel = new JLabel("");
		panel_1.add(lblNewLabel);
		
		
	}
	
	private void atualizaImagem(){
		try {
			ArvoreAVL.savetofile(arvore, "arvore.out");
			Runtime.getRuntime().exec("python viewtree.py arvore.out arvore.png");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro: "+e.getMessage());
		} 
		
		try { Thread.sleep(2000); } catch (InterruptedException e) {}
		ImageIcon imagem = new ImageIcon("arvore.png");
		Image img = imagem.getImage().getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(), Image.SCALE_DEFAULT);
		if(arvore == null)
			lblNewLabel.setIcon(new ImageIcon());
		else
			lblNewLabel.setIcon(new ImageIcon(img));
		
	}
	
	private void atualizaSequencia(int numBuscado){
		DefaultListModel<String> listModel = new DefaultListModel<>();
		ExibirArvore.sequencia.clear();
		
		ExibirArvore.exibirpreOrdem(arvore);
		if(colorir == false)
			listModel.add(0, "Pre-ordem:            "+ExibirArvore.sequencia);
		else
			listModel.add(0, "<html>Pre-ordem:            "+ caminhoColorido(ExibirArvore.sequencia ,numBuscado));
		ExibirArvore.sequencia.clear();
		
		ExibirArvore.exibirpposOrdem(arvore);
		if(colorir == false)
			listModel.add(1, "Pos-ordem:            "+ExibirArvore.sequencia);
		else
			listModel.add(1, "<html>Pos-ordem:            "+caminhoColorido(ExibirArvore.sequencia ,numBuscado));
		ExibirArvore.sequencia.clear();
		
		ExibirArvore.exibirOrdemSimetrica(arvore);
		if(colorir == false)
			listModel.add(2, "Ordem simetrica:  "+ExibirArvore.sequencia);
		else
			listModel.add(2, "<html>Ordem simetrica:  "+caminhoColorido(ExibirArvore.sequencia, numBuscado));
		ExibirArvore.sequencia.clear();

		
		ExibirArvore.exibirNossaOrdem(arvore);
		if(colorir == false)
			listModel.add(3, "Ordem por nivel:   "+ExibirArvore.sequencia);
		else
			listModel.add(3, "<html>Ordem por nivel:   "+caminhoColorido(ExibirArvore.sequencia, numBuscado));
		
		colorir = false;
		list.setModel(listModel);
	}
	
	private String caminhoColorido(ArrayList<Integer> sequencia, int num){
		String caminho = "[";
		for(Integer i : sequencia){
			if(i == num){
				caminho += "<font color=\"red\">"+ i +"</font>, ";
				break;
			} else {
				caminho += "<font color=\"blue\">"+ i +"</font>, ";
			}
		}
		
		caminho += "]</hmtl>";
		return caminho;
	}
}
