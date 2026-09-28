    // Port of Node/Net/SocketAddress.js.
    public static final class Address {
        public String address = "";
        public String family = "IPv4";
        public int port = 0;
        public Object flowLabel = null;
    }

    public static Object newImpl = (java.util.function.Function<Object, Object>) (options) ->
        (java.util.function.Supplier<Object>) () -> {
            Address address = new Address();
            if (options instanceof java.util.Map) {
                java.util.Map<String, Object> record = (java.util.Map<String, Object>) options;
                if (record.get("address") instanceof String) address.address = (String) record.get("address");
                if (record.get("family") instanceof String) address.family = (String) record.get("family");
                if (record.get("port") instanceof Number) address.port = ((Number) record.get("port")).intValue();
                address.flowLabel = record.get("flowLabel");
            }
            return address;
        };

    public static Object address = (java.util.function.Function<Object, Object>) (addressObj) ->
        ((Address) addressObj).address;

    public static Object familyImpl = (java.util.function.Function<Object, Object>) (addressObj) ->
        ((Address) addressObj).family;

    public static Object flowLabelImpl = (java.util.function.Function<Object, Object>) (addressObj) ->
        ((Address) addressObj).flowLabel;

    public static Object port = (java.util.function.Function<Object, Object>) (addressObj) ->
        ((Address) addressObj).port;
