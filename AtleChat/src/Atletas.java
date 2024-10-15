import java.io.Serializable;
public class Atletas implements Serializable{
/*Jogadores/Atletas (numero registo, nome, idade, anos no clube, modalidade do
atleta/jogador, categoria [senior/junior/infantil], marcas
registadas[golos/tempos/etc], numero jogos nacionais, numero jogos
internacionais);*/
	private static final long serialVersionUID = 1L;
	private int numero;
	private String nome,modalidade,categoria,marcas;
	private byte idade,nrCorridas,nrCorridaInter;
	private byte anosClube;
	
	
	public Atletas(int numero, String nome, byte idade, byte anosClube, String modalidade, String categoria,
			String marcas, byte nrCorridas, byte nrCorridaInter) {
		super();
		this.numero = numero;
		this.nome = nome;
		this.idade = idade;
		this.anosClube = anosClube;
		this.modalidade = modalidade;
		this.categoria = categoria;
		this.marcas = marcas;
		this.nrCorridas = nrCorridas;
		this.nrCorridaInter = nrCorridaInter;
	}
	public Atletas() {
		this(0,"",(byte)0,(byte)0,"","","",(byte)0,(byte)0);
	}
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public byte getIdade() {
		return idade;
	}

	public void setIdade(byte idade) {
		this.idade = idade;
	}

	public byte getAnosClube() {
		return anosClube;
	}

	public void setAnosClube(byte anosClube) {
		this.anosClube = anosClube;
	}

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getMarcas() {
		return marcas;
	}

	public void setMarcas(String marcas) {
		this.marcas = marcas;
	}

	public byte getNrCorridas() {
		return nrCorridas;
	}

	public void setNrCorridas(byte nrCorridas) {
		this.nrCorridas = nrCorridas;
	}

	public byte getNrCorridaInter() {
		return nrCorridaInter;
	}

	public void setNrCorridaInter(byte nrCorridaInter) {
		this.nrCorridaInter = nrCorridaInter;
	}

	@Override
	public String toString() {
		return "Atleta [Numero- " + numero + " Nome- " + nome + " Idade- " + idade + " Anos de Clube- " + anosClube
				+ " Modalidade- " + modalidade + " Categoria- " + categoria + " Marcas- " + marcas + " Nr. Corridas- "
				+ nrCorridas + " Nr. Corridas Internacionais- " + nrCorridaInter + "]";
	}
	
	
	
}
