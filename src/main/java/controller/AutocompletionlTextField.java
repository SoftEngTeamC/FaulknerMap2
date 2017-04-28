package controller;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;


public class AutocompletionlTextField extends TextField {
    //Local variables
    //entries to autocomplete
    private final SortedSet<String> entries;
    //popup GUI
    private ContextMenu entriesPopup;


    public AutocompletionlTextField() {
        super();
        this.entries = new TreeSet<>();
        this.entriesPopup = new ContextMenu();

        setListner();
    }

    private void setListner() {
        //Add "suggestions" by changing text
        textProperty().addListener((observable, oldValue, newValue) -> {
            String enteredText = getText();
            //always hide suggestion if nothing has been entered (only "spacebars" are dissalowed in TextFieldWithLengthLimit)
            if (enteredText == null || enteredText.isEmpty()) {
                entriesPopup.hide();
            } else {
                //filter all possible suggestions depends on "Text", case insensitive
                List<String> filteredEntries = entries.stream()
                        .filter(e -> e.toLowerCase().contains(enteredText.toLowerCase()))
                        .collect(Collectors.toList());
                //some suggestions are found
                if (!filteredEntries.isEmpty()) {
                    //build popup - list of "CustomMenuItem"
                    populatePopup(filteredEntries, enteredText);
                    if (!entriesPopup.isShowing()) { //optional
                        entriesPopup.show(AutocompletionlTextField.this, Side.BOTTOM, 0, 0); //position of popup
                    }
                    //no suggestions -> hide
                } else {
                    entriesPopup.hide();
                }
            }
        });

        //Hide always by focus-in (optional) and out
        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            entriesPopup.hide();
        });
    }

    private void populatePopup(List<String> searchResult, String searchReauest) {
        //List of "suggestions"
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //List size - 10 or founded suggestions count
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        //Build list as set of labels
        for (int i = 0; i < count; i++) {
            final String result = searchResult.get(i);
            //label with graphic (text flow) to highlight founded subtext in suggestions
            Label entryLabel = new Label();
            entryLabel.setGraphic(buildTextFlow(result, searchReauest));
            entryLabel.setPrefHeight(10);  //don't sure why it's changed with "graphic"
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            menuItems.add(item);

            //if any suggestion is select set it into text and close popup
            item.setOnAction(actionEvent -> {
                setText(result);
                positionCaret(result.length());
                entriesPopup.hide();
            });
        }

        //"Refresh" context menu
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

    public SortedSet<String> getEntries() { return entries; }

    public static TextFlow buildTextFlow(String text, String filter) {
        int filterIndex = text.toLowerCase().indexOf(filter.toLowerCase());
        Text textBefore = new Text(text.substring(0, filterIndex));
        Text textAfter = new Text(text.substring(filterIndex + filter.length()));
        Text textFilter = new Text(text.substring(filterIndex,  filterIndex + filter.length())); //instead of "filter" to keep all "case sensitive"
        textFilter.setFill(Color.BLUE);
        textFilter.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        return new TextFlow(textBefore, textFilter, textAfter);
    }
}
