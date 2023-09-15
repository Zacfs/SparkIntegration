package teste.spark;

import static spark.Spark.*;

public class Aplicacao {
	
	private static produtoService produtoService = new produtoService();
	
    public static void main(String[] args) {
        port(6789);

        post("/produto/insert", (request, response) -> produtoService.inserirJogo(request, response));

        get("/produto/:id", (request, response) -> produtoService.get(request, response));
        
        get("/produto/list/:orderby", (request, response) -> produtoService.getAll(request, response));

        get("/produto/update/:id", (request, response) -> produtoService.getToUpdate(request, response));
        
        post("/produto/update/:id", (request, response) -> produtoService.atualizarJogo(request, response));
           
        get("/produto/delete/:id", (request, response) -> produtoService.excluirJogo(request, response));

               
    }
}