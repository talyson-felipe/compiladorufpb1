package analisador.sintaticosemantico;

import java.util.ArrayList;
import java.util.List;
import modelo.ErroSintatico;
import modelo.Token;
import modelo.Erro;
import modelo.tipos.TipoErroSintatico;
import modelo.tipos.TipoToken;

/**
 *
 * @author Clodoaldo Brasilino
 */
public class AnalisadorSintaticoSemantico {

    private List<Token> tokens;
    List<Erro> erros;
    public AnalisadorSintaticoSemantico(List<Token> tokens){
        this.tokens = new ArrayList<Token>(tokens);
    }

    public List<Erro> analisar(){
        erros = new ArrayList<Erro>();
        try {
            programa();
        } catch (ErroSintatico ex) {
            erros.add(ex);
        }
        return erros;
    }

    public void programa() throws ErroSintatico {
        consumir(TipoToken.PALAVRA_RESERVADA, "program");
        consumir(TipoToken.IDENTIFICADOR);
        consumir(TipoToken.DELIMITADOR, ";");
        declaracoesDeVariaveis();
        declaracoesDeSubprogramas();
        comandoComposto();
        consumir(TipoToken.DELIMITADOR, ".");
    }

    public void declaracoesDeVariaveis() throws ErroSintatico {

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "var")){
            consumir(TipoToken.PALAVRA_RESERVADA, "var");
            listaDeDeclaracoesDeVariaveis();
            return;
        }
        
        return; // VAZIO

    }

    public void listaDeDeclaracoesDeVariaveis() throws ErroSintatico {

        listaDeIdentificadores();
        consumir(TipoToken.DELIMITADOR, ":");
        tipo();
        consumir(TipoToken.DELIMITADOR, ";");
        listaDeDeclaracoesDeVariaveis1();

    }

    public void listaDeDeclaracoesDeVariaveis1() throws ErroSintatico {

        if (isProximoToken(TipoToken.IDENTIFICADOR)) {
            listaDeIdentificadores();
            consumir(TipoToken.DELIMITADOR, ":");
            tipo();
            consumir(TipoToken.DELIMITADOR, ";");
            listaDeDeclaracoesDeVariaveis1();
            return;
        }
        return; // VAZIO
    }

    public void listaDeIdentificadores() throws ErroSintatico {

        consumir(TipoToken.IDENTIFICADOR);
        listaDeIdentificadores1();
    }

    public void listaDeIdentificadores1() throws ErroSintatico {

        if (isProximoToken(TipoToken.DELIMITADOR, ",")) {
            consumir(TipoToken.DELIMITADOR, ",");
            consumir(TipoToken.IDENTIFICADOR);
            listaDeIdentificadores1();
            return;
        }

        return; // VAZIO
        
    }

    public void tipo() throws ErroSintatico {
        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "integer")) {
            consumir(TipoToken.PALAVRA_RESERVADA, "integer");
            return;
        }

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "real")){
            consumir(TipoToken.PALAVRA_RESERVADA, "real");
            return;
        }

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "boolean")){
            consumir(TipoToken.PALAVRA_RESERVADA, "boolean");
            return;
        }

        throw new ErroSintatico(lerTokenDaLista(), TipoErroSintatico.TOKEN_ESPERADO);

    }

    public void declaracoesDeSubprogramas() throws ErroSintatico {
        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "procedure")) {
            declaracaoDeSubprograma();
            consumir(TipoToken.DELIMITADOR, ";");
            declaracoesDeSubprogramas();
            return;
        }
        
        return; // VAZIO
    }

    public void declaracaoDeSubprograma() throws ErroSintatico {
        consumir(TipoToken.PALAVRA_RESERVADA, "procedure");
        consumir(TipoToken.IDENTIFICADOR);
        argumentos();
        consumir(TipoToken.DELIMITADOR, ";");
        declaracoesDeVariaveis();
        declaracoesDeSubprogramas();
        comandoComposto();
    }

    public void argumentos() throws ErroSintatico {
        if (isProximoToken(TipoToken.DELIMITADOR, "(")) {
            consumir(TipoToken.DELIMITADOR, "(");
            listaDeParametros();
            consumir(TipoToken.DELIMITADOR, ")");
            return;
        }
        
        return; // vazio
    }

    public void listaDeParametros() throws ErroSintatico {
        listaDeIdentificadores();
        consumir(TipoToken.DELIMITADOR, ":");
        tipo();
        listaDeParametros1();
    }

    public void listaDeParametros1() throws ErroSintatico {
        if (isProximoToken(TipoToken.DELIMITADOR, ";")) {
            consumir(TipoToken.DELIMITADOR, ";");
            listaDeIdentificadores();
            consumir(TipoToken.DELIMITADOR, ":");
            tipo();
            listaDeParametros1();
            return;
        }
        
        return; // VAZIO
    }

    public void comandoComposto() throws ErroSintatico {
        consumir(TipoToken.PALAVRA_RESERVADA, "begin");
        comandosOpcionais();
        consumir(TipoToken.PALAVRA_RESERVADA, "end");
    }

    public void comandosOpcionais() throws ErroSintatico {
        if (isProximoToken(TipoToken.IDENTIFICADOR) ||
            isProximoToken(TipoToken.PALAVRA_RESERVADA, "begin") ||
            isProximoToken(TipoToken.PALAVRA_RESERVADA, "if") ||
            isProximoToken(TipoToken.PALAVRA_RESERVADA, "while")) {
            listaDeComandos();
            return;
        }

        return; // VAZIO
    }

    // TODO Clodoaldo: Corrigir a lista de composto
    public void listaDeComandos() throws ErroSintatico {
        comando();
        listaDeComandos1();
    }

    public void listaDeComandos1() throws ErroSintatico {

        if (isProximoToken(TipoToken.IDENTIFICADOR) ||
            isProximoToken(TipoToken.PALAVRA_RESERVADA, "begin") ||
            isProximoToken(TipoToken.PALAVRA_RESERVADA, "if") ||
            isProximoToken(TipoToken.PALAVRA_RESERVADA, "while") ||
            isProximoToken(TipoToken.DELIMITADOR, ";")) {
            consumir(TipoToken.DELIMITADOR, ";");
            comando();
            listaDeComandos1();
            return;
        }

        return; // VAZIO
    }

    public void comando() throws ErroSintatico {
        if (isProximoToken(TipoToken.IDENTIFICADOR)) {
            if (isProximoToken(TipoToken.COMANDO_ATRIBUICAO, 1)){
                variavel();
                consumir(TipoToken.COMANDO_ATRIBUICAO);
                expressao();
                return;
            }
            if (isProximoToken(TipoToken.DELIMITADOR, "(", 1)) {
                ativacaoDeProcedimento();
                return;
            }
            
        }

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "begin")) {
            comandoComposto();
            return;
        }

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "if")) {
            consumir(TipoToken.PALAVRA_RESERVADA, "if");
            expressao();
            consumir(TipoToken.PALAVRA_RESERVADA, "then");
            comando();
            parteElse();
            return;
        }

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "while")) {
            consumir(TipoToken.PALAVRA_RESERVADA, "while");
            expressao();
            consumir(TipoToken.PALAVRA_RESERVADA, "do");
            comando();
            return;
        }

        return; // VAZIO
    }

    public void parteElse() throws ErroSintatico {
        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "else")) {
            consumir(TipoToken.PALAVRA_RESERVADA, "else");
            comando();
            return;
        }

        return; // VAZIO
    }

    public void variavel() throws ErroSintatico {
        consumir(TipoToken.IDENTIFICADOR);
    }

    public void ativacaoDeProcedimento() throws ErroSintatico {
        consumir(TipoToken.IDENTIFICADOR);
        ativacaoDeProcedimento1();
        
    }

    public void ativacaoDeProcedimento1() throws ErroSintatico {
        if (isProximoToken(TipoToken.DELIMITADOR, "(")) {
            consumir(TipoToken.DELIMITADOR, "(");
            listaDeExpressoes();
            consumir(TipoToken.DELIMITADOR, ")");
            return;
        }

        return; // VAZIO
    }

    public void listaDeExpressoes() throws ErroSintatico {
        expressao();
        listaDeExpressoes1();
    }

    public void listaDeExpressoes1() throws ErroSintatico {
        if (isProximoToken(TipoToken.DELIMITADOR, ",")) {
            consumir(TipoToken.DELIMITADOR, ",");
            expressao();
            listaDeExpressoes1();
            return;
        }

        return; // VAZIO
    }

    public void expressao() throws ErroSintatico {
        expressaoSimples();
        expressao1();
    }

    public void expressao1() throws ErroSintatico {

        if(isProximoToken(TipoToken.OPERADOR_RELACIONAL)) {
            operadorRelacional();
            expressaoSimples();
            return;
        }

        return; // VAZIO
    }

    public void expressaoSimples() throws ErroSintatico {
        if (isProximoToken(TipoToken.IDENTIFICADOR) ||
            isProximoToken(TipoToken.NUMERO_INTEIRO) ||
            isProximoToken(TipoToken.NUMERO_REAL) ||
            isProximoToken(TipoToken.PALAVRA_RESERVADA, "true") ||
            isProximoToken(TipoToken.PALAVRA_RESERVADA, "false") ||
            isProximoToken(TipoToken.DELIMITADOR, "(") ||
            isProximoToken(TipoToken.DELIMITADOR, ")") ||
            isProximoToken(TipoToken.PALAVRA_RESERVADA, "not")) {
            termo();
            expressaoSimples1();
            return;
        }

        if (isProximoToken(TipoToken.OPERADOR_ADITIVO)) {
            sinal();
            termo();
            expressaoSimples1();
            return;
        }

        throw new ErroSintatico(lerTokenDaLista(), TipoErroSintatico.TOKEN_ESPERADO);

    }

    public void expressaoSimples1() throws ErroSintatico {
        if (isProximoToken(TipoToken.OPERADOR_ADITIVO)) {
            operadorAditivo();
            termo();
            expressaoSimples1();
            return;
        }

        return; // VAZIO
    }

    public void termo() throws ErroSintatico {
        fator();
        termo1();
    }

    public void termo1() throws ErroSintatico {
        if (isProximoToken(TipoToken.OPERADOR_MULTIPLICATIVO)) {
            operadorMultiplicativo();
            fator();
            termo1();
            return;
        }

        return; // VAZIO
    }

    public void fator() throws ErroSintatico {

        if (isProximoToken(TipoToken.IDENTIFICADOR)) {
            consumir(TipoToken.IDENTIFICADOR);
            return;
        }

        if (isProximoToken(TipoToken.IDENTIFICADOR)) {
            consumir(TipoToken.IDENTIFICADOR);
            consumir(TipoToken.DELIMITADOR, "(");
            listaDeExpressoes();
            consumir(TipoToken.DELIMITADOR, ")");
            return;
        }

        if (isProximoToken(TipoToken.NUMERO_INTEIRO)) {
            consumir(TipoToken.NUMERO_INTEIRO);
            return;
        }
        
        if (isProximoToken(TipoToken.NUMERO_REAL)) {
            consumir(TipoToken.NUMERO_REAL);
            return;
        }
        
        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "true")) {
            consumir(TipoToken.PALAVRA_RESERVADA, "true");
            return;
        }

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "false")) {
            consumir(TipoToken.PALAVRA_RESERVADA, "false");
            return;
        }

        if (isProximoToken(TipoToken.DELIMITADOR, "(")) {
            consumir(TipoToken.DELIMITADOR, "(");
            expressao();
            consumir(TipoToken.DELIMITADOR, ")");
            return;
        }

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "not")) {
            consumir(TipoToken.PALAVRA_RESERVADA, "not");
            fator();
            return;
        }

        throw new ErroSintatico(lerTokenDaLista(), TipoErroSintatico.TOKEN_ESPERADO);

    }

    public void sinal() throws ErroSintatico {
        consumir(TipoToken.OPERADOR_ADITIVO);
    }

    public void operadorRelacional() throws ErroSintatico {
        consumir(TipoToken.OPERADOR_RELACIONAL);
    }

    public void operadorAditivo() throws ErroSintatico {
        consumir(TipoToken.OPERADOR_ADITIVO);
    }

    public void operadorMultiplicativo() throws ErroSintatico {

        if (isProximoToken(TipoToken.OPERADOR_MULTIPLICATIVO)) {
            consumir(TipoToken.OPERADOR_MULTIPLICATIVO);
            return;
        }

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "and")) {
            consumir(TipoToken.PALAVRA_RESERVADA, "and");
            return;
        }

        throw new ErroSintatico(lerTokenDaLista(), TipoErroSintatico.TOKEN_ESPERADO);
    }

    public void consumir(TipoToken token) throws ErroSintatico {
        consumir(token, null);
    }

    public void consumir(TipoToken tipoToken, String tokenName) throws ErroSintatico {
        Token atual = lerTokenDaLista();
        if (atual == null) {
            throw new ErroSintatico(atual, TipoErroSintatico.FIM_INESPERADO);
        }
        if (atual.getSimbolo() == tipoToken && tokenName == null) {
            consumirTokenDaLista();
        }
        else {
            if (atual.getSimbolo() == tipoToken && atual.getToken().equals(tokenName)) {
                consumirTokenDaLista();
            }
            else {
                throw new ErroSintatico(atual, new Token(tokenName, tipoToken, 0), TipoErroSintatico.TOKEN_ESPERADO);
            }
        }
    }

    private Token lerTokenDaLista() {
        try {
            return tokens.get(0);
        } catch (IndexOutOfBoundsException ex){
            return null;
        }
    }
    
    private Token lerTokenDaLista(int index) {
        try {
            return tokens.get(index);
        } catch (IndexOutOfBoundsException ex){
            return null;
        }
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


    public String imprimeErros(){
        String erroString="";
        ErroSintatico erroSintatico;
        for(Erro erro:erros){
                erroSintatico =(ErroSintatico)erro;
                erroString+=erroSintatico.errosToString();
         }
            return erroString;
    }

    private boolean isProximoToken(TipoToken tipoToken) {
        return isProximoToken(tipoToken, null);
    }

    private boolean isProximoToken(TipoToken tipoToken, String tokenName) {
        Token atual = lerTokenDaLista();
        if (atual == null) {
            return false;
        }
        if (atual.getSimbolo() == tipoToken && tokenName == null) {
            return true;
        }
        else {
            if (atual.getSimbolo() == tipoToken && atual.getToken().equals(tokenName)) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    private boolean isProximoToken(TipoToken tipoToken, int index) {
        return isProximoToken(tipoToken, null, index);
    }

    private boolean isProximoToken(TipoToken tipoToken, String tokenName,int index) {
        Token atual = lerTokenDaLista(index);
        if (atual == null) {
            return false;
        }
        if (atual.getSimbolo() == tipoToken && tokenName == null) {
            return true;
        }
        else {
            if (atual.getSimbolo() == tipoToken && atual.getToken().equals(tokenName)) {
                return true;
            }
            else {
                return false;
            }
        }
    }
}
