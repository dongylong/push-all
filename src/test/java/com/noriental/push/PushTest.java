//package com.noriental.push;
//
//import com.noriental.utils.Application;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * @author dongyl
// * @date 16:55 12/25/17
// * @project push-all
// */
//public class PushTest {
//    private static final Logger logger = LoggerFactory.getLogger(Application.class);
//
//    public static void main(String[] args) {
//
//    }
//
//    @Test
//    public void threadPool() {
//        HashSet<String> hashSet = new HashSet<>();
//        hashSet.add("123");
//        hashSet.add("456");
//        hashSet.add("1456");
//        hashSet.add("9876");
//        System.out.println("hashSet:");
//
//    }
//
//    @Test
//    public void test() {
//        Set<String> pushIdSetLimit = new HashSet<>();
//        pushIdSetLimit.add("123");
//        pushIdSetLimit.add("456");
//        pushIdSetLimit.add("1456");
//        String[] limitStr = pushIdSetLimit.toArray(new String[pushIdSetLimit.size()]);
//        System.out.println(limitStr);
//    }
////    @Test
////    public void test() {
////        client = new JPushClient(environment.getProperty("push.security_key"), environment.getProperty("push.app_key"));
////        List<PushUserInfo> pushList = pushUserInfoRepository.findByAppKey(environment.getProperty("push.app_key"));
//////        String[] pushIds = new String[pushList.size()];
////        String pushExtraTitle = environment.getProperty("push.extra.title");
////        String pushExtraUrl = environment.getProperty("push.extra.url");
////        Map<String, String> ex = new HashMap<>(16);
////        ex.put("p", "com.okay.personalcenter");
////        ex.put("title", pushExtraTitle);
////        ex.put("url", pushExtraUrl);
////        String alert = environment.getProperty("push.alert");
////        String title = environment.getProperty("push.title");
////
////        Set<String> pushIdSet = new HashSet<>();
////        for (PushUserInfo aPushList : pushList) {
////            String pushId = aPushList.getPushId();
////            if (!pushIdSet.contains(pushId)) {
////                pushIdSet.add(pushId);
////                threadPool.execute(
////                        //new Runnable()
////                        () -> {
////                            try {
////                                pushWithRegistrationID(alert, title, ex, pushId);
////                            } catch (Exception e) {
////                                logger.info("e:", e);
////                            }
////                        }
////                );
////                Thread.sleep(100);
////            }
////        }
////    }
////    public void lambda(){
////        pushList.forEach(aPushList->{
////            String pushId = aPushList.getPushId();
////            if (!pushIdSet.contains(pushId)) {
////                pushIdSet.add(pushId);
////                threadPool.execute(
////                        () -> {
////                            try {
////                                pushWithRegistrationID(alert, title, ex, pushId);
////                            } catch (Exception e) {
////                                logger.info("e:", e);
////                            }
////                        }
////                );
////                try {
////                    Thread.sleep(100);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
////        });
////    }
//}
