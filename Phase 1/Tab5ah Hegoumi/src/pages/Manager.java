package pages;

import data.Item;
import data.MenuData;
import data.SqlCon;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.sql.SQLException;
import java.util.function.UnaryOperator;

import static data.MenuData.menuItems;

public class Manager {
    private static Stage managerStage;

    private void styleButton(Button button, String bgColor, String textColor, double radius, double width, double height) {
        button.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-text-fill: " + textColor + ";" +
                        "-fx-background-radius: " + radius + "px;"
        );
        button.setPrefWidth(width);
        button.setPrefHeight(height);
    }

    public void styleField(TextField field) {
        field.setPrefSize(350, 50);
        field.setStyle(dark ? "-fx-background-color: #121212; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #AAAAAA;"
                + "-fx-border-color: #2C2C2C; -fx-border-width: 2; -fx-border-radius: 4; -fx-background-radius: 4;"
                : "-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-prompt-text-fill: #999999;"
                + "-fx-border-color: #DDDDDD; -fx-border-width: 2; -fx-border-radius: 4; -fx-background-radius: 4;");
        field.focusedProperty().addListener((obs, oldVal, newVal) -> Platform.runLater(() -> {
            field.setStyle(dark ?
                    "-fx-background-color: #121212;" +
                            "-fx-text-fill: #FFFFFF;" +
                            "-fx-border-color: " + (field.isFocused() ? "#F98D36" : "#2C2C2C") + ";" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 4;" +
                            "-fx-background-radius: 4;" +
                            "-fx-prompt-text-fill: #AAAAAA;"
                    : "-fx-background-color: #FFFFFF;" +
                    "-fx-text-fill: #000000;" +
                    "-fx-border-color: " + (field.isFocused() ? "#F67F20" : "#DDDDDD") + ";" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 4;" +
                    "-fx-background-radius: 4;" +
                    "-fx-prompt-text-fill: #999999;"
            );

        }));
    }

    public void effectButton(Button button, String color) {
        DropShadow shadow = new DropShadow(10, Color.web(color));
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


    private void showAlert(Alert.AlertType type, String message, Image icon) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(icon);
        alert.showAndWait();
    }


    public void showWindow() {
        if (managerStage != null && managerStage.isShowing()) {
            managerStage.setIconified(false);
            managerStage.toFront();
            managerStage.requestFocus();
            return;
        }

        managerStage = new Stage();
        managerStage.setResizable(false);
        managerStage.setMaximized(true);
        managerStage.initStyle(StageStyle.UNDECORATED);
        managerStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        Image icon = new Image(getClass().getResource("/images/icon.png").toExternalForm());

        // -------------------- شريط العنوان --------------------

        HBox bar = new HBox(10);
        ImageView logo = new ImageView(icon);
        logo.setFitHeight(25);
        logo.setFitWidth(25);

        Label title = new Label("Manager");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-Black.ttf"), 20));
        title.setStyle("-fx-text-fill: " + (dark ? "#FFFFFF" : "#000000") + ";");

        ImageView closeImg = new ImageView(new Image(getClass().getResource(dark ? "/images/close_dark.png" : "/images/close.png").toExternalForm()));
        closeImg.setFitHeight(20);
        closeImg.setFitWidth(20);
        Button close = new Button("", closeImg);
        styleButton(close, "transparent", "#FFFFFF", 0, 50, 50);
        close.setOnAction(e -> managerStage.close());
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
        //-----------------------------------------------------

        ListView<Item> list = new ListView<>();
        list.setItems(menuItems);
        list.setStyle((dark ? "-fx-background-color: #121212;" : "-fx-background-color: #FFFFFF; ")
                + "-fx-padding: 10; -fx-background-insets: 0;");
        list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle(dark ? "-fx-background-color: #121212;" : "-fx-background-color: #FFFFFF;");
                } else {
                    VBox box = new VBox(5);
                    HBox hbox = new HBox(5);
                    Label nameLabel = new Label(item.getName());
                    Label codeLabel = new Label(item.getCode() + "");
                    Label priceLabel = new Label(item.getPrice() + " EGP");

                    nameLabel.setStyle(dark ? "-fx-text-fill: #F98D36" : "-fx-text-fill: #F67F20");
                    priceLabel.setStyle(dark ? "-fx-text-fill: #F98D36" : "-fx-text-fill: #F67F20");
                    codeLabel.setStyle(dark ? "-fx-text-fill: #F98D36" : "-fx-text-fill: #F67F20");
                    nameLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
                    priceLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
                    codeLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    hbox.getChildren().addAll(priceLabel, spacer);
                    box.setStyle(dark ? "-fx-padding: 10; -fx-background-color: #121212; -fx-border-color: #2C2C2C;" +
                            " -fx-border-radius: 8; -fx-background-radius: 8;" :
                            "-fx-padding: 10; -fx-background-color: #FFFFFF; -fx-border-color: #DDDDDD;" +
                                    " -fx-border-radius: 8; -fx-background-radius: 8;");
                    box.getChildren().addAll(codeLabel, nameLabel, hbox);

                    hbox.setStyle("-fx-padding: 0;" + (dark ? "-fx-background-color: #121212;" : "fx-background-color: #FFFFFF;"));
                    setGraphic(box);
                }
            }
        });

        VBox addBox = new VBox(20);
        addBox.setStyle("-fx-background-color: transparent;");
        TextField code = new TextField();
        code.setPromptText("enter code");
        TextField name = new TextField();
        name.setPromptText("Enter Name");
        TextField price = new TextField();
        price.setPromptText("Enter Price");
        Button add = new Button("add");
        Button update = new Button("Update");
        Button delete = new Button("Delete");
        code.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
        name.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
        price.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
        add.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
        update.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
        delete.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));

        add.setOnAction(e -> {
            if (name.getText().isEmpty() || code.getText().isEmpty() || price.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please enter full data", icon);
            } else {
                try {
                    int codeTxt = Integer.parseInt(code.getText());
                    String nameTxt = name.getText();
                    double priceTxt = Double.parseDouble(price.getText());
                    if (!SqlCon.itemExists(codeTxt)) {
                        Item newItem = new Item(codeTxt, nameTxt, priceTxt);
                        SqlCon.addItem(newItem);
                        MenuData.menuItems.setAll(SqlCon.getAllItems());
                        showAlert(Alert.AlertType.INFORMATION, "Item added successfully", icon);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Item already exists in database", icon);
                    }
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid number format", icon);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database error occurred", icon);
                }
            }
        });

        update.setOnAction(e -> {
            if (name.getText().isEmpty() || code.getText().isEmpty() || price.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please enter full data", icon);
            } else {
                try {
                    int codeTxt = Integer.parseInt(code.getText());
                    String nameTxt = name.getText();
                    double priceTxt = Double.parseDouble(price.getText());

                    if (SqlCon.itemExists(codeTxt)) {
                        SqlCon.updateItem(codeTxt, nameTxt, priceTxt);
                        MenuData.menuItems.setAll(SqlCon.getAllItems());
                        list.refresh();
                        showAlert(Alert.AlertType.INFORMATION, "Item updated successfully", icon);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Item is not found in database", icon);
                    }
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid number format", icon);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        delete.setOnAction(e -> {
            if (code.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please enter code", icon);
            } else {
                try {
                    int codeTxt = Integer.parseInt(code.getText());
                    if (SqlCon.itemExists(codeTxt)) {
                        Item item = SqlCon.getItemByCode(codeTxt);
                        if (item != null) {
                            SqlCon.deleteItem(codeTxt);
                            MenuData.menuItems.setAll(SqlCon.getAllItems());
                            showAlert(Alert.AlertType.INFORMATION, "Item deleted successfully", icon);
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Item is not found in database", icon);
                    }
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid number format", icon);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        addBox.getChildren().addAll(code, name, price, add, update, delete);

        styleButton(add, "#12991F", "#FFFFFF", 8, 350, 30);
        styleButton(update, "#12991F", "#FFFFFF", 8, 350, 30);
        styleButton(delete, "#F67F20", "#FFFFFF", 8, 350, 30);
        effectButton(add, "12991F");
        effectButton(update, "12991F");
        effectButton(delete, "F67F20");

        styleField(code);
        styleField(name);
        styleField(price);

        HBox listsBox = new HBox(15);
        listsBox.getChildren().addAll(list, addBox);
        listsBox.setAlignment(Pos.CENTER);

        ImageView back = new ImageView(new Image(getClass().getResource(dark ? "/images/back_dark.png"
                : "/images/back.png").toExternalForm()));
        back.setFitHeight(25);
        back.setFitWidth(25);
        Button backBtn = new Button("", back);
        backBtn.setOnAction(e -> {
            new Home().showWindow();
            managerStage.hide();
        });
        styleButton(backBtn, null, "#682c26", 8, 25, 25);
        HBox toolbar = new HBox(10, backBtn);
        toolbar.setPadding(new Insets(10));
        backBtn.setOnMouseEntered(e -> styleButton(backBtn, dark ? "#F98D36" : "#F67F20", "#FFFFFF", 0, 25, 25));
        backBtn.setOnMouseExited(e -> styleButton(backBtn, "transparent", "#FFFFFF", 0, 25, 25));

        VBox root = new VBox(10);
        listsBox.setPadding(new Insets(0, 15, 15, 15));
        root.getChildren().addAll(bar, toolbar, listsBox);
        root.setStyle(dark ? "-fx-background-color: #121212;-fx-border-color: #1E1E1E;" +
                " -fx-border-width: 0 6 6 6; -fx-border-radius: 0; -fx-background-radius: 0;"
                : "-fx-background-color: #FFFFFF;-fx-border-color: #F5F5F5; -fx-border-width: 0 6 6 6" +
                "; -fx-border-radius: 0; -fx-background-radius: 0;");
        Scene scene = new Scene(root);
        listsBox.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(listsBox, Priority.ALWAYS);
        list.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(list, Priority.ALWAYS);
        Platform.runLater(() -> {
            for (Node node : list.lookupAll(".scroll-bar")) {
                if (node instanceof ScrollBar) {
                    ScrollBar sb = (ScrollBar) node;

                    if (sb.getOrientation() == Orientation.VERTICAL) {
                        sb.setOpacity(1.0);
                        sb.setStyle(dark ? "-fx-background-color: #121212;" : "-fx-background-color: #FFFFFF;");
                        sb.setPrefWidth(12);
                    }
                }
            }
        });

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        code.setTextFormatter(formatter);

        UnaryOperator<TextFormatter.Change> filter2 = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> formatter2 = new TextFormatter<>(filter2);
        price.setTextFormatter(formatter2);

        managerStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (name.getText().isEmpty() || code.getText().isEmpty() || price.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Please enter full data", icon);
                } else {
                    try {
                        int codeTxt = Integer.parseInt(code.getText());
                        String nameTxt = name.getText();
                        double priceTxt = Double.parseDouble(price.getText());
                        if (!SqlCon.itemExists(codeTxt)) {
                            Item newItem = new Item(codeTxt, nameTxt, priceTxt);
                            SqlCon.addItem(newItem);
                            MenuData.menuItems.setAll(SqlCon.getAllItems());
                            showAlert(Alert.AlertType.INFORMATION, "Item added successfully", icon);
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Item already exists in database", icon);
                        }
                    } catch (NumberFormatException ex) {
                        showAlert(Alert.AlertType.ERROR, "Invalid number format", icon);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Database error occurred", icon);
                    }
                }
            }
        });

        managerStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE) {
                if (code.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Please enter code", icon);
                } else {
                    try {
                        int codeTxt = Integer.parseInt(code.getText());
                        if (SqlCon.itemExists(codeTxt)) {
                            Item item = SqlCon.getItemByCode(codeTxt);
                            if (item != null) {
                                SqlCon.deleteItem(codeTxt);
                                MenuData.menuItems.setAll(SqlCon.getAllItems());
                                showAlert(Alert.AlertType.INFORMATION, "Item deleted successfully", icon);
                            }
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Item is not found in database", icon);
                        }
                    } catch (NumberFormatException ex) {
                        showAlert(Alert.AlertType.ERROR, "Invalid number format", icon);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        managerStage.setScene(scene);
        managerStage.show();
    }
}