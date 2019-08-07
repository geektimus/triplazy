/*
 * Copyright (c) 2019 Geektimus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package example

import com.codingmaniacs.triplazy.protocol.greeter.{ GreeterGrpc, HelloRequest, HelloResponse }
import com.typesafe.scalalogging.LazyLogging
import io.grpc.{ Server, ServerBuilder }

import scala.concurrent.{ ExecutionContext, Future }

object GreeterServer {

  def main(args: Array[String]): Unit = {
    val server = new GreeterServer(ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }

  private val port = 50051
}

class GreeterServer(executionContext: ExecutionContext) extends LazyLogging { self =>
  private[this] var server: Server = _

  private def start(): Unit = {
    val serverBuilder =
      ServerBuilder
        .forPort(GreeterServer.port)

    serverBuilder
      .addService(GreeterGrpc.bindService(new GreeterImpl, executionContext))

    server = serverBuilder.build.start

    logger.info("Server started, listening on " + GreeterServer.port)
    sys.addShutdownHook {
      System.err.println("*** shutting down gRPC server since JVM is shutting down")
      self.stop()
      System.err.println("*** server shut down")
    }
    ()
  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
    ()
  }

  private def blockUntilShutdown(): Unit =
    if (server != null) {
      server.awaitTermination()
    }

  private class GreeterImpl extends GreeterGrpc.Greeter {
    override def sayHello(req: HelloRequest): Future[HelloResponse] = {
      val reply = HelloResponse(message = "Hello " + req.name)
      Future.successful(reply)
    }
  }

}
