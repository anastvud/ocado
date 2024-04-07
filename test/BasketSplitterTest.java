import com.ocado.basket.BasketSplitter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BasketSplitterTest {
    @Test
    public void testBadJSON() {
        Exception exception = assertThrows(com.google.gson.JsonSyntaxException.class, () -> {
            new BasketSplitter("/home/nastia/ocado/src/main/resources/badjson.json");
        });

        String expectedMessage = "Invalid JSON";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSplitWithAvailableMethods() {
        BasketSplitter basketSplitter = new BasketSplitter("/home/nastia/ocado/src/main/resources/config.json");
        List<String> items = Arrays.asList("Steak (300g)", "Carrots (1kg)", "Cold Beer (330ml)");

        Map<String, List<String>> result = basketSplitter.split(items);

        assertTrue(result.containsKey("Express Delivery"));
        assertEquals(1, result.size());
    }

    @Test
    public void testSplitWithNoAvailableMethods() {
        BasketSplitter basketSplitter = new BasketSplitter("/home/nastia/ocado/src/main/resources/config.json");
        List<String> items = Arrays.asList("Random Product");

        Map<String, List<String>> result = basketSplitter.split(items);

        assertTrue(result.containsKey("no_group"));
        assertEquals(1, result.size());
        assertEquals(1, result.get("no_group").size());
    }

    @Test
    public void testSplitWithEmptyBasket() {
        BasketSplitter basketSplitter = new BasketSplitter("/home/nastia/ocado/src/main/resources/config.json");
        List<String> items = Arrays.asList();

        Map<String, List<String>> result = basketSplitter.split(items);

        assertTrue(result.isEmpty());
    }

}
