package ru.dmitriymx.minecraft.metrics;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.dmitriymx.minecraft.logger.LoggerAdapter;

@RequiredArgsConstructor
public class MetricsServer {

    private final String host;
    private final int port;
    private final String endpoint;
    private final LoggerAdapter loggerAdapter;
    private final MinecraftInfoProvider minecraftInfoProvider;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @SneakyThrows
    public void start() {
        bossGroup = createEventLoopGroup();
        workerGroup = createEventLoopGroup();

        buildServerBootstrap().bind(host, port)
            .sync().channel().closeFuture();
    }

    @SneakyThrows
    public void stop() {
        workerGroup.shutdownGracefully().sync();
        bossGroup.shutdownGracefully().sync();
    }

    private EventLoopGroup createEventLoopGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup(1);
        } else {
            return new NioEventLoopGroup(1);
        }
    }

    private ServerBootstrap buildServerBootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
            .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline()
                        .addLast(new HttpServerCodec())
                        .addLast(new HttpHandler(endpoint, loggerAdapter, minecraftInfoProvider));
                }
            });

        return bootstrap;
    }
}
