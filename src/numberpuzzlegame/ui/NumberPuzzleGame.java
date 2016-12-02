/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package numberpuzzlegame.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Bui Quang Nhat Chau
 */
public class NumberPuzzleGame extends javax.swing.JFrame implements Runnable {
    
    private boolean win;
    private int moveCount;
    private int elapsedTime;
    private int size;
    Thread thread;

    /**
     * Creates new form NumberPuzzleGame
     */
    public NumberPuzzleGame() {
        initComponents();
        win = false;
        moveCount = 0;
        elapsedTime = 0;
        size = 3;
        thread = new Thread(this);
        setupUI();
    }
    
    @Override
    public void run() {
        while (!win) {
            elapsedTime += 1;
            
            setTextElapsedTime(elapsedTime);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
    }
    
    private void setupUI() {
        this.setTitle("Number Puzzle Game");
        this.setSize(300, 250);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        pGameDisplay.setLayout(new GridLayout());
    }
    
    private void addNumberButton() {
        ArrayList<JButton> listTemp = new ArrayList<>();
        for (int i = 1; i <= size * size; i++) {
            final JButton button;
            if (i == size * size) {
                button = new JButton("");
            } else {
                button = new JButton(i + "");
            }
            button.setPreferredSize(new Dimension(50, 50));
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    move(button);
                    if (checkWin()) {
                        win = true;
                        JOptionPane.showMessageDialog(null, "You won after " + getFormattedTime(elapsedTime) + " and " + moveCount + " moves.",
                                "You won", JOptionPane.DEFAULT_OPTION);
                    }
                }
            });
            listTemp.add(button);
        }
        Collections.shuffle(listTemp);
        while (!checkCanSolve(listTemp)) {
            Collections.shuffle(listTemp);
        }
        while (listTemp.size() > 0) {
            pGameDisplay.add(listTemp.remove(0));
        }
    }
    
    private boolean checkCanSolve(ArrayList<JButton> list) {
        int count = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            if (!"".equals(list.get(i).getText())) {
                int number = Integer.parseInt(list.get(i).getText());
                for (int j = i + 1; j < list.size(); j++) {
                    if (!"".equals(list.get(j).getText())) {
                        int numberBehind = Integer.parseInt(list.get(j).getText());
                        if (number > numberBehind) {
                            ++count;
                        }
                    }
                }
            }
        }
        return ((size % 2 == 0 && count % 2 != 0) || (size % 2 != 0 && count % 2 == 0));
    }
    
    private void move(JButton button) {
        String number = button.getText();
        if (!"".equals(number)) {
            int x = 0;
            int y = 0;
            for (x = 0; x < size; x++) {
                boolean matched = false;
                for (y = 0; y < size; y++) {
                    if (button.equals(getButtonAt(x, y))) {
                        matched = true;
                        break;
                    }
                }
                if (matched) {
                    break;
                }
            }
            JButton btnTemp = getButtonAt(x + 1, y);
            if (btnTemp != null && "".equals(btnTemp.getText())) {
                setTextMoveCount(++moveCount);
                btnTemp.setText(number);
                button.setText("");
                return;
            }
            btnTemp = getButtonAt(x - 1, y);
            if (btnTemp != null && "".equals(btnTemp.getText())) {
                setTextMoveCount(++moveCount);
                btnTemp.setText(number);
                button.setText("");
                return;
            }
            btnTemp = getButtonAt(x, y + 1);
            if (btnTemp != null && "".equals(btnTemp.getText())) {
                setTextMoveCount(++moveCount);
                btnTemp.setText(number);
                button.setText("");
                return;
            }
            btnTemp = getButtonAt(x, y - 1);
            if (btnTemp != null && "".equals(btnTemp.getText())) {
                setTextMoveCount(++moveCount);
                btnTemp.setText(number);
                button.setText("");
            }
        }
    }
    
    private boolean checkWin() {
        int check = 0;
        for (Component comp : pGameDisplay.getComponents()) {
            JButton btnTemp = (JButton) comp;
            if (!String.valueOf(++check).equals(btnTemp.getText())) {
                break;
            }
        }
        return (check == size * size);
    }
    
    private JButton getButtonAt(int x, int y) {
        if (x < 0 || y < 0 || x > size - 1 || y > size - 1) {
            return null;
        }
        return (JButton) pGameDisplay.getComponents()[x * size + y];
    }
    
    private void setTextElapsedTime(int elapsedTime) {
        lblElapsedTime.setText("Elapsed time: " + getFormattedTime(elapsedTime));
    }
    
    private static String getFormattedTime(int secs) { // from stackoverflow
//         int secs = (int) Math.round((double) milliseconds / 1000); // for millisecs arg instead of secs
        if (secs < 60) {
            return secs + "s";
        } else {
            int mins = (int) secs / 60;
            int remainderSecs = secs - (mins * 60);
            if (mins < 60) {
                return (mins < 10 ? "0" : "") + mins + "m "
                        + (remainderSecs < 10 ? "0" : "") + remainderSecs + "s";
            } else {
                int hours = (int) mins / 60;
                int remainderMins = mins - (hours * 60);
                return (hours < 10 ? "0" : "") + hours + "h "
                        + (remainderMins < 10 ? "0" : "") + remainderMins + "m "
                        + (remainderSecs < 10 ? "0" : "") + remainderSecs + "s";
            }
        }
    }
    
    private void setTextMoveCount(int moveCount) {
        lblMoveCount.setText("Move count: " + moveCount);
    }
    
    private void setResolution() {
        int index = cboSize.getSelectedIndex();
        switch (index) {
            case 0:
                this.setSize(330, 370);
                break;
            case 1:
                this.setSize(450, 420);
                break;
            case 2:
                this.setSize(480, 470);
                break;
            case 3:
                this.setSize(510, 520);
                break;
            default:
                this.setSize(400, 370);
                break;
        }
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pGameDisplay = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnNewGame = new javax.swing.JButton();
        cboSize = new javax.swing.JComboBox();
        lblSize = new javax.swing.JLabel();
        lblMoveCount = new javax.swing.JLabel();
        lblElapsedTime = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout pGameDisplayLayout = new javax.swing.GroupLayout(pGameDisplay);
        pGameDisplay.setLayout(pGameDisplayLayout);
        pGameDisplayLayout.setHorizontalGroup(
            pGameDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pGameDisplayLayout.setVerticalGroup(
            pGameDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 235, Short.MAX_VALUE)
        );

        btnNewGame.setText("New game");
        btnNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGameActionPerformed(evt);
            }
        });

        cboSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3 x 3", "4 x 4", "5 x 5", "6 x 6" }));
        cboSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSizeActionPerformed(evt);
            }
        });

        lblSize.setText("Size:");

        lblMoveCount.setText("Move count: 0");

        lblElapsedTime.setText("Elapsed time: 0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblElapsedTime)
                            .addComponent(lblMoveCount)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblSize)
                                .addGap(18, 18, 18)
                                .addComponent(cboSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(btnNewGame)))
                .addGap(117, 117, 117))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblElapsedTime)
                .addGap(11, 11, 11)
                .addComponent(lblMoveCount)
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblSize))
                    .addComponent(cboSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(btnNewGame)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pGameDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pGameDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGameActionPerformed
        win = false;
        moveCount = 0;
        elapsedTime = 0;
        setTextElapsedTime(elapsedTime);
        setTextMoveCount(moveCount);
        setResolution();
        pGameDisplay.removeAll();
        pGameDisplay.setLayout(new GridLayout(size, size));
        addNumberButton();
        if (!thread.isAlive()) {
            thread = new Thread(this);
            thread.start();
        }
    }//GEN-LAST:event_btnNewGameActionPerformed

    private void cboSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSizeActionPerformed
        int index = cboSize.getSelectedIndex();
        switch (index) {
            case 0:
                size = 3;
                break;
            case 1:
                size = 4;
                break;
            case 2:
                size = 5;
                break;
            case 3:
                size = 6;
                break;
            default:
                size = 3;
                break;
        }
    }//GEN-LAST:event_cboSizeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NumberPuzzleGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NumberPuzzleGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NumberPuzzleGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NumberPuzzleGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NumberPuzzleGame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewGame;
    private javax.swing.JComboBox cboSize;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblElapsedTime;
    private javax.swing.JLabel lblMoveCount;
    private javax.swing.JLabel lblSize;
    private javax.swing.JPanel pGameDisplay;
    // End of variables declaration//GEN-END:variables
}
