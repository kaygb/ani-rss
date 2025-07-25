package ani.rss.notification;

import ani.rss.entity.Ani;
import ani.rss.entity.WebHookHeader;
import ani.rss.entity.NotificationConfig;
import ani.rss.enums.NotificationStatusEnum;
import ani.rss.util.HttpReq;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * WebHook
 */
@Slf4j
public class WebHookNotification implements BaseNotification {
    @Override
    public Boolean send(NotificationConfig notificationConfig, Ani ani, String text, NotificationStatusEnum notificationStatusEnum) {
        String webHookMethod = notificationConfig.getWebHookMethod();
        String webHookUrl = notificationConfig.getWebHookUrl();
        String webHookBody = notificationConfig.getWebHookBody();
        List<WebHookHeader> headers = notificationConfig.getWebHookHeaders();

        webHookUrl = replaceNotificationTemplate(ani, webHookUrl, text, notificationStatusEnum);
        webHookBody = replaceNotificationTemplate(ani, webHookBody, text, notificationStatusEnum);

        if (StrUtil.isBlank(webHookUrl)) {
            log.warn("webhook url is blank");
            return false;
        }

        String notificationTemplate = replaceNotificationTemplate(ani, notificationConfig, text, notificationStatusEnum);
        notificationTemplate = notificationTemplate.replace("\n", "\\n");

        webHookUrl = webHookUrl.replace("${message}", notificationTemplate);
        webHookUrl = webHookUrl.replace("${notification}", notificationTemplate);

        webHookBody = webHookBody.replace("${message}", notificationTemplate);
        webHookBody = webHookBody.replace("${notification}", notificationTemplate);

        String image = "https://docs.wushuo.top/null.png";

        image = Opt.ofNullable(ani)
                .map(Ani::getImage)
                .filter(StrUtil::isNotBlank)
                .orElse(image);

        webHookUrl = webHookUrl.replace("${image}", image);
        webHookBody = webHookBody.replace("${image}", image);

        HttpRequest httpRequest = HttpReq.get(webHookUrl, true)
                .method(Method.valueOf(webHookMethod));

        if (StrUtil.isNotBlank(webHookBody)) {
            httpRequest.body(webHookBody);
        }
        if (CollectionUtil.isNotEmpty(headers)) {
            for (WebHookHeader header : headers) {
                httpRequest.header(header.getKey(), header.getValue());
                System.out.println("header: " + header.getKey() + " " + header.getValue());
            }
        }
        return httpRequest.thenFunction(HttpResponse::isOk);
    }
}
