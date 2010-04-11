/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analisador.lexico;

import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import modelo.Erro;
import modelo.ErroLexico;
import modelo.Token;
import modelo.tipos.TipoErroLexico;
import modelo.tipos.TipoToken;

/**
 *
 * @author clodbrasilino
 */
public class AnalisadorLexico {

    // Ver PDF associado aos estados do analisador léxico para mais informações.
    private ResourceBundle regexEstadosAnalisadorLexico;

    private String codigoFonte;
    private List<Erro> erros;
    private List<Token> tokens;
    private int numeroDaLinha;
    private String token = "";
    private Integer index = 0;

    public AnalisadorLexico() {
        super();
        regexEstadosAnalisadorLexico = ResourceBundle.getBundle("analisador.lexico.properties.regex.properties");
    }

    public void analisar() {
        
        index = numeroDaLinha = 0;
        
        while(index < codigoFonte.length()) {
            Character atual = codigoFonte.charAt(index);
            
            if (atual.equals(' ') || atual.equals('\f') || atual.equals('\t')) {
                token = "";
                index++;
            } else if (atual.equals('\n')) {
                numeroDaLinha++;
                token = "";
                index++;
            } else if (Pattern.matches(regexEstadosAnalisadorLexico.getString("i0A"), atual.toString())) {
                estadoA();
            }

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

    private void estadoA(){
        String atual = ((Character) codigoFonte.charAt(++index)).toString();
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("AA"), atual)) {
            estadoA();
        } else {
            construirPalavra(token, numeroDaLinha);
        }
    }

    private void estadoB(){
        String atual = ((Character) codigoFonte.charAt(++index)).toString();
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("BB"), atual)) {
            estadoA();
        } else if (Pattern.matches(regexEstadosAnalisadorLexico.getString("BC"), atual)) {
            estadoC();
        } else {
            construirNumeroReal(token, numeroDaLinha);
        }
    }

    private void estadoC(){
        String atual = ((Character) codigoFonte.charAt(++index)).toString();
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("CC"), atual)) {
            estadoC();
        } else {
            construirNumeroInteiro(token, numeroDaLinha);
        }
    }

    private void estadoD(){
        construirOperadorAritmetico(token, numeroDaLinha);
    }

    private void estadoE(){
        String atual = ((Character) codigoFonte.charAt(++index)).toString();
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("EF"), atual)){
            estadoF();
        } else {
            construirDelimitador(token, numeroDaLinha);
        }
    }

    private void estadoF(){
        construirAtribuicao(token, numeroDaLinha);
    }

    private void estadoG(){
        String atual = ((Character) codigoFonte.charAt(++index)).toString();
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("GH"), atual)){
            estadoH();
        } else {
            construirRelacional(token, numeroDaLinha);
        }
    }

    private void estadoH(){
        construirRelacional(token, numeroDaLinha);
    }

    private void estadoI(){
        String atual = ((Character) codigoFonte.charAt(++index)).toString();
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("GH"), atual)){
            estadoJ();
        } else {
            construirRelacional(token, numeroDaLinha);
        }
    }

    private void estadoJ(){
        construirRelacional(token, numeroDaLinha);
    }

    private void estadoL(){
        construirDelimitador(token, numeroDaLinha);
    }

    private void estadoi1(){
        String atual = ((Character) codigoFonte.charAt(++index)).toString();

        try {
            if (Pattern.matches(regexEstadosAnalisadorLexico.getString("i1M"), atual)) {
                estadoM();
            } else {
                estadoi1();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            erros.add(new ErroLexico(construirTokenErroLexico(token, numeroDaLinha), TipoErroLexico.COMENTARIO_ABERTO));
        }
    }

    private void estadoM(){

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

    private Token construirTokenErroLexico(String token, int numeroDaLinha){
        return new Token(token, TipoToken.INVALIDO, numeroDaLinha);
    }

}
