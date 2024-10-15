import java.awt.EventQueue;
import java.net.Socket; 
import java.io.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.Panel;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.Cursor;
import javax.swing.SwingConstants;
import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.SpringLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;

public class LoginClienteUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFrame frame;
	private JTextField txtFUname;
	private JLabel lblStatus;
	private int port=6006;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginClienteUI frame= new LoginClienteUI();
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
	public LoginClienteUI() {
		inicializador();
	}
	
	
	public void inicializador() {//Inicializa componentes do User Interface
		setTitle("LOGIN");
		setIconImage(Toolkit.getDefaultToolkit().getImage("atletLogo.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Panel panelBack = new Panel();
		panelBack.setBounds(0, 78, 434, 183);
		panelBack.setBackground(new Color(0, 128, 192));
		contentPane.add(panelBack);
		panelBack.setLayout(null);
		
		txtFUname = new JTextField();
		txtFUname.setToolTipText("Digite o Username");
		txtFUname.setBorder(UIManager.getBorder("ComboBox.editorBorder"));
		txtFUname.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 12));
		txtFUname.setBounds(26, 79, 251, 31);
		panelBack.add(txtFUname);
		txtFUname.setColumns(10);
		//#################################################################################
		
		JButton btnConectar = new JButton("CONECTAR");
		btnConectar.setIcon(new ImageIcon("networking.png"));
		btnConectar.setEnabled(false);
		
	    // Adiciona um DocumentListener ao campo de texto para bloquear o button Conectar se for vazio
	    txtFUname.getDocument().addDocumentListener(new DocumentListener() {
	        public void changedUpdate(DocumentEvent e) {
	            validateText();
	        }

	        public void removeUpdate(DocumentEvent e) {
	            validateText();
	        }

	        public void insertUpdate(DocumentEvent e) {
	            validateText();
	        }

	        // Método para validar o campo de texto e habilitar/desabilitar o botão
	        private void validateText() {
	            String text = txtFUname.getText();
	            // Se o texto não estiver vazio, habilita o botão, senão desabilita
	            btnConectar.setEnabled(text != null && !text.trim().isEmpty());
	        }
	    });
	    
		btnConectar.addActionListener(new ActionListener() { // Ação ao clicar no botão
		    public void actionPerformed(ActionEvent e) {
		        try {
		            // Tentativa de conexão para verificar o status do servidor
		            Socket statusSocket = new Socket("localhost", port);
		            DataInputStream statusInput = new DataInputStream(statusSocket.getInputStream());

		            String statusServer = statusInput.readUTF(); // Recebe o status do servidor
		            System.out.println("Status do servidor: " + statusServer);

		            if ("ON".equals(statusServer)) {
		                lblStatus.setText("ONLINE");
		                

		                // Obtém o username digitado
		                String id = txtFUname.getText();
		                    // Cria um novo socket para comunicação com o servidor
		                    Socket s = new Socket("localhost", port);
		                    DataInputStream inputStream = new DataInputStream(s.getInputStream());
		                    DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());

		                    // Envia o username ao servidor
		                    outputStream.writeUTF(id);

		                    // Recebe a resposta do servidor
		                    String msgDoServidor = new DataInputStream(s.getInputStream()).readUTF();

		                    if ("Username ja em uso!".equals(msgDoServidor)) {
		                        // Se o username já está em uso, exibe um aviso
		                        JOptionPane.showMessageDialog(frame, "Username já em uso!");
		                    } else {
		                        // Abre a janela do cliente e fecha a janela de registro
		                        ClienteUI cliente = new ClienteUI(id, s);
		                        cliente.setVisible(true);
		                        dispose();
		                    }
		                
		              }
		            

		            // Fechando a conexão do status
		            statusInput.close();
		            statusSocket.close();

		        } catch (Exception ex) {
		            // Se o servidor estiver offline ou qualquer outra exceção ocorrer
		            JOptionPane.showMessageDialog(frame, "Servidor Offline!");
		            lblStatus.setText("OFFLINE");
		        }
		    }
		});
		
		
		
		btnConectar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnConectar.setBorder(null);
		btnConectar.setForeground(new Color(255, 255, 255));
		btnConectar.setBackground(new Color(0, 193, 0));
		btnConectar.setFont(new Font("Yu Gothic Medium", Font.BOLD, 12));
		btnConectar.setBounds(307, 79, 117, 30);
		panelBack.add(btnConectar);
		
		JLabel lblNewLabel = new JLabel("Digite o Username");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(26, 47, 147, 21);
		panelBack.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Servidor:");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblNewLabel_1.setBounds(26, 147, 62, 25);
		panelBack.add(lblNewLabel_1);
		
		lblStatus = new JLabel("OFFLINE");
		lblStatus.setForeground(new Color(255, 255, 255));
		lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblStatus.setBounds(92, 149, 62, 22);
		panelBack.add(lblStatus);
		
		JPanel pheader = new JPanel();
		pheader.setBackground(new Color(255, 255, 255));
		pheader.setBounds(0, 0, 434, 74);
		contentPane.add(pheader);
		pheader.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		
		JLabel lbLogo = new JLabel("");
		lbLogo.setIcon(new ImageIcon("atletLogo.png"));
		pheader.add(lbLogo);
		
		JLabel lbHeader = new JLabel("Registo do Cliente");
		lbHeader.setFont(new Font("Yu Gothic Medium", Font.BOLD, 18));
		pheader.add(lbHeader);
	}
}
