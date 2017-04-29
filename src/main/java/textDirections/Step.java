package textDirections;


public class Step {
    private String step;
    private Double distance;

    Step(TextualDirections.Direction direction, TextualDirections.Sharpness sharpness, Double distance) {
        this.distance = distance;

        String sharpnessModifier = "";
        switch (sharpness) {
            case SHARP:
                sharpnessModifier = " sharp ";
                break;
            case NORMAL:
                sharpnessModifier = "";
                break;
            case SLIGHT:
                sharpnessModifier = " slight ";
                break;
        }

        String directionString = "";
        String commandString = "";
        String distanceString = " and continue straight for " + distance.toString() + " feet.";
        switch (direction) {
            case LEFT:
                commandString = "Take a ";
                directionString = "left turn";
                break;
            case RIGHT:
                commandString = "Take a ";
                directionString = "right turn";
                break;
            case STRAIGHT:
                commandString = "Continue ";
                directionString = "straight";
                distanceString = " for " + distance.toString() + " feet.";
                break;
            case BACKWARDS:
                directionString = "Turn around";
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
