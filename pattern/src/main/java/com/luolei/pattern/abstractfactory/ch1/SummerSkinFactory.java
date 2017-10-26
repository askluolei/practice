package com.luolei.pattern.abstractfactory.ch1;

/**
 * @author luolei
 * @date 2017-03-29 17:18
 */
public class SummerSkinFactory implements SkinFactory {
    public Button createButton() {
        return new SummerButton();
    }

    public TextField createTextField() {
        return new SummerTextField();
    }

    public ComboBox createComboBox() {
        return new SummerComboBox();
    }
}
