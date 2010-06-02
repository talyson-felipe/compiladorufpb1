/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analisador.semantico;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import modelo.ErroSemantico;
import modelo.Token;
import modelo.Variavel;
import modelo.tipos.TipoVariavel;

/**
 *
 * @author clodbrasilino
 */
public class TabelaDeVariaveis {

    private Map<String, Variavel> variaveis;

    public TabelaDeVariaveis(){
        super();
        variaveis = new HashMap<String, Variavel>();
    }

    public boolean areDeclarada(Set<Token> s){
        for (Token t: s){
            if(isDeclarada(t.getToken())){
                return true;
            }
        }
        return false;
    }

    public boolean isDeclarada(String chave){
        return variaveis.keySet().contains(chave);
    }

    public Variavel get(String chave){
        return variaveis.get(chave);
    }

    public void createAll(Set<Token> tokens, TipoVariavel t) throws ErroSemantico {
        try {
            for(Token identificador: tokens) {
                create(identificador.getToken(), t);
            }
        } catch (NullPointerException ex) {
            throw new ErroSemantico() {

                public String errosToString() {
                    return "Erro interno.";
                }
            };
        }

    }

    public void create(String chave, TipoVariavel tipo){
        switch(tipo){
            case BOOLEAN:
                variaveis.put(chave, new Variavel(tipo, new Boolean(false)));
                break;
            case INTEGER:
                variaveis.put(chave, new Variavel(tipo, new Integer(0)));
                break;
            case REAL:
                variaveis.put(chave, new Variavel(tipo, new Float(0)));
                break;
        }
    }

    public void create(String chave, TipoVariavel tipo, Object valor){
        variaveis.put(chave, new Variavel(tipo, valor));
    }

}
