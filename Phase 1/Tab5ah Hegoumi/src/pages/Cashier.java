package pages;

import data.Item;
import data.MenuData;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Optional;


public class Cashier {

    private static Stage cashierStage;
    Label totalPrice = new Label("");

    private void styleButton(Button button, String bgColor, String textColor, double radius, double width, double height) {
        String style = "";
        if (bgColor != null)
            style += "-fx-background-color: " + bgColor + ";";
        if (textColor != null)
            style += "-fx-text-fill: " + textColor + ";";
        style += "-fx-background-radius: " + radius + "px;";
        button.setStyle(style);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
    }

    private Button createIconButton(String imagePath, String textColor, double width, double height) {
        ImageView img = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        img.setFitHeight(20);
        img.setFitWidth(20);
        Button btn = new Button("", img);
        styleButton(btn, "transparent", textColor, 8, width, height);
        btn.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 24));
        return btn;
    }

    private void showAlert(Alert.AlertType type, String message, Image icon) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(icon);
        alert.showAndWait();
    }

    boolean dark = util.ThemeManager.isDarkMode();

    public void effectButton(Button button) {
        effectButton(button, null);
    }

    public void effectButton(Button button, String shadowColor) {
        DropShadow shadow = (shadowColor != null) ? new DropShadow(10, Color.web(shadowColor)) : null;

        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1);
            st.setToY(1);
            st.play();
            if (shadow != null) button.setEffect(shadow);
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

    void updateTotalOrderPrice() {
        double total = 0.0;
        for (Item item : MenuData.cartItems) {
            total += item.getPrice() * item.getCount();
        }
        totalPrice.setText("total price is: " + total + " EGP");
        totalPrice.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
    }

    public void showWindow() {
        if (cashierStage != null && cashierStage.isShowing()) {
            cashierStage.setIconified(false);
            cashierStage.toFront();
            cashierStage.requestFocus();
            return;
        }

        cashierStage = new Stage();
        cashierStage.setResizable(false);
        cashierStage.setMaximized(true);
        cashierStage.initStyle(StageStyle.UNDECORATED);
        Image icon = new Image(getClass().getResource("/images/icon.png").toExternalForm());
        cashierStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));

        HBox bar = new HBox(10);
        ImageView logo = new ImageView(icon);
        logo.setFitHeight(25);
        logo.setFitWidth(25);

        Label title = new Label("Cashier");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-Black.ttf"), 20));
        title.setStyle("-fx-text-fill: " + (dark ? "#FFFFFF" : "#000000") + ";");

        ImageView closeImg = new ImageView(new Image(getClass().getResource(dark ? "/images/close_dark.png" : "/images/close.png").toExternalForm()));
        closeImg.setFitHeight(20);
        closeImg.setFitWidth(20);
        Button close = new Button("", closeImg);
        styleButton(close, "transparent", "#FFFFFF", 0, 50, 50);
        close.setOnAction(e -> cashierStage.close());
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

        ImageView backIcon = new ImageView(new Image(getClass().getResource(dark ? "/images/back_dark.png" : "/images/back.png").toExternalForm()));
        backIcon.setFitHeight(25);
        backIcon.setFitWidth(25);
        Button backBtn = new Button("", backIcon);
        backBtn.setOnAction(e -> {
            new Home().showWindow();
            cashierStage.close();
        });
        styleButton(backBtn, "transparent", "#682c26", 8, 25, 25);
        backBtn.setOnMouseEntered(e -> styleButton(backBtn, dark ? "#F98D36" : "#F67F20", "#FFFFFF", 0, 25, 25));
        backBtn.setOnMouseExited(e -> styleButton(backBtn, "transparent", "#FFFFFF", 0, 25, 25));


        Color textColor = Color.web(dark ? "#F98D36" : "#F67F20");

        ListView<Item> cartListView = new ListView<>(MenuData.cartItems);
        cartListView.setPrefSize(600, 400);
        cartListView.setStyle("-fx-background-color: " + (dark ? "#121212" : "#FFFFFF") + "; -fx-padding: 10;");

        cartListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: " + (dark ? "#121212" : "#FFFFFF") + ";");
                    return;
                }
                Label nameLabel = new Label(item.getName());
                Label priceLabel = new Label("Price: " + item.getPrice() + " EGP");
                Label countLabel = new Label("Qty: " + item.getCount());
                nameLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
                priceLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
                countLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
                nameLabel.setTextFill(textColor);
                priceLabel.setTextFill(textColor);
                countLabel.setTextFill(textColor);

                // Buttons
                Button addBtn = createIconButton("/images/add.png", "#12991F", 50, 30);
                Button removeBtn = createIconButton("/images/remove.png", "#F67F20", 50, 30);

                addBtn.setOnAction(e -> {
                    item.increaseCount();
                    countLabel.setText("Qty: " + item.getCount());
                    cartListView.refresh();
                    updateTotalOrderPrice();
                });

                removeBtn.setOnAction(e -> {
                    if (item.getCount() == 1) {
                        MenuData.cartItems.remove(item);
                    } else {
                        item.decreaseCount();
                        countLabel.setText("Qty: " + item.getCount());
                    }
                    cartListView.refresh();
                    updateTotalOrderPrice();
                });

                effectButton(addBtn);
                effectButton(removeBtn);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox btns = new HBox(5, addBtn, spacer, removeBtn);
                VBox lbls = new VBox(5, nameLabel, priceLabel, countLabel);
                HBox container = new HBox(5, lbls, spacer, btns);
                container.setStyle("-fx-padding: 10; -fx-background-color: " + (dark ? "#121212" : "#FFFFFF") +
                        "; -fx-border-color: " + (dark ? "#2C2C2C" : "#DDDDDD") + "; -fx-border-radius: 8; -fx-background-radius: 8;");
                setGraphic(container);
            }
        });

        // Menu ListView
        ListView<Item> menuListView = new ListView<>(MenuData.menuItems);
        menuListView.setPrefSize(600, 400);
        menuListView.setStyle("-fx-background-color: " + (dark ? "#121212" : "#FFFFFF") + "; -fx-padding: 10;");

        menuListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: " + (dark ? "#121212" : "#FFFFFF") + ";");
                    return;
                }
                Label nameLabel = new Label(item.getName());
                Label priceLabel = new Label(item.getPrice() + " EGP");
                Button addBtn = new Button("Add");
                addBtn.setStyle("-fx-text-fill: #12991F; -fx-background-color: transparent;");
                effectButton(addBtn, "transparent");

                nameLabel.setTextFill(textColor);
                priceLabel.setTextFill(textColor);
                nameLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
                priceLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));

                addBtn.setOnAction(e -> {
                    boolean found = false;
                    for (Item it : MenuData.cartItems) {
                        if (it.getCode() == item.getCode()) {
                            it.increaseCount();
                            cartListView.refresh();
                            updateTotalOrderPrice();
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        MenuData.cartItems.add(new Item(item.getCode(), item.getName(), item.getPrice()));
                    updateTotalOrderPrice();
                });

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                HBox priceAndAdd = new HBox(5, priceLabel, spacer, addBtn);
                VBox container = new VBox(5, nameLabel, priceAndAdd);
                container.setStyle("-fx-padding: 10; -fx-background-color: " + (dark ? "#121212" : "#FFFFFF") +
                        "; -fx-border-color: " + (dark ? "#2C2C2C" : "#DDDDDD") + "; -fx-border-radius: 8; -fx-background-radius: 8;");
                setGraphic(container);
            }
        });

        Button deleteAllBtn = new Button("Delete All");
        deleteAllBtn.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 20));
        styleButton(deleteAllBtn, dark ? "#F98D36" : "#F67F20", "#FFFFFF", 8, 600, 30);
        deleteAllBtn.setOnAction(e -> {
            if (!MenuData.cartItems.isEmpty()) {
                MenuData.cartItems.clear();
                updateTotalOrderPrice();
                showAlert(Alert.AlertType.INFORMATION, "All items removed from cart", icon);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "The cart is already empty.", icon);
            }
        });

        Button confirmBtn = new Button("Confirm Order");
        confirmBtn.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Beiruti-SemiBold.ttf"), 18));
        styleButton(confirmBtn, "#12991F", "#FFFFFF", 8, 600, 30);
        confirmBtn.setOnAction(e -> {
            if (MenuData.cartItems.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "The cart is empty, please add items first.", icon);
                return;
            }
            StringBuilder orderSummary = new StringBuilder("Order will be confirmed for:\n");
            double total = 0;
            for (Item item : MenuData.cartItems) {
                double itemTotal = item.getPrice() * item.getCount();
                orderSummary.append("- ").append(item.getName()).append(" x ").append(item.getCount())
                        .append(" = ").append(itemTotal).append(" EGP\n");
                total += itemTotal;
            }
            orderSummary.append("\nTotal: ").append(total).append(" EGP");

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, orderSummary.toString(), ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("Order Confirmation");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(icon);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                showAlert(Alert.AlertType.INFORMATION, "Your order confirmed successfully.", icon);
                MenuData.cartItems.clear();
                updateTotalOrderPrice();
            }
        });

        effectButton(confirmBtn, "#12991F");
        effectButton(deleteAllBtn, "#F67F20");

        totalPrice.setStyle("-fx-text-fill: " + (dark ? "#BBBBBB" : "#121212") + "; -fx-font-size: 18px;");
        updateTotalOrderPrice();

        VBox cartBox = new VBox(20, cartListView, totalPrice, deleteAllBtn, confirmBtn);
        cartBox.setPadding(new Insets(10));
        cartBox.setAlignment(Pos.CENTER);

        HBox listsBox = new HBox(20, menuListView, cartBox);
        listsBox.setAlignment(Pos.CENTER);

        HBox toolbar = new HBox(10, backBtn);
        toolbar.setPadding(new Insets(10));
        VBox root = new VBox(10, bar, toolbar, listsBox);
        root.setStyle("-fx-background-color: " + (dark ? "#121212" : "#FFFFFF") +
                "; -fx-border-color: " + (dark ? "#1E1E1E" : "#F5F5F5") +
                "; -fx-border-width: 0 6 6 6;");

        Platform.runLater(() -> {
            for (Node node : menuListView.lookupAll(".scroll-bar")) {
                if (node instanceof ScrollBar sb && sb.getOrientation() == Orientation.VERTICAL) {
                    sb.setOpacity(1.0);
                    sb.setStyle("-fx-background-color: " + (dark ? "#121212" : "#FFFFFF") + ";");
                    sb.setPrefWidth(12);
                }
            }
            for (Node node : cartListView.lookupAll(".scroll-bar")) {
                if (node instanceof ScrollBar sb && sb.getOrientation() == Orientation.VERTICAL) {
                    sb.setOpacity(1.0);
                    sb.setStyle("-fx-background-color: " + (dark ? "#121212" : "#FFFFFF") + ";");
                    sb.setPrefWidth(12);
                }
            }
        });

        cashierStage.setScene(new Scene(root));
        cashierStage.getIcons().add(icon);
        cashierStage.show();
    }
}
