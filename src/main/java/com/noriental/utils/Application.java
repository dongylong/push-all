package com.noriental.utils;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author dongyl
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @Resource
    private PushUserInfoRepository pushUserInfoRepository;

    private JPushClient client;
    private static final int PUSH_LIMIT_TOTAL = 1000;

    @Resource
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    static int cpuCore = Runtime.getRuntime().availableProcessors();


    static ExecutorService threadPool = new ThreadPoolExecutor(
             1,
             1,
            0L,
            TimeUnit.MICROSECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    @Override
    public void run(String... args) throws Exception {
        logger.info("spring.profiles.active:{}", environment.getProperty("spring.profiles.active"));
        client = new JPushClient(environment.getProperty("push.security_key"), environment.getProperty("push.app_key"));
        List<PushUserInfo> pushList = pushUserInfoRepository.findByAppKey(environment.getProperty("push.app_key"));
//        String[] pushIds = new String[pushList.size()];
        String pushExtraTitle = environment.getProperty("push.extra.title");
        String pushExtraUrl = environment.getProperty("push.extra.url");
        String style = environment.getProperty("push.extra.style");
        String bgColor = environment.getProperty("push.extra.bgcolor");

        String pushLimitStr = environment.getProperty("push.limit");
        int pushLimit = Integer.valueOf(pushLimitStr.trim());

        logger.info("PUSH_LIMIT_TOTAL:{},pushLimit:{}", PUSH_LIMIT_TOTAL, pushLimit);
        if (PUSH_LIMIT_TOTAL < pushLimit) {
            logger.error("push.limit超过最大值1000");
            return;
        }
        Map<String, String> ex = new HashMap<>(16);
        ex.put("p", "com.okay.personalcenter");
        ex.put("title", pushExtraTitle);
        ex.put("url", pushExtraUrl);
        ex.put("style", style);
        ex.put("bgcolor", bgColor);
        String alert = environment.getProperty("push.alert");
        String title = environment.getProperty("push.title");
        logger.info("title:{}", title);
        Set<String> pushIdSet = new HashSet<>();
        Set<String> pushIdSetLimit = new HashSet<>();
//        int cpuCore = Runtime.getRuntime().availableProcessors();
//
//        logger.info("cpuCore:{}", cpuCore);
//        ExecutorService threadPool = new ThreadPoolExecutor(
//                cpuCore+1,
//                cpuCore+1,
//                0L,
//                TimeUnit.MICROSECONDS,
//                new LinkedBlockingQueue<>(),
//                new ThreadPoolExecutor.DiscardOldestPolicy());
        //jpush 测试环境 限流1分钟600次。600*1000=600000。一分钟可以发送60万用户。
        //jpush 生产环境 限流1分钟1200次。1200*1000=1200000。一分钟可以发送120万用户。用户量若达到120+需做delay或增加频率次数
        pushList.forEach(aPushList -> {
            String pushId = aPushList.getPushId();
            if (!pushIdSet.contains(pushId)) {
                pushIdSet.add(pushId);
                pushIdSetLimit.add(pushId);
                if (pushLimit == pushIdSetLimit.size()) {
                    String[] limitStr = pushIdSetLimit.toArray(new String[pushIdSetLimit.size()]);
                    threadPool.execute(
                            () -> {
                                try {
                                    pushWithRegistrationID(alert, title, ex, limitStr);
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                    logger.info("e:", e);
                                }
                            }
                    );
                    pushIdSetLimit.clear();
                }
            }
        });
//        for (PushUserInfo aPushList : pushList) {
//            String pushId = aPushList.getPushId();
//            if (!pushIdSet.contains(pushId)) {
//                pushIdSet.add(pushId);
//                pushIdSetLimit.add(pushId);
//                if (pushLimit == pushIdSetLimit.size()) {
//                    String[] limitStr = pushIdSetLimit.toArray(new String[pushIdSetLimit.size()]);
//                    threadPool.execute(
//                            () -> {
//                                try {
//                                    pushWithRegistrationID(alert, title, ex, limitStr);
//                                } catch (Exception e) {
//                                    logger.info("e:", e);
//                                }
//                            }
//                    );
//                    pushIdSetLimit.clear();
//                }
//            }
//        }

        if (!CollectionUtils.isEmpty(pushIdSetLimit)) {
            try {
                String[] limitStr = pushIdSetLimit.toArray(new String[pushIdSetLimit.size()]);
                pushWithRegistrationID(alert, title, ex, limitStr);
            } catch (Exception e) {
                logger.info("e:", e);
            }
        }
        logger.info("send all success");
    }

    private void pushWithRegistrationID(String alert, String title,
                                        Map<String, String> extras, String... pushId) {
        try {
            PushPayload pushPayload = buildPushObjectMessage(alert, title, extras, pushId);

            logger.info("pushPayload---\n {}\n", pushPayload.toJSON());
            PushResult pushResult = client.sendPush(pushPayload);
            logger.info("pushResult:{}", pushResult.toString());
            logger.info("pushResult.msg_id:{}", pushResult.msg_id);
            logger.info("pushResult.sendno:{}", pushResult.sendno);
            logger.info("pushResult.isResultOK:{}", pushResult.isResultOK());
            logger.info("频率次数:{}", pushResult.getRateLimitQuota());
            logger.info("重置时间:{}", pushResult.getRateLimitReset());
            logger.info("可用频率:{}", pushResult.getRateLimitRemaining());
        } catch (APIConnectionException e) {
            logger.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            logger.error("Should review the error, and fix the request", e);
            logger.info("HTTP Status: {}", e.getStatus());
            logger.info("Error Code: {}", e.getErrorCode());
            logger.info("Error Message: {}", e.getErrorMessage());
        } catch (Exception e) {
            logger.error("server error.", e);
        }
    }

    private PushPayload buildPushObjectMessage(String content, String title, Map<String, String> extras, String...
            registrationID) {
        PushPayload.Builder builder = PushPayload.newBuilder();
        builder.setPlatform(Platform.android());
        builder.setAudience(Audience.registrationId(registrationID));

        Message.Builder messageBuilder = Message.newBuilder();
        messageBuilder.setMsgContent(content).setTitle(title);
        if (extras != null && extras.size() > 0) {
            messageBuilder.addExtras(extras);
        }
        Message message = messageBuilder.build();
        builder.setMessage(message);
        builder.setOptions(Options.newBuilder().setTimeToLive(864000).build());

        return builder.build();
    }
}
