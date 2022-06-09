package com.company.bessties;

import com.company.bessties.socket.Client;

public class Validation {
    public static boolean isValid = false;

    public static String validateLogin(Client client, LogIn_Activity activity){
        if(!ageIsValid(client.getAge())){
            activity.ageInput.setText("");
            return "age";
        }
        else if(!nameIsValid(client.getFirstName())){
            activity.firstNameInput.setText("");
            return "firstName";
        }
        else if(!nameIsValid(client.getLastName())){
            activity.lastNameInput.setText("");
            return "lastname";
        }
        else if(!validateBarcode(client)){
            activity.barcodeInput.setText("");
            return "barcode";
        }

        return "valid";
    }

    public static boolean validateBarcode(Client client){
        Thread thread = new Thread(() -> {
            isValid = client.sendLogin(client.getFirstName(), client.getBarcode());
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public static boolean ageIsValid(int age){
        if (age < 18){
            return false;
        }
        return true;
    }

    public static boolean nameIsValid(String name){
        if (!name.matches("^[a-zA-Záéíóúàèìòùâêîôûãõñç]{2,32}$")){
            return false;
        }
        return true;
    }
}
