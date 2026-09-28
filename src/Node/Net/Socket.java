    // Port of Node/Net/Socket.js over java.net. A socket is a duplex stream:
    // writes go to the network and a reader thread emits data/end/close.
    public static final class TcpSocket extends __M$Node_Stream.WritableStream {
        public java.net.Socket socket;
        public boolean connected = false;
        public int bytesRead = 0;
        public int bytesWritten = 0;
        public boolean closed = false;
        public java.math.BigDecimal timeoutMs = null;

        @Override
        public void writeBytes(byte[] bytes) {
            if (socket == null || socket.isClosed()) return;
            try {
                socket.getOutputStream().write(bytes);
                socket.getOutputStream().flush();
                bytesWritten += bytes.length;
            } catch (java.io.IOException failure) {
                fire("error", new RuntimeException(failure));
            }
        }

        @Override
        public void endStream() {
            try {
                if (socket != null && !socket.isClosed()) socket.shutdownOutput();
            } catch (java.io.IOException ignored) { }
        }
    }

    public static void __startReader(TcpSocket stream) {
        Thread reader = new Thread(() -> {
            try {
                java.io.InputStream input = stream.socket.getInputStream();
                byte[] buffer = new byte[8192];
                int count;
                while ((count = input.read(buffer)) > 0) {
                    stream.bytesRead += count;
                    stream.fire("data", __M$Node_Buffer.__wrap(java.util.Arrays.copyOf(buffer, count)));
                }
            } catch (java.io.IOException failure) {
                if (!stream.closed) stream.fire("error", new RuntimeException(failure));
            } finally {
                stream.closed = true;
                stream.fire("end");
                stream.fire("close", false);
            }
        });
        reader.setDaemon(true);
        reader.start();
    }

    private static java.util.Map<String, Object> __address(java.net.SocketAddress address) {
        java.util.Map<String, Object> record = new java.util.LinkedHashMap<>();
        if (address instanceof java.net.InetSocketAddress) {
            java.net.InetSocketAddress inet = (java.net.InetSocketAddress) address;
            record.put("port", inet.getPort());
            record.put("family", inet.getAddress() instanceof java.net.Inet6Address ? "IPv6" : "IPv4");
            record.put("address", inet.getAddress() != null ? inet.getAddress().getHostAddress() : inet.getHostString());
        } else {
            record.put("port", 0);
            record.put("family", "IPv4");
            record.put("address", "");
        }
        return record;
    }

    private static String __host(Object options) {
        if (options instanceof java.util.Map) {
            Object host = ((java.util.Map<String, Object>) options).get("host");
            if (host instanceof String) return (String) host;
        }
        return "localhost";
    }

    private static int __port(Object options) {
        if (options instanceof java.util.Map) {
            Object port = ((java.util.Map<String, Object>) options).get("port");
            if (port instanceof Number) return ((Number) port).intValue();
        }
        return 0;
    }

    public static void __connect(TcpSocket stream, Object options) {
        try {
            stream.socket = new java.net.Socket();
            stream.socket.connect(new java.net.InetSocketAddress(__host(options), __port(options)), 10000);
            stream.connected = true;
            stream.fire("connect");
            stream.fire("ready");
            __startReader(stream);
        } catch (java.io.IOException failure) {
            stream.fire("error", new RuntimeException(failure));
            stream.fire("close", true);
        }
    }

    public static Object newImpl = (java.util.function.Function<Object, Object>) (options) ->
        (java.util.function.Supplier<Object>) () -> new TcpSocket();

    public static Object addressImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Supplier<Object>) () -> __address(((TcpSocket) socketObj).socket.getLocalSocketAddress());

    public static Object bytesReadImpl = (java.util.function.Function<Object, Object>) (socket) ->
        (java.util.function.Supplier<Object>) () -> ((TcpSocket) socket).bytesRead;

    public static Object bytesWrittenImpl = (java.util.function.Function<Object, Object>) (socket) ->
        (java.util.function.Supplier<Object>) () -> ((TcpSocket) socket).bytesWritten;

    public static Object createConnectionImpl = (java.util.function.Function<Object, Object>) (options) ->
        (java.util.function.Supplier<Object>) () -> {
            TcpSocket stream = new TcpSocket();
            __connect(stream, options);
            return stream;
        };

    public static Object connectTcpImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Function<Object, Object>) (options) ->
            (java.util.function.Supplier<Object>) () -> {
                __connect((TcpSocket) socketObj, options);
                return socketObj;
            };

    public static Object connectIpcImpl = (java.util.function.Function<Object, Object>) (socket) ->
        (java.util.function.Function<Object, Object>) (path) ->
            (java.util.function.Supplier<Object>) () -> socket;

    public static Object connectingImpl = (java.util.function.Function<Object, Object>) (socket) ->
        (java.util.function.Supplier<Object>) () -> false;

    public static Object destroySoonImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Supplier<Object>) () -> {
            TcpSocket stream = (TcpSocket) socketObj;
            try { if (stream.socket != null) stream.socket.close(); } catch (java.io.IOException ignored) { }
            return null;
        };

    public static Object localAddressImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Supplier<Object>) () -> (String) __address(((TcpSocket) socketObj).socket.getLocalSocketAddress()).get("address");

    public static Object localPortImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Supplier<Object>) () -> ((Number) __address(((TcpSocket) socketObj).socket.getLocalSocketAddress()).get("port")).intValue();

    public static Object localFamilyImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Supplier<Object>) () -> (String) __address(((TcpSocket) socketObj).socket.getLocalSocketAddress()).get("family");

    public static Object pendingImpl = (java.util.function.Function<Object, Object>) (socket) ->
        (java.util.function.Supplier<Object>) () -> false;

    public static Object refImpl = (java.util.function.Function<Object, Object>) (socket) ->
        (java.util.function.Supplier<Object>) () -> null;

    public static Object remoteAddressImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Supplier<Object>) () -> (String) __address(((TcpSocket) socketObj).socket.getRemoteSocketAddress()).get("address");

    public static Object remotePortImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Supplier<Object>) () -> ((Number) __address(((TcpSocket) socketObj).socket.getRemoteSocketAddress()).get("port")).intValue();

    public static Object remoteFamilyImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Supplier<Object>) () -> (String) __address(((TcpSocket) socketObj).socket.getRemoteSocketAddress()).get("family");

    public static Object resetAndDestroyImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Supplier<Object>) () -> {
            TcpSocket stream = (TcpSocket) socketObj;
            try { if (stream.socket != null) stream.socket.close(); } catch (java.io.IOException ignored) { }
            return null;
        };

    public static Object setKeepAliveImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Supplier<Object>) () -> __keepAlive((TcpSocket) socketObj, true);

    public static Object setKeepAliveBooleanImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Function<Object, Object>) (enabled) ->
            (java.util.function.Supplier<Object>) () -> __keepAlive((TcpSocket) socketObj, Boolean.TRUE.equals(enabled));

    private static Object __keepAlive(TcpSocket stream, boolean enabled) {
        try { if (stream.socket != null) stream.socket.setKeepAlive(enabled); } catch (java.net.SocketException ignored) { }
        return null;
    }

    public static Object setKeepAliveInitialDelayImpl = (java.util.function.Function<Object, Object>) (socket) ->
        (java.util.function.Function<Object, Object>) (delay) ->
            (java.util.function.Supplier<Object>) () -> null;

    public static Object setKeepAliveAllImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Function<Object, Object>) (enabled) ->
        (java.util.function.Function<Object, Object>) (delay) ->
            (java.util.function.Supplier<Object>) () -> __keepAlive((TcpSocket) socketObj, Boolean.TRUE.equals(enabled));

    public static Object setNoDelayImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Supplier<Object>) () -> __noDelay((TcpSocket) socketObj, true);

    public static Object setNoDelayBooleanImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Function<Object, Object>) (enabled) ->
            (java.util.function.Supplier<Object>) () -> __noDelay((TcpSocket) socketObj, Boolean.TRUE.equals(enabled));

    private static Object __noDelay(TcpSocket stream, boolean enabled) {
        try { if (stream.socket != null) stream.socket.setTcpNoDelay(enabled); } catch (java.net.SocketException ignored) { }
        return null;
    }

    public static Object setTimeoutImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Function<Object, Object>) (milliseconds) ->
            (java.util.function.Supplier<Object>) () -> {
                ((TcpSocket) socketObj).timeoutMs = milliseconds instanceof java.math.BigDecimal
                    ? (java.math.BigDecimal) milliseconds
                    : java.math.BigDecimal.valueOf(((Number) milliseconds).doubleValue());
                return null;
            };

    public static Object timeoutImpl = (java.util.function.Function<Object, Object>) (socket) ->
        (java.util.function.Supplier<Object>) () -> null;

    public static Object unrefImpl = (java.util.function.Function<Object, Object>) (socket) ->
        (java.util.function.Supplier<Object>) () -> null;

    public static Object readyStateImpl = (java.util.function.Function<Object, Object>) (socketObj) ->
        (java.util.function.Supplier<Object>) () -> {
            TcpSocket stream = (TcpSocket) socketObj;
            return stream.closed ? "closed" : stream.connected ? "open" : "opening";
        };
