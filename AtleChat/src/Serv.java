
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Serv {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6007)) {
            System.out.println("Servidor aguardando conexão...");

            while (true) {
                try (Socket socket = serverSocket.accept();
                     ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

                    // Recebe o objeto enviado pelo cliente
                    Object obj = ois.readObject();

                    if (obj instanceof Atletas) {
                    	Atletas atleta = (Atletas) obj;
                        System.out.println("Objeto Carro recebido: " + atleta);
                    } else {
                        System.out.println("Tipo de objeto inválido");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
