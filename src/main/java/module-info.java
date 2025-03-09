module org.main.module_03_card_24 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;
    requires commons.jexl3;
    requires exp4j;


    opens org.main.module_03_card_24 to javafx.fxml;
    exports org.main.module_03_card_24;
}