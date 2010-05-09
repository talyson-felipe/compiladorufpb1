/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analisador.sintatico;

import modelo.tipos.TipoToken;

/**
 *
 * @author Clodoaldo Brasilino
 */
public class AnalisadorSintatico {



    public void programa(){
        consumir(TipoToken.PALAVRA_RESERVADA, "program");
        consumir(TipoToken.IDENTIFICADOR);
        consumir(TipoToken.DELIMITADOR);
        declaracoesDeVariaveis();
        declaracoesDeSubprogramas();
        comandoComposto();
        consumir(TipoToken.DELIMITADOR);
    }

    public void declaracoesDeVariaveis(){
        // var
        listaDeDeclaracoesDeVariaveis();
        // OU
        // vazio
    }

    public void listaDeDeclaracoesDeVariaveis(){

    }

    public void listaDeIdentificadores(){

    }

    public void tipo(){

    }

    public void declaracoesDeSubprogramas(){

    }

    public void declaracaoDeSubprograma(){

    }

    public void argumentos(){

    }

    public void listaDeParametros(){

    }

    public void comandoComposto(){

    }

    public void comandosOpcionais(){

    }

    public void listaDeComandos(){

    }

    public void comando(){

    }

    public void parteElse(){

    }

    public void variavel(){

    }

    public void ativacaoDeProcedimento(){

    }

    public void listaDeExpressoes(){

    }

    public void expressao(){

    }

    public void expressaoSimples(){

    }

    public void termo(){

    }

    public void fator(){

    }

    public void sinal(){

    }

    public void operadorRelacional(){

    }

    public void operadorAditivo(){

    }

    public void operadorMultiplicativo(){

    }

    public Boolean consumir(TipoToken token){
        return consumir(token, null);
    }

    public Boolean consumir(TipoToken token, String tokenName){
        if (tokenName == null){

            return true;
        } else {
            // COM TOKEN NAME
            return true;
        }
    }
}

