package com.company.bessties;

/**
 * Public class pictureHandler
 * Saves selected profile picture
 */

public class pictureHandler {
    static int imageId;

    /**
     * Method saveImageID
     * Saved ID from selected profile picture
     * @param input from chosen profile picture
     */

    public static void saveImageID(int input){
        imageId = input;
    }

    /**
     * Method getImageID
     * Gets ID from selected profile picture
     * @return ID of chosen profile picture
     */

    public static int getImageID(){
        return imageId;
    }
}
