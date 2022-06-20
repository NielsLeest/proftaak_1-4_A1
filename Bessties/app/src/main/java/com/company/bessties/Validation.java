package com.company.bessties;

import com.company.bessties.socket.Client;

/**
 * Public class Validation
 * Checks if input values are valid or not
 */

public class Validation {

    public static boolean isValid = false;

    /**
     * Method validateLogin
     * Makes the input fields empty when info is invalid
     * @param client to get info from
     * @param activity to validate
     * @return new input based on field
     */

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

    /**
     * Methode ageIsValid
     * Checks if the age is valid
     * @param age of client
     * @return true if age is valid
     */

    public static boolean ageIsValid(int age){
        return age >= 18;
    }

    /**
     * Method nameIsValid
     * Checks if first/lastname is valid
     * @param name of client
     * @return true if valid
     */

    public static boolean nameIsValid(String name){
        return name.matches("^[a-zA-Záéíóúàèìòùâêîôûãõñç ]{2,32}$");
    }
}
