package com.example.serg.seabattle.common.service;

public class DialogService {
    private static DialogService dialogService;

    private DialogService() {

    }

    public static DialogService getDialogService() {
        if(dialogService == null) {
            dialogService = new DialogService();
        }
        return dialogService;
    }
}
