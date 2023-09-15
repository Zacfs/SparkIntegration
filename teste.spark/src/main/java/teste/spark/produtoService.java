package teste.spark;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import spark.Request;
import spark.Response;


public class produtoService {

	private DAO dao = new DAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_NOME = 2;
	private final int FORM_ORDERBY_PRECO = 3;
	
	
	public produtoService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Jogo(), FORM_ORDERBY_NOME);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Jogo(), orderBy);
	}

	
	public void makeForm(int tipo, Jogo produto, int orderBy) {
		String nomeArquivo = "form.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umJogo = "";
		if(tipo != FORM_INSERT) {
			umJogo += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/produto/list/1\">Novo Jogo</a></b></font></td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t</table>";
			umJogo += "\t<br>";			
		}
		
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/produto/";
			String name, nome, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "inserirJogo";
				name = "Inserir Jogo";
				nome = "leite, pão, ...";
				buttonLabel = "Inserir";
			} else {
				action += "atualizarJogo/" + produto.getId();
				name = "Atualizar Jogo (ID " + produto.getId() + ")";
				nome = produto.getNome();
				buttonLabel = "Atualizar";
			}
			umJogo += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umJogo += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td>&nbsp;Nome: <input class=\"input--register\" type=\"text\" name=\"nome\" value=\""+ nome +"\"></td>";
			umJogo += "\t\t\t<td>Preco: <input class=\"input--register\" type=\"text\" name=\"preco\" value=\""+ produto.getPreco() +"\"></td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t</table>";
			umJogo += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umJogo += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Jogo (ID " + produto.getId() + ")</b></font></td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td>&nbsp;Nome: "+ produto.getNome() +"</td>";
			umJogo += "\t\t\t<td>Preco: "+ produto.getPreco() +"</td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td>&nbsp;</td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-PRODUTO>", umJogo);
		
		String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Jogos</b></font></td></tr>\n" +
				"\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td><a href=\"/produto/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
        		"\t<td><a href=\"/produto/list/" + FORM_ORDERBY_NOME + "\"><b>Nome</b></a></td>\n" +
        		"\t<td><a href=\"/produto/list/" + FORM_ORDERBY_PRECO + "\"><b>Preço</b></a></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Jogo> produtos;
		if (orderBy == FORM_ORDERBY_ID) {                 	produtos = dao.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_NOME) {		produtos = dao.getOrderByNome();
		} else if (orderBy == FORM_ORDERBY_PRECO) {			produtos = dao.getOrderByPreco();
		} else {											produtos = dao.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Jogo p : produtos) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td>" + p.getId() + "</td>\n" +
            		  "\t<td>" + p.getNome() + "</td>\n" +
            		  "\t<td>" + p.getPreco() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/produto/update/" + p.getId() + "\"><img src=\"/image/atualizarJogo.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/produto/delete/" +p.getId()+ "\"><img src=\"/image/excluirJogo.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<LISTAR-PRODUTO>", list);				
	}
	
	
	public Object inserirJogo(Request request, Response response) {
		int id = Integer.parseInt(request.queryParams("id"));
		String nome = request.queryParams("nome");
		float preco = Float.parseFloat(request.queryParams("preco"));
		String resp = "";
		
		Jogo produto = new Jogo(id, nome, preco);
		
		if(dao.inserirJogo(produto) == true) {
            resp = "Jogo (" + nome + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Jogo (" + nome + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Jogo produto = (Jogo) dao.get(id);
		
		if (produto != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, produto, FORM_ORDERBY_NOME);
        } else {
            response.status(404); // 404 Not found
            String resp = "Jogo " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Jogo produto = (Jogo) dao.get(id);
		
		if (produto != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, produto, FORM_ORDERBY_NOME);
        } else {
            response.status(404); // 404 Not found
            String resp = "Jogo " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object atualizarJogo(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Jogo produto = dao.get(id);
		System.out.println(produto.getId());
        String resp = "";       

        if (produto != null) {
        	produto.setNome(request.queryParams("nome"));
        	produto.setPreco(Float.parseFloat(request.queryParams("preco")));
        	dao.atualizarJogo(produto);
        	response.status(200); // success
            resp = "Jogo (ID " + produto.getId() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Jogo (ID \" + produto.getId() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object excluirJogo(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Jogo produto = dao.get(id);
        String resp = "";       

        if (produto != null) {
        	dao.excluirJogo(id);
            response.status(200); // success
            resp = "Jogo (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Jogo (" + id + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}
