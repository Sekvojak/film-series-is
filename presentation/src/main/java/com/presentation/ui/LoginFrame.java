package com.presentation.ui;

import com.domain.User;
import com.service.UserService;
import com.service.session.UserSession;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final UserService userService = new UserService();

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Prihlásenie");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        emailField = new JTextField();
        passwordField = new JPasswordField();

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Heslo:"));
        panel.add(passwordField);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();

        JButton loginBtn = new JButton("Prihlásiť");
        JButton registerBtn = new JButton("Registrovať");

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> openRegister());

        panel.add(loginBtn);
        panel.add(registerBtn);

        return panel;
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Zadajte email aj heslo.");
            return;
        }

        User user = userService.login(email, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Nesprávny email alebo heslo.");
            return;
        }

        UserSession.login(user); // uložíme prihláseného používateľa

        JOptionPane.showMessageDialog(this, "Prihlásenie úspešné!");

        // otvorenie hlavného okna
        MainFrame main = new MainFrame();
        main.setVisible(true);

        dispose(); // zatvorí login okno
    }

    private void openRegister() {
        RegisterFrame reg = new RegisterFrame();
        reg.setVisible(true);
        dispose();
    }
}
