package main;

import controller.MainController;


public class UrMain {

    static MainController mainController;

    public static void main(String[] args) {
        mainController = new MainController();
        mainController.start();

    }



}
