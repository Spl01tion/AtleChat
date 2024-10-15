import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.InputStreamReader;
import java.util.*;
import javax.swing.*;
public class Validacoes {
	
	private BufferedReader br;
	private SimpleDateFormat hora,data;
	public Validacoes() {
		br=new BufferedReader(new InputStreamReader(System.in));
		hora= new SimpleDateFormat("HH:mm");
		data=new SimpleDateFormat("dd/MM/yyyy");
	}
	
	//Metodo validar byte
			public byte validarByte(String intro, byte min,byte max) {
				byte val=0;
				do
				{
					
					try
					{
						
						
						val=Byte.parseByte(JOptionPane.showInputDialog(intro));
					}catch(NumberFormatException ex)
					{System.out.println(ex.getMessage());}
					catch(Exception e) {System.out.println(e.getMessage());}
					if(val<min||val>max)
					{
						JOptionPane.showMessageDialog(null, "Introduza novamente");
					}
				}while(val<min||val>max);
				return val;
			} 
			
	//Metodo para validar Int
		 public int validarInt(String msg,int min,int max) {
			 int num=0;
				do
				{
					try
					{
						System.out.println(msg);
						num=Integer.parseInt(br.readLine());
					}catch(NumberFormatException ex)
					{System.out.println(ex.getMessage());}
					catch(Exception e) {System.out.println(e.getMessage());}
					if(num<min||num>max)
					{
						System.out.println("Introduza novamente");
					}
				}while(num<min||num>max);
				return num;
			}
		 
		//Metodo para validar String
		  public String validarString(String msg,int tam) {
			  String intro="";
		        	  
			  do
				{
					
					try
					{
						
						intro=JOptionPane.showInputDialog(msg);
					}catch(NumberFormatException ex)
					{System.out.println(ex.getMessage());}
					catch(Exception e) {System.out.println(e.getMessage());}
					if(intro.length()<tam)
					{
						JOptionPane.showMessageDialog(null,"Tamanho indesejavel, Introduza novamente!");
					}
				}while(intro.length()<tam);
				return intro;

		  }
		  
		  //Metodo para validar Hora
		  public String validarHora(String msg,int min,int max) {
			 String intro="",dt1="";
			 int num=0;
			 boolean passou=false;
			 do {
				 
				 try
					{
						System.out.println(msg);
						intro=validarString("",5);
						if(intro.matches("[0-9]{2}[:]{1}[0-9]{2}")) {
							
							try {
								Date dt=hora.parse(intro);
								
								dt1=hora.format(dt);
								passou=true;

							}catch(ParseException e) {
								e.printStackTrace();
							}
						}else {
							
							System.out.println("Formato invalido");
						}
					}catch(NumberFormatException ex){System.out.println(ex.getMessage());}
					catch(Exception e) {System.out.println(e.getMessage());}
				 	

				 	if(passou=true)
					{
						
						num=Integer.parseInt(dt1.substring(0,2));
						if(num<min||num>max)
						{
							System.out.println("Horario nao Disponivel, Introduza novamente");
							passou=false;
						}
						
					}

			 }while(passou!=true);
			 
			 return intro;
		  }
		  
		  
		  //Metodo validar Data

		  public String validarData(String msg,int min,int max) {
			 String intro="",dt1="";
			 //int num=0;
			 boolean passou=false;
			 do {
				 
				 try
					{
						System.out.println(msg);
						intro=validarString("",10);
						if(intro.matches("[0-9]{2}[/]{1}[0-9]{2}[/]{1}[0-9]{4}")) {
							
							try {
								Date dt=data.parse(intro);
								
								dt1=data.format(dt);
								passou=true;

							}catch(ParseException e) {
								e.printStackTrace();
							}
						}else {
							
							System.out.println("Formato invalido");
						}
					}catch(NumberFormatException ex){System.out.println(ex.getMessage());}
					catch(Exception e) {System.out.println(e.getMessage());}
				 	

				 	/*if(passou=true)
					{
						
						num=Integer.parseInt(dt1.substring(0,2));
						if(num<min||num>max)
						{
							System.out.println("Horario nao Disponivel, Introduza novamente");
							passou=false;
						}
						
					}*/

			 }while(passou!=true);
			 
			 return intro;
		  }
}
