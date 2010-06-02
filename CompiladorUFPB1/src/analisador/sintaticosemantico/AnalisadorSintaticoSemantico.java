package analisador.sintaticosemantico;

import analisador.semantico.ChecagemDeTipos;
import analisador.semantico.Escopo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import modelo.ErroSemantico;
import modelo.ErroSintatico;
import modelo.Token;
import modelo.Erro;
import modelo.Variavel;
import modelo.VariavelJaDeclaradaException;
import modelo.tipos.TipoErroSintatico;
import modelo.tipos.TipoToken;
import modelo.tipos.TipoVariavel;

/**
 *
 * @author Clodoaldo Brasilino
 */
public class AnalisadorSintaticoSemantico {

    private Escopo escopoAtual = null;
    private List<Token> tokens;
    private List<Erro> erros = null;
    private Set<Token> conjuntoDeDeclaracoes;
    private ChecagemDeTipos checagemAtribuicao;
    private ChecagemDeTipos checagemExpressao;

    public AnalisadorSintaticoSemantico(List<Token> tokens){
        this.tokens = new ArrayList<Token>(tokens);
    }

    public List<Erro> analisar(){
        erros = new ArrayList<Erro>();
        try {
            programa();
        } catch (ErroSintatico ex) {
            erros.add(ex);
        } catch (ErroSemantico ex) {
            erros.add(ex);
        }
        return erros;
    }

    private void criarEscopo(){
        escopoAtual = new Escopo(escopoAtual);
    }

    private void destruirEscopo(){
        escopoAtual = escopoAtual.getPai();
    }

    public void programa() throws ErroSintatico, ErroSemantico {
        consumir(TipoToken.PALAVRA_RESERVADA, "program");
        consumir(TipoToken.IDENTIFICADOR);
        consumir(TipoToken.DELIMITADOR, ";");
        criarEscopo();
        declaracoesDeVariaveis();
        declaracoesDeSubprogramas();
        comandoComposto();
        consumir(TipoToken.DELIMITADOR, ".");
        destruirEscopo();
    }

    public void declaracoesDeVariaveis() throws ErroSintatico, ErroSemantico {

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "var")){
            consumir(TipoToken.PALAVRA_RESERVADA, "var");
            listaDeDeclaracoesDeVariaveis();
            return;
        }
        
        return; // VAZIO

    }

    public void listaDeDeclaracoesDeVariaveis() throws ErroSintatico, ErroSemantico {

        conjuntoDeDeclaracoes = new HashSet<Token>();
        listaDeIdentificadores();
        consumir(TipoToken.DELIMITADOR, ":");
        TipoVariavel tipoVariavel = tipo();
        consumir(TipoToken.DELIMITADOR, ";");
        escopoAtual.addVariaveis(conjuntoDeDeclaracoes, tipoVariavel);
        listaDeDeclaracoesDeVariaveis1();

    }

    public void listaDeDeclaracoesDeVariaveis1() throws ErroSintatico, ErroSemantico {

        if (isProximoToken(TipoToken.IDENTIFICADOR)) {
            conjuntoDeDeclaracoes = new HashSet<Token>();
            listaDeIdentificadores();
            consumir(TipoToken.DELIMITADOR, ":");
            TipoVariavel tipoVariavel = tipo();
            consumir(TipoToken.DELIMITADOR, ";");
            escopoAtual.addVariaveis(conjuntoDeDeclaracoes, tipoVariavel);
            listaDeDeclaracoesDeVariaveis1();
            return;
        }
        return; // VAZIO
    }

    public void listaDeIdentificadores() throws ErroSintatico, ErroSemantico {

        conjuntoDeDeclaracoes.add(consumir(TipoToken.IDENTIFICADOR));
        listaDeIdentificadores1();
    }

    public void listaDeIdentificadores1() throws ErroSintatico, ErroSemantico {

        if (isProximoToken(TipoToken.DELIMITADOR, ",")) {
            consumir(TipoToken.DELIMITADOR, ",");
            Token t = consumir(TipoToken.IDENTIFICADOR);
            if (!conjuntoDeDeclaracoes.add(t)) {
                throw new VariavelJaDeclaradaException(t.getToken(), t.getLinha());
            }
            listaDeIdentificadores1();
            return;
        }

        return; // VAZIO
        
    }

    public TipoVariavel tipo() throws ErroSintatico {
        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "integer")) {
            consumir(TipoToken.PALAVRA_RESERVADA, "integer");
            return TipoVariavel.INTEGER;
        }

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "real")){
            consumir(TipoToken.PALAVRA_RESERVADA, "real");
            return TipoVariavel.REAL;
        }

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "boolean")){
            consumir(TipoToken.PALAVRA_RESERVADA, "boolean");
            return TipoVariavel.BOOLEAN;
        }

        throw new ErroSintatico(lerTokenDaLista(), TipoErroSintatico.TOKEN_ESPERADO);

    }

    public void declaracoesDeSubprogramas() throws ErroSintatico, ErroSemantico {
        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "procedure")) {
            declaracaoDeSubprograma();
            consumir(TipoToken.DELIMITADOR, ";");
            declaracoesDeSubprogramas();
            return;
        }
        
        return; // VAZIO
    }

    public void declaracaoDeSubprograma() throws ErroSintatico, ErroSemantico {
        consumir(TipoToken.PALAVRA_RESERVADA, "procedure");
        consumir(TipoToken.IDENTIFICADOR);
        criarEscopo();
        argumentos();
        consumir(TipoToken.DELIMITADOR, ";");
        declaracoesDeVariaveis();
        declaracoesDeSubprogramas();
        comandoComposto();
        destruirEscopo();
    }

    public void argumentos() throws ErroSintatico, ErroSemantico {
        if (isProximoToken(TipoToken.DELIMITADOR, "(")) {
            consumir(TipoToken.DELIMITADOR, "(");
            listaDeArgumentos();
            consumir(TipoToken.DELIMITADOR, ")");
            return;
        }
        
        return; // vazio
    }

    public void listaDeArgumentos() throws ErroSintatico, ErroSemantico {
        conjuntoDeDeclaracoes = new HashSet<Token>();
        listaDeIdentificadores();
        consumir(TipoToken.DELIMITADOR, ":");
        TipoVariavel tipoVariavel = tipo();
        escopoAtual.addVariaveis(conjuntoDeDeclaracoes, tipoVariavel);
        listaDeArgumentos1();

        
    }

    public void listaDeArgumentos1() throws ErroSintatico, ErroSemantico {
        if (isProximoToken(TipoToken.DELIMITADOR, ";")) {
            consumir(TipoToken.DELIMITADOR, ";");
            conjuntoDeDeclaracoes = new HashSet<Token>();
            listaDeIdentificadores();
            consumir(TipoToken.DELIMITADOR, ":");
            TipoVariavel tipoVariavel = tipo();
            escopoAtual.addVariaveis(conjuntoDeDeclaracoes, tipoVariavel);
            listaDeArgumentos1();
            return;
        }

        return; // VAZIO
    }

    public void comandoComposto() throws ErroSintatico, ErroSemantico {
        consumir(TipoToken.PALAVRA_RESERVADA, "begin");
        comandosOpcionais();
        consumir(TipoToken.PALAVRA_RESERVADA, "end");
    }

    public void comandosOpcionais() throws ErroSintatico, ErroSemantico {
        if (isProximoToken(TipoToken.IDENTIFICADOR) ||
            isProximoToken(TipoToken.PALAVRA_RESERVADA, "begin") ||
            isProximoToken(TipoToken.PALAVRA_RESERVADA, "if") ||
            isProximoToken(TipoToken.PALAVRA_RESERVADA, "while")) {
            listaDeComandos();
            return;
        }

        return; // VAZIO
    }


    public void listaDeComandos() throws ErroSintatico, ErroSemantico {
        comando();
        listaDeComandos1();
    }

    public void listaDeComandos1() throws ErroSintatico, ErroSemantico {

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

    public void comando() throws ErroSintatico, ErroSemantico {
        if (isProximoToken(TipoToken.IDENTIFICADOR)) {
            if (isProximoToken(TipoToken.COMANDO_ATRIBUICAO, 1)){
                Variavel v1 = variavel();
                checagemAtribuicao = new ChecagemDeTipos();
                checagemAtribuicao.push(v1.getTipo());
                consumir(TipoToken.COMANDO_ATRIBUICAO);
                checagemAtribuicao.push(expressao());
                checagemAtribuicao.avaliarAtribuicao();
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

    public void parteElse() throws ErroSintatico, ErroSemantico {
        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "else")) {
            consumir(TipoToken.PALAVRA_RESERVADA, "else");
            comando();
            return;
        }

        return; // VAZIO
    }

    public Variavel variavel() throws ErroSintatico, ErroSemantico {
        Token variavel = consumir(TipoToken.IDENTIFICADOR);
        return escopoAtual.getVariavel(variavel.getToken(), variavel.getLinha());

    }

    public void ativacaoDeProcedimento() throws ErroSintatico, ErroSemantico {
        consumir(TipoToken.IDENTIFICADOR);
        ativacaoDeProcedimento1();
        
    }

    public void ativacaoDeProcedimento1() throws ErroSintatico, ErroSemantico {
        if (isProximoToken(TipoToken.DELIMITADOR, "(")) {
            consumir(TipoToken.DELIMITADOR, "(");
            listaDeExpressoes();
            consumir(TipoToken.DELIMITADOR, ")");
            return;
        }

        return; // VAZIO
    }

    public void listaDeExpressoes() throws ErroSintatico, ErroSemantico {
        expressao();
        listaDeExpressoes1();
    }

    public void listaDeExpressoes1() throws ErroSintatico, ErroSemantico {
        if (isProximoToken(TipoToken.DELIMITADOR, ",")) {
            consumir(TipoToken.DELIMITADOR, ",");
            expressao();
            listaDeExpressoes1();
            return;
        }

        return; // VAZIO
    }

    public TipoVariavel expressao() throws ErroSintatico, ErroSemantico {
        checagemExpressao = new ChecagemDeTipos();
        expressaoSimples();
        expressao1();
        return checagemExpressao.avaliar();
    }

    public void expressao1() throws ErroSintatico, ErroSemantico {

        if(isProximoToken(TipoToken.OPERADOR_RELACIONAL)) {
            operadorRelacional();
            expressaoSimples();
            return;
        }

        return; // VAZIO
    }

    public void expressaoSimples() throws ErroSintatico, ErroSemantico {
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

    public void expressaoSimples1() throws ErroSintatico, ErroSemantico {
        if (isProximoToken(TipoToken.OPERADOR_ADITIVO)) {
            operadorAditivo();
            termo();
            expressaoSimples1();
            return;
        }

        return; // VAZIO
    }

    public void termo() throws ErroSintatico, ErroSemantico {
        fator();
        termo1();
    }

    public void termo1() throws ErroSintatico, ErroSemantico {
        if (isProximoToken(TipoToken.OPERADOR_MULTIPLICATIVO)) {
            operadorMultiplicativo();
            fator();
            termo1();
            return;
        }

        return; // VAZIO
    }

    public void fator() throws ErroSintatico, ErroSemantico {

        if (isProximoToken(TipoToken.IDENTIFICADOR)) {
            Token t = consumir(TipoToken.IDENTIFICADOR);
            Variavel v = escopoAtual.getVariavel(t.getToken(), t.getLinha());
            checagemExpressao.push(v.getTipo());
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
            checagemExpressao.push(TipoVariavel.INTEGER);
            return;
        }
        
        if (isProximoToken(TipoToken.NUMERO_REAL)) {
            consumir(TipoToken.NUMERO_REAL);
            checagemExpressao.push(TipoVariavel.REAL);
            return;
        }
        
        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "true")) {
            consumir(TipoToken.PALAVRA_RESERVADA, "true");
            checagemExpressao.push(TipoVariavel.BOOLEAN);
            return;
        }

        if (isProximoToken(TipoToken.PALAVRA_RESERVADA, "false")) {
            consumir(TipoToken.PALAVRA_RESERVADA, "false");
            checagemExpressao.push(TipoVariavel.BOOLEAN);
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

    public Token consumir(TipoToken token) throws ErroSintatico {
        return consumir(token, null);
    }

    public Token consumir(TipoToken tipoToken, String tokenName) throws ErroSintatico {
        Token atual = lerTokenDaLista();
        if (atual == null) {
            throw new ErroSintatico(atual, TipoErroSintatico.FIM_INESPERADO);
        }
        if (atual.getSimbolo() == tipoToken && tokenName == null) {
             return consumirTokenDaLista();
        }
        else {
            if (atual.getSimbolo() == tipoToken && atual.getToken().equals(tokenName)) {
                return consumirTokenDaLista();
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
        String erroString = "";
        for(Erro erro:erros){
                erroString += erro.errosToString();
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
