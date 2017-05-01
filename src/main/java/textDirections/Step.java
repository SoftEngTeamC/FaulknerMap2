package textDirections;

import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class Step {
    private String step;
    private Double distance;
    private static ResourceBundle bundle;

    Step(TextualDirections.Direction direction, TextualDirections.Sharpness sharpness,
         Double distance, ResourceBundle bundle) {
        this.distance = distance;
        Step.bundle = bundle;

        String sharpnessModifier = "";
        switch (sharpness) {
            case SHARP:
                sharpnessModifier = bundle.getString("sharp") + " ";
                break;
            case NORMAL:
                sharpnessModifier = "";
                break;
            case SLIGHT:
                sharpnessModifier = bundle.getString("slight") + " ";
                break;
        }

        String directionString = "";
        String commandString = "";
        String distanceString = " " + bundle.getString("andcontinuestraight") + " " +
                getDistance() + " " + bundle.getString("feet");
        switch (direction) {
            case LEFT:
                commandString = bundle.getString("takeA");
                directionString = bundle.getString("leftTurn");
                break;
            case RIGHT:
                commandString = bundle.getString("takeA");
                directionString = bundle.getString("rightTurn");
                break;
            case STRAIGHT:
                commandString = bundle.getString("continue");
                directionString = bundle.getString("straight");
                distanceString = bundle.getString("for") + getDistance() + bundle.getString("feet");
                break;
            case BACKWARDS:
                directionString = bundle.getString("turnAround");
                break;
        }

        this.step = commandString + sharpnessModifier + directionString + distanceString;
    }

    @Override
    public String toString() {
        return step;
    }

    public String getDistance() {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(distance);
    }
}
