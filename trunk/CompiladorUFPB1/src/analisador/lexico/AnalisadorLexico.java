/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analisador.lexico;

import java.util.ArrayList;
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
    private int linhaDoUltimoComentarioAberto;
    private boolean comentarioAberto;
    private String token = "";
    private Integer index = 0;
    private String atual;

    public AnalisadorLexico() {
        super();
        regexEstadosAnalisadorLexico = ResourceBundle.getBundle("analisador.lexico.properties.RegexAutomato");
        erros = new ArrayList<Erro>();
        tokens = new ArrayList<Token>();
    }

    public String currentChar(){
        return atual;
    }

    public void nextChar(){
        try{
            atual = ((Character) codigoFonte.charAt(++index)).toString();
        } catch (StringIndexOutOfBoundsException e) {
            atual = "FIM DO FONTE"; // Esse try-catch block é uma gambiarra de fim da string :)
            return;
        }
    }

    public void firstChar(){
        atual = ((Character) codigoFonte.charAt(0)).toString();
    }

    public void analisar() {

        erros = new ArrayList<Erro>();
        tokens = new ArrayList<Token>();
        
        index = 0;
        numeroDaLinha = 1;

        if (!codigoFonte.isEmpty()){
            firstChar();
        }
        
        while(index < codigoFonte.length()) {

            if(atual.equals("FIM DO FONTE")) return; // Gambiarra de fim da string :)
            
            if (currentChar().equals(" ") || currentChar().equals("\f") || currentChar().equals("\t")) {
                token = "";
                nextChar();
                continue;
            } else if (currentChar().equals("\n")) {
                numeroDaLinha++;
                token = "";
                nextChar();
                continue;
            } else if (Pattern.matches(regexEstadosAnalisadorLexico.getString("i0A"), currentChar().toString())) {
                token += currentChar();
                estadoA();
            } else if (Pattern.matches(regexEstadosAnalisadorLexico.getString("i0B"), currentChar().toString())) {
                token += currentChar();
                estadoB();
            } else if (Pattern.matches(regexEstadosAnalisadorLexico.getString("i0D"), currentChar().toString())) {
                token += currentChar();
                estadoD();
            } else if (Pattern.matches(regexEstadosAnalisadorLexico.getString("i0E"), currentChar().toString())) {
                token += currentChar();
                estadoE();
            } else if (Pattern.matches(regexEstadosAnalisadorLexico.getString("i0G"), currentChar().toString())) {
                token += currentChar();
                estadoG();
            } else if (Pattern.matches(regexEstadosAnalisadorLexico.getString("i0I"), currentChar().toString())) {
                token += currentChar();
                estadoI();
            } else if (Pattern.matches(regexEstadosAnalisadorLexico.getString("i0L"), currentChar().toString()) ||
                       Pattern.matches(",", currentChar().toString())) {
                token += currentChar();
                estadoL();
            } else if (Pattern.matches(regexEstadosAnalisadorLexico.getString("i0N"), currentChar().toString())) {
                token += currentChar();
                estadoN();
            } else if (Pattern.matches(regexEstadosAnalisadorLexico.getString("i0i1"), currentChar().toString())) {
                token += currentChar();
                estadoi1();
            } else {
                token += currentChar();
                erros.add(new ErroLexico(new Token(token, TipoToken.INVALIDO, numeroDaLinha), TipoErroLexico.SIMBOLO_INEXISTENTE));
                nextChar();
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
    public String imprimeErros(){
        String erroString="";
        for(Erro erro:erros){
            //if(erro instanceof ErroLexico){
                ErroLexico erroLexico =(ErroLexico)erro;
              //  switch(erroLexico.getTipoErro()){
                //    case COMENTARIO_ABERTO:
               // erroString += "Comentário iniciado na linha "+erroLexico.getToken().getLinha()+" não fechado devidamente.\n";
                  //      break;
                    //case SIMBOLO_INEXISTENTE:
                 //       erroString += "O símbolo encontrado \"" + erroLexico.getToken().getToken() + "\" na linha "+erroLexico.getToken().getLinha()+" não é reconhecido.\n";
                   //     break;
                //}
            //}
                erroString+=erroLexico.errosToString();
         }
            return erroString;
    }

    private void estadoA(){
        nextChar();
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("AA"), currentChar())) {
            token += currentChar();
            estadoA();
        } else {
            construirPalavra(token, numeroDaLinha);
        }
    }

    private void estadoB(){
        nextChar();
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("BB"), currentChar())) {
            token += currentChar();
            estadoB();
        } else if (Pattern.matches(regexEstadosAnalisadorLexico.getString("BC"), currentChar())) {
            token += currentChar();
            estadoC();
        } else {
            construirNumeroInteiro(token, numeroDaLinha);
        }
    }

    private void estadoC(){
        nextChar();
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("CC"), currentChar())) {
            token += currentChar();
            estadoC();
        } else {
            construirNumeroReal(token, numeroDaLinha);
        }
    }

    private void estadoD(){
        construirOperadorAritmetico(token, numeroDaLinha);
        nextChar();
    }

    private void estadoE(){
        nextChar();
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("EF"), currentChar())){
            token += currentChar();
            estadoF();
        } else {
            construirDelimitador(token, numeroDaLinha);
        }
    }

    private void estadoF(){
        construirAtribuicao(token, numeroDaLinha);
        nextChar();
    }

    private void estadoG(){
        nextChar();
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("GH"), currentChar())){
            token += currentChar();
            estadoH();
        } else {
            construirRelacional(token, numeroDaLinha);
        }
    }

    private void estadoH(){
        construirRelacional(token, numeroDaLinha);
        nextChar();
    }

    private void estadoI(){
        nextChar();
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("GH"), currentChar())){
            token += currentChar();
            estadoJ();
        } else {
            construirRelacional(token, numeroDaLinha);
        }
    }

    private void estadoJ(){
        construirRelacional(token, numeroDaLinha);
        nextChar();
    }

    private void estadoL(){
        construirDelimitador(token, numeroDaLinha);
        nextChar();
    }

    private void estadoi1(){
        nextChar();
        if (!comentarioAberto){
            linhaDoUltimoComentarioAberto = numeroDaLinha;
            comentarioAberto = true;
        }

        if (currentChar().equals("FIM DO FONTE")){
            erros.add(new ErroLexico(construirTokenErroLexico(token, linhaDoUltimoComentarioAberto), TipoErroLexico.COMENTARIO_ABERTO));
            return;
        }
        if (currentChar().equals("\n")){
            numeroDaLinha++;
        }
        if (Pattern.matches(regexEstadosAnalisadorLexico.getString("i1M"), currentChar())) {
            token += currentChar();
            estadoM();
        } else {
            token += currentChar();
            estadoi1();
        }
    }

    private void estadoM(){
        nextChar();
        comentarioAberto = false;
    }

    private void estadoN(){
        construirRelacional(token, numeroDaLinha);
        nextChar();
    }

    private void construirPalavra(String token, int numeroDaLinha) {
        PalavrasReservadas reservadas = PalavrasReservadas.getInstance();
        if (reservadas.isPalavraReservada(token)){
            tokens.add(new Token(token, TipoToken.PALAVRA_RESERVADA, numeroDaLinha));
            this.token = "";
        } else {
            tokens.add(new Token(token, TipoToken.IDENTIFICADOR, numeroDaLinha));
            this.token = "";
        }
    }

    private void construirNumeroInteiro(String token, int numeroDaLinha) {
        tokens.add(new Token(token, TipoToken.NUMERO_INTEIRO, numeroDaLinha));
        this.token = "";
    }

    private void construirNumeroReal(String token, int numeroDaLinha) {
        tokens.add(new Token(token, TipoToken.NUMERO_REAL, numeroDaLinha));
        this.token = "";
    }

    private void construirOperadorAritmetico(String token, int numeroDaLinha) {
        if (token.equals("+") || token.equals("-")) {
            tokens.add(new Token(token, TipoToken.OPERADOR_ADITIVO, numeroDaLinha));
            this.token = "";
        }
        if (token.equals("*") || token.equals("/")) {
            tokens.add(new Token(token, TipoToken.OPERADOR_MULTIPLICATIVO, numeroDaLinha));
            this.token = "";
        }
    }

    private void construirDelimitador(String token, int numeroDaLinha) {
        tokens.add(new Token(token, TipoToken.DELIMITADOR, numeroDaLinha));
        this.token = "";
    }

    private void construirComentario(String token, int numeroDaLinha) {
        tokens.add(new Token(token, TipoToken.COMENTARIO, numeroDaLinha));
        this.token = "";
    }

    private void construirRelacional(String token, int numeroDaLinha) {
        tokens.add(new Token(token, TipoToken.OPERADOR_RELACIONAL, numeroDaLinha));
        this.token = "";
    }

    private void construirAtribuicao(String token, int numeroDaLinha) {
        tokens.add(new Token(token, TipoToken.COMANDO_ATRIBUICAO, numeroDaLinha));
        this.token = "";
    }

    private Token construirTokenErroLexico(String token, int numeroDaLinha){
        Token t = new Token(token, TipoToken.INVALIDO, numeroDaLinha);
        this.token = "";
        return t;
    }

}
