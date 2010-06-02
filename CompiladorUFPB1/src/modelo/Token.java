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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Token other = (Token) obj;
        if ((this.token == null) ? (other.token != null) : !this.token.equals(other.token)) {
            return false;
        }
        if (this.simbolo != other.simbolo && (this.simbolo == null || !this.simbolo.equals(other.simbolo))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.token != null ? this.token.hashCode() : 0);
        hash = 71 * hash + (this.simbolo != null ? this.simbolo.hashCode() : 0);
        return hash;
    }

}
