import java.io.Serializable;
public class Campeonatos implements Serializable {
/*Campeonato (titulo do campeonato, modalidade, local da realização, período da
realização, número de equipas envolvidas, …)*/
	private static final long serialVersionUID = 1L;
	private String tituloCamp,modalidade,local,periodo;
	private int nrEquipes;
	
	public Campeonatos(String tituloCamp, String modalidade, String local, String periodo, int nrEquipes) {
		super();
		this.tituloCamp = tituloCamp;
		this.modalidade = modalidade;
		this.local = local;
		this.periodo = periodo;
		this.nrEquipes = nrEquipes;
	}
	public Campeonatos() {
		this("","","","",0);
	}
	public String getTituloCamp() {
		return tituloCamp;
	}

	public void setTituloCamp(String tituloCamp) {
		this.tituloCamp = tituloCamp;
	}

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public int getNrEquipes() {
		return nrEquipes;
	}

	public void setNrEquipes(int nrEquipes) {
		this.nrEquipes = nrEquipes;
	}

	@Override
	public String toString() {
		return "Campeonato [\nTitulo do Campeonado- " + tituloCamp + "\nModalidade- " + modalidade + "\nLocal- " + local
				+ "\nPeriodo- " + periodo + "\nNr. Equipes- " + nrEquipes + "]\n";
	}
	
	
	
}
