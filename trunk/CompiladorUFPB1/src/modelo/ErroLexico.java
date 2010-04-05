/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

import modelo.tipos.TipoErroLexico;

/**
 *
 * @author clodbrasilino
 */
public class ErroLexico implements Erro {

    private Token token;
    private TipoErroLexico tipoErro;

    public ErroLexico(){
        super();
    }

    public ErroLexico(Token token, TipoErroLexico tipoErro){
        super();
        this.tipoErro = tipoErro;
        this.token = token;
    }

    public TipoErroLexico getTipoErro() {
        return tipoErro;
    }

    public void setTipoErro(TipoErroLexico tipoErro) {
        this.tipoErro = tipoErro;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

}
