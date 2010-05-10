package analisador.sintatico;

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
public class AnalisadorSintatico {

    private List<Token> tokens;

    public AnalisadorSintatico(List<Token> tokens){
        this.tokens = tokens;
    }

    public List<Erro> analisar(){
        List<Erro> erros = new ArrayList<Erro>();
        return erros;
    }

    public void programa() throws ErroSintatico {
        consumir(TipoToken.PALAVRA_RESERVADA, "program");
        consumir(TipoToken.IDENTIFICADOR);
        consumir(TipoToken.DELIMITADOR);
        declaracoesDeVariaveis();
        declaracoesDeSubprogramas();
        comandoComposto();
        consumir(TipoToken.DELIMITADOR, ".");
    }

    public void declaracoesDeVariaveis() throws ErroSintatico {
        consumir(TipoToken.PALAVRA_RESERVADA, "var");
        listaDeDeclaracoesDeVariaveis();
        // OU
        return; // vazio
    }

    public void listaDeDeclaracoesDeVariaveis() throws ErroSintatico {
        listaDeIdentificadores();
        consumir(TipoToken.DELIMITADOR, ":");
        tipo();
        consumir(TipoToken.DELIMITADOR, ";");
        listaDeDeclaracoesDeVariaveis1();

    }

    public void listaDeDeclaracoesDeVariaveis1() throws ErroSintatico {

        listaDeIdentificadores();
        consumir(TipoToken.DELIMITADOR, ":");
        tipo();
        consumir(TipoToken.DELIMITADOR, ";");
        listaDeDeclaracoesDeVariaveis1();
        // OU
        return; // vazio
    }

    public void listaDeIdentificadores() throws ErroSintatico {

        consumir(TipoToken.IDENTIFICADOR);
        listaDeIdentificadores1();
    }

    public void listaDeIdentificadores1() throws ErroSintatico {

        consumir(TipoToken.DELIMITADOR, ",");
        consumir(TipoToken.IDENTIFICADOR);
        listaDeIdentificadores1();
        // OU
        return; // vazio
        
    }

    public void tipo() throws ErroSintatico {
        consumir(TipoToken.PALAVRA_RESERVADA, "integer");
        // OU
        consumir(TipoToken.PALAVRA_RESERVADA, "real");
        // OU
        consumir(TipoToken.PALAVRA_RESERVADA, "boolean");
    }

    public void declaracoesDeSubprogramas() throws ErroSintatico {
        declaracaoDeSubprograma();
        consumir(TipoToken.DELIMITADOR, ";");
        declaracoesDeSubprogramas();
        // OU
        return; // vazio
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
        consumir(TipoToken.DELIMITADOR, "(");
        listaDeParametros();
        consumir(TipoToken.DELIMITADOR, ")");
        // ou
        return; // vazio
    }

    public void listaDeParametros() throws ErroSintatico {
        listaDeIdentificadores();
        consumir(TipoToken.DELIMITADOR, ":");
        tipo();
        listaDeParametros1();
    }

    public void listaDeParametros1() throws ErroSintatico {
        consumir(TipoToken.DELIMITADOR, ";");
        listaDeIdentificadores();
        consumir(TipoToken.DELIMITADOR, ":");
        tipo();
        listaDeParametros1();
        // OU
        return; //vazio
    }

    public void comandoComposto() throws ErroSintatico {
        consumir(TipoToken.PALAVRA_RESERVADA, "begin");
        comandosOpcionais();
        consumir(TipoToken.PALAVRA_RESERVADA, "end");
    }

    public void comandosOpcionais() throws ErroSintatico {
        listaDeComandos();
        // OU
        return; // vazio
    }

    public void listaDeComandos() throws ErroSintatico {
        comando();
        listaDeComandos1();
    }

    public void listaDeComandos1() throws ErroSintatico {
        consumir(TipoToken.DELIMITADOR, ";");
        comando();
        listaDeComandos1();
        // OU
        return; // vazio
    }

    public void comando() throws ErroSintatico {
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

    public void parteElse() throws ErroSintatico {
        consumir(TipoToken.PALAVRA_RESERVADA, "else");
        comando();
        // ou
        return; // vazio
    }

    public void variavel() throws ErroSintatico {
        consumir(TipoToken.IDENTIFICADOR);
    }

    public void ativacaoDeProcedimento() throws ErroSintatico {
        consumir(TipoToken.IDENTIFICADOR);
        // ou
        consumir(TipoToken.IDENTIFICADOR);
        consumir(TipoToken.DELIMITADOR, "(");
        listaDeExpressoes();
        consumir(TipoToken.DELIMITADOR, ")");
    }

    public void listaDeExpressoes() throws ErroSintatico {
        expressao();
        listaDeExpressoes1();
    }

    public void listaDeExpressoes1() throws ErroSintatico {
        consumir(TipoToken.DELIMITADOR, ",");
        expressao();
        listaDeExpressoes1();
        // ou
        return; // vazio
    }

    public void expressao() throws ErroSintatico {
        expressaoSimples();
        // ou
        expressaoSimples();
        operadorRelacional();
        expressaoSimples();
    }

    public void expressaoSimples() throws ErroSintatico {
        termo();
        expressaoSimples1();
        // ou
        sinal();
        termo();
        expressaoSimples1();
    }

    public void expressaoSimples1() throws ErroSintatico {
        operadorAditivo();
        termo();
        expressaoSimples1();
        // ou
        return; // vazio
    }

    public void termo() throws ErroSintatico {
        fator();
        termo1();
    }

    public void termo1() throws ErroSintatico {
        operadorMultiplicativo();
        fator();
        termo1();
        // ou
        return; // vazio
    }

    public void fator() throws ErroSintatico {
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
        consumir(TipoToken.OPERADOR_MULTIPLICATIVO);
        // ou
        consumir(TipoToken.PALAVRA_RESERVADA, "and");
    }

    public void consumir(TipoToken token) throws ErroSintatico {
        consumir(token, null);
    }

    public void consumir(TipoToken tipoToken, String tokenName) throws ErroSintatico {
        Token atual = lerTokenDaLista();
        if (atual == null) {
            throw new ErroSintatico(atual, TipoErroSintatico.FIM_INESPERADO);
        }
        if (atual.getSimbolo() == tipoToken || tokenName == null) {
            consumirTokenDaLista();
        }
        else {
            if (atual.getSimbolo() == tipoToken || atual.getToken().equals(tokenName)) {
                consumirTokenDaLista();
            }
            else {
                throw new ErroSintatico(atual, new Token(tokenName, tipoToken, 0), TipoErroSintatico.TOKEN_ESPERADO);
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
