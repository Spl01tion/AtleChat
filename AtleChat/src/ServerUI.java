import java.awt.EventQueue;
import java.awt.Toolkit;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.*;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import javax.swing.DefaultListModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.GridLayout;

public class ServerUI {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFrame frame=new JFrame();
	private JTextArea textArea;//variavel do campo de Mensagem do servidor
	
	
	private static int port=6006;//numero de porta que sera usada
	
	private static Map<String,Socket> todosUsersList= new ConcurrentHashMap<>();//Mapeia todos os usuarios
	
	private static Set<String> onUsersSet= new HashSet<>();//Este set serve para rastreiar usuarios ativos
	
	private JList todosUsernameList;
	
	private JList onUsuariosList;
	
	private DefaultListModel<String>onDlm=new DefaultListModel<String>();//Mantem list dos usuarios ativos
	
	private DefaultListModel<String>todosDlm=new DefaultListModel<String>();//Mantem lista de todos usuarios
	
	private ServerSocket serverSocket;//Variavel do Server Socket

	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerUI window = new ServerUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerUI() {
		iniciar();//Componentes da Janela sao inicializados
		try {
			serverSocket=new ServerSocket(port);//cria o socket para o servidor
			textArea.append("~ Servidor ON no port: "+port+"\n");
			textArea.append("~ Esperando por Usuarios...\n\n");
			new ServerStatus().start();//Cria uma nova Thread para monitorar o estado do servidor
			new ClienteAceito().start();// Cria uma nova Thread para Usuarios
		}catch(Exception ex) {
			
			JOptionPane.showMessageDialog(frame,"Servidor em Funcionamento no mesmo PORT");
			System.exit(0);
		}
		
	}
	
	public String data() {
		LocalDateTime dataHoraAtual = LocalDateTime.now();
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	    String dataHora = dataHoraAtual.format(formatador);
	    
	    return dataHora;
	}
	public class ServerStatus extends Thread{
		@Override
		public void run() {
			while(true) {
				try {
					Socket status=serverSocket.accept();
					DataOutputStream statusServer=new DataOutputStream(status.getOutputStream());
					//System.out.println("status="+statusServer);
					statusServer.writeUTF("ON");
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	public class ClienteAceito extends Thread{
		@Override
		public void run() {
			while(true) {
				try {
					Socket clienteSocket=serverSocket.accept();//cria um socket para o cliente
					String uName=new DataInputStream(clienteSocket.getInputStream()).readUTF();//Vai receber o Username criado pelo cliente no registo
					DataOutputStream cOutStream=new DataOutputStream(clienteSocket.getOutputStream());//Cria Output Stream do cliente
					
					if(onUsersSet!=null && onUsersSet.contains(uName)) {//Se o username esta em uso deve-se inserir outro
						cOutStream.writeUTF("Username ja em uso!");
					}else {
						todosUsersList.put(uName, clienteSocket);//Adiciona o user a lista de todos e OnusersSet
						onUsersSet.add(uName);
						cOutStream.writeUTF("");//Apaga a mensagem existente
						onDlm.addElement(uName);// adiciona o user na JList
						
						if(!todosDlm.contains(uName))
							todosDlm.addElement(uName);//se o nome de usuário for obtido anteriormente, não adicione a TodosUser JList, caso contrário, adicione-o
						
						onUsuariosList.setModel(onDlm);//Mostra o usuarios on na JList
						todosUsernameList.setModel(todosDlm);
						
						textArea.append(" |"+data()+"| "+"["+uName+"]=>CONECTADO \n");//IMPRIMI mensagem no server que cliente conectou-se
						new MsgLeitura (uName,clienteSocket).start();//Cria Thread para leitura de Mensagem
						new ObjLeitura(uName,clienteSocket).start();//Cria Thread para leitura de Objectos
						new PreparaClienteLista().start();//cria Thread para preparar lista de clientes
						
					}
				}catch(IOException ioex) {
					ioex.printStackTrace();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public class ObjLeitura extends Thread{
		Socket socket;
		String id;
		
		private ObjLeitura(String uname,Socket s) {
			this.socket=s;
			this.id=uname;
		}
		
		@Override
		public void run() {
			
			 try (ServerSocket serverSocket = new ServerSocket(6007)) {
		            

		            while (true) {
		                try (Socket socket = serverSocket.accept();
		                     ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

		                    // Recebe o objeto enviado pelo cliente
		                    Object obj = ois.readObject();

		                    if (obj instanceof Atletas) {
		                    	Atletas atleta = (Atletas) obj;
		                        
		                        textArea.append(obj.toString());
		                        
		                        String mensagem=new DataInputStream(socket.getInputStream()).readUTF();//le a mensagem do cliente
		    					textArea.append("\n"+mensagem);//imprimi a mensagem para testagens
		    					//registrar(mensagem);
		    					String[] msgList= mensagem.split(":");//identificador para decidir que accao tomar
		    					
		    					if(msgList[0].equalsIgnoreCase("multicast")) {//Se a accao é multicast entao envia mensagem para usuarios ativos selecionados
		    						String[] sendToList=msgList[1].split(",");//Contem lista dos clientes que irao receber a mensagem
		    						
		    						for(String usr : sendToList) {//para todo enviar a mensagem
		    							try {
		    								if(onUsersSet.contains(usr)) {//verifica de novo se o user esta online depois envia a mensagem
		    									new DataOutputStream(((Socket) todosUsersList.get(usr))
		    											.getOutputStream()).writeUTF(" |"+data()+"| "+"< "+id+" >");
		    									new ObjectOutputStream(((Socket) todosUsersList.get(usr))
		    											.getOutputStream()).writeObject(obj);
		    									
		    								}
		    							}catch(Exception e) {
		    								e.printStackTrace();
		    							}
		    						}
		    					} else if(msgList[0].equalsIgnoreCase("broadcast")) {//se é broadcast entao envia mensagem para todos ativos
		    						
		    						Iterator<String> itr1=todosUsersList.keySet().iterator();//interagi com todos users
		    						while(itr1.hasNext()) {
		    							String usrName=(String)itr1.next();//é o username
		    							
		    							if(!usrName.equalsIgnoreCase(id)) {//Nao precisamos enviar mensagem para nos
		    								try {
		    									if(onUsersSet.contains(usrName)) {//Se o cliente esta ativo entao envia a mensagem
		    										new DataOutputStream(((Socket)todosUsersList.get(usrName))
		    												.getOutputStream()).writeUTF("< "+id+" >");
		    										new ObjectOutputStream(((Socket) todosUsersList.get(usrName))
			    											.getOutputStream()).writeObject(obj);
		    									}else {
		    										//Se o usuario nao estiver Ativo enviar mensagem de desconectado
		    										new DataOutputStream(socket.getOutputStream())
		    										.writeUTF("Mensagem nao pode ter sido entrega ao "+usrName+" status:Desconectado");
		    									}
		    									
		    								}catch(Exception e) {
		    									e.printStackTrace();
		    								}
		    								
		    							}
		    						}
		    						
		    						
		    					}
		                        
		                    } else {
		                        System.out.println("Tipo de objeto inválido");
		                    }

		                } catch (Exception e) {
		                    e.printStackTrace();
		                }
		            }
		        } catch (Exception e) {
		            
		        }
		}
	}
	public class MsgLeitura extends Thread{//Esta class le as mensagens vindo do cliente e faz o devido tratamento
		Socket s;
		String id;
		
		private  MsgLeitura(String uname,Socket s) {//Socket e Username serao providos pelo cliente
			this.s=s;
			this.id=uname;
			
		}
		
		@Override
		public void run() {
			while(todosUsernameList !=null && !todosUsersList.isEmpty()) {//se a  todosUsernameList nao esta vazia
				try {
					String mensagem=new DataInputStream(s.getInputStream()).readUTF();//le a mensagem do cliente
					textArea.append("\n"+mensagem);//imprimi a mensagem para testagens
					//registrar(mensagem);
					String[] msgList= mensagem.split(":");//identificador para decidir que accao tomar
					
					if(msgList[0].equalsIgnoreCase("multicast")) {//Se a accao é multicast entao envia mensagem para usuarios ativos selecionados
						String[] sendToList=msgList[1].split(",");//Contem lista dos clientes que irao receber a mensagem
						
						for(String usr : sendToList) {//para todo enviar a mensagem
							try {
								if(onUsersSet.contains(usr)) {//verifica de novo se o user esta online depois envia a mensagem
									new DataOutputStream(((Socket) todosUsersList.get(usr))
											.getOutputStream()).writeUTF(" |"+data()+"| "+"< "+id+" >"+msgList[2]);
									
								}
							}catch(Exception e) {
								e.printStackTrace();
							}
						}
					} else if(msgList[0].equalsIgnoreCase("broadcast")) {//se é broadcast entao envia mensagem para todos ativos
						
						Iterator<String> itr1=todosUsersList.keySet().iterator();//interagi com todos users
						while(itr1.hasNext()) {
							String usrName=(String)itr1.next();//é o username
							
							if(!usrName.equalsIgnoreCase(id)) {//Nao precisamos enviar mensagem para nos
								try {
									if(onUsersSet.contains(usrName)) {//Se o cliente esta ativo entao envia a mensagem
										new DataOutputStream(((Socket)todosUsersList.get(usrName))
												.getOutputStream()).writeUTF("< "+id+" >"+msgList[1]);
									}else {
										//Se o usuario nao estiver Ativo enviar mensagem de desconectado
										new DataOutputStream(s.getOutputStream())
										.writeUTF("Mensagem nao pode ter sido entrega ao "+usrName+" status:Desconectado");
									}
									
								}catch(Exception e) {
									e.printStackTrace();
								}
								
							}
						}
						
						
					}else if(msgList[0].equalsIgnoreCase("exit")) {//Se o cliente é terminado o processo notifica os outros clientes
						onUsersSet.remove(id);//remove o cliente da lista de online
						textArea.append(" |"+data()+"| ["+ id + "]=> DESCONECTADO! \n");//Imprime a mensagem na area de mensagem do servidor
						
						new PreparaClienteLista().start();//atualiza a lista de usuarios
						
						Iterator<String> itr=onUsersSet.iterator();//iterar por todos os elementos presentes onUsersSet
						
						while(itr.hasNext()) {
							String usrName2=(String)itr.next();
							
							if(!usrName2.equalsIgnoreCase(id)) {//Nao precisamos enviar a mensagem para nos
								try {
									new DataOutputStream(((Socket) todosUsersList.get(usrName2)).getOutputStream()).writeUTF(" |"+data()+"| ["+ id + "]=> DESCONECTADO! \n");
									
								}catch(Exception e) {
									e.printStackTrace();
								}
								new PreparaClienteLista().start();//Atualiza a Lista de usuarios ativos para todos clientes apos a desconexão
								
							}
						}
						onDlm.removeElement(id)	;//Remove o cliente da JList
						onUsuariosList.setModel(onDlm);
					}
					//registrar(mensagem);
				}catch(Exception e) {
					
				}
				
				
			}
		}
	}
	

	public class PreparaClienteLista extends Thread{//Prepara lista de OnUser para visualizar no UI
		@Override
		public void run() {
			try {
				String ids="";
				Iterator itr=onUsersSet.iterator();//itera todos os users on
				while(itr.hasNext()) {//prepara string para todos users
					String key=(String)itr.next();
					ids +=key + ",";
;				}
				if(ids.length() !=0) {//ajustando a lista com corte
					ids=ids.substring(0,ids.length()-1);
				}
				itr=onUsersSet.iterator();
				while(itr.hasNext()) {
					String key = (String) itr.next();
					try {
						new DataOutputStream(((Socket) todosUsersList.get(key)).getOutputStream())
								.writeUTF(":;.,/=" + ids); // set output stream and send the list of active users with identifier prefix :;.,/=
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/*public void registrar(String ms) {
		String dado="----------------------------------\n"+data()+"\n----------------------------------\n"+ms+"\n----------------------------------\n";
        
		try {
			FileWriter fw = new FileWriter("logServer.txt");
			BufferedWriter bw = new BufferedWriter(fw);

            // Escreve o texto no ficheiro
            bw.write(dado);
            bw.newLine();

            // Fecha o ficheiro
            bw.close();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao salvar o texto.");
            e.printStackTrace();
        }
	}*/
	public void iniciar() {
		frame.setResizable(false);
		frame.setTitle("AtleChat SERVER");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("atletLogo.png"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 11, 479, 439);
		contentPane.add(textArea);
		textArea.setText("Iniciando o Servidor...\n");
		
		JPanel pListUser = new JPanel();
		pListUser.setBackground(SystemColor.textHighlight);
		pListUser.setBounds(493, 11, 281, 439);
		contentPane.add(pListUser);
		pListUser.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Usuarios Ativos");
		lblNewLabel.setBounds(0, 0, 281, 21);
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
		pListUser.add(lblNewLabel);
		
		onUsuariosList = new JList();
		onUsuariosList.setBounds(0, 22, 281, 188);
		onUsuariosList.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		pListUser.add(onUsuariosList);
		
		JLabel lblTodosUser = new JLabel("Todos Usuarios");
		lblTodosUser.setForeground(Color.WHITE);
		lblTodosUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblTodosUser.setBounds(0, 229, 281, 21);
		pListUser.add(lblTodosUser);
		
		todosUsernameList = new JList();
		todosUsernameList.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		todosUsernameList.setBounds(0, 251, 281, 188);
		pListUser.add(todosUsernameList);
	}
}
