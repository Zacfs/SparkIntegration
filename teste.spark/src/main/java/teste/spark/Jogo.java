package teste.spark;

public class Jogo {
	private int id;
	private String nome;
	private double preco;
	public Jogo()
	{
		this.id=0;
		this.nome="";
		this.preco=0.0;
	}
	public Jogo(int id, String nome, double preco) {
		this.id = id;
		this.nome = nome;
		this.preco = preco;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}
}

