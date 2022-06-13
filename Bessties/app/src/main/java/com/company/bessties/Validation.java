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

        return "valid";
    }

    public static boolean validateBarcode(Client client, String barcode){
        Thread thread = new Thread(() -> {
            isValid = client.sendLogin(barcode);

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
        return age >= 18;
    }

    public static boolean nameIsValid(String name){
        return name.matches("^[a-zA-Záéíóúàèìòùâêîôûãõñç]{2,32}$");
    }
}
