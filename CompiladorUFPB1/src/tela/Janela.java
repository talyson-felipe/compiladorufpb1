/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * interfac.java
 *
 * Created on 07/04/2010, 22:01:36
 */
package tela;

import analisador.lexico.AnalisadorLexico;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import analisador.lexico.AnalisadorLexico;
import java.util.List;
import modelo.Token;

/**
 *
 * @author Josa
 */
public class Janela extends javax.swing.JFrame {

    private JFileChooser fc;
    private char[] caracteres = null;
    private List<Token> tokens;
    File arquivo = null;
    /** Creates new form interfac */
    public Janela() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textoCodigo = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        textoErro = new javax.swing.JTextArea();
        separador1 = new javax.swing.JSeparator();
        analisar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        textoTabela = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        open = new javax.swing.JMenuItem();
        separador2 = new javax.swing.JPopupMenu.Separator();
        exit = new javax.swing.JMenuItem();
        about = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Analisador Lexico");
        setResizable(false);

        textoCodigo.setColumns(20);
        textoCodigo.setRows(5);
        textoCodigo.setBorder(javax.swing.BorderFactory.createTitledBorder("Código Fonte"));
        textoCodigo.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(textoCodigo);

        textoErro.setColumns(20);
        textoErro.setEditable(false);
        textoErro.setForeground(new java.awt.Color(255, 0, 0));
        textoErro.setRows(5);
        textoErro.setBorder(javax.swing.BorderFactory.createTitledBorder("Erros"));
        jScrollPane2.setViewportView(textoErro);

        analisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tela/icons/lupa.png"))); // NOI18N
        analisar.setToolTipText("Analisar texto");
        analisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analisarActionPerformed(evt);
            }
        });

        textoTabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Token", "Simbolo", "Linha"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        textoTabela.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(textoTabela);
        textoTabela.getColumnModel().getColumn(0).setResizable(false);
        textoTabela.getColumnModel().getColumn(0).setPreferredWidth(5);
        textoTabela.getColumnModel().getColumn(1).setResizable(false);
        textoTabela.getColumnModel().getColumn(2).setResizable(false);
        textoTabela.getColumnModel().getColumn(2).setPreferredWidth(2);

        jMenu1.setText("File");

        open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        open.setText("Open");
        open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });
        jMenu1.add(open);
        jMenu1.add(separador2);

        exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        jMenu1.add(exit);

        jMenuBar1.add(jMenu1);

        about.setText("Help");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("About");
        about.add(jMenuItem2);

        jMenuBar1.add(about);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
                            .addComponent(separador1, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(319, 319, 319)
                        .addComponent(analisar, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, 0, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separador1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(analisar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    private void openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openActionPerformed
        try {
            // TODO add your handling code here:
            AbrirArquivo();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Janela.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Janela.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_openActionPerformed

    private void analisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analisarActionPerformed
        // TODO add your handling code here:

        if (arquivo != null) {
            System.out.println("Ronaldo");
            AnalisadorLexico analisador = new AnalisadorLexico();

            analisador.setCodigoFonte(textoCodigo.getText());
            analisador.analisar();

            //Depois de te feito toda analise lexica

            //Pega lista de tokens
            tokens = analisador.getTokens();

            //Exibe na tabela a lista de tokens
            InserirTabela(tokens);
            //mostraErros(analisador.errosToString());

        }
        else
            JOptionPane.showMessageDialog(null, "Primeiro carregue um arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_analisarActionPerformed

    /**
     * @param args the command line arguments
     */
    public void InserirTabela(List<Token> list) {
        Token token;
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) textoTabela.getModel();

        while (textoTabela.getRowCount() > 0) {
            model.removeRow(0);
        }

        for (int i = 0; i < list.size(); i++) {
            token = list.get(i);
            model.addRow(new Object[]{token.getToken(), token.getSimbolo(), token.getLinha()});
        }

    }

    public void AbrirArquivo() throws FileNotFoundException, IOException {
        fc = new JFileChooser();
        String linha, nome;
        int tam = 0;
        int resultado = fc.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            arquivo = fc.getSelectedFile();
            nome = arquivo.getName();
            tam = (int) nome.length();
            if ((nome.substring(tam - 3, tam).equals("txt"))) {

                FileReader ler = new FileReader(arquivo);
                BufferedReader leitor = new BufferedReader(ler);

                while ((linha = leitor.readLine()) != null) {
                    textoCodigo.append(linha + "\n");
                    textoCodigo.setCaretPosition(textoCodigo.getText().length());
                }
            } else {
                JOptionPane.showMessageDialog(null, "O arquivo não é válido", "Erro", JOptionPane.ERROR_MESSAGE);
                arquivo = null;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostraErros(String erros){
        textoErro.setText(erros);
    }
        public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Janela().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu about;
    private javax.swing.JButton analisar;
    private javax.swing.JMenuItem exit;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JMenuItem open;
    private javax.swing.JSeparator separador1;
    private javax.swing.JPopupMenu.Separator separador2;
    private javax.swing.JTextArea textoCodigo;
    private javax.swing.JTextArea textoErro;
    private javax.swing.JTable textoTabela;
    // End of variables declaration//GEN-END:variables
}
