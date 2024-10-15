import java.io.Serializable;
public class Arbitros {
/*Arbitros (nome, idade, modalidade, anos experiência, 
 * província morada).*/
	private static final long serialVersionUID = 1L;
	private String nome,modalidade,morada;
	private byte idade,anosExperi;
	
	public Arbitros(String nome, String modalidade, String morada, byte idade, byte anosExperi) {
		super();
		this.nome = nome;
		this.modalidade = modalidade;
		this.morada = morada;
		this.idade = idade;
		this.anosExperi = anosExperi;
	}

	public Arbitros() {
		this("","","",(byte)0,(byte)0);
	}
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public String getMorada() {
		return morada;
	}

	public void setMorada(String morada) {
		this.morada = morada;
	}

	public byte getIdade() {
		return idade;
	}

	public void setIdade(byte idade) {
		this.idade = idade;
	}

	public byte getAnosExperi() {
		return anosExperi;
	}

	public void setAnosExperi(byte anosExperi) {
		this.anosExperi = anosExperi;
	}

	@Override
	public String toString() {
		return "Arbitro [\nNome- " + nome + "\nModalidade- " + modalidade + "\nMorada- " + morada + "\nIdade- " + idade
				+ "\nAnos de Experiencia- " + anosExperi + "]\n";
	}
	
	
}
