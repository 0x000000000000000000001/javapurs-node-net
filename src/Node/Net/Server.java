    // Port of Node/Net/Server.js over java.net.ServerSocket.
    public static final class TcpServer extends __M$Node_EventEmitter.EmitterBase {
        public java.net.ServerSocket server;
        public int port = 0;
        public String host = "localhost";
        public boolean listening = false;
        public boolean closed = false;
    }

    private static void __listen(TcpServer server, int port, String host) {
        try {
            server.server = new java.net.ServerSocket();
            server.server.setReuseAddress(true);
            server.server.bind(new java.net.InetSocketAddress(host, port));
            server.port = server.server.getLocalPort();
            server.host = host;
            server.listening = true;
            server.fire("listening");
        } catch (java.io.IOException failure) {
            server.fire("error", new RuntimeException(failure));
            return;
        }
        Thread acceptor = new Thread(() -> {
            while (!server.closed) {
                try {
                    java.net.Socket accepted = server.server.accept();
                    __M$Node_Net_Socket.TcpSocket socket = new __M$Node_Net_Socket.TcpSocket();
                    socket.socket = accepted;
                    socket.connected = true;
                    server.fire("connection", socket);
                    __M$Node_Net_Socket.__startReader(socket);
                } catch (java.io.IOException failure) {
                    if (!server.closed) server.fire("error", new RuntimeException(failure));
                    break;
                }
            }
            server.fire("close");
        });
        acceptor.setDaemon(true);
        acceptor.start();
    }

    public static Object newServerImpl = (java.util.function.Supplier<Object>) () -> new TcpServer();

    public static Object newServerOptionsImpl = (java.util.function.Function<Object, Object>) (options) ->
        (java.util.function.Supplier<Object>) () -> new TcpServer();

    public static Object addressTcpImpl = (java.util.function.Function<Object, Object>) (serverObj) ->
        (java.util.function.Supplier<Object>) () -> {
            TcpServer server = (TcpServer) serverObj;
            if (!server.listening) return null;
            java.util.Map<String, Object> record = new java.util.LinkedHashMap<>();
            record.put("port", server.port);
            record.put("family", "IPv4");
            record.put("address", server.server.getInetAddress().getHostAddress());
            return new __M$Data_Maybe.Just(record);
        };

    public static Object addressIpcImpl = (java.util.function.Function<Object, Object>) (server) ->
        (java.util.function.Supplier<Object>) () -> null;

    public static Object closeImpl = (java.util.function.Function<Object, Object>) (serverObj) ->
        (java.util.function.Supplier<Object>) () -> {
            TcpServer server = (TcpServer) serverObj;
            if (server.closed) return null;
            server.closed = true;
            server.listening = false;
            try { if (server.server != null) server.server.close(); } catch (java.io.IOException ignored) { }
            server.fire("close");
            return null;
        };

    public static Object getConnectionsImpl = (java.util.function.Function<Object, Object>) (server) ->
        (java.util.function.Function<Object, Object>) (callback) ->
            (java.util.function.Supplier<Object>) () -> null;

    public static Object listenImpl = (java.util.function.Function<Object, Object>) (serverObj) ->
        (java.util.function.Function<Object, Object>) (options) ->
            (java.util.function.Supplier<Object>) () -> {
                java.util.Map<String, Object> record = (java.util.Map<String, Object>) options;
                int port = record.get("port") instanceof Number ? ((Number) record.get("port")).intValue() : 0;
                String host = record.get("host") instanceof String ? (String) record.get("host") : "0.0.0.0";
                __listen((TcpServer) serverObj, port, host);
                return null;
            };

    public static Object listeningImpl = (java.util.function.Function<Object, Object>) (serverObj) ->
        (java.util.function.Supplier<Object>) () -> ((TcpServer) serverObj).listening;

    public static Object maxConnectionsImpl = (java.util.function.Function<Object, Object>) (server) ->
        (java.util.function.Supplier<Object>) () -> 0;

    public static Object refImpl = (java.util.function.Function<Object, Object>) (server) ->
        (java.util.function.Supplier<Object>) () -> null;

    public static Object unrefImpl = (java.util.function.Function<Object, Object>) (server) ->
        (java.util.function.Supplier<Object>) () -> null;
