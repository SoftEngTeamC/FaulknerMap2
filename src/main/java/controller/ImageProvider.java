package controller;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

public class ImageProvider {
    private static final Map<String, Image> images = new HashMap<>();

    public static Image getImage(String name) {
        if (!images.containsKey(name)) {
            Image newImage = new Image(name);
            images.put(name, newImage);
            return newImage;
        }
        return images.get(name);
    }

    public static Image getImageByFloor(int i){
        String ImageName ="";
        switch(i) {
            case 1:
                ImageName = "images/1_thefirstfloor.png";
            case 2:
                ImageName = "images/2_thesecondfloor.png";
            case 3:
                ImageName = "images/3_thethirdfloor.png";
            case 4:
                ImageName = "images/4_thefourthfloor.png";
            case 5:
                ImageName = "images/5_thefifthfloor.png";
            case 6:
                ImageName = "images/6_thesixthfloor.png";
            case 7:
                ImageName = "images/7_theseventhfloor.png";
        }
        return getImage(ImageName);
    }
}
