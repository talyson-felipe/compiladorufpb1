/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analisador.lexico;

import java.util.List;
import modelo.Erro;
import modelo.Token;

/**
 *
 * @author clodbrasilino
 */
public class AnalisadorLexico {

    private String codigoFonte;
    private List<Erro> erros;
    private List<Token> tokens;

    public AnalisadorLexico(){
        super();
    }

    public void analisar(){
        int index = 0;
        while(index < codigoFonte.length()){
            Character atual = codigoFonte.charAt(index);
            switch(atual){
                
            }
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
}
