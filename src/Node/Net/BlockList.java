    // Port of Node/Net/BlockList.js: the rules are kept as strings.
    public static final class BlockListValue {
        public final java.util.List<String> rules = new java.util.concurrent.CopyOnWriteArrayList<>();
    }

    public static Object addAddressImpl = (java.util.function.Function<Object, Object>) (list) ->
        (java.util.function.Function<Object, Object>) (address) ->
        (java.util.function.Function<Object, Object>) (family) ->
            (java.util.function.Supplier<Object>) () -> {
                ((BlockListValue) list).rules.add(String.valueOf(address));
                return null;
            };

    public static Object addRangeImpl = (java.util.function.Function<Object, Object>) (list) ->
        (java.util.function.Function<Object, Object>) (start) ->
        (java.util.function.Function<Object, Object>) (end) ->
        (java.util.function.Function<Object, Object>) (family) ->
            (java.util.function.Supplier<Object>) () -> {
                ((BlockListValue) list).rules.add(String.valueOf(start) + "-" + String.valueOf(end));
                return null;
            };

    public static Object addSubnetImpl = (java.util.function.Function<Object, Object>) (list) ->
        (java.util.function.Function<Object, Object>) (network) ->
        (java.util.function.Function<Object, Object>) (prefix) ->
        (java.util.function.Function<Object, Object>) (family) ->
            (java.util.function.Supplier<Object>) () -> {
                ((BlockListValue) list).rules.add(String.valueOf(network) + "/" + String.valueOf(prefix));
                return null;
            };

    public static Object checkImpl = (java.util.function.Function<Object, Object>) (list) ->
        (java.util.function.Function<Object, Object>) (address) ->
        (java.util.function.Function<Object, Object>) (family) ->
            (java.util.function.Supplier<Object>) () -> false;

    public static Object rulesImpl = (java.util.function.Function<Object, Object>) (list) ->
        (java.util.function.Supplier<Object>) () -> ((BlockListValue) list).rules.toArray(new Object[0]);
