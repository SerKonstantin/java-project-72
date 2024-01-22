package hexlet.code;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AppTest {
    @Test
    public void testAdd() {
        int result = App.add(2, 3);
        assertEquals(5, result);
    }
}
