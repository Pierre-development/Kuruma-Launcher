package fr.pierre.launcher;

import fr.litarvan.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static fr.pierre.launcher.Utils.loadImage;

public class Panel extends JPanel {

    public JTextField eMailField = new JTextField();
    public JPasswordField passwordField = new JPasswordField();
    private JButton playButton = new JButton(new ImageIcon(loadImage("ConnexionButton.png")));
    public JLabel infoLabel = new JLabel("", SwingConstants.CENTER);



    public Panel() {
        this.setLayout(null);

        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(eMailField.getFont().deriveFont(19F));
        infoLabel.setBounds(-290, 340, 975, 25);
        this.add(infoLabel);




        eMailField.setBounds(58, 243, 275, 48);
        eMailField.setOpaque(false);
        eMailField.setBorder(null);
        eMailField.setFont(eMailField.getFont().deriveFont(20F));
        eMailField.setCaretColor(Color.WHITE);
        eMailField.setForeground(Color.WHITE);
        this.add(eMailField);

        passwordField.setBounds(420, 243, 275, 48);
        passwordField.setOpaque(false);
        passwordField.setBorder(null);
        passwordField.setFont(passwordField.getFont().deriveFont(20F));
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setForeground(Color.WHITE);
        this.add(passwordField);

        playButton.setBounds(442, 362, 229, 65);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread gameThread = new Thread() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void run() {
                        playButton.setEnabled(false);
                        if(eMailField.getText().replaceAll(" ", "").length() == 0 || passwordField.getText().length() == 0) {
                            JOptionPane.showMessageDialog(Panel.this, "Erreur, veulliez entrer une EMail et un mot de passe valides", "Erreur", JOptionPane.ERROR_MESSAGE);
                            playButton.setEnabled(true);
                            return;
                        }

                        try {
                            Internal.auth(eMailField.getText(), passwordField.getText());
                        } catch (AuthenticationException authenticationException) {
                            JOptionPane.showMessageDialog(Panel.this, "Erreur, impossible de se connecter : " + authenticationException.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                            playButton.setEnabled(true);
                            this.interrupt();
                            return;
                        }

                        try {
                            Internal.update();
                        } catch (Exception e) {
                            Internal.updateThread.interrupt();
                            JOptionPane.showMessageDialog(Panel.this, "Erreur, impossible de mettre le jeu a jour : " + e, "Erreur", JOptionPane.ERROR_MESSAGE);
                            playButton.setEnabled(true);
                            this.interrupt();
                            return;
                        }

                        try {
                            Internal.launch();
                        } catch (LaunchException launchException) {
                            JOptionPane.showMessageDialog(Panel.this, "Erreur, de lancer le jeu : " + launchException.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                            playButton.setEnabled(true);
                            this.interrupt();
                        }

                    }
                };
                gameThread.start();
            }
        });
        this.add(playButton);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(loadImage("background.png"), 0, 0, this);
    }



}
