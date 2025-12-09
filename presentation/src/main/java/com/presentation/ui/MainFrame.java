package com.presentation.ui;

import com.dataaccess.DatabaseMode;
import com.dataaccess.GlobalConf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.service.session.UserSession;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Denník filmov a seriálov");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ----------------------
        // HORNÝ PANEL – info o userovi + prepínače
        // ----------------------
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel lblUser = new JLabel(
                "Prihlásený: " + UserSession.getLoggedUser().getFname()
                        + " " + UserSession.getLoggedUser().getLname()
        );
        lblUser.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel rightPanel = new JPanel();

        // ---- Databázový prepínač ----
        JComboBox<String> cmbDb = new JComboBox<>(new String[]{
                "SQL databáza",
                "Textová databáza"
        });
        cmbDb.setSelectedIndex(GlobalConf.isTextMode() ? 1 : 0);
        cmbDb.addActionListener(e -> switchDatabase(cmbDb.getSelectedIndex()));

        // ---- Dark mode prepínač ----
        JCheckBox darkToggle = new JCheckBox("Dark mode");

        // default: sme v light móde
        darkToggle.setSelected(false);

        darkToggle.addActionListener(e -> {
            if (darkToggle.isSelected()) {
                FlatDarkLaf.setup();
            } else {
                FlatLightLaf.setup();
            }
            SwingUtilities.updateComponentTreeUI(this);
        });

        // ---- Logout ----
        JButton btnLogout = new JButton("Odhlásiť sa");
        btnLogout.addActionListener(e -> logout());

        rightPanel.add(new JLabel("Databáza:"));
        rightPanel.add(cmbDb);
        rightPanel.add(darkToggle);
        rightPanel.add(btnLogout);

        topPanel.add(lblUser, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ----------------------
        // TABS – Filmy, Kolekcie
        // ----------------------
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Filmy", new FilmPanel());
        tabs.addTab("Kolekcie", new CollectionPanel());

        add(tabs, BorderLayout.CENTER);
    }

    // --------------------------
    // LOGOUT FUNKCIA
    // --------------------------
    private void logout() {
        UserSession.logout();
        new LoginFrame().setVisible(true);
        dispose();
    }

    // --------------------------
    // PREPÍNANIE DATABÁZY
    // --------------------------
    private void switchDatabase(int selected) {

        DatabaseMode newMode = (selected == 0)
                ? DatabaseMode.SQL
                : DatabaseMode.TEXT;

        if (newMode == GlobalConf.getCurrentMode()) {
            return; // nič sa nemení
        }

        GlobalConf.setDatabaseMode(newMode);

        JOptionPane.showMessageDialog(this,
                "Databáza bola prepnutá na: " +
                        (selected == 0 ? "SQL" : "Textovú") +
                        "\nBudete odhlásený."
        );

        logout();
    }
}
