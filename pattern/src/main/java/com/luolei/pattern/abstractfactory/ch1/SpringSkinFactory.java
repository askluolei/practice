package com.luolei.pattern.abstractfactory.ch1;

/**
 * @author luolei
 * @date 2017-03-29 17:18
 */
public class SpringSkinFactory implements SkinFactory {
    public Button createButton() {
        return new SpringButton();
    }

    public TextField createTextField() {
        return new SpringTextField();
    }

    public ComboBox createComboBox() {
        return new SpringComboBox();
    }
}
