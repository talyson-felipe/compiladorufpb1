/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analisador.lexico;

import java.util.List;
import java.util.regex.Pattern;
import modelo.Erro;
import modelo.Token;
import modelo.tipos.TipoToken;

/**
 *
 * @author clodbrasilino
 */
public class AnalisadorLexico {

    private static final String regexReal = "[0-9]+\\.[0-9]*";
    private static final String regexInteiro = "[0-9]+";
    private static final String regexPalavras = "[a-zA-Z]+|[a-z]+|[A-Z]+";
    private static final String regexComentario = "{[\\p{Graph}]+}";
    private static final String regexAritmetico = "+|-|\\*|/";
    private static final String regexRelacional = "";
    private static final String regexAtribuicao = "";
    private static final String regexDelimitador = "";
    // TODO botar regex ausente


    private String codigoFonte;
    private List<Erro> erros;
    private List<Token> tokens;
    private int numeroDaLinha;

    public AnalisadorLexico() {
        super();
    }

    public void analisar() {
        int index = 0;
        String token = "";
        
        while(index < codigoFonte.length()) {
            Character atual = codigoFonte.charAt(index);
            
            if (atual.equals(' ')) {
                token += atual;

                if (Pattern.matches(regexPalavras, token)) {
                    construirPalavra(token, numeroDaLinha);
                } else if (Pattern.matches(regexInteiro, token)) {
                    construirNumeroInteiro(token, numeroDaLinha);
                } else if (Pattern.matches(regexReal, token)) {
                    construirNumeroReal(token, numeroDaLinha);
                } else if (Pattern.matches(regexAritmetico, token)) {
                    construirOperadorAritmetico(token, numeroDaLinha);
                } else if (Pattern.matches(regexDelimitador, token)) {
                    construirDelimitador(token, numeroDaLinha);
                } else if (Pattern.matches(regexComentario, token)) {
                    construirComentario(token, numeroDaLinha);
                } else if (Pattern.matches(regexRelacional, token)) {
                    construirRelacional(token, numeroDaLinha);
                } else if (Pattern.matches(regexAtribuicao, token)) {
                    construirAtribuicao(token, numeroDaLinha);
                }

                // TODO botar regex ausente
                token = "";
            } else if (atual.equals('\n')) {
                token += atual;
                numeroDaLinha++;

                if (Pattern.matches(regexPalavras, token)) {
                    construirPalavra(token, numeroDaLinha);
                } else if (Pattern.matches(regexInteiro, token)) {
                    construirNumeroInteiro(token, numeroDaLinha);
                } else if (Pattern.matches(regexReal, token)) {
                    construirNumeroReal(token, numeroDaLinha);
                } else if (Pattern.matches(regexAritmetico, token)) {
                    construirOperadorAritmetico(token, numeroDaLinha);
                }
                // TODO botar regex ausente
                token = "";
            } else
                index++;
        }
    }

    public void setCodigoFonte(String codigoFonte){
        this.codigoFonte = codigoFonte;
    }

    public String getCodigoFonte(){
        return this.codigoFonte;
    }

    public List<Erro> getErros(){
        return erros;
    }

    public List<Token> getTokens(){
        return tokens;
    }

    private void construirPalavra(String token, int numeroDaLinha) {
        PalavrasReservadas reservadas = PalavrasReservadas.getInstance();
        if (reservadas.isPalavraReservada(token)){
            tokens.add(new Token(token, TipoToken.PALAVRA_RESERVADA, numeroDaLinha));
        } else {
            tokens.add(new Token(token, TipoToken.IDENTIFICADOR, numeroDaLinha));
        }
    }

    private void construirNumeroInteiro(String token, int numeroDaLinha) {
        tokens.add(new Token(token, TipoToken.NUMERO_INTEIRO, numeroDaLinha));
    }

    private void construirNumeroReal(String token, int numeroDaLinha) {
        tokens.add(new Token(token, TipoToken.NUMERO_REAL, numeroDaLinha));
    }

    private void construirOperadorAritmetico(String token, int numeroDaLinha) {
        if (token.equals("+") || token.equals("-")) {
            tokens.add(new Token(token, TipoToken.OPERADOR_ADITIVO, numeroDaLinha));
        }
        if (token.equals("*") || token.equals("/")) {
            tokens.add(new Token(token, TipoToken.OPERADOR_MULTIPLICATIVO, numeroDaLinha));
        }
    }

    private void construirDelimitador(String token, int numeroDaLinha) {
        tokens.add(new Token(token, TipoToken.DELIMITADOR, numeroDaLinha));
    }

    private void construirComentario(String token, int numeroDaLinha) {
        tokens.add(new Token(token, TipoToken.COMENTARIO, numeroDaLinha));
    }

    private void construirRelacional(String token, int numeroDaLinha) {
        tokens.add(new Token(token, TipoToken.OPERADOR_RELACIONAL, numeroDaLinha));
    }

    private void construirAtribuicao(String token, int numeroDaLinha) {
        tokens.add(new Token(token, TipoToken.COMANDO_ATRIBUICAO, numeroDaLinha));
    }


}
