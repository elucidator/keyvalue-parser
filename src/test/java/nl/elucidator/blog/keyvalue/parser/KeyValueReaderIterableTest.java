package nl.elucidator.blog.keyvalue.parser;

import org.junit.Test;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;

/**
 * Test cases for the Streaming kV String
 */
public class KeyValueReaderIterableTest {
    private static final String INPUT = "key=\"value\" withEqualsSign=\"Base64==\" isEmpty=\"\" withSpaces=\" s p a c e s \" withEscapeChar=\"aaa\\\\bbb\" withEscapeChar2=\"aaa\\\"bbb\" withEscapeChar3=\"aaa\\]bbb\"";

    @Test
    public void variousVariations() throws Exception {
        final List<Map.Entry<String, String>> collect = KeyValueReaderIterable.stream(INPUT).collect(Collectors.toList());
        assertThat(collect, hasItem(new AbstractMap.SimpleEntry<>("key", "value")));
        assertThat(collect, hasItem(new AbstractMap.SimpleEntry<>("withEqualsSign", "Base64==")));
        assertThat(collect, hasItem(new AbstractMap.SimpleEntry<>("isEmpty", "")));
        assertThat(collect, hasItem(new AbstractMap.SimpleEntry<>("withSpaces", " s p a c e s ")));
        assertThat(collect, hasItem(new AbstractMap.SimpleEntry<>("withEscapeChar", "aaa\\\\bbb")));
        assertThat(collect, hasItem(new AbstractMap.SimpleEntry<>("withEscapeChar2", "aaa\\\"bbb")));
        assertThat(collect, hasItem(new AbstractMap.SimpleEntry<>("withEscapeChar3", "aaa\\]bbb")));
    }

    @Test(expected = IllegalStateException.class)
    public void notClosedData() throws Exception {
        KeyValueReaderIterable.stream("key=\"jdklasjdasjklda").collect(Collectors.toList());

    }

    @Test(expected = IllegalStateException.class)
    public void noDataSpecified() throws Exception {
        KeyValueReaderIterable.stream("key=").collect(Collectors.toList());
    }

    @Test(expected = IllegalStateException.class)
    public void illigalFormatting() throws Exception {
        KeyValueReaderIterable.stream("key").collect(Collectors.toList());
    }

    @Test
    public void processEmptyStringCorrectly() throws Exception {
        final List<Map.Entry<String, String>> collect = KeyValueReaderIterable.stream("").collect(Collectors.toList());
        assertThat(collect.isEmpty(), is(true));
    }

    @Test
    public void singleEqualsShouldNotResultInEntries() throws Exception {
        final List<Map.Entry<String, String>> collect = KeyValueReaderIterable.stream("=").collect(Collectors.toList());
        assertThat(collect.isEmpty(), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void noKeyAndNoEndValue() throws Exception {
        KeyValueReaderIterable.stream("=\"").collect(Collectors.toList());
    }

    @Test(expected = IllegalStateException.class)
    public void noKeyAndValidData() throws Exception {
        KeyValueReaderIterable.stream("=\"SomeData value\"").collect(Collectors.toList());
    }
}