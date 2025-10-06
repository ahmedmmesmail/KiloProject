package pages;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.ThemeManager;

public class Settings {

    private static Stage settingsStage;

    private void styleButton(Button button, String bgColor, String textColor, double radius, double width, double height) {
        button.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-text-fill: " + textColor + ";" +
                        "-fx-background-radius: " + radius + "px;"
        );
        button.setPrefWidth(width);
        button.setPrefHeight(height);
    }

    public void showWindow() {
        boolean dark = ThemeManager.isDarkMode();

        if (settingsStage != null && settingsStage.isShowing()) {
            settingsStage.setIconified(false);
            settingsStage.toFront();
            settingsStage.requestFocus();
            return;
        }

        settingsStage = new Stage();
        settingsStage.initStyle(StageStyle.UNDECORATED);
        settingsStage.setResizable(false);
        settingsStage.setMaximized(true);
        settingsStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        Image icon = new Image(getClass().getResource("/images/icon.png").toExternalForm());

        // -------------------- Title Bar --------------------
        HBox bar = new HBox(10);
        ImageView logo = new ImageView(icon);
        logo.setFitHeight(25);
        logo.setFitWidth(25);

        Label title = new Label("Settings");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-Black.ttf"), 20));
        title.setStyle("-fx-text-fill: " + (dark ? "#FFFFFF" : "#000000") + ";");

        ImageView closeImg = new ImageView(new Image(getClass().getResource(dark ? "/images/close_dark.png" : "/images/close.png").toExternalForm()));
        closeImg.setFitHeight(20);
        closeImg.setFitWidth(20);
        Button close = new Button("", closeImg);
        styleButton(close, "transparent", "#FFFFFF", 0, 50, 50);
        close.setOnAction(e -> settingsStage.close());
        close.setOnMouseEntered(e -> styleButton(close, dark ? "#F98D36" : "#F67F20", "#FFFFFF", 0, 50, 50));
        close.setOnMouseExited(e -> styleButton(close, "transparent", "#FFFFFF", 0, 50, 50));

        HBox leftBox = new HBox(10, logo, title);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        bar.getChildren().addAll(leftBox, spacer, close);
        bar.setPadding(new Insets(0, 0, 0, 15));
        bar.setPrefHeight(35);
        bar.setStyle("-fx-background-color: " + (dark ? "#1E1E1E" : "#F5F5F5") + ";");

        // -------------------- Buttons --------------------
        ImageView backIcon = new ImageView(new Image(getClass().getResource(dark ? "/images/back_dark.png" : "/images/back.png").toExternalForm()));
        backIcon.setFitHeight(25);
        backIcon.setFitWidth(25);
        Button backBtn = new Button("", backIcon);
        backBtn.setOnAction(e -> {
            new Home().showWindow();
            settingsStage.close();
        });
        styleButton(backBtn, "transparent", "#682c26", 8, 25, 25);
        backBtn.setOnMouseEntered(e -> styleButton(backBtn, dark ? "#F98D36" : "#F67F20", "#FFFFFF", 0, 25, 25));
        backBtn.setOnMouseExited(e -> styleButton(backBtn, "transparent", "#FFFFFF", 0, 25, 25));

        Label label = new Label("by");
        label.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-Black.ttf"), 26));
        label.setStyle(dark ? "-fx-text-fill: #FFFFFF;" : "-fx-text-fill: #000000;");

        Label name = new Label("Ahmed Esmail");
        name.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-Black.ttf"), 30));
        name.setStyle(dark ? "-fx-text-fill: #FFFFFF;" : "-fx-text-fill: #000000;");

        Label from = new Label("from");
        from.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-Black.ttf"), 26));
        from.setStyle(dark ? "-fx-text-fill: #FFFFFF;" : "-fx-text-fill: #000000;");

        Label codenytra = new Label("CodeNytra");
        codenytra.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Turntablz BB.TTF"), 30));
        codenytra.setStyle(dark ? "-fx-text-fill: #FFFFFF;" : "-fx-text-fill: #000000;");

        VBox meBox = new VBox(10, label, name, from, codenytra);
        meBox.setAlignment(Pos.CENTER);

        // -------------------- Theme Switcher --------------------
        Label themeLabel = new Label("Theme:");
        themeLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-Black.ttf"), 20));
        themeLabel.setStyle(dark ? "-fx-text-fill: #FFFFFF;" : "-fx-text-fill: #000000;");

        ComboBox<String> themeSelector = new ComboBox<>();
        themeSelector.setStyle("-fx-background-color: " + (dark ? "#1E1E1E" : "# F5F5F5") + ";");
        themeSelector.getItems().addAll("Light", "Dark", "System");
        themeSelector.setValue(ThemeManager.getTheme());
        themeSelector.setStyle("-fx-pref-width: 120px;");
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 16);

        themeSelector.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(customFont);
                    setTextFill(dark? Color.web("F98D36") : Color.web("F67F20")); // لون النص
                    setStyle("-fx-background-color:" + (dark? "#1E1E1E;": "#F5F5F5"));
                }
            }
        });

// غيّر اللون في عناصر القائمة المنسدلة
        themeSelector.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(customFont);
                    setTextFill(dark? Color.web("F98D36") : Color.web("F67F20")); // لون النص
                    setStyle("-fx-background-color:" + (dark? "#1E1E1E;": "#F5F5F5")); // لون الخلفية
                }
            }
        });
        themeSelector.setOnAction(e -> {
            String choice = themeSelector.getValue();
            ThemeManager.setTheme(choice);
            settingsStage.close();
            Platform.runLater(() -> new Settings().showWindow());
        });

        HBox themeBox = new HBox(10, themeLabel, themeSelector);
        themeBox.setAlignment(Pos.CENTER);

        VBox centerBox = new VBox();
        centerBox.setPadding(new Insets(50));
        centerBox.setAlignment(Pos.TOP_CENTER);

        Region spacerCenter = new Region();
        VBox.setVgrow(spacerCenter, Priority.ALWAYS);

        centerBox.getChildren().addAll(themeBox, spacerCenter, meBox);



        // -------------------- Scene Root --------------------
        HBox toolbar = new HBox(10, backBtn);
        toolbar.setPadding(new Insets(10));
        VBox root = new VBox(10, bar, toolbar, centerBox);
        VBox.setVgrow(centerBox, Priority.ALWAYS);
        root.setStyle((dark ? "-fx-background-color: #121212;-fx-border-color: #1E1E1E;" :
                "-fx-background-color: #FFFFFF;-fx-border-color: #F5F5F5;") +
                " -fx-border-width: 0 6 6 6; -fx-border-radius: 0; -fx-background-radius: 0;");

        Scene scene = new Scene(root, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
        settingsStage.setScene(scene);

        Platform.runLater(() -> root.requestFocus());
        Platform.runLater(() -> {
            Region arrowButton = (Region) themeSelector.lookup(".arrow-button");
            if (arrowButton != null) {
                arrowButton.setStyle("-fx-background-color: " + (dark ? "#1E1E1E" : "#F5F5F5") + ";");
            }

            Region arrow = (Region) themeSelector.lookup(".arrow");
            if (arrow != null) {
                // استخدم تأثير إسقاط لون فوق السهم بدل ما نغير شكله
                arrow.setStyle("-fx-background-color: " + (dark ? "#F98D36" : "#F67F20") + ";");
            }

            themeSelector.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
                if (isNowShowing) {
                    Platform.runLater(() -> {
                        ListView<?> listView = (ListView<?>) themeSelector.lookup(".combo-box-popup .list-view");
                        if (listView != null) {
                            listView.setStyle(
                                    "-fx-background-color: " + (dark ? "#F5F5F5" : "#1E1E1E") + ";" +
                                            "-fx-border-color: " + (dark ? "#F98D36" : "#F67F20") + ";" +
                                            "-fx-border-width: 6;" +
                                            "-fx-border-radius: 6;" +
                                            "-fx-background-radius: 6;"
                            );
                        }
                    });
                }
            });
        });



        settingsStage.show();
    }
}
