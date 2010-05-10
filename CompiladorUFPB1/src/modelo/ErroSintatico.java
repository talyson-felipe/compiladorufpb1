/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

import modelo.tipos.TipoErroSintatico;

/**
 *
 * @author clodbrasilino
 */
public class ErroSintatico extends Exception implements Erro {

    private Token token;
    private Token tokenEsperado = null;
    private TipoErroSintatico tipoErro;

    public ErroSintatico(){
        super();
    }

    public ErroSintatico(Token token, TipoErroSintatico tipoErro){
        super();
        this.token = token;
        this.tipoErro = tipoErro;
    }

    public ErroSintatico(Token token, Token tokenEsperado, TipoErroSintatico tipoErro){
        super();
        this.token = token;
        this.tipoErro = tipoErro;
        this.tokenEsperado = tokenEsperado;
    }

    public TipoErroSintatico getTipoErro() {
        return tipoErro;
    }

    public void setTipoErro(TipoErroSintatico tipoErro) {
        this.tipoErro = tipoErro;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}