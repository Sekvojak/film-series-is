package com.presentation.ui;

import com.domain.Collection;
import com.domain.Film;
import com.presentation.ui.components.FilmCardPanel;
import com.service.CollectionService;
import com.service.FilmService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionPanel extends JPanel {

    private final CollectionService collectionService = new CollectionService();
    private final FilmService filmService = new FilmService();

    private JList<Collection> collectionList;
    private DefaultListModel<Collection> listModel;

    // detail kontajner (pravá strana)
    private JPanel detailContainer;

    public CollectionPanel() {
        setLayout(new BorderLayout(10, 10));

        // -----------------------------------------
        // ĽAVÝ PANEL – ZOZNAM KOLEKCIÍ
        // -----------------------------------------
        listModel = new DefaultListModel<>();
        collectionList = new JList<>(listModel);
        collectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        collectionList.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(collectionList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Moje kolekcie"));
        scrollPane.setPreferredSize(new Dimension(200, 0));
        add(scrollPane, BorderLayout.WEST);

        // -----------------------------------------
        // PRAVÝ PANEL – DETAIL KOLEKCIE
        // -----------------------------------------
        detailContainer = new JPanel(new BorderLayout());
        detailContainer.setOpaque(false);
        add(detailContainer, BorderLayout.CENTER);

        // -----------------------------------------
        // SPODNÝ PANEL S TLAČIDLAMI
        // -----------------------------------------
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnAdd = new JButton("Nová kolekcia");
        JButton btnEdit = new JButton("Upraviť kolekciu");

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);

        add(bottomPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> openCreateCollectionDialog());
        btnEdit.addActionListener(e -> {
            Collection selected = collectionList.getSelectedValue();
            if (selected != null) openEditCollectionDialog(selected);
        });

        // kliknutie na kolekciu
        collectionList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Collection selected = collectionList.getSelectedValue();
                if (selected != null) showCollectionDetail(selected);
            }
        });

        loadCollections();
    }

    // ---------------------------------------------------------
    // LOAD
    // ---------------------------------------------------------
    private void loadCollections() {
        List<Collection> collections = collectionService.getUserCollections();
        listModel.clear();

        for (Collection c : collections) listModel.addElement(c);

        if (!collections.isEmpty()) {
            collectionList.setSelectedIndex(0);
        }
    }

    // ---------------------------------------------------------
    // DETAIL KOLEKCIE
    // ---------------------------------------------------------
    private void showCollectionDetail(Collection collection) {

        detailContainer.removeAll();

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        detailPanel.setOpaque(false);

        boolean isDark = UIManager.getLookAndFeel().getName().toLowerCase().contains("dark");

        JLabel lblName = new JLabel(collection.getName());
        lblName.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblOwner = new JLabel(
                "Vlastník: " + collection.getOwner().getFname() + " " + collection.getOwner().getLname()
        );
        lblOwner.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblOwner.setAlignmentX(Component.CENTER_ALIGNMENT);

        detailPanel.add(lblName);
        detailPanel.add(lblOwner);
        detailPanel.add(Box.createVerticalStrut(20));

        if (collection.getFilmCollection().isEmpty()) {
            JLabel empty = new JLabel("(Kolekcia nemá žiadne filmy)");
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            detailPanel.add(empty);
        } else {
            for (Film f : collection.getFilmCollection()) {

                JPanel card = new FilmCardPanel(f, isDark, () -> openFilmDetailDialog(f));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
                card.setAlignmentX(Component.CENTER_ALIGNMENT);

                detailPanel.add(card);
                detailPanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scroll = new JScrollPane(detailPanel);
        scroll.setBorder(null);
        detailContainer.add(scroll, BorderLayout.CENTER);

        detailContainer.revalidate();
        detailContainer.repaint();
    }

    // ---------------------------------------------------------
    // DETAIL FILMU
    // ---------------------------------------------------------
    private void openFilmDetailDialog(Film film) {

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Detail filmu", true
        );
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        // ----------- CONTENT PANEL -----------
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel(film.getName() + " (" + film.getReleaseYear() + ")");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRating = new JLabel("Hodnotenie: " + film.getRating());
        lblRating.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea txtDesc = new JTextArea(film.getDescription());
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setBorder(BorderFactory.createTitledBorder("Popis"));

        content.add(lblTitle);
        content.add(Box.createVerticalStrut(5));
        content.add(lblRating);
        content.add(Box.createVerticalStrut(10));
        content.add(new JScrollPane(txtDesc));

        dialog.add(content, BorderLayout.CENTER);

        // ----------- BOTTOM BUTTONS -----------
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnTrailer = new JButton("Prehrať trailer");
        JButton btnClose = new JButton("Zavrieť");

        bottom.add(btnTrailer);
        bottom.add(btnClose);

        dialog.add(bottom, BorderLayout.SOUTH);

        // ----------- ACTIONS -----------

        btnTrailer.addActionListener(e -> {
            Film full = filmService.getFilmById(film.getId());
            String url = full.getTrailerUrl();

            if (url == null || url.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Tento film nemá trailer URL.");
                return;
            }

            try {
                Desktop.getDesktop().browse(new java.net.URI(url));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Nepodarilo sa otvoriť URL.");
            }
        });


        btnClose.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }


    // ---------------------------------------------------------
    // NOVÁ KOLEKCIA
    // ---------------------------------------------------------
    private void openCreateCollectionDialog() {
        openEditDialogInternal(null);
    }

    // ---------------------------------------------------------
    // UPRAVIŤ KOLEKCIU
    // ---------------------------------------------------------
    private void openEditCollectionDialog(Collection collection) {
        openEditDialogInternal(collection);
    }

    /**
     * Vnútorná metóda pre CREATE aj EDIT dialóg
     */
    private void openEditDialogInternal(Collection collection) {

        boolean isEdit = (collection != null);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                isEdit ? "Upraviť kolekciu" : "Nová kolekcia", true);

        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        // názov
        JTextField txtName = new JTextField(isEdit ? collection.getName() : "");
        txtName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        form.add(new JLabel("Názov kolekcie:"));
        form.add(txtName);

        // filmy
        form.add(new JLabel("Filmy:"));
        JPanel filmPanel = new JPanel();
        filmPanel.setLayout(new BoxLayout(filmPanel, BoxLayout.Y_AXIS));

        List<JCheckBox> checkboxes = new ArrayList<>();
        List<Film> allFilms = filmService.getAllFilms();

        for (Film f : allFilms) {
            JCheckBox cb = new JCheckBox(f.getName() + " (" + f.getReleaseYear() + ")");
            cb.putClientProperty("film", f);

            if (isEdit && collection.getFilmCollection().stream()
                    .anyMatch(x -> x.getId().equals(f.getId()))) {
                cb.setSelected(true);
            }

            filmPanel.add(cb);
            checkboxes.add(cb);
        }

        JScrollPane scroll = new JScrollPane(filmPanel);
        scroll.setPreferredSize(new Dimension(350, 200));
        form.add(scroll);

        dialog.add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton btnSave = new JButton("Uložiť");
        JButton btnCancel = new JButton("Zrušiť");

        bottom.add(btnSave);
        bottom.add(btnCancel);
        dialog.add(bottom, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {

            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Názov kolekcie je povinný.");
                return;
            }

            ArrayList<Film> selectedFilms = new ArrayList<>();
            for (JCheckBox cb : checkboxes) {
                if (cb.isSelected()) {
                    selectedFilms.add((Film) cb.getClientProperty("film"));
                }
            }

            if (isEdit) {
                collection.setName(name);
                collection.setFilmCollection(selectedFilms);
                collectionService.updateCollection(collection);
            } else {
                Collection newC = new Collection();
                newC.setName(name);
                newC.setFilmCollection(selectedFilms);
                collectionService.saveCollection(newC);
            }


            dialog.dispose();
            loadCollections();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }
}
