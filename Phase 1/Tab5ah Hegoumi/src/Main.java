import data.MenuData;
import data.SqlCon;
import javafx.application.Application;
import javafx.stage.Stage;
import pages.Home;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SqlCon db = new SqlCon();
        MenuData.menuItems.setAll(db.getAllItems());
        new Home().showWindow();

    }
}
