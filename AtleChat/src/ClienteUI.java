import java.awt.EventQueue;
import java.awt.Toolkit;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.StringTokenizer;
import java.io.*;
import javax.swing.*;

import javax.swing.DefaultListModel;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.border.BevelBorder;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Window.Type;
import java.awt.Dialog.ModalExclusionType;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;
import javax.swing.border.SoftBevelBorder;
import java.awt.Cursor;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.io.Serializable;

import java.awt.Insets;
import javax.swing.border.LineBorder;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import java.awt.Dimension;

public class ClienteUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JPanel contentPane;
	
	private JTextField campoMensagem;
	private JRadioButton rdbUmparaN;
	private JRadioButton rdbBroadcast;
	private JTextArea areaMensagem;
	private JButton btnEndProcess;
	private JList listaOnusers;
	
	DataInputStream inputStream;
	DataOutputStream outStream;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	DefaultListModel<String> dm;
	
	String id, clientIds="";
	Validacoes val;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClienteUI frame = new ClienteUI();
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
	public ClienteUI() {
		iniciar();
		val=new Validacoes();
	}
	public void disDelay(){
		int delay = 1200; // 1.2 segundos
	    Timer timer = new Timer(delay, new ActionListener() {
	    	@Override
	        public void actionPerformed(ActionEvent evt) {
	    		dispose();
	    	}
	            
	    });
	    timer.setRepeats(false);
	    timer.start();
	}
	
	public String data() {
		LocalDateTime dataHoraAtual = LocalDateTime.now();
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	    String dataHora = dataHoraAtual.format(formatador);
	    
	    return dataHora;
	}
	
	public ClienteUI(String id, Socket s) {//Chamada do construtor, ira iniacializar variaveis necessarias
		iniciar();
		this.id=id;
		
		try {
			setTitle("AtleChat - ["+id+"]");// Define o titulo da janela com o nome do usuario
			dm=new DefaultListModel<String>();//default list para listar os usuarios ativos
			listaOnusers.setModel(dm);// Mostra a lista na componente JList
			
			inputStream=new DataInputStream(s.getInputStream());//inicialização de Input e Output Stream
			outStream= new DataOutputStream(s.getOutputStream());
			
			
			
			new Leitura().start();//cria uma nova Thread para para leitura das mensagens
			//new ObjLeitura().start();//Nova Threah para leitura de Objecto
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	//Thread de Leitura
	public class Leitura extends Thread{
		@Override
		public void run() {
			while(true) {
				try {
					String ms = inputStream.readUTF();//le a mensagem do servidor, contem :,;./= [virgula separa clientIds]
					//Object ob=ois.readObject(); System.out.println(ms +" "+ob);
					//registrar(ms);
					
					if(ms.contains(":;.,/=")) {
						ms=ms.substring(6);//virgula separa todos user ativos
						dm.clear();
						StringTokenizer st= new StringTokenizer(ms,",");//Separa todos clientesIds e adiciona no dm
						while(st.hasMoreTokens()) {
							String u=st.nextToken();
							if(!id.equals(u)) //Nao precisamos mostrar o nosso id
								dm.addElement(u);//adicione todos os IDs de usuário ativos ao defaultList para exibir no ativo
								
						}
						
					}else {
						areaMensagem.append("" + ms +"\n"); //caso contrário, imprima no quadro de mensagens do cliente
					}
					//registrar(ms);
				}catch(Exception ex) {
					//connection reset
					break;
				}
			}
		}
	}
	
	/*public class ObjLeitura extends Thread{
		@Override
		public void run() {
			while(true) {
				try {
					String ms = inputStream.readUTF();//le a mensagem do servidor, contem :,;./= [virgula separa clientIds]
					System.out.println("Conteudo Leitura Thread: "+ms);//print para testagem
					
					if(ms.contains(":;.,/=")) {
						ms=ms.substring(6);//virgula separa todos user ativos
						dm.clear();
						StringTokenizer st= new StringTokenizer(ms,",");//Separa todos clientesIds e adiciona no dm
						while(st.hasMoreTokens()) {
							String u=st.nextToken();
							if(!id.equals(u)) //Nao precisamos mostrar o nosso id
								dm.addElement(u);//adicione todos os IDs de usuário ativos ao defaultList para exibir no ativo
								
						}
						
					}else {
						areaMensagem.append("" + ms + "\n"); //caso contrário, imprima no quadro de mensagens do cliente
					}
					
				}catch(Exception ex) {
					//connection reset
					break;
				}
			}
		}
	}*/
	public void registrar(String ms) {
		
			try {
	            
				FileWriter fw = new FileWriter("logServer.txt");
				BufferedWriter bw = new BufferedWriter(fw);

	            // Escreve o texto no ficheiro
	            bw.write(ms);
	            bw.newLine();

	            // Fecha o ficheiro
	            bw.close();

	            
	        } catch (IOException e) {
	            System.out.println("Ocorreu um erro ao salvar o texto.");
	            e.printStackTrace();
	        }
		}
	//Inicia as componentes da Janela
		public void iniciar() {
			setResizable(false);
			setTitle("AtleChat");
			setIconImage(Toolkit.getDefaultToolkit().getImage("atletLogo.png"));
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 750, 500);
			//setUndecorated(true);
			contentPane = new JPanel();
			contentPane.setBackground(new Color(0, 128, 192));
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

			setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JPanel pHeaderChat = new JPanel();
			pHeaderChat.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255)));
			pHeaderChat.setBackground(new Color(0, 128, 192));
			FlowLayout fl_pHeaderChat = (FlowLayout) pHeaderChat.getLayout();
			fl_pHeaderChat.setHgap(50);
			pHeaderChat.setBounds(0, 0, 402, 37);
			contentPane.add(pHeaderChat);
			
			JPanel p1paraN = new JPanel();
			p1paraN.setBackground(new Color(0, 128, 192));
			FlowLayout flowLayout_1 = (FlowLayout) p1paraN.getLayout();
			flowLayout_1.setVgap(0);
			flowLayout_1.setHgap(0);
			pHeaderChat.add(p1paraN);
			
			JLabel lbl_Icon1toN = new JLabel("");
			lbl_Icon1toN.setIcon(new ImageIcon("conexao.png"));
			p1paraN.add(lbl_Icon1toN);
			
			rdbUmparaN = new JRadioButton("1 para N");
			rdbUmparaN.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					listaOnusers.setEnabled(true);
				}
			});
			p1paraN.add(rdbUmparaN);
			rdbUmparaN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			rdbUmparaN.setForeground(new Color(255, 255, 255));
			rdbUmparaN.setBackground(new Color(0, 128, 192));
			rdbUmparaN.setFont(new Font("Segoe UI", Font.BOLD, 11));
			rdbUmparaN.setSelected(true);
			rdbUmparaN.setHorizontalAlignment(SwingConstants.RIGHT);
			
			JPanel pBroadcast = new JPanel();
			pBroadcast.setBackground(new Color(0, 128, 192));
			FlowLayout flowLayout_2 = (FlowLayout) pBroadcast.getLayout();
			flowLayout_2.setVgap(0);
			flowLayout_2.setHgap(0);
			pHeaderChat.add(pBroadcast);
			
			JLabel lbl_IconBroadcast = new JLabel("");
			lbl_IconBroadcast.setIcon(new ImageIcon("conexao-global.png"));
			pBroadcast.add(lbl_IconBroadcast);
			
			rdbBroadcast = new JRadioButton("Broadcast");
			rdbBroadcast.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					listaOnusers.setEnabled(false);
				}
			});
			pBroadcast.add(rdbBroadcast);
			rdbBroadcast.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			rdbBroadcast.setBackground(new Color(0, 128, 192));
			rdbBroadcast.setForeground(new Color(255, 255, 255));
			rdbBroadcast.setFont(new Font("Segoe UI", Font.BOLD, 11));
			
			ButtonGroup btngrp = new ButtonGroup();
			btngrp.add(rdbUmparaN);
			btngrp.add(rdbBroadcast);
			
			JPanel pHeaderExit = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pHeaderExit.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			pHeaderExit.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255)));
			pHeaderExit.setBackground(new Color(0, 128, 192));
			pHeaderExit.setBounds(401, 0, 333, 37);
			contentPane.add(pHeaderExit);
			
			btnEndProcess = new JButton("Terminar Processo");
			btnEndProcess.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnEndProcess.setMnemonic('x');
			btnEndProcess.setForeground(new Color(255, 255, 255));
			btnEndProcess.setBorder(null);
			btnEndProcess.setFont(new Font("Segoe UI", Font.BOLD, 14));
			btnEndProcess.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						outStream.writeUTF("exit"); // Fecha a Thread e mostra a mensagem no servidor 
													
						areaMensagem.append("Voce foi desconectado.\n");
						
						
						disDelay();
						
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			btnEndProcess.setBackground(new Color(0, 128, 192));
			btnEndProcess.setIcon(new ImageIcon("sair24x24.png"));
			pHeaderExit.add(btnEndProcess);
			
			JPanel pActive = new JPanel();
			pActive.setBounds(507, 80, 217, 320);
			contentPane.add(pActive);
			pActive.setLayout(null);
			
			
			listaOnusers = new JList();
			listaOnusers.setBounds(0, 0, 217, 338);
			pActive.add(listaOnusers);
			
			JPanel pChat = new JPanel();
			pChat.setBounds(10, 46, 475, 354);
			contentPane.add(pChat);
			pChat.setLayout(null);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setBackground(new Color(192, 192, 192));
			scrollPane.setBounds(0, 0, 475, 354);
			pChat.add(scrollPane);
			
			areaMensagem = new JTextArea();
			scrollPane.setViewportView(areaMensagem);
			areaMensagem.setWrapStyleWord(true);
			areaMensagem.setEditable(false);
			
			campoMensagem = new JTextField();
			campoMensagem.setFont(new Font("Segoe UI", Font.BOLD, 13));
			campoMensagem.setBounds(10, 407, 382, 43);
			contentPane.add(campoMensagem);
			campoMensagem.setColumns(5);
			
			JPanel pSend = new JPanel();
			pSend.setBackground(new Color(0, 128, 192));
			pSend.setBounds(401, 407, 323, 43);
			contentPane.add(pSend);
			pSend.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 5));
			
			JButton btnEnviar = new JButton("ENVIAR");
			btnEnviar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					String textAreaMensagem = campoMensagem.getText(); // leva a mensagem do textbox
					
					if (textAreaMensagem != null && !textAreaMensagem.isEmpty()) {//somente se a mensagem nao esta vazia entao envia caso nao nao faz nada
						try {
							String msgParaoServer = "";
							String cast="broadcast";//iedntifica o tipo de mensagem
							int flag=0;//flag usada para verificar se o usado selecionou algum cliente ou não para multicast
							
							if(rdbUmparaN.isSelected()) {//Se 1 para N esta selecionado entao faz isto
								cast="multicast";
								List<String> clienteLista =listaOnusers.getSelectedValuesList();//obtem todos usuarios selecionados
								if(clienteLista.size()==0)//se nenhum usuário for selecionado, defina o sinalizador para uso posterior
									flag=1;
								for(String selecionadoUsr : clienteLista) {//anexe todos os nomes de usuário selecionados em uma variável
									if (clientIds.isEmpty())
										clientIds += selecionadoUsr;
									else
										clientIds += "," + selecionadoUsr;
									
								}
								msgParaoServer = cast + ":" +clientIds+":"+textAreaMensagem;//prepara mensagem para o servidor
							}else {
								msgParaoServer=cast + ":"+textAreaMensagem;//Neste caso se for broadcast nao precisamos mostrar o nosso 
							}
							if(cast.equalsIgnoreCase("multicast")) {
								if(flag==1) {//Para multicast verifica se nenhum usuario foi selecionado em seguida imprimi a mensagem
									JOptionPane.showMessageDialog(frame, "Nenhum Usuario foi selecionado");
								}else {
									outStream.writeUTF(msgParaoServer);
									campoMensagem.setText("");
									areaMensagem.append(" |"+data()+"| "+"< Voce enviou para " + clientIds + ">" + textAreaMensagem + "\n"); //show the sent message to the sender's message board
								}
							}else {//in case of broadcast
								outStream.writeUTF(msgParaoServer);
								campoMensagem.setText("");
								areaMensagem.append("|"+data()+"| "+"< Voce enviou a todos >" + textAreaMensagem  + "\n");
								
							}
							clientIds = ""; // limpa os ids
							
						}catch(Exception ex) {
							JOptionPane.showMessageDialog(frame, "Usuario nao existe mais."); // Se o usuario ja nao existe
						}
						
					}
				}
			});
			btnEnviar.setActionCommand("");
			btnEnviar.setPreferredSize(new Dimension(100, 35));
			btnEnviar.setMaximumSize(new Dimension(100, 23));
			btnEnviar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnEnviar.setIconTextGap(5);
			btnEnviar.setIcon(new ImageIcon("enviar-correio.png"));
			btnEnviar.setMnemonic('e');
			btnEnviar.setForeground(new Color(255, 255, 255));
			btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 12));
			btnEnviar.setBorder(null);
			btnEnviar.setBackground(new Color(0, 153, 255));
			pSend.add(btnEnviar);
			
			JButton btnEnviarObjecto = new JButton("Enviar Objecto");
			btnEnviarObjecto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean entrou=false; byte op,op1;
					Object obj=null;
					val=new Validacoes();
					
						List<String> clienteLista =listaOnusers.getSelectedValuesList();
						System.out.println(clienteLista);
						if(clienteLista.isEmpty()){
							JOptionPane.showMessageDialog(null, "Nenhum usuário selecionado!", 
				                    "Erro", JOptionPane.ERROR_MESSAGE);
						}else {
							
							do {
								op = val.validarByte("[SELECIONE UMA OPCAO]\n-----------| MENU |-----------\n1.Campeonato\n2.Clube\n3.Atleta\n4.Arbitro\n5.Enviar", (byte)1, (byte)5);
							    
								switch(op) {
								//Campeonato
								case 1:
									
									do {
										 op1 = val.validarByte("[SELECIONE UMA OPCAO]\n-----------| MENU 2 |-----------\n1.Inserir\n2.Editar Objecto\n3. Campeonato Maputo\n4.Campeonato Austral\n5.Confirmar para Enviar", (byte)1, (byte)5);
							                
										switch(op1) {
										case 1:
											entrou=true;
									        String tituloCamp = val.validarString("Informe o título do campeonato:",3);
									        String modalidade = val.validarString("Informe a modalidade:",3);
									        String local = val.validarString("Informe o local da realização:",3);
									        String periodo = val.validarString("Informe o período da realização:",3);
									        
									        
									        int nrEquipes = Integer.parseInt(JOptionPane.showInputDialog("Informe o número de equipes:"));

									        // Criando o objeto Campeonatos com os dados fornecidos
									        Campeonatos campeonato = new Campeonatos(tituloCamp, modalidade, local, periodo, nrEquipes);
									        
									        obj = campeonato;
			                                JOptionPane.showMessageDialog(null, "Campeonato registrado com sucesso:\n" + campeonato.toString());
											break;
										case 2:
											if(entrou==true) {	
												String novoTitulo=val.validarString("Informe o Novo título do campeonato:",3);
												((Campeonatos) obj).setTituloCamp(novoTitulo);
												
												String novamodalidade =val.validarString("Informe a Nova modalidade:",3);
												((Campeonatos) obj).setModalidade(novamodalidade);
												
										        String novolocal = val.validarString("Informe o Novo local da realização:",3);
										        ((Campeonatos) obj).setLocal(novolocal);
										        
										        String novoperiodo = val.validarString("Informe o Novo período da realização:",3);
										        ((Campeonatos) obj).setPeriodo(novoperiodo);
										        
										        int novonrEquipes = Integer.parseInt(JOptionPane.showInputDialog("Informe o Novo número de equipes:"));
										        ((Campeonatos) obj).setNrEquipes(novonrEquipes);
											}else {
												JOptionPane.showMessageDialog(null, "Inserir os dados primeiro\n");
											}
											
											break;
										case 3:
											campeonato=new Campeonatos("AtleChamp Maputo","Atletismo","Maputo","Setembro-Outubro",11);
											obj=campeonato;
											JOptionPane.showMessageDialog(null, "Campeonato carregado com sucesso:\n" + obj.toString());
											break;
										case 4:
											campeonato=new Campeonatos("AtleChamp Austral","Atletismo","Africa do Sul","Novembro-Dez",11);
											obj=campeonato;
											JOptionPane.showMessageDialog(null, "Campeonato carregado com sucesso:\n" + obj.toString());
											break;
										case 5:
											JOptionPane.showMessageDialog(null, "Pronto para Enviar");
											break;
										}
									}while(op1!=5);
									
									break;
								//Clube
								case 2:
									
									
									do {
										op1 = val.validarByte("[SELECIONE UMA OPCAO]\n-----------| MENU 2 |-----------\n1.Inserir\n2.Editar Objecto\n3.Confirmar para Enviar", (byte)1, (byte)3);
							              
										switch(op1) {
										case 1:
											entrou=true;
									        String nome = val.validarString("Informe o nome do clube:",3);
									        String localizacao = val.validarString("Informe a localização:",3);
									        String nomePresi = val.validarString("Informe o nome do presidente:",3);
									        String treinadorPri = val.validarString("Informe o nome do treinador principal:",3);

									        // Criando o objeto Clubes com os dados fornecidos
									        Clubes clube = new Clubes(nome, localizacao, nomePresi, treinadorPri);
									        
									        obj = clube;
			                                JOptionPane.showMessageDialog(null, "Atleta registrado com sucesso:\n" + clube.toString());
											break;
										case 2:
											if(entrou==true) {
												String novonome = val.validarString("Informe o Novo nome do clube:",3);
												((Clubes) obj).setNome(novonome);
										        String novalocalizacao = val.validarString("Informe a Nova localização:",3);
										        ((Clubes) obj).setLocalizacao(novalocalizacao);
										        String novonomePresi = val.validarString("Informe o Novo nome do presidente:",3);
										        ((Clubes) obj).setNomePresi(novonomePresi);
										        String novotreinadorPri = val.validarString("Informe o Novo nome do treinador principal:",3);
										        ((Clubes) obj).setTreinadorPri(novotreinadorPri);
											}else {
												JOptionPane.showMessageDialog(null, "Inserir os dados primeiro\n");
											}
											
											break;
										case 3:
											JOptionPane.showMessageDialog(null, "Pronto para Enviar");
											break;
										}
									}while(op1!=3);
									
									break;
								//Atleta
								case 3:
									//System.out.println("Case 3");
									
									do {
										op1 = val.validarByte("[SELECIONE UMA OPCAO]\n-----------| MENU 2 |-----------\n1.Inserir\n2.Editar Objecto\n3.Melhor Atleta\n4.Confirmar para Enviar", (byte)1, (byte)4);
							              
										switch(op1) {
										case 1:
											entrou=true;
											// Inserir o número de registro
								            int numero = Integer.parseInt(JOptionPane.showInputDialog("Digite o número de registro do atleta:"));
								            // Inserir o nome
								            String nome = val.validarString("Digite o nome do atleta:",3);
								            // Inserir a idade
								            byte idade = Byte.parseByte(JOptionPane.showInputDialog("Digite a idade do atleta:"));
								            // Inserir os anos no clube
								            byte anosClube = Byte.parseByte(JOptionPane.showInputDialog("Digite o número de anos no clube:"));
								       
								            // Inserir a modalidade
								            String modalidade = val.validarString("Digite a modalidade do atleta:",3);
								            
								            // Inserir a categoria (ex: senior, junior, infantil)
								            String categoria = val.validarString("Digite a categoria (senior, junior, infantil):",3);
								            
								            // Inserir as marcas registradas (ex: gols, tempos, etc.)
								            String marcas = val.validarString("Digite as marcas registradas do atleta (gols, tempos, etc.):",3);
								           
								            // Inserir o número de corridas/jogos nacionais
								            byte nrCorridas = Byte.parseByte(JOptionPane.showInputDialog("Digite o número de corridas/jogos nacionais:"));
								           
								            // Inserir o número de corridas/jogos internacionais
								            byte nrCorridaInter = Byte.parseByte(JOptionPane.showInputDialog("Digite o número de corridas/jogos internacionais:"));
			                                
			                                // Criar o objeto Atletas
			                                Atletas atleta = new Atletas(numero, nome, idade, anosClube, modalidade, categoria, marcas, nrCorridas, nrCorridaInter);
			                                obj = atleta;
			                                JOptionPane.showMessageDialog(null, "Atleta registrado com sucesso:\n" + atleta.toString());
											break;
										case 2:
											if(entrou==true) {
												// Inserir o número de registro
									            int novonumero = Integer.parseInt(JOptionPane.showInputDialog("Digite o Novo número de registro do atleta:"));
									            ((Atletas) obj).setNumero(novonumero);
									            // Inserir o nome
									            String novonome = val.validarString("Digite o Novo nome do atleta:",3);
									            ((Atletas) obj).setNome(novonome);
									            // Inserir a idade
									            byte novaidade = Byte.parseByte(JOptionPane.showInputDialog("Digite a Nova idade do atleta:"));
									            ((Atletas) obj).setIdade(novaidade);
									            // Inserir os anos no clube
									            byte novoanosClube = Byte.parseByte(JOptionPane.showInputDialog("Digite o Novo número de anos no clube:"));
									            ((Atletas) obj).setAnosClube(novoanosClube);
									       
									            // Inserir a modalidade
									            String novamodalidade = val.validarString("Digite a Nova modalidade do atleta:",3);
									            ((Atletas) obj).setModalidade(novamodalidade);
									            // Inserir a categoria (ex: senior, junior, infantil)
									            String novacategoria = val.validarString("Digite a Nova categoria (senior, junior, infantil):",3);
									            ((Atletas) obj).setNumero(novonumero);
									            
									            // Inserir as marcas registradas (ex: gols, tempos, etc.)
									            String novamarcas = val.validarString("Digite as Novas marcas registradas do atleta (gols, tempos, etc.):",3);
									            ((Atletas) obj).setMarcas(novamarcas);
									            
									            // Inserir o número de corridas/jogos nacionais
									            byte novonrCorridas = Byte.parseByte(JOptionPane.showInputDialog("Digite o novo número de corridas/jogos nacionais:"));
									            ((Atletas) obj).setNrCorridas(novonrCorridas);
									            // Inserir o número de corridas/jogos internacionais
									            byte novonrCorridaInter = Byte.parseByte(JOptionPane.showInputDialog("Digite o novo número de corridas/jogos internacionais:"));
									            ((Atletas) obj).setNrCorridaInter(novonrCorridaInter);
											}else {
												JOptionPane.showMessageDialog(null, "Inserir os dados primeiro\n");
											}
											
											break;
										case 3:
											atleta=new Atletas(23,"Lurdes Mutola",(byte)27,(byte)7,"Atletismo","Senior","100M-15segundos",(byte)25,(byte)15);
											obj=atleta;
											JOptionPane.showMessageDialog(null, "Atleta Carregado");
											break;
										case 4:
											JOptionPane.showMessageDialog(null, "Pronto para Enviar");
											break;
										}
									}while(op1!=4);
									break;
								//Arbitro
								case 4:
									
									
									do {
										op1 = val.validarByte("[SELECIONE UMA OPCAO]\n-----------| MENU 2 |-----------\n1.Inserir\n2.Editar Objecto\n3.Confirmar para Enviar", (byte)1, (byte)3);
							              
										switch(op1) {
										case 1:
											entrou=true;
											String nome = val.validarString("Informe o nome do árbitro:",3);
									        String modalidade = val.validarString("Informe a modalidade:",3);
									        String morada = val.validarString("Informe a morada:",3);
									        
									        // Recebendo a idade e os anos de experiência como strings e convertendo para byte
									        byte idade = Byte.parseByte(JOptionPane.showInputDialog("Informe a idade:"));
									        byte anosExperi = Byte.parseByte(JOptionPane.showInputDialog("Informe os anos de experiência:"));

									        // Criando o objeto Arbitros com os dados fornecidos
									        Arbitros arbitro = new Arbitros(nome, modalidade, morada, idade, anosExperi);
									        
									        obj = arbitro;
			                                JOptionPane.showMessageDialog(null, "Atleta registrado com sucesso:\n" + arbitro.toString());
											break;
										case 2:
											if(entrou==true) {
												String novonome = val.validarString("Informe o Novo nome do árbitro:",3);
												((Arbitros) obj).setNome(novonome);
										        String novamodalidade = val.validarString("Informe a Nova modalidade:",3);
										        ((Arbitros) obj).setModalidade(novamodalidade);
										        String novamorada = val.validarString("Informe a Nova morada:",3);
										        ((Arbitros) obj).setMorada(novamorada);
										        // Recebendo a idade e os anos de experiência como strings e convertendo para byte
										        byte novaidade = Byte.parseByte(JOptionPane.showInputDialog("Informe a Nova idade:"));
										        ((Arbitros) obj).setIdade(novaidade);
										        byte novoanosExperi = Byte.parseByte(JOptionPane.showInputDialog("Informe os Novos anos de experiência:"));
										        ((Arbitros) obj).setAnosExperi(novoanosExperi);
											}else {
												JOptionPane.showMessageDialog(null, "Inserir os dados primeiro\n");
											}
											
											break;
										case 3:
											JOptionPane.showMessageDialog(null, "Pronto para Enviar");
											break;
										}
									}while(op1!=3);
									break;
								case 5:
									JOptionPane.showMessageDialog(null, "Objecto Enviado\n");
									break;
								}
								
							}while(op!=5);
						}
					try {
						Socket socket = new Socket("localhost", 6007);
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						String msgParaoServer = "";
						String cast="broadcast";//iedntifica o tipo de mensagem
						int flag=0;//flag usada para verificar se o usado selecionou algum cliente ou não para multicast
						
						if(rdbUmparaN.isSelected()) {//Se 1 para N esta selecionado entao faz isto
							cast="multicast";
							
							if(clienteLista.size()==0)//se nenhum usuário for selecionado, defina o sinalizador para uso posterior
								flag=1;
							
							
							for(String selecionadoUsr : clienteLista) {//anexe todos os nomes de usuário selecionados em uma variável
								if (clientIds.isEmpty())
									clientIds += selecionadoUsr;
								else
									clientIds += "," + selecionadoUsr;
								
							}
							
							msgParaoServer = cast + ":" +clientIds+":"+"Objecto  ";//prepara mensagem para o servidor
						}else {
							msgParaoServer= cast + ":"+"Objecto  ";//Neste caso se for broadcast nao precisamos mostrar o nosso 
						}
						if(cast.equalsIgnoreCase("multicast")) {
							if(flag==1) {//Para multicast verifica se nenhum usuario foi selecionado em seguida imprimi a mensagem
								JOptionPane.showMessageDialog(frame, "Nenhum Usuario foi selecionado");
							}else {
								outStream.writeUTF(msgParaoServer);
								 oos.writeObject(obj); 
								campoMensagem.setText("");
								areaMensagem.append(" |"+data()+"| "+"< Voce enviou para " + clientIds + ">" + obj.toString() + "\n"); //show the sent message to the sender's message board
							}
						}else {//in case of broadcast
							outStream.writeUTF(msgParaoServer);
							oos.writeObject(obj); 
							oos.flush();
							campoMensagem.setText("");
							areaMensagem.append("\t|"+data()+"| "+"< Voce enviou a todos >" + obj.toString()  + "\n");
							
						}
						clientIds = ""; // limpa os ids
						
						
						
			           // oos.writeObject(obj);  // Serializa e envia o objeto
			            //System.out.println("Objeto enviado para o servidor: " + obj);
			           // oos.flush();
			            
					}catch(Exception ec) {
						ec.printStackTrace()	;				
					}
					
						/*try {	            
							
						}catch(Exception ex) {
							JOptionPane.showMessageDialog(frame, "Usuario nao existe mais."); // Se o usuario ja nao existe
						}*/
				
					}
				
				
		
			});
			btnEnviarObjecto.setPreferredSize(new Dimension(125, 35));
			btnEnviarObjecto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnEnviarObjecto.setMargin(new Insets(2, 14, 2, 21));
			btnEnviarObjecto.setIconTextGap(5);
			btnEnviarObjecto.setIcon(new ImageIcon("enviar_data.png"));
			btnEnviarObjecto.setMnemonic('o');
			btnEnviarObjecto.setForeground(new Color(255, 255, 255));
			btnEnviarObjecto.setFont(new Font("Segoe UI", Font.BOLD, 12));
			btnEnviarObjecto.setBorder(null);
			btnEnviarObjecto.setBackground(new Color(0, 153, 255));
			pSend.add(btnEnviarObjecto);
			
			JPanel pOnUsers = new JPanel();
			FlowLayout flowLayout_3 = (FlowLayout) pOnUsers.getLayout();
			flowLayout_3.setVgap(0);
			flowLayout_3.setHgap(0);
			pOnUsers.setBackground(new Color(0, 128, 192));
			pOnUsers.setBounds(507, 48, 217, 28);
			contentPane.add(pOnUsers);
			
			JLabel lblNewLabel = new JLabel("ON-Users");
			pOnUsers.add(lblNewLabel);
			lblNewLabel.setIcon(new ImageIcon("networking.png"));
			lblNewLabel.setForeground(new Color(255, 255, 255));
			lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
		}
		public void menu() {
		
			System.out.println("[SELECIONE UMA OPCAO]");
			System.out.println("-----------| MENU |-----------");
			System.out.println("1.Campeonato");				
			System.out.println("2.Clube");
			System.out.println("3.Atleta");
			System.out.println("4.Arbitro");
			System.out.println("5.Enviar");
			System.out.println("");
		}
		public void menu2(){
			System.out.println("[SELECIONE UMA OPCAO]");
			System.out.println("-----------| MENU 2 |-----------");
			System.out.println("1.Inserir");				
			System.out.println("2.Editar Objecto");
			System.out.println("3.Confirmar para Enviar");
			System.out.println("");
		}
}
