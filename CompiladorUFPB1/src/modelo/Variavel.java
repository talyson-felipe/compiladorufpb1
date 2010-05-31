/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

import modelo.tipos.TipoVariavel;

/**
 *
 * @author clodbrasilino
 */
public class Variavel {

    private TipoVariavel tipo;
    private Object valor;

    public Variavel(TipoVariavel tipo, Object valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public TipoVariavel getTipo() {
        return tipo;
    }

    public void setTipo(TipoVariavel tipo) {
        this.tipo = tipo;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }
}
