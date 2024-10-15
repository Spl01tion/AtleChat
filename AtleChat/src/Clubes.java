import java.io.Serializable;
public class Clubes {
/*Clubes (nome do clube, localização, 
 * nome do presidente, treinador principal)*/
	
	private static final long serialVersionUID = 1L;
	private String nome,localizacao,nomePresi,treinadorPri;

	public Clubes(String nome, String localizacao, String nomePresi, String treinadorPri) {
		super();
		this.nome = nome;
		this.localizacao = localizacao;
		this.nomePresi = nomePresi;
		this.treinadorPri = treinadorPri;
	}
	public Clubes() {
		this("","","","");
	}
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public String getNomePresi() {
		return nomePresi;
	}

	public void setNomePresi(String nomePresi) {
		this.nomePresi = nomePresi;
	}

	public String getTreinadorPri() {
		return treinadorPri;
	}

	public void setTreinadorPri(String treinadorPri) {
		this.treinadorPri = treinadorPri;
	}

	@Override
	public String toString() {
		return "Clube [\nNome- " + nome + "\nLocalizacao- " + localizacao + "\nNome do Presidente- " + nomePresi + "\nTreinador Principal- "
				+ treinadorPri + "]\n";
	}
	
	
	
	
}
