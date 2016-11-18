package nl.elucidator.blog.keyvalue.parser;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Extension of the KeyValue reader to make it an Iterable.
 * This allows for the KV String to be streamed
 */
class KeyValueReaderIterable extends KeyValueReader implements Iterable<Map.Entry<String, String>> {

    /**
     * Default constructor
     * @param l line
     */

    public KeyValueReaderIterable(String l) {
        super(l);
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return new Iterator<Map.Entry<String, String>>() {
            @Override
            public boolean hasNext() {
                return available();
            }

            @Override
            public Map.Entry<String, String> next() {
                return nextEntry();
            }
        };
    }

    private String getKey() {
        if (is('\"')) {
            getc();
        }
        mark();
        skipUntil('=');
        return getMarkedSegment();
    }


    private String getValue() {
        skipTo('"');
        mark();
        skipUntil('"');
        return getMarkedSegment();
    }

    private Map.Entry<String, String> nextEntry() {
        return new AbstractMap.SimpleEntry<>(getKey().trim(), getValue());
    }

    /**
     * Stream a KVString
     * @param data kv data string
     * @return {@link Stream} of {@link Map.Entry}
     */
    public static Stream<Map.Entry<String, String>> stream(final String data) {
        return StreamSupport.stream(new KeyValueReaderIterable(data).spliterator(), true);
    }
}
