module mp3view {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires jid3lib;
    requires javafx.media;

    exports sterneindernacht.com.github.mp3view to javafx.graphics;
    opens sterneindernacht.com.github.mp3view.controller to javafx.fxml;
    opens sterneindernacht.com.github.mp3view.mp3 to javafx.base;


}