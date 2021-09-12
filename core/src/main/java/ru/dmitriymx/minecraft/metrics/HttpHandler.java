package ru.dmitriymx.minecraft.metrics;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import ru.dmitriymx.minecraft.logger.LoggerAdapter;

import java.nio.charset.StandardCharsets;

public class HttpHandler extends ChannelInboundHandlerAdapter {

    private final FullHttpResponse responseNotFound;

    private final String endpoint;
    private final LoggerAdapter logger;
    private final MinecraftInfoProvider minecraftInfoProvider;

    public HttpHandler(String endpoint, LoggerAdapter logger, MinecraftInfoProvider minecraftInfoProvider) {
        this.endpoint = endpoint;
        this.logger = logger;
        this.minecraftInfoProvider = minecraftInfoProvider;

        ByteBuf byteBuf = Unpooled.wrappedBuffer("404: Not Found".getBytes(StandardCharsets.UTF_8));
        this.responseNotFound = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, byteBuf);
        this.responseNotFound.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
        this.responseNotFound.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof HttpRequest)) {
            if (!(msg instanceof LastHttpContent)) {
                logger.debug("Incoming request is unknown");
                logger.debug("request class: {}", msg.getClass());
            }
            return;
        }

        channelRead1(ctx, (HttpRequest) msg);
    }

    private void channelRead1(ChannelHandlerContext ctx, HttpRequest request) {
        HttpResponse response;

        if (request.method() != HttpMethod.GET) {
            logger.warn("Incoming request method \"{}\" not allowed", request.method());
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED);
        } else {
            QueryStringDecoder queryString = new QueryStringDecoder(request.uri());
            logger.debug("uri.path: {}", queryString.path());

            if (!queryString.path().equals(endpoint)) {
                response = responseNotFound;
            } else {
                ByteBuf buffer = Unpooled.wrappedBuffer(metrics().getBytes(StandardCharsets.UTF_8));

                response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
                response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buffer.readableBytes());
            }
        }

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private String metrics() {
        return players() + tps();
    }

    private String players() {
        return "# HELP mc_players_online Online players\n" +
            "# TYPE mc_players_online gauge\n" +
            "mc_players_online " + minecraftInfoProvider.playersOnline() + '\n';
    }

    private String tps() {
        return "# HELP mc_tps TPS\n" +
            "# TYPE mc_tps gauge\n" +
            "mc_tps " + minecraftInfoProvider.tps() + '\n';
    }
}
