package site.yanrun;


import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

public class WechatNotifier extends Notifier {

    private final String appid;
    private final String secret;
    
    private final String templateId;
    private final String openIds;

    private final boolean onStart;

    private final boolean onSuccess;

    private final boolean onFailed;

    public String getJenkinsURL() {
        return jenkinsURL;
    }

    private String jenkinsURL;

    public boolean isOnStart() {
        return onStart;
    }

    public boolean isOnSuccess() {
        return onSuccess;
    }

    public boolean isOnFailed() {
        return onFailed;
    }

 

    @DataBoundConstructor
    public WechatNotifier(String appid,String secret,String templateId,String openids, boolean onStart, boolean onSuccess, boolean onFailed, String jenkinsURL) {
        super();
        this.appid = appid;
        this.secret = secret;
        this.templateId = templateId;
        this.openIds = openids;
        this.onStart = onStart;
        this.onSuccess = onSuccess;
        this.onFailed = onFailed;
        this.jenkinsURL = jenkinsURL;
    }

    public WechatService newWechatService(AbstractBuild build, TaskListener listener) {
        return new WechatServiceImpl(jenkinsURL, appid,secret,templateId,openIds, onStart, onSuccess, onFailed, listener, build);
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        return true;
    }


    @Override
    public WechatNotifierDescriptor getDescriptor() {
        return (WechatNotifierDescriptor) super.getDescriptor();
    }

    @Extension
    public static class WechatNotifierDescriptor extends BuildStepDescriptor<Publisher> {


        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "微信通知器配置";
        }

        public String getDefaultURL() {
            Jenkins instance = Jenkins.getInstance();
            assert instance != null;
            if(instance.getRootUrl() != null){
                return instance.getRootUrl();
            }else{
                return "";
            }
        }

    }
}
