package analisador.sintatico;

import java.util.List;
import modelo.Token;
import modelo.tipos.TipoToken;

/**
 *
 * @author Clodoaldo Brasilino
 */
public class AnalisadorSintatico {

    private List<Token> tokens;

    public AnalisadorSintatico(List<Token> tokens){
        this.tokens = tokens;
    }

    

    public void programa(){
        consumir(TipoToken.PALAVRA_RESERVADA, "program");
        consumir(TipoToken.IDENTIFICADOR);
        consumir(TipoToken.DELIMITADOR);
        declaracoesDeVariaveis();
        declaracoesDeSubprogramas();
        comandoComposto();
        consumir(TipoToken.DELIMITADOR, ".");
    }

    public void declaracoesDeVariaveis(){
        consumir(TipoToken.PALAVRA_RESERVADA, "var");
        listaDeDeclaracoesDeVariaveis();
        // OU
        return; // vazio
    }

    public void listaDeDeclaracoesDeVariaveis(){
        listaDeIdentificadores();
        consumir(TipoToken.DELIMITADOR, ":");
        tipo();
        consumir(TipoToken.DELIMITADOR, ";");
        listaDeDeclaracoesDeVariaveis1();

    }

    public void listaDeDeclaracoesDeVariaveis1(){

        listaDeIdentificadores();
        consumir(TipoToken.DELIMITADOR, ":");
        tipo();
        consumir(TipoToken.DELIMITADOR, ";");
        listaDeDeclaracoesDeVariaveis1();
        // OU
        return; // vazio
    }

    public void listaDeIdentificadores(){

        consumir(TipoToken.IDENTIFICADOR);
        listaDeIdentificadores1();
    }

    public void listaDeIdentificadores1(){

        consumir(TipoToken.DELIMITADOR, ",");
        consumir(TipoToken.IDENTIFICADOR);
        listaDeIdentificadores1();
        // OU
        return; // vazio
        
    }

    public void tipo(){
        consumir(TipoToken.PALAVRA_RESERVADA, "integer");
        // OU
        consumir(TipoToken.PALAVRA_RESERVADA, "real");
        // OU
        consumir(TipoToken.PALAVRA_RESERVADA, "boolean");
    }

    public void declaracoesDeSubprogramas(){
        declaracaoDeSubprograma();
        consumir(TipoToken.DELIMITADOR, ";");
        declaracoesDeSubprogramas();
        // OU
        return; // vazio
    }

    public void declaracaoDeSubprograma(){
        consumir(TipoToken.PALAVRA_RESERVADA, "procedure");
        consumir(TipoToken.IDENTIFICADOR);
        argumentos();
        consumir(TipoToken.DELIMITADOR, ";");
        declaracoesDeVariaveis();
        declaracoesDeSubprogramas();
        comandoComposto();
    }

    public void argumentos(){
        consumir(TipoToken.DELIMITADOR, "(");
        listaDeParametros();
        consumir(TipoToken.DELIMITADOR, ")");
        // ou
        return; // vazio
    }

    public void listaDeParametros(){
        listaDeIdentificadores();
        consumir(TipoToken.DELIMITADOR, ":");
        tipo();
        listaDeParametros1();
    }

    public void listaDeParametros1(){
        consumir(TipoToken.DELIMITADOR, ";");
        listaDeIdentificadores();
        consumir(TipoToken.DELIMITADOR, ":");
        tipo();
        listaDeParametros1();
        // OU
        return; //vazio
    }

    public void comandoComposto(){
        consumir(TipoToken.PALAVRA_RESERVADA, "begin");
        comandosOpcionais();
        consumir(TipoToken.PALAVRA_RESERVADA, "end");
    }

    public void comandosOpcionais(){
        listaDeComandos();
        // OU
        return; // vazio
    }

    public void listaDeComandos(){
        comando();
        listaDeComandos1();
    }

    public void listaDeComandos1(){
        consumir(TipoToken.DELIMITADOR, ";");
        comando();
        listaDeComandos1();
        // OU
        return; // vazio
    }

    public void comando(){
        variavel();
        consumir(TipoToken.COMANDO_ATRIBUICAO);
        expressao();
        //OU
        ativacaoDeProcedimento();
        //ou
        comandoComposto();
        // ou
        consumir(TipoToken.PALAVRA_RESERVADA, "if");
        expressao();
        consumir(TipoToken.PALAVRA_RESERVADA, "then");
        comando();
        parteElse();
        // ou
        consumir(TipoToken.PALAVRA_RESERVADA, "while");
        expressao();
        consumir(TipoToken.PALAVRA_RESERVADA, "do");
        comando();
    }

    public void parteElse(){
        consumir(TipoToken.PALAVRA_RESERVADA, "else");
        comando();
        // ou
        return; // vazio
    }

    public void variavel(){
        consumir(TipoToken.IDENTIFICADOR);
    }

    public void ativacaoDeProcedimento(){
        consumir(TipoToken.IDENTIFICADOR);
        // ou
        consumir(TipoToken.IDENTIFICADOR);
        consumir(TipoToken.DELIMITADOR, "(");
        listaDeExpressoes();
        consumir(TipoToken.DELIMITADOR, ")");
    }

    public void listaDeExpressoes(){
        expressao();
        listaDeExpressoes1();
    }

    public void listaDeExpressoes1(){
        consumir(TipoToken.DELIMITADOR, ",");
        expressao();
        listaDeExpressoes1();
        // ou
        return; // vazio
    }

    public void expressao(){
        expressaoSimples();
        // ou
        expressaoSimples();
        operadorRelacional();
        expressaoSimples();
    }

    public void expressaoSimples(){
        termo();
        expressaoSimples1();
        // ou
        sinal();
        termo();
        expressaoSimples1();
    }

    public void expressaoSimples1(){
        operadorAditivo();
        termo();
        expressaoSimples1();
        // ou
        return; // vazio
    }

    public void termo(){
        fator();
        termo1();
    }

    public void termo1(){
        operadorMultiplicativo();
        fator();
        termo1();
        // ou
        return; // vazio
    }

    public void fator(){
        consumir(TipoToken.IDENTIFICADOR);
        // ou
        consumir(TipoToken.IDENTIFICADOR);
        consumir(TipoToken.DELIMITADOR, "(");
        listaDeExpressoes();
        consumir(TipoToken.DELIMITADOR, ")");
        // ou
        consumir(TipoToken.NUMERO_INTEIRO);
        // ou
        consumir(TipoToken.NUMERO_REAL);
        // ou
        consumir(TipoToken.PALAVRA_RESERVADA, "true");
        // ou
        consumir(TipoToken.PALAVRA_RESERVADA, "false");
        // ou
        consumir(TipoToken.DELIMITADOR, "(");
        expressao();
        consumir(TipoToken.DELIMITADOR, ")");
        // ou
        consumir(TipoToken.PALAVRA_RESERVADA, "not");
        fator();

    }

    public void sinal(){
        consumir(TipoToken.OPERADOR_ADITIVO);
    }

    public void operadorRelacional(){
        consumir(TipoToken.OPERADOR_RELACIONAL);
    }

    public void operadorAditivo(){
        consumir(TipoToken.OPERADOR_ADITIVO);
    }

    public void operadorMultiplicativo(){
        consumir(TipoToken.OPERADOR_MULTIPLICATIVO);
        // ou
        consumir(TipoToken.PALAVRA_RESERVADA, "and");
    }

    public Boolean consumir(TipoToken token) {
        return consumir(token, null);
    }

    public Boolean consumir(TipoToken tipoToken, String tokenName) {
        Token atual = lerTokenDaLista();
        if (atual == null) {
            return false;
        }
        if (atual.getSimbolo() == tipoToken || tokenName == null) {
            consumirTokenDaLista();
            return true;
        }
        else {
            if (atual.getSimbolo() == tipoToken || atual.getToken().equals(tokenName)) {
                consumirTokenDaLista();
                return true;
            }
            else {
                return false;
            }
        }
    }

    private Token lerTokenDaLista() {
        return tokens.get(0);
    }
    private Token consumirTokenDaLista() {
        Token atual = null;
        try {
            atual = tokens.get(0);
            tokens.remove(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        return atual;
    }
}
