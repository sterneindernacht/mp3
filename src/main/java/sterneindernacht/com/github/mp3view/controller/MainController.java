package sterneindernacht.com.github.mp3view.controller;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import sterneindernacht.com.github.mp3view.mp3.Mp3Parser;
import sterneindernacht.com.github.mp3view.mp3.Mp3Player;
import sterneindernacht.com.github.mp3view.mp3.Mp3Song;

import java.io.File;
import java.io.IOException;

public class MainController {
    private static final String TITLE_COLUMN = "Tytuł";
    private static final String AUTHOR_COLUMN = "Autor";
    private static final String ALBUM_COLUMN = "Album";

    @FXML
    private MenuItem fileMenuItem;

    @FXML
    private MenuItem dirMenuItem;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private TableView<?> contentTableView;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    @FXML
    private ToggleButton playToggleButton;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider progressSlider;

    private Mp3Player mp3player;

    public void initialize() {
        createPlayer();
        configureActions();
        configureMenu();
        configureTableColumns();
    }

    private void createPlayer() {
        ObservableList<Mp3Song> items = (ObservableList<Mp3Song>) contentTableView.getItems();
        mp3player = new Mp3Player(items);
    }

    private void configureTableColumns() {
        TableColumn titleColumn = new TableColumn<>(TITLE_COLUMN);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));

        TableColumn authorColumn = new TableColumn<>(AUTHOR_COLUMN);
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("Author"));

        TableColumn albumColumn = new TableColumn<>(ALBUM_COLUMN);
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("Album"));

        contentTableView.getColumns().add(titleColumn);
        contentTableView.getColumns().add(authorColumn);
        contentTableView.getColumns().add(albumColumn);
    }

    private void configureMenu() {
        fileMenuItem.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mp3", "*.mp3"));
            File file = fc.showOpenDialog(new Stage());
            try {
                ((ObservableList<Mp3Song>) contentTableView.getItems()).add(Mp3Parser.createMp3Song(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        dirMenuItem.setOnAction(event -> {
            DirectoryChooser dc = new DirectoryChooser();
            File dir = dc.showDialog(new Stage());
            try {
                ((ObservableList<Mp3Song>) contentTableView.getItems()).addAll(Mp3Parser.createMp3List(dir));
            } catch (Exception e) {
                e.printStackTrace(); //ignore
            }
        });
    }

    private void playSelectedSong(int selectedIndex) {
        mp3player.loadSong(selectedIndex);
        configureProgressBar();
        configureVolume();
        playToggleButton.setSelected(true);
    }

    private void configureProgressBar() {
        mp3player.getMediaPlayer().setOnReady(() -> progressSlider.setMax(mp3player.getLoadedSongLength()));

        mp3player.getMediaPlayer().currentTimeProperty().addListener((arg, oldVal, newVal) ->
                progressSlider.setValue(newVal.toSeconds()));

        progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (progressSlider.isValueChanging()) {
                mp3player.getMediaPlayer().seek(Duration.seconds(newValue.doubleValue()));
            }
        });

    }

    @FXML
    private void configureVolume() {
        volumeSlider.valueProperty().unbind();
        volumeSlider.setMax(1.0);
        volumeSlider.valueProperty().bindBidirectional(mp3player.getMediaPlayer().volumeProperty());
    }

    @FXML
    private void configureActions() {
        playToggleButton.setOnAction(event -> {
            if (playToggleButton.isSelected()) {
                mp3player.play();
            } else {
                mp3player.stop();
            }
        });

        contentTableView.getSelectionModel().selectedItemProperty().addListener(
                ((observableValue, o, t1) -> {
                    if (contentTableView.getSelectionModel().getSelectedItem() != null) {
                        playSelectedSong(contentTableView.getSelectionModel().getSelectedIndex());
                    }
                })
        );

        nextButton.setOnAction(event -> {
            contentTableView.getSelectionModel().select(contentTableView.getSelectionModel().getSelectedIndex() + 1);
            playSelectedSong(contentTableView.getSelectionModel().getSelectedIndex());
        });

        previousButton.setOnAction(event -> {
            contentTableView.getSelectionModel().select(contentTableView.getSelectionModel().getSelectedIndex() - 1);
            playSelectedSong(contentTableView.getSelectionModel().getSelectedIndex());
        });
    }
}