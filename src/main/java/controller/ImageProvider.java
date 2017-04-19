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
}
