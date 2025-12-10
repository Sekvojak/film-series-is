package com.presentation.ui.components;

import com.domain.Film;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FilmCardPanel extends JPanel {

    public FilmCardPanel(Film film, boolean isDark, Runnable onClick) {

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        Color bg = isDark
                ? new Color(55, 57, 60)
                : new Color(250, 250, 250);

        Color borderColor = isDark
                ? new Color(80, 80, 80)
                : new Color(210, 210, 210);

        Color hoverBg = isDark
                ? new Color(70, 72, 75)
                : new Color(240, 240, 240);

        Color text = isDark ? Color.WHITE : Color.BLACK;

        setBackground(bg);
        setOpaque(true);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(film.getName() + " (" + film.getReleaseYear() + ")");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitle.setForeground(text);

        JLabel lblRating = new JLabel("Hodnotenie: " + film.getRating());
        lblRating.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblRating.setForeground(text);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.add(lblTitle);
        content.add(Box.createVerticalStrut(5));
        content.add(lblRating);

        add(content, BorderLayout.CENTER);

        // -----------------------------------------
        // KLIKATEĽNOSŤ + HOVER EFEKT
        // -----------------------------------------
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBg);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(bg);
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) onClick.run();
            }
        });
    }
}
