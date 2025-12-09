package com.presentation;

import com.dataaccess.GlobalConf;

import com.formdev.flatlaf.FlatLightLaf;
import com.presentation.ui.LoginFrame;

public class App {
    public static void main(String[] args) {

        FlatLightLaf.setup();

        GlobalConf.init();

        new LoginFrame().setVisible(true);
    }
}
