import java.awt.EventQueue;
import java.awt.Toolkit;
import java.net.Socket;
import java.util.*;
import java.util.StringTokenizer;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ImageIcon;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class CadastroObjUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private int port=6006;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CadastroObjUI frame = new CadastroObjUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CadastroObjUI() {
		setResizable(false);
		setTitle("AtleChat - Enviar Objectos");
		setIconImage(Toolkit.getDefaultToolkit().getImage("atletLogo.png"));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 447, 279);
		contentPane = new JPanel();
		contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		contentPane.setBackground(new Color(0, 128, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel pCadBtn = new JPanel();
		pCadBtn.setBackground(SystemColor.textHighlight);
		pCadBtn.setBounds(10, 52, 411, 177);
		contentPane.add(pCadBtn);
		
		JPanel pBtndados = new JPanel();
		pBtndados.setBackground(SystemColor.textHighlight);
		pCadBtn.add(pBtndados);
		GridBagLayout gbl_pBtndados = new GridBagLayout();
		gbl_pBtndados.columnWidths = new int[] {0, 0};
		gbl_pBtndados.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_pBtndados.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_pBtndados.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pBtndados.setLayout(gbl_pBtndados);
		
		JButton btnCamp = new JButton("Campeonato");
		btnCamp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Coletando dados do usuário usando JOptionPane
		        String tituloCamp = JOptionPane.showInputDialog("Informe o título do campeonato:");
		        String modalidade = JOptionPane.showInputDialog("Informe a modalidade:");
		        String local = JOptionPane.showInputDialog("Informe o local da realização:");
		        String periodo = JOptionPane.showInputDialog("Informe o período da realização:");
		        
		        // Recebendo o número de equipes como string e convertendo para int
		        int nrEquipes = Integer.parseInt(JOptionPane.showInputDialog("Informe o número de equipes:"));

		        // Criando o objeto Campeonatos com os dados fornecidos
		        Campeonatos campeonato = new Campeonatos(tituloCamp, modalidade, local, periodo, nrEquipes);

		        // Exibindo os dados do campeonato inserido
		        JOptionPane.showMessageDialog(null, "Dados do campeonato:\n" + campeonato.toString());
		        enviarObjecto(campeonato);
			}
		});
		btnCamp.setPreferredSize(new Dimension(155, 35));
		btnCamp.setIcon(new ImageIcon("mail-send.png"));
		btnCamp.setSelectedIcon(new ImageIcon("mail-send.png"));
		btnCamp.setBackground(new Color(255, 255, 255));
		btnCamp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCamp.setForeground(Color.BLACK);
		btnCamp.setBorder(null);
		btnCamp.setFont(new Font("Segoe UI", Font.BOLD, 14));
		GridBagConstraints gbc_btnCamp = new GridBagConstraints();
		gbc_btnCamp.insets = new Insets(0, 0, 5, 0);
		gbc_btnCamp.gridx = 0;
		gbc_btnCamp.gridy = 0;
		pBtndados.add(btnCamp, gbc_btnCamp);
		
		JButton btnClubes = new JButton("Clube");
		btnClubes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        // Coletando dados do usuário usando JOptionPane
		        String nome = JOptionPane.showInputDialog("Informe o nome do clube:");
		        String localizacao = JOptionPane.showInputDialog("Informe a localização:");
		        String nomePresi = JOptionPane.showInputDialog("Informe o nome do presidente:");
		        String treinadorPri = JOptionPane.showInputDialog("Informe o nome do treinador principal:");

		        // Criando o objeto Clubes com os dados fornecidos
		        Clubes clube = new Clubes(nome, localizacao, nomePresi, treinadorPri);

		        // Exibindo os dados do clube inserido
		        JOptionPane.showMessageDialog(null, "Dados do clube:\n" + clube.toString());
		        enviarObjecto(clube);
			}
		});
		btnClubes.setPreferredSize(new Dimension(155, 35));
		btnClubes.setIcon(new ImageIcon("mail-send.png"));
		btnClubes.setSelectedIcon(new ImageIcon("mail-send.png"));
		btnClubes.setBackground(new Color(255, 255, 255));
		btnClubes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClubes.setForeground(Color.BLACK);
		btnClubes.setBorder(null);
		btnClubes.setFont(new Font("Segoe UI", Font.BOLD, 14));
		GridBagConstraints gbc_btnClubes = new GridBagConstraints();
		gbc_btnClubes.insets = new Insets(0, 0, 5, 0);
		gbc_btnClubes.gridx = 0;
		gbc_btnClubes.gridy = 1;
		pBtndados.add(btnClubes, gbc_btnClubes);
		
		JButton btnAtleta = new JButton("Atleta");
		btnAtleta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Inserir o número de registro
	            int numero = Integer.parseInt(JOptionPane.showInputDialog("Digite o número de registro do atleta:"));

	            // Inserir o nome
	            String nome = JOptionPane.showInputDialog("Digite o nome do atleta:");
	            

	            // Inserir a idade
	            byte idade = Byte.parseByte(JOptionPane.showInputDialog("Digite a idade do atleta:"));
	            

	            // Inserir os anos no clube
	            byte anosClube = Byte.parseByte(JOptionPane.showInputDialog("Digite o número de anos no clube:"));
	            

	            // Inserir a modalidade
	            String modalidade = JOptionPane.showInputDialog("Digite a modalidade do atleta:");
	            

	            // Inserir a categoria (ex: senior, junior, infantil)
	            String categoria = JOptionPane.showInputDialog("Digite a categoria (senior, junior, infantil):");
	            

	            // Inserir as marcas registradas (ex: gols, tempos, etc.)
	            String marcas = JOptionPane.showInputDialog("Digite as marcas registradas do atleta (gols, tempos, etc.):");
	           

	            // Inserir o número de corridas/jogos nacionais
	            byte nrCorridas = Byte.parseByte(JOptionPane.showInputDialog("Digite o número de corridas/jogos nacionais:"));
	           

	            // Inserir o número de corridas/jogos internacionais
	            byte nrCorridaInter = Byte.parseByte(JOptionPane.showInputDialog("Digite o número de corridas/jogos internacionais:"));
	            Atletas atleta=new Atletas(numero,nome,idade,anosClube,modalidade,categoria,marcas,nrCorridas,nrCorridaInter);
	            JOptionPane.showMessageDialog(null, "Dados do Atleta:\n" + atleta.toString());
	            enviarObjecto(atleta);
			}
		});
		btnAtleta.setPreferredSize(new Dimension(155, 35));
		btnAtleta.setIcon(new ImageIcon("mail-send.png"));
		btnAtleta.setSelectedIcon(new ImageIcon("mail-send.png"));
		btnAtleta.setBackground(new Color(255, 255, 255));
		btnAtleta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnAtleta.setForeground(Color.BLACK);
		btnAtleta.setBorder(null);
		btnAtleta.setFont(new Font("Segoe UI", Font.BOLD, 14));
		GridBagConstraints gbc_btnAtleta = new GridBagConstraints();
		gbc_btnAtleta.insets = new Insets(0, 0, 5, 0);
		gbc_btnAtleta.gridx = 0;
		gbc_btnAtleta.gridy = 2;
		pBtndados.add(btnAtleta, gbc_btnAtleta);
		
		JButton btnArbitro = new JButton("Arbitro");
		btnArbitro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Coletando dados do usuário usando JOptionPane
		        String nome = JOptionPane.showInputDialog("Informe o nome do árbitro:");
		        String modalidade = JOptionPane.showInputDialog("Informe a modalidade:");
		        String morada = JOptionPane.showInputDialog("Informe a morada:");
		        
		        // Recebendo a idade e os anos de experiência como strings e convertendo para byte
		        byte idade = Byte.parseByte(JOptionPane.showInputDialog("Informe a idade:"));
		        byte anosExperi = Byte.parseByte(JOptionPane.showInputDialog("Informe os anos de experiência:"));

		        // Criando o objeto Arbitros com os dados fornecidos
		        Arbitros arbitro = new Arbitros(nome, modalidade, morada, idade, anosExperi);

		        // Exibindo os dados do árbitro inserido
		        JOptionPane.showMessageDialog(null, "Dados do árbitro:\n" + arbitro.toString());
		        enviarObjecto(arbitro);
			}
		});
		btnArbitro.setPreferredSize(new Dimension(155, 35));
		btnArbitro.setIcon(new ImageIcon("mail-send.png"));
		btnArbitro.setSelectedIcon(new ImageIcon("mail-send.png"));
		btnArbitro.setBackground(new Color(255, 255, 255));
		btnArbitro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnArbitro.setForeground(Color.BLACK);
		btnArbitro.setBorder(null);
		btnArbitro.setFont(new Font("Segoe UI", Font.BOLD, 14));
		GridBagConstraints gbc_btnArbitro = new GridBagConstraints();
		gbc_btnArbitro.gridx = 0;
		gbc_btnArbitro.gridy = 3;
		pBtndados.add(btnArbitro, gbc_btnArbitro);
		
		JLabel lblNewLabel = new JLabel("Selecione o Dado a Enviar");
		lblNewLabel.setForeground(SystemColor.text);
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 11, 219, 30);
		contentPane.add(lblNewLabel);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
		});
		btnVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnVoltar.setBorder(null);
		btnVoltar.setBackground(new Color(0, 128, 192));
		btnVoltar.setForeground(SystemColor.text);
		btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnVoltar.setIcon(new ImageIcon("seta-esquerda.png"));
		btnVoltar.setBounds(313, 11, 108, 29);
		contentPane.add(btnVoltar);
	}
	
	public void enviarObjecto(Object obj){
		try {
			Socket s = new Socket("localhost", port);
			ObjectInputStream obin=new ObjectInputStream(s.getInputStream());
			ObjectOutputStream obout=new ObjectOutputStream(s.getOutputStream());
			
			obout.writeObject(obj);
			obout.flush();
			obout.close();
			
			s.close();
			System.out.println(obout);
			dispose();
			
		}catch(Exception io) {
			io.printStackTrace();
		}
	}
 
}
