package pages;

import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


public class Home {

    private void styleButton(Button button, String bgColor, String textColor, double radius, double width, double height) {
        button.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-text-fill: " + textColor + ";" +
                        "-fx-background-radius: " + radius + "px;"
        );
        button.setPrefWidth(width);
        button.setPrefHeight(height);
    }

    public void effectButton(Button button) {
        DropShadow shadow = new DropShadow(10, Color.web("#F67F20"));
        shadow.setRadius(10);
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1);
            st.setToY(1);
            st.play();
            button.setEffect(shadow);
        });
        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            button.setEffect(null);
        });
        button.setOnMousePressed(e -> {
            ScaleTransition press = new ScaleTransition(Duration.millis(100), button);
            press.setToX(0.9);
            press.setToY(0.9);
            press.play();
        });
        button.setOnMouseReleased(e -> {
            PauseTransition pause = new PauseTransition(Duration.millis(150));
            pause.setOnFinished(event -> {
                ScaleTransition release = new ScaleTransition(Duration.millis(120), button);
                release.setToX(1.0);
                release.setToY(1.0);
                release.play();
            });
            pause.play();
        });
    }

    boolean dark = util.ThemeManager.isDarkMode();


    private static Stage stage;



    public void showWindow() {

        if (stage != null && stage.isShowing()) {
            stage.setIconified(false);
            stage.toFront();
            stage.requestFocus();
            return;
        }

        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));


        Image icon = new Image(getClass().getResource("/images/icon.png").toExternalForm());

        // -------------------- شريط العنوان --------------------
        HBox bar = new HBox(10);
        ImageView logo = new ImageView(icon);
        logo.setFitHeight(25);
        logo.setFitWidth(25);

        Label title = new Label("Login");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-Black.ttf"), 20));
        title.setStyle("-fx-text-fill: " + (dark ? "#FFFFFF" : "#000000") + ";");

        ImageView closeImg = new ImageView(new Image(getClass().getResource(dark ? "/images/close_dark.png" : "/images/close.png").toExternalForm()));
        closeImg.setFitHeight(20);
        closeImg.setFitWidth(20);
        Button close = new Button("", closeImg);
        styleButton(close, "transparent", "#FFFFFF", 0, 50, 50);
        close.setOnAction(e -> stage.close());
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
        // -------------------- باقي العناصر --------------------
        Label label = new Label("Hello, Please login!");
        label.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-Black.ttf"), 30));

        label.setStyle(dark ? "-fx-text-fill: #FFFFFF;" : "-fx-text-fill: #000000;");

        PasswordField pass = new PasswordField();
        pass.setPromptText("Enter password");
        pass.setPrefSize(265, 50);
        pass.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 20));
        pass.textProperty().addListener((obs, oldVal, newVal) -> {

            pass.setFont(newVal.isEmpty() ? Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 20)
                    : Font.font("System", 20));
        });
        pass.setStyle(dark ? "-fx-background-color: #121212; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #AAAAAA;"
                + "-fx-border-color: " + (pass.isFocused() ? "#F98D36" : "#2C2C2C") + "; -fx-border-width: 2;" +
                " -fx-border-radius: 4; -fx-background-radius: 4;"
                : "-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-prompt-text-fill: #999999;"
                + "-fx-border-color: " + (pass.isFocused() ? "#F67F20" : "#DDDDDD") + "; -fx-border-width: 2;" +
                " -fx-border-radius: 4; -fx-background-radius: 4;");
        pass.focusedProperty().addListener((obs, oldVal, newVal) -> Platform.runLater(() -> {
            pass.setStyle(dark ?
                    "-fx-background-color: #121212;" +
                            "-fx-text-fill: #FFFFFF;" +
                            "-fx-border-color: " + (pass.isFocused() ? "#F98D36" : "#2C2C2C") + ";" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 4;" +
                            "-fx-background-radius: 4;" +
                            "-fx-prompt-text-fill: #AAAAAA;"
                    : "-fx-background-color: #FFFFFF;" +
                    "-fx-text-fill: #000000;" +
                    "-fx-border-color: " + (pass.isFocused() ? "#F67F20" : "#DDDDDD") + ";" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 4;" +
                    "-fx-background-radius: 4;" +
                    "-fx-prompt-text-fill: #999999;"
            );
        }));

        TextField passVisible = new TextField();
        passVisible.setPrefSize(265, 50);
        passVisible.setVisible(false);
        passVisible.setManaged(false);
        passVisible.setPromptText("Enter password");
        passVisible.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 20));
        passVisible.setStyle(dark ? "-fx-background-color: #121212; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #AAAAAA;"
                + "-fx-border-color: " + (pass.isFocused() ? "#F98D36" : "#2C2C2C") +
                "; -fx-border-width: 2; -fx-border-radius: 4; -fx-background-radius: 4;"
                : "-fx-background-color: #FFFFFF; -fx-tet-fill: #000000; -fx-prompt-text-fill: #999999;"
                + "-fx-border-color: " + (pass.isFocused() ? "#F67F20" : "#DDDDDD") +
                "; -fx-border-width: 2; -fx-border-radius: 4; -fx-background-radius: 4;");
        passVisible.textProperty().bindBidirectional(pass.textProperty());
        passVisible.focusedProperty().addListener((obs, oldVal, newVal) -> Platform.runLater(() -> {
            passVisible.setStyle(dark ?
                    "-fx-background-color: #121212;" +
                            "-fx-text-fill: #FFFFFF;" +
                            "-fx-border-color: " + (passVisible.isFocused() ? "#F98D36" : "#2C2C2C") + ";" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 4;" +
                            "-fx-background-radius: 4;" +
                            "-fx-prompt-text-fill: #AAAAAA;"
                    : "-fx-background-color: #FFFFFF;" +
                    "-fx-text-fill: #000000;" +
                    "-fx-border-color: " + (passVisible.isFocused() ? "#F67F20" : "#DDDDDD") + ";" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 4;" +
                    "-fx-background-radius: 4;" +
                    "-fx-prompt-text-fill: #999999;"
            );

        }));
        ImageView eyeOpen = new ImageView(new Image(getClass().getResource(dark ? "/images/eye_open_dark.png"
                : "/images/eye_open.png").toExternalForm()));
        eyeOpen.setFitWidth(25);
        eyeOpen.setFitHeight(25);


        ImageView eyeClosed = new ImageView(new Image(getClass().getResource(dark ? "/images/eye_closed_dark.png"
                : "/images/eye_closed.png").toExternalForm()));
        eyeClosed.setFitWidth(25);
        eyeClosed.setFitHeight(25);

        Button togglePassBtn = new Button();
        togglePassBtn.setGraphic(eyeOpen);
        togglePassBtn.setPrefSize(35, 50);
        togglePassBtn.setStyle("-fx-background-color: transparent;");
        togglePassBtn.setTooltip(new

                Tooltip("Show Password"));

        togglePassBtn.setOnAction(e ->

        {
            boolean visible = passVisible.isVisible();
            passVisible.setVisible(!visible);
            passVisible.setManaged(!visible);
            pass.setVisible(visible);
            pass.setManaged(visible);
            togglePassBtn.setGraphic(visible ? eyeOpen : eyeClosed);
            togglePassBtn.setTooltip(new Tooltip(visible ? "Show Password" : "Hide Password"));
        });


        HBox passwordBox = new HBox(5, pass, passVisible, togglePassBtn);
        passwordBox.setAlignment(Pos.CENTER);

        // -------------------- الأزرار --------------------
        ImageView managerImg = new ImageView(new Image(getClass().getResource("/images/manager.png").toExternalForm()));
        managerImg.setFitHeight(25);
        managerImg.setFitWidth(25);
        Button managerBtn = new Button("Login as Manager", managerImg);

        ImageView cashierImg = new ImageView(new Image(getClass().getResource("/images/cashier.png").toExternalForm()));
        cashierImg.setFitHeight(25);
        cashierImg.setFitWidth(25);
        Button cashierBtn = new Button("Login as Cashier", cashierImg);

        ImageView customerImg = new ImageView(new Image(getClass().getResource("/images/client.png").toExternalForm()));
        customerImg.setFitHeight(25);
        customerImg.setFitWidth(25);
        Button customerBtn = new Button("Login as Customer", customerImg);

        customerBtn.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 22));
        cashierBtn.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 22));
        managerBtn.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 22));
        if (dark) {
            styleButton(managerBtn, "#F98D36", "#121212", 8, 200, 30);
            styleButton(cashierBtn, "#F98D36", "#121212", 8, 200, 30);
            styleButton(customerBtn, "#F98D36", "#121212", 8, 220, 30);
        } else {
            styleButton(managerBtn, "#F67F20", "#FFFFFF", 8, 200, 30);
            styleButton(cashierBtn, "#F67F20", "#FFFFFF", 8, 200, 30);
            styleButton(customerBtn, "#F67F20", "#FFFFFF", 8, 220, 30);
        }
        effectButton(managerBtn);
        effectButton(cashierBtn);
        effectButton(customerBtn);

        HBox loginButtons = new HBox(10, managerBtn, cashierBtn);
        loginButtons.setAlignment(Pos.CENTER);

        Label customerLabel = new Label("Or continue as a customer");
        customerLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-Regular.ttf"), 22));
        customerLabel.setStyle(dark ? "-fx-text-fill: #BBBBBB;" : "-fx-text-fill: #999999;");

        VBox centerBox = new VBox(50, label, passwordBox, loginButtons, customerLabel, customerBtn);
        centerBox.setAlignment(Pos.CENTER);
        // -------------------- جذر المشهد --------------------
        ImageView backIcon = new ImageView(new Image(getClass().getResource(dark ? "/images/settings_dark.png" : "/images/settings.png").toExternalForm()));
        backIcon.setFitHeight(25);
        backIcon.setFitWidth(25);
        Button settingsBtn = new Button("", backIcon);
        settingsBtn.setOnAction(e -> {
            new Settings().showWindow();
            stage.close();
        });
        styleButton(settingsBtn, "transparent", "#682c26", 8, 25, 25);
        settingsBtn.setOnMouseEntered(e -> styleButton(settingsBtn, dark ? "#F98D36" : "#F67F20", "#FFFFFF", 0, 25, 25));
        settingsBtn.setOnMouseExited(e -> styleButton(settingsBtn, "transparent", "#FFFFFF", 0, 25, 25));
        HBox toolbar = new HBox(10, settingsBtn);
        toolbar.setPadding(new Insets(10));
        VBox root = new VBox(10, bar, toolbar, centerBox);
        centerBox.setLayoutY(root.getHeight() - centerBox.getHeight() / 2);
        root.setStyle((dark ? "-fx-background-color: #121212;-fx-border-color: #1E1E1E;"
                : "-fx-background-color: #FFFFFF;-fx-border-color: #F5F5F5;") +
                " -fx-border-width: 0 6 6 6; -fx-border-radius: 0; -fx-background-radius: 0;");
        Scene scene = new Scene(root, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
        stage.setScene(scene);

        // -------------------- الأحداث --------------------
        managerBtn.setOnAction(e ->

        {
            String password = pass.getText();
            if ("admin".equals(password)) {
                new Manager().showWindow();
                stage.close();
            } else if (password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter the password", ButtonType.OK);
                Stage alertstage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertstage.getIcons().add(icon);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The password is wrong", ButtonType.OK);
                Stage alertstage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertstage.getIcons().add(icon);
                alert.showAndWait();
            }
        });

        cashierBtn.setOnAction(e ->

        {
            String password = pass.getText();
            if ("csh".equals(password)) {
                new Cashier().showWindow();
                stage.close();
            } else if (password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter the password", ButtonType.OK);
                Stage alertstage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertstage.getIcons().add(icon);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The password is wrong", ButtonType.OK);
                Stage alertstage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertstage.getIcons().add(icon);
                alert.showAndWait();
            }
        });

        customerBtn.setOnAction(e ->

        {
            new Customer().showWindow();
            stage.close();
        });
        Platform.runLater(() -> root.requestFocus());
        stage.show();
    }
}
