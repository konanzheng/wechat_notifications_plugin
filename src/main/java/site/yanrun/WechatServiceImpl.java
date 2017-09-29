package site.yanrun;

import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import weixin.popular.api.MessageAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageResult;

/**
 * Created by Marvin on 16/10/8.
 */
public class WechatServiceImpl implements WechatService {
    
    private final String appid;
    private final String secret;
    private final String openIds;
    
    private final String templateId;

    private Logger logger = LoggerFactory.getLogger(WechatService.class);

    private String jenkinsURL;

    private boolean onSuccess;

    private boolean onFailed;

    private TaskListener listener;

    private AbstractBuild build;

    private static final String apiUrl = "https://oapi.dingtalk.com/robot/send?access_token=";


    public WechatServiceImpl(String jenkinsURL, String appid,String secret,String templateId,String openids, boolean onStart, boolean onSuccess, boolean onFailed, TaskListener listener, AbstractBuild build) {
        this.jenkinsURL = jenkinsURL;
        this.appid = appid;
        this.secret = secret;
        this.templateId = templateId;
        this.openIds = openids;
        this.onSuccess = onSuccess;
        this.onFailed = onFailed;
        this.listener = listener;
        this.build = build;
    }

    @Override
    public void start() {
        

    }

    private String getBuildUrl() {
        if (jenkinsURL.endsWith("/")) {
            return jenkinsURL + build.getUrl();
        } else {
            return jenkinsURL + "/" + build.getUrl();
        }
    }

    @Override
    public void success() {
        String pic = "http://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/sign-check-icon.png";
        String title = String.format("%s%s构建成功", build.getProject().getDisplayName(), build.getDisplayName());

        String link = getBuildUrl();
        LinkedHashMap data =   getData();
        Map first = new HashMap();
        first.put("value", title);
        data.put("first", first);
        
        Map result = new HashMap();
        result.put("value", "成功");
        result.put("color", "#44b549");
        data.put("result", result);
        if (onSuccess) {
            logger.info("send link msg from " + listener.toString());
            sendLinkMessage(link, data);
        }
    }

    @Override
    public void failed() {
        String pic = "http://www.iconsdb.com/icons/preview/soylent-red/x-mark-3-xxl.png";
        String title = String.format("%s%s构建失败", build.getProject().getDisplayName(), build.getDisplayName());
        String link = getBuildUrl();
        LinkedHashMap data =   getData();
        Map first = new HashMap();
        first.put("value", title);
        data.put("first", first);
        
        Map result = new HashMap();
        result.put("value", "失败");
        result.put("color", "#ff0000");
        data.put("result", result);
        
        if (onFailed) {
            logger.info("send link msg from " + listener.toString());
            sendLinkMessage(link, data);
        }
    }

    private LinkedHashMap  getData() {
        LinkedHashMap data = new LinkedHashMap();
        
        Map project = new HashMap();
        project.put("value", build.getProject().getDisplayName());
        data.put("project", project);
        
        Map buildNo = new HashMap();
        buildNo.put("value", build.getDisplayName());
        data.put("build", buildNo);
        
        
        Map summary = new HashMap();
        summary.put("value", build.getBuildStatusSummary().message);
        data.put("summary", summary);
        
        Map duration = new HashMap();
        duration.put("value", build.getDurationString());
        data.put("duration", duration);
        
        Map remark = new HashMap();
        remark.put("value", "贾维斯为您服务。");
        data.put("remark", remark);
        
        
        return data;

    }

    private void sendLinkMessage(String link, LinkedHashMap data) {
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setUrl(link);
        
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(data);
        if(openIds!=null){
            String access_token =  TokenAPI.token(appid, secret).getAccess_token();
            String[] split = openIds.split(",");
            for(String openId:split){
                templateMessage.setTouser(openId);
                TemplateMessageResult result = MessageAPI.messageTemplateSend(access_token, templateMessage);
                logger.info("MsgId:"+result.getMsgid()+",ErrorCode:"+result.getErrcode()+",ErrMsg:"+result.getErrmsg());
            }
        }
    }


    
}
