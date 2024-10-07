import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private String FindNameForId(String user_id) {
        return Main.GetNameForId(user_id).orElse("");
    }

    @Test
    void testSearchTargets() {
        assertEquals(FindNameForId("5x82lq"), "Dr Adriana Wilde");
        assertEquals(FindNameForId("5ykft7"), "Ms Afrooz Shoaa");
        assertEquals(FindNameForId("5wy6dl"), "Professor Kirk Martinez");
        assertEquals(FindNameForId("5xfl2w"), "Dr Son Hoang");
        assertEquals(FindNameForId("5x96vy"), "Dr Jian Shi");
        assertEquals(FindNameForId("dem"), "Professor David Millard");
    }

    @Test
    void testSearchNonsenseTargets() {
        assertFalse(Main.GetNameForId("foo").isPresent());
        assertFalse(Main.GetNameForId("bar").isPresent());
        assertFalse(Main.GetNameForId("baz").isPresent());
    }

    @Test
    void testSearchIncorrectTargets() {
        assertFalse(Main.GetNameForId("5x96vy/login/etc").isPresent());
        assertFalse(Main.GetNameForId("5x96vy#").isPresent());
        assertFalse(Main.GetNameForId("#").isPresent());
        assertFalse(Main.GetNameForId("!:%$/\\").isPresent());
    }
}