# 微信通知插件 

jenkins微信通知插件，通过微信测试号发送jenkins构建结果通知

## 插件使用说明

需要配置 appid 、 appSecret 、 模板消息的 id,和 接收人openid


多个openid用英文逗号分割即可
模板目前是固定的，可以根据自己需求修改

```
        {{first.DATA}}
        项目名称：{{project.DATA}}
        构建编号：{{build.DATA}}
        构建结果：{{result.DATA}}
        概要情况：{{summary.DATA}}
        持续时间: {{duration.DATA}}
        {{remark.DATA}}

```

[微信测试号地址](https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index)

## 源码构建

maven构建

```mvn package --settings=./settings.xml```


源代码是拿钉钉jenkins插件改的。[dingding-notifications插件github地址](https://github.com/jenkinsci/dingding-notifications-plugin)


 

 