package vidmot;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class HelloController {

    /**
     * Called when any grid cell is clicked.
     * Computes which cell was clicked and switches view accordingly.
     */
    @FXML
    private void handleCellClick(MouseEvent event) {
        // The clicked node is the StackPane (the grid cell)
        StackPane cell = (StackPane) event.getSource();

        // Get the row and column indices from the GridPane layout.
        // (Note: These might be null if not explicitly set, so we default them to 0)
        Integer row = GridPane.getRowIndex(cell);
        Integer col = GridPane.getColumnIndex(cell);
        if (row == null) row = 0;
        if (col == null) col = 0;

        // Calculate a 1-based index for the cell.
        // Assuming 3 columns in the GridPane:
        // index = (row * number_of_columns) + col + 1.
        int cellIndex = row * 3 + col + 1;
        System.out.println("Cell clicked: " + cellIndex);

        try {
            // Use the cell index to get the corresponding View enum constant.
            // For example, if cellIndex is 1, then we get View.GRID1.
            View view = View.valueOf("GRID" + cellIndex);

            // Switch to the view using your ViewSwitcher class.
            ViewSwitcher.switchTo(view);
        } catch (IllegalArgumentException e) {
            System.err.println("No view available for cell: " + cellIndex);
        }
    }
}
