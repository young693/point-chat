package com.point.chat.pointadmin.tio.start;

import com.point.chat.pointadmin.tio.config.JRTioConfig;
import com.point.chat.pointadmin.tio.handler.JRWsMsgHandler;
import com.point.chat.pointadmin.tio.listener.JRIpStatListener;
import com.point.chat.pointadmin.tio.listener.JRWsTioServerListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tio.server.TioServerConfig;
import org.tio.websocket.server.WsServerStarter;

/**
 * 启动websocket服务
 */
@Slf4j
@Component
public class JRWebsocketStarter {

    @Resource
    private JRWsMsgHandler wsMsgHandler;

    @Resource
    private JRIpStatListener ipStatListener;

    @Resource
    private JRWsTioServerListener wsTioServerListener;

    @Resource
    private JRTioConfig jrTioConfig;


    /**
     * 启动websocket服务
     */
    @PostConstruct
    public void start() throws Exception {
        WsServerStarter wsServerStarter = new WsServerStarter(jrTioConfig.getPort(), wsMsgHandler);
        TioServerConfig serverTioConfig = wsServerStarter.getTioServerConfig();
        serverTioConfig.setName(jrTioConfig.getProtocolName());
        serverTioConfig.setTioServerListener(wsTioServerListener);

        // 设置ip监控
        serverTioConfig.setIpStatListener(ipStatListener);
        // 设置ip统计时间段
        serverTioConfig.ipStats.addDurations(jrTioConfig.IPSTAT_DURATIONS);

        // 设置心跳超时时间
        serverTioConfig.setHeartbeatTimeout(jrTioConfig.getHeartbeatTimeout());

        // 如果你希望通过wss来访问(不通过nginx转发),到配置中开启,不过首先你得有SSL证书（证书必须和域名相匹配，否则可能访问不了ssl）
        if (jrTioConfig.getSsl().isEnable()) {
            log.info("开启ssl");
            serverTioConfig.useSsl(jrTioConfig.getSsl().getKeyStore(), jrTioConfig.getSsl().getTrustStore(), jrTioConfig.getSsl().getPwd());
        }
        jrTioConfig.setTioConfig(serverTioConfig);
        log.info("开始启动Web Socket");
        wsServerStarter.start();
    }

}
