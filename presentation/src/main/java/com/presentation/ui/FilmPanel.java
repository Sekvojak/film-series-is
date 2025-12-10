package com.presentation.ui;

import com.domain.Film;
import com.domain.Genre;
import com.service.FilmService;
import com.service.GenreService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FilmPanel extends JPanel {

    private final FilmService filmService = new FilmService();
    private final GenreService genreService = new GenreService();

    // Quick filter components
    private JTextField txtQuickSearch;
    private JComboBox<Genre> cmbQuickGenre;
    private JComboBox<String> cmbQuickYear;
    private JComboBox<String> cmbQuickRating;

    // Film form
    private JComboBox<Genre> cmbGenre;
    private JTextField txtName;
    private JTextField txtYear;
    private JTextField txtRating;
    private JTextArea txtDescription;

    // Film list
    private DefaultListModel<Film> filmListModel;
    private JList<Film> filmList;

    private JTextField txtTrailerUrl;


    private Film currentFilm = null;

    public FilmPanel() {
        setLayout(new BorderLayout(10, 10));

        // ---------- QUICK FILTER BAR ----------
        add(createQuickFilterBar(), BorderLayout.NORTH);

        // ---------- FORM ----------
        add(createFilmForm(), BorderLayout.CENTER);

        // ---------- RIGHT SIDE ─ LIST ----------
        add(createFilmList(), BorderLayout.EAST);

        // ---------- BOTTOM BUTTONS ----------
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadFilms();
    }

    // -------------------------------------------------------------------------
    // QUICK FILTER BAR
    // -------------------------------------------------------------------------
    private JPanel createQuickFilterBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Search bar
        txtQuickSearch = new JTextField(20);
        txtQuickSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applyQuickFilter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applyQuickFilter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applyQuickFilter();
            }
        });


        panel.add(new JLabel("Hľadať:"));
        panel.add(txtQuickSearch);

        // Genre filter
        cmbQuickGenre = new JComboBox<>();
        cmbQuickGenre.addItem(null);
        for (Genre g : genreService.getAllGenres()) cmbQuickGenre.addItem(g);
        cmbQuickGenre.addActionListener(e -> applyQuickFilter());
        panel.add(new JLabel("Žáner:"));
        panel.add(cmbQuickGenre);

        // Year filter
        cmbQuickYear = new JComboBox<>(new String[]{"Všetko", "2020+", "2010–2019", "2000–2009", "< 2000"});
        cmbQuickYear.addActionListener(e -> applyQuickFilter());
        panel.add(new JLabel("Rok:"));
        panel.add(cmbQuickYear);

        // Rating filter
        cmbQuickRating = new JComboBox<>(new String[]{"Všetko", "8+", "7+", "6+"});
        cmbQuickRating.addActionListener(e -> applyQuickFilter());
        panel.add(new JLabel("Rating:"));
        panel.add(cmbQuickRating);

        return panel;
    }

    private void applyQuickFilter() {
        String name = txtQuickSearch.getText().trim().toLowerCase();
        Genre genre = (Genre) cmbQuickGenre.getSelectedItem();

        Integer yearFrom = null, yearTo = null;
        String yearFilter = (String) cmbQuickYear.getSelectedItem();

        if ("2020+".equals(yearFilter)) {
            yearFrom = 2020;
        } else if ("2010–2019".equals(yearFilter)) {
            yearFrom = 2010;
            yearTo = 2019;
        } else if ("2000–2009".equals(yearFilter)) {
            yearFrom = 2000;
            yearTo = 2009;
        } else if ("< 2000".equals(yearFilter)) {
            yearTo = 1999;
        }


        Double minRating = null;
        String ratingFilter = (String) cmbQuickRating.getSelectedItem();
        if ("8+".equals(ratingFilter)) {
            minRating = 8.0;
        } else if ("7+".equals(ratingFilter)) {
            minRating = 7.0;
        } else if ("6+".equals(ratingFilter)) {
            minRating = 6.0;
        }


        filmListModel.clear();
        for (Film f : filmService.filterFilms(name, genre, yearFrom, yearTo, minRating)) {
            filmListModel.addElement(f);
        }
    }

    // -------------------------------------------------------------------------
    // FORM
    // -------------------------------------------------------------------------
    private JPanel createFilmForm() {
        JPanel formPanel = new JPanel(new BorderLayout());

        // ZMENENÉ: 5 riadkov namiesto 4
        JPanel fields = new JPanel(new GridLayout(5, 2, 5, 5));

        txtName = new JTextField();
        txtYear = new JTextField();
        txtRating = new JTextField();
        txtTrailerUrl = new JTextField();   // 🔥 nové pole

        fields.add(new JLabel("Názov filmu:"));
        fields.add(txtName);

        cmbGenre = new JComboBox<>();
        loadGenresIntoComboBox();
        fields.add(new JLabel("Žáner:"));
        fields.add(cmbGenre);

        fields.add(new JLabel("Rok vydania:"));
        fields.add(txtYear);

        fields.add(new JLabel("Hodnotenie (0–10):"));
        fields.add(txtRating);

        // 🔥 nový riadok
        fields.add(new JLabel("Trailer URL:"));
        fields.add(txtTrailerUrl);

        formPanel.add(fields, BorderLayout.NORTH);

        txtDescription = new JTextArea(5, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txtDescription), BorderLayout.CENTER);

        return formPanel;
    }


    // -------------------------------------------------------------------------
    // FILM LIST
    // -------------------------------------------------------------------------
    private JScrollPane createFilmList() {
        filmListModel = new DefaultListModel<>();
        filmList = new JList<>(filmListModel);
        filmList.setBorder(BorderFactory.createTitledBorder("Filmy v databáze"));

        filmList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Film selected = filmList.getSelectedValue();
                if (selected != null) fillForm(selected);
            }
        });

        JScrollPane scroll = new JScrollPane(filmList);
        scroll.setPreferredSize(new Dimension(250, 0));
        return scroll;
    }

    // -------------------------------------------------------------------------
    // BUTTONS
    // -------------------------------------------------------------------------
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();

        JButton btnNew = new JButton("Nový");
        JButton btnSave = new JButton("Uložiť");
        JButton btnDelete = new JButton("Vymazať");


        panel.add(btnNew);
        panel.add(btnSave);
        panel.add(btnDelete);


        btnNew.addActionListener(e -> { currentFilm = null; clearForm(); filmList.clearSelection(); });
        btnSave.addActionListener(e -> saveFilm());
        btnDelete.addActionListener(e -> deleteFilm());

        return panel;
    }

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------
    private void saveFilm() {
        String name = txtName.getText().trim();
        String yearText = txtYear.getText().trim();
        String ratingText = txtRating.getText().trim();
        String description = txtDescription.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Názov filmu je povinný.");
            return;
        }

        int year;
        double rating;

        try { year = Integer.parseInt(yearText); }
        catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Rok musí byť číslo."); return; }

        try { rating = Double.parseDouble(ratingText); }
        catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Hodnotenie musí byť číslo."); return; }

        Film film = (currentFilm == null) ? new Film() : currentFilm;

        film.setName(name);
        film.setDescription(description);
        film.setReleaseYear(year);
        film.setRating(rating);
        film.setGenre((Genre) cmbGenre.getSelectedItem());
        film.setTrailerUrl(txtTrailerUrl.getText().trim());


        if (film.getId() == null) {
            filmService.saveFilm(film);
            JOptionPane.showMessageDialog(this, "Film pridaný.");
        } else {
            filmService.updateFilm(film);
            JOptionPane.showMessageDialog(this, "Film upravený.");
        }

        clearForm();
        loadFilms();
    }

    private void clearForm() {
        txtName.setText("");
        cmbGenre.setSelectedIndex(-1);
        txtYear.setText("");
        txtRating.setText("");
        txtDescription.setText("");
        txtTrailerUrl.setText("");

    }

    private void loadFilms() {
        filmListModel.clear();
        for (Film f : filmService.getAllFilms()) filmListModel.addElement(f);
    }

    private void fillForm(Film film) {
        currentFilm = film;
        txtName.setText(film.getName());
        cmbGenre.setSelectedItem(film.getGenre());
        txtYear.setText(String.valueOf(film.getReleaseYear()));
        txtRating.setText(String.valueOf(film.getRating()));
        txtDescription.setText(film.getDescription());
        txtTrailerUrl.setText(film.getTrailerUrl());

    }

    private void deleteFilm() {
        if (currentFilm == null) {
            JOptionPane.showMessageDialog(this, "Vyber film zo zoznamu.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Naozaj chceš odstrániť film: " + currentFilm.getName() + "?",
                "Potvrdenie",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            filmService.deleteFilm(currentFilm.getId());
            JOptionPane.showMessageDialog(this, "Film odstránený.");
            clearForm();
            loadFilms();
        }
    }

    private void loadGenresIntoComboBox() {
        cmbGenre.removeAllItems();
        for (Genre g : genreService.getAllGenres()) cmbGenre.addItem(g);
    }
}
