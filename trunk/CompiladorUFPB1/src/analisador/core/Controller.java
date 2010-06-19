// This file is part of the Code Translator source code
//
// Copyright (C) 2009 UFPB (http://www.di.ufpb.br),
// Federal University of Paraiba.
// All rights reserved.
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public
// License along with this program; if not, write to the Free
// Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
// Boston, MA 02110-1301, USA.
// *****************************************************************

/*
 * File: Controller.java
 */

package analisador.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import tela.Janela;
import analisador.exceptions.DuplicatedException;
import analisador.exceptions.ExpectedException;
import analisador.exceptions.FatalException;
import analisador.exceptions.OpenProjectException;
import analisador.exceptions.SaveProjectException;
import analisador.exceptions.UndefinedException;
import analisador.util.ExtensionFileFilter;
import javax.swing.JFileChooser;

/**
 *
 * @author Erisvaldo
 */
public class Controller {

    private static Compiler compiler;
    private static Controller instance;

    private Controller() {
        compiler = null;
    }

    public static Controller getInstance() {
        if (instance == null)
            instance = new Controller();

        return instance;
    }

    public Compiler getCompiler() {
        return compiler;
    }

    public void generateIJVMCode(String inputCode, Janela view) {
        compiler = IJVMCompiler.getInstance();
        String prefix = "[IJVM] ";

        try {
            String code = compiler.getCode(inputCode);
            view.getIjvmTextArea().setText(code);
            //addOutputText(prefix + "C�digo gerado com sucesso.", view);
        } catch (DuplicatedException ex) {
            //addOutputText(prefix + "ERRO! Duplicado: " + ex.getMessage(), view);
        } catch (ExpectedException ex) {
            //addOutputText(prefix + "ERRO! Esperado: " + ex.getMessage(), view);
        } catch (FatalException ex) {
            //addOutputText(prefix + "ERRO: " + ex.getMessage(), view);
        } catch (UndefinedException ex) {
            //addOutputText(prefix + "ERRO! Indefinido: " + ex.getMessage(), view);
        } catch (UnsupportedOperationException ex) {
            //addOutputText(prefix + "ERRO! Opera��o n�o suportada: " + ex.getMessage(), view);
        }
    }

    public void saveProject(String filename, String inputText, String x86Text, String ijvmText) throws SaveProjectException {
        try {
            saveTextInFile(filename + ".ct", "Projeto Code Translator\nArquivo de entrada: " + Constants.PROJECT_NAME + ".in\nC�digo Assembly 80x86: " + Constants.PROJECT_NAME + ".asm\nC�digo IJVM: " + Constants.PROJECT_NAME + ".jvm");
            saveTextInFile(filename + ".in", inputText == null ? "" : inputText);
            saveTextInFile(filename + ".asm", x86Text == null ? "" : x86Text);
            saveTextInFile(filename + ".jvm", ijvmText == null ? "" : ijvmText);
        } catch (Exception ex) {
            throw new SaveProjectException("N�o foi poss�vel salvar o projeto");
        }
    }

    private void addOutputText(String outputText, Janela view) {
        // Get current time
        GregorianCalendar gc = new GregorianCalendar();
        String currentTimeString = gc.get(Calendar.HOUR_OF_DAY) + ":" + gc.get(Calendar.MINUTE) + ":" + ( gc.get(Calendar.SECOND) < 10 ? "0" + gc.get(Calendar.SECOND) : gc.get(Calendar.SECOND) );
        // Add text to the output area
        //String currentOutputText = view.getOutputTextArea().getText();
        //view.getOutputTextArea().setText(currentOutputText + "[" + currentTimeString + "] " + outputText + "\n");
    }

    private void saveTextInFile(String filename, String text) throws Exception {
        String content = text;
        // o false significa q o arquivo ser� substitu�do
        FileWriter x = new FileWriter(filename, false);
        x.write(content); // armazena o texto no objeto x, que aponta para o arquivo
        x.close(); // cria o arquivo
    }
}
