import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Gravar {

    // Método que grava uma mensagem no ficheiro
    public static void gravarMensagem(String mensagem) {
        // Caminho do ficheiro onde as mensagens serão salvas
        String caminhoFicheiro = "logServer.txt";

        // 'true' no FileWriter para adicionar (append) sem sobrescrever
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoFicheiro, true))) {
            writer.write(mensagem); // Escreve a mensagem
            writer.newLine(); // Adiciona uma nova linha após a mensagem
            System.out.println("Mensagem gravada: " + mensagem);
        } catch (IOException e) {
            System.err.println("Erro ao gravar mensagem: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Testando o método gravarMensagem
    	int n =10;
    	for(int i=0;i<n;i++) {
    		gravarMensagem("Primeira mensagem. "+i);
            gravarMensagem("Segunda mensagem."+(1+i));
            gravarMensagem("Terceira mensagem."+(7+i));
    	}
        
    }
}