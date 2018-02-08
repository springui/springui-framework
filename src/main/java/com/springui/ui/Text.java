package com.springui.ui;

import com.springui.i18n.Message;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/text")
public class Text extends Component {

    private final Message message;

    @Override
    public Message getMessage() {
        return message;
    }

    public String getValue() {
        return message.getText();
    }

    public void setDefaultValue(String defaultValue) {
        message.setDefaultText(defaultValue);
    }

    public Text(Message message) {
        this.message = message;
    }

    public Text(String messageCode, Object[] messageArgs, String defaultValue) {
        message = new Message();
        message.setCode(messageCode);
        message.setArgs(messageArgs);
        message.setDefaultText(defaultValue);
    }

    public Text(String messageCode, String defaultValue) {
        this(messageCode, null, defaultValue);
    }

    public Text(String messageCode) {
        this(messageCode, null, String.format("??%s??", messageCode));
    }
}