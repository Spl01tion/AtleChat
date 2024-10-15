import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cli {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 6007);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

            // Criação do objeto Carro
        	Atletas atleta=new Atletas(23,"Lurdes Mutola",(byte)27,(byte)7,"Atletismo","Senior","100M-15segundos",(byte)25,(byte)15);
			

            // Envio do objeto para o servidor
            oos.writeObject(atleta);
            oos.flush();
            System.out.println("Objeto Carro enviado: " + atleta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}