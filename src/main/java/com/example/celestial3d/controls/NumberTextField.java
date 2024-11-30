package com.example.celestial3d.controls;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class NumberTextField extends TextField {
    private boolean acceptsNegativeValues = false;
    public NumberTextField() {
        this.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d*)?")) { // Allow digits, one optional '.', and optional '-'
                return change; // Accept the change
            }
            return null; // Reject the change
        }));
    }
    public NumberTextField(boolean acceptsNegativeValues) {
        this.acceptsNegativeValues = acceptsNegativeValues;
        if (this.acceptsNegativeValues)
            this.setTextFormatter(new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.matches("-?\\d*(\\.\\d*)?")) { // Allow digits, one optional '.', and optional '-'
                    return change; // Accept the change
                }
                return null; // Reject the change
            }));
    }
    /*@Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text)
    {
        return text.matches("[0-9]*") ? !acceptsNegativeValues : text.matches("/^(0|-*[1-9]+[0-9]*)$/");
    }*/
}
