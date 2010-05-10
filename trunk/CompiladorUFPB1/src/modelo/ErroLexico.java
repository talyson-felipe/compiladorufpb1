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

    @Override
    public String errosToString(){
        switch(this.getTipoErro()){
                    case COMENTARIO_ABERTO:
                        return "Comentário iniciado na linha "+this.getToken().getLinha()+" não fechado devidamente.\n";
                    case SIMBOLO_INEXISTENTE:
                        return "O símbolo encontrado \"" + this.getToken().getToken() + "\" na linha "+this.getToken().getLinha()+" não é reconhecido.\n";
                        //break;
                }
        return "";
    }
}

