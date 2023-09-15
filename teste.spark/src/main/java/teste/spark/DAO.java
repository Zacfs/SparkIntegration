package teste.spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
public class DAO {
	private Connection conexao;

	public DAO() {
		conexao = null;
		conectar();
	}

	public boolean conectar() {
		String driverName = "org.postgresql.Driver";
		String serverName = "localhost";
		String mydatabase = "JogoTeste";
		int porta = 5432;
		String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + mydatabase;
		String username = "ti2cc";
		String password = "ti@cc";
		boolean status = false;

		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao == null);
			System.out.println("Conexão efetuada com o postgres!");
		} catch (ClassNotFoundException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
		}

		return status;
	}

	public boolean close() {
		boolean status = false;

		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}

	public boolean inserirJogo(Jogo jogo) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement();
			st.executeUpdate("INSERT INTO \"public\".\"Jogo\" (id, nome, preco) " + "VALUES (" + jogo.getId()
					+ ", '" + jogo.getNome() + "', " + jogo.getPreco()+ ");");
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}

	public boolean atualizarJogo(Jogo jogo) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement();
			String sql = "UPDATE \"public\".\"Jogo\" SET nome = '" + jogo.getNome() + "', preco = " + jogo.getPreco()
					 + " WHERE id = " + jogo.getId();
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}

	public boolean excluirJogo(int id) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM \"public\".\"Jogo\" WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}
	public Jogo[] getJogos() {
		Jogo[] Jogos = null;

		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM \"public\".\"Jogo\"");
			if (rs.next()) {
				rs.last();
				Jogos = new Jogo[rs.getRow()];
				rs.beforeFirst();

				for (int i = 0; rs.next(); i++) {
					Jogos[i] = new Jogo(rs.getInt("id"), rs.getString("nome"), rs.getDouble("preco"));
				}
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return Jogos;
	}
	private List<Jogo> get(String orderBy) {
		List<Jogo> produtos = new ArrayList<Jogo>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM \"public\".\"Jogo\"" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Jogo p = new Jogo(rs.getInt("id"), rs.getString("nome"), (float)rs.getDouble("preco"));
	            produtos.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return produtos;
	}
	public List<Jogo> get() {
		return get("");
	}
	public List<Jogo> getOrderByID() {
		return get("id");		
	}
	
	
	public List<Jogo> getOrderByNome() {
		return get("nome");		
	}
	
	public List<Jogo> getOrderByPreco() {
		return get("preco");		
	}
	public Jogo get(int id) {
		Jogo produto = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM \"public\".\"Jogo\" WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 produto = new Jogo(rs.getInt("id"), rs.getString("nome"), (float)rs.getDouble("preco"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return produto;
	}
}