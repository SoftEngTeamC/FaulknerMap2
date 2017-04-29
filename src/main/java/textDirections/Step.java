package textDirections;


import java.util.ResourceBundle;

public class Step {
    private String step;
    private Double distance;
    private static ResourceBundle bundle;

    Step(TextualDirections.Direction direction, TextualDirections.Sharpness sharpness, Double distance) {
        this.distance = distance;

        String sharpnessModifier = "";
        switch (sharpness) {
            case SHARP:
                sharpnessModifier = bundle.getString(" sharp ");
                break;
            case NORMAL:
                sharpnessModifier = "";
                break;
            case SLIGHT:
                sharpnessModifier = bundle.getString(" slight ");
                break;
        }

        String directionString = "";
        String commandString = "";
        String distanceString = bundle.getString(" and continue straight for ") + distance.toString() + bundle.getString(" feet.");
        switch (direction) {
            case LEFT:
                commandString = bundle.getString("Take a ");
                directionString = bundle.getString("left turn");
                break;
            case RIGHT:
                commandString = bundle.getString("Take a ");
                directionString = bundle.getString("right turn");
                break;
            case STRAIGHT:
                commandString = bundle.getString("Continue ");
                directionString = bundle.getString("straight");
                distanceString = bundle.getString(" for ") + distance.toString() + bundle.getString(" feet.");
                break;
            case BACKWARDS:
                directionString = bundle.getString("Turn around");
                break;
        }

        this.step = commandString + sharpnessModifier + directionString + distanceString;
    }

    @Override
    public String toString() {
        return step;
    }

    public Double getDistance() {
        return distance;
    }
}
