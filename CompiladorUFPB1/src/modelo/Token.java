/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

import modelo.tipos.TipoToken;

/**
 *
 * @author clodbrasilino
 */
public class Token {

    private String token;
    private TipoToken simbolo;
    private int linha;

    public Token(){
        super();
    }

    public Token(String token, TipoToken simbolo, int linha){
        super();
        this.token = token;
        this.linha = linha;
        this.simbolo = simbolo;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public TipoToken getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(TipoToken simbolo) {
        this.simbolo = simbolo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString(){
        return "Token: ["+ token+ ", "+ simbolo+ ", "+ linha+ "]";
    }
}
