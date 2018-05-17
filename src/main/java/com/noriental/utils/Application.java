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
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.*;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @Resource
    private PushUserInfoRepository pushUserInfoRepository;

    private JPushClient client;

    @Resource
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        client = new JPushClient(environment.getProperty("push.security_key"), environment.getProperty("push.app_key"));
        List<PushUserInfo> pushList = pushUserInfoRepository.findByAppKey(environment.getProperty("push.app_key"));
//        String[] pushIds = new String[pushList.size()];
        Map<String, String> ex = new HashMap<>();
        ex.put("p", "com.okay.personalcenter");
        String alert = environment.getProperty("push.alert");
        String title = environment.getProperty("push.title");
        Set<String> pushIdSet = new HashSet<>();
        for (PushUserInfo aPushList : pushList) {
            String pushId = aPushList.getPushId();
            if (!pushIdSet.contains(pushId)) {
                pushIdSet.add(pushId);
                pushWithRegistrationID(alert, title, ex, pushId);
                Thread.sleep(100);
            }
        }
    }

    private void pushWithRegistrationID(String alert, String title,
                                        Map<String, String> extras, String... pushId) {
        try {
            PushPayload pushPayload = buildPushObjectMessage(alert, title, extras, pushId);

            logger.info("pushPayload---\n {}\n", pushPayload.toJSON());
            PushResult pushResult = client.sendPush(pushPayload);
            logger.debug("pushResult.msg_id:{}", pushResult.msg_id);
            logger.debug("pushResult.sendno:{}", pushResult.sendno);
            logger.debug("pushResult.isResultOK:{}", pushResult.isResultOK());
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

    private PushPayload buildPushObjectMessage(String content, String title, Map<String, String> extras, String... registrationID) {
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
