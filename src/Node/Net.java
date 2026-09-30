    // Port of Node/Net.js.
    public static Object isIPImpl = (java.util.function.Function<Object, Object>) (value) -> {
        String text = (String) value;
        if (text.isEmpty()) return 0;
        if (text.contains(":")) {
            return text.matches("[0-9a-fA-F:.]+") ? 6 : 0;
        }
        String[] parts = text.split("\\.", -1);
        if (parts.length != 4) return 0;
        for (String part : parts) {
            if (part.isEmpty() || part.length() > 3) return 0;
            try {
                int octet = Integer.parseInt(part);
                if (octet < 0 || octet > 255) return 0;
            } catch (NumberFormatException failure) {
                return 0;
            }
        }
        return 4;
    };

    public static Object isIPv4 = (java.util.function.Function<Object, Object>) (value) ->
        ((Number) ((java.util.function.Function<Object, Object>) isIPImpl).apply(value)).intValue() == 4;

    public static Object isIPv6 = (java.util.function.Function<Object, Object>) (value) ->
        ((Number) ((java.util.function.Function<Object, Object>) isIPImpl).apply(value)).intValue() == 6;
