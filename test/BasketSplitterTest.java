import com.ocado.basket.BasketSplitter;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BasketSplitterTest {

    String json = "/home/nastia/ocado/src/main/resources/config.json";
    String badJson = "/home/nastia/ocado/src/main/resources/badjson.json";
    @Test
    public void testBadJSON() {
        Exception exception = assertThrows(com.google.gson.JsonSyntaxException.class, () -> {
            new BasketSplitter(badJson);
        });

        String expectedMessage = "Invalid JSON";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSplitWithAvailableMethods() throws FileNotFoundException {
        BasketSplitter basketSplitter = new BasketSplitter(json);
        List<String> items = Arrays.asList("Steak (300g)", "Carrots (1kg)", "Cold Beer (330ml)");

        Map<String, List<String>> result = basketSplitter.split(items);

        assertTrue(result.containsKey("Express Delivery"));
        assertEquals(1, result.size());
    }

    @Test
    public void testSplitWithNoAvailableMethods() throws FileNotFoundException {
        BasketSplitter basketSplitter = new BasketSplitter(json);
        List<String> items = Arrays.asList("Random Product");

        Map<String, List<String>> result = basketSplitter.split(items);

        assertTrue(result.containsKey("no_group"));
        assertEquals(1, result.size());
        assertEquals(1, result.get("no_group").size());
    }

    @Test
    public void testSplitWithEmptyBasket() throws FileNotFoundException {
        BasketSplitter basketSplitter = new BasketSplitter(json);
        List<String> items = Arrays.asList();

        Map<String, List<String>> result = basketSplitter.split(items);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFileNotFound() throws FileNotFoundException {
        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            new BasketSplitter("home/config.json");
        });

        String expectedMessage = "File not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
