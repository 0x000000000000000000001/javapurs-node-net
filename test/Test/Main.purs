module Test.Main (main, testNet) where

import Prelude

import Data.Maybe (Maybe(..))
import Effect (Effect)
import Effect.Aff (Aff, Milliseconds(..), delay, launchAff_, throwError)
import Effect.Class (liftEffect)
import Effect.Console (errorShow, infoShow, logShow)
import Effect.Exception (error)
import Effect.Ref as Ref
import Node.Buffer (toString)
import Node.Encoding (Encoding(..))
import Node.EventEmitter (on_)
import Node.Net.Server as Server
import Node.Net.Socket as Socket
import Node.Stream as Stream

main :: Effect Unit
main = launchAff_ testNet

-- The JVM port keeps the socket lifecycle on background threads. Wait for the
-- final close event instead of returning immediately after setup.
testNet :: Aff Unit
testNet = do
  closed <- liftEffect $ Ref.new false
  liftEffect $ start closed
  wait closed 0
  where
  wait closed attempts = do
    finished <- liftEffect $ Ref.read closed
    if finished then pure unit
    else if attempts >= 500 then throwError (error "Node Net suite timed out")
    else delay (Milliseconds 10.0) *> wait closed (attempts + 1)

start :: Ref.Ref Boolean -> Effect Unit
start closed = do
  server <- Server.createTcpServer
  server # on_ Server.connectionH \socket -> do
    let sDuplex = Socket.toDuplex socket
    infoShow { _message: "Server received connection" }
    void $ Stream.writeString sDuplex UTF8 "Hello socket\n"
    void $ Stream.writeString sDuplex UTF8 "Server is ending connection\n"
    Stream.end sDuplex

  server # on_ Server.closeH do
    infoShow { _message: "Server closed" }
  server # on_ Server.connectionH \socket -> do
    addr <- Socket.localAddress socket
    port <- Socket.localPort socket
    infoShow { _message: "Connection successfully made", addr, port }
  server # on_ Server.errorH \err -> do
    infoShow { _message: "Server had an error", err }
  server # on_ Server.listeningH do
    infoShow { _message: "Server is listening" }

  server # on_ Server.listeningH do
    addr <- Server.addressTcp server
    infoShow { _message: "Opened server", addr }
    case addr of
      Nothing ->
        Server.close server
      Just { port } -> do
        socket <- Socket.createConnectionTCP { port, host: "localhost" }
        let sDuplex = Socket.toDuplex socket
        socket # on_ Socket.connectH do
          infoShow { _message: "Connected to server" }

        socket # on_ Socket.closeH \failed -> do
          case failed of
            false -> infoShow { _message: "Socket closed without an error" }
            true -> errorShow { _message: "Socket closed with an error" }
          Ref.write true closed
        socket # on_ Socket.connectH do
          infoShow { _message: "Socket connected" }
        sDuplex # on_ Stream.dataH \buffer -> do
          bufferString <- toString UTF8 buffer
          logShow { _message: "Received some data", bufferString }
        sDuplex # on_ Stream.endH do
          infoShow { _message: "Socket ended, closing server" }
          Server.close server
        sDuplex # on_ Stream.errorH \err ->
          errorShow { _message: "Socket had an error", err }
        socket # on_ Socket.lookupH \err address family host ->
          case err of
            Just err' ->
              infoShow { _message: "Socket had an error resolving DNS", err: err' }
            Nothing ->
              infoShow { _message: "Socket successfully resolved DNS", address, family, host }
        socket # on_ Socket.readyH do
          infoShow { _message: "Socket is ready" }
          void $ Stream.writeString sDuplex UTF8 "Hello server"
        socket # on_ Socket.timeoutH do
          infoShow { _message: "Socket timed out" }
          void $ Stream.writeString sDuplex UTF8 "Closing connection"
          Stream.end sDuplex

  -- Both listening handlers must exist before the eager Java bind fires.
  Server.listenTcp server { port: 0, host: "localhost" }
