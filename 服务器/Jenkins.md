

https://www.jenkins.io/



注意jdk版本适配

![image-20260101091109893](assets/image-20260101091109893.png)







https://jishuzhan.net/article/1955855444752248834



### Jenkins Pipeline 实战 ------ 编写自动化脚本

Pipeline 是 Jenkins 中最强大的功能，通过代码（Jenkinsfile）定义整个构建部署流程，支持版本控制和复用。我们将创建一个包含 "拉取代码→编译→测试→打包→部署" 的完整 Pipeline。

#### 5.1 创建 Pipeline 项目

1. 登录 Jenkins，点击左侧 "新建 Item"

2. 输入项目名称（如`jenkins-demo-pipeline`），选择 "Pipeline"，点击 "确定"

   ![image-20260101120034958](assets/image-20260101120034958.png)

3. 在项目配置页面，拉到 "Pipeline" 配置区域

#### 5.2 编写基础 Pipeline 脚本

在 "Pipeline" 配置中选择 "Definition" 为 "Pipeline script"，输入以下脚本：

![image-20260101120355869](assets/image-20260101120355869.png)

<details data-line="647" class="md-editor-code" open="" style="color: rgb(169, 183, 198); font-size: 12px; line-height: 1; margin: 20px 0px; font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI Variable&quot;, &quot;Segoe UI&quot;, system-ui, ui-sans-serif, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;"><summary class="md-editor-code-head" style="background-color: rgb(40, 44, 52); border-radius: 5px 5px 0px 0px; display: grid; font-size: 12px; grid-template: &quot;1rf 1rf&quot;; height: 32px; justify-content: space-between; margin-bottom: 0px; width: 683.458px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); list-style: none; cursor: pointer;"><div class="md-editor-code-flag" style="margin-left: 12px;"><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; background-color: rgb(236, 106, 94);"></span><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; margin-left: 4px; background-color: rgb(244, 191, 79);"></span><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; margin-left: 4px; background-color: rgb(97, 197, 84);"></span></div><div class="md-editor-code-action" style="align-items: center; display: flex;"><span class="md-editor-code-lang" style="margin-right: 10px; line-height: 32px;"></span><span class="md-editor-copy-button" data-tips="复制代码" style="margin-right: 10px; cursor: pointer; line-height: 32px; position: static;">复制代码</span><span class="md-editor-collapse-tips" style="margin-right: 12px;"><svg class="md-editor-icon" aria-hidden="true"><use xlink:href="#md-editor-icon-collapse-tips"></use></svg></span></div></summary><pre style="margin: 0px; position: relative;"><code class="language-" language="" style="background-color: rgb(40, 44, 52); border-radius: 0px 0px 5px 5px; color: rgb(169, 183, 198); line-height: 1.6; padding: 1em; display: block; font-family: source-code-pro, Menlo, Monaco, Consolas, &quot;Courier New&quot;, monospace; font-size: 14px; overflow: auto; position: relative; box-shadow: rgba(0, 0, 0, 0.333) 0px 2px 2px;"><span class="md-editor-code-block" style="color: rgb(169, 183, 198); display: inline-block; overflow: auto; vertical-align: bottom; width: 620.458px;">pipeline {
    <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 指定在哪个节点执行（默认在Jenkins所在节点）</span>
    agent any
    
    <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 环境变量定义</span>
    environment {
        <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 项目名称</span>
        PROJECT_NAME = <span class="hljs-string" style="color: rgb(152, 195, 121);">'jenkins-demo'</span>
        <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 项目版本（与pom.xml一致）</span>
        PROJECT_VERSION = <span class="hljs-string" style="color: rgb(152, 195, 121);">'1.0.0'</span>
        <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 打包后的文件名</span>
        JAR_FILE = <span class="hljs-string" style="color: rgb(152, 195, 121);">"${PROJECT_NAME}-${PROJECT_VERSION}.jar"</span>
        <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 部署目录</span>
        DEPLOY_DIR = <span class="hljs-string" style="color: rgb(152, 195, 121);">'/opt/deploy'</span>
        <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 服务端口（与application.yml一致）</span>
        SERVICE_PORT = <span class="hljs-number" style="color: rgb(209, 154, 102);">8081</span>
    }
    
    <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 工具配置（关联全局工具配置中的名称）</span>
    tools {
        maven <span class="hljs-string" style="color: rgb(152, 195, 121);">'Maven3.8'</span>
        jdk <span class="hljs-string" style="color: rgb(152, 195, 121);">'JDK17'</span>
        git <span class="hljs-string" style="color: rgb(152, 195, 121);">'Git'</span>
    }
    
    <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 构建阶段定义</span>
    stages {
        <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 1. 拉取代码</span>
        <span class="hljs-title function_ invoke__" style="color: rgb(97, 174, 238);">stage</span>(<span class="hljs-string" style="color: rgb(152, 195, 121);">'拉取代码'</span>) {
            steps {
                <span class="hljs-keyword" style="color: rgb(198, 120, 221);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"开始拉取代码..."</span>
                <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 替换为你的Git仓库地址</span>
                git url: <span class="hljs-string" style="color: rgb(152, 195, 121);">'https://gitee.com/your-username/jenkins-demo.git'</span>,
                    branch: <span class="hljs-string" style="color: rgb(152, 195, 121);">'master'</span>
            }
        }
        
        <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 2. 编译代码</span>
        <span class="hljs-title function_ invoke__" style="color: rgb(97, 174, 238);">stage</span>(<span class="hljs-string" style="color: rgb(152, 195, 121);">'编译代码'</span>) {
            steps {
                <span class="hljs-keyword" style="color: rgb(198, 120, 221);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"开始编译代码..."</span>
                sh <span class="hljs-string" style="color: rgb(152, 195, 121);">'mvn clean compile'</span>
            }
        }
        
        <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 3. 运行单元测试</span>
        <span class="hljs-title function_ invoke__" style="color: rgb(97, 174, 238);">stage</span>(<span class="hljs-string" style="color: rgb(152, 195, 121);">'运行单元测试'</span>) {
            steps {
                <span class="hljs-keyword" style="color: rgb(198, 120, 221);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"开始运行单元测试..."</span>
                sh <span class="hljs-string" style="color: rgb(152, 195, 121);">'mvn test'</span>
            }
            post {
                <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 测试失败后保存测试报告</span>
                always {
                    junit <span class="hljs-string" style="color: rgb(152, 195, 121);">'**/target/surefire-reports/*.xml'</span>
                }
            }
        }
        
        <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 4. 打包构建</span>
        <span class="hljs-title function_ invoke__" style="color: rgb(97, 174, 238);">stage</span>(<span class="hljs-string" style="color: rgb(152, 195, 121);">'打包构建'</span>) {
            steps {
                <span class="hljs-keyword" style="color: rgb(198, 120, 221);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"开始打包构建..."</span>
                sh <span class="hljs-string" style="color: rgb(152, 195, 121);">'mvn package -DskipTests'</span>
            }
        }
        
        <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 5. 部署应用</span>
        <span class="hljs-title function_ invoke__" style="color: rgb(97, 174, 238);">stage</span>(<span class="hljs-string" style="color: rgb(152, 195, 121);">'部署应用'</span>) {
            steps {
                <span class="hljs-keyword" style="color: rgb(198, 120, 221);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"开始部署应用..."</span>
                
                <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 创建部署目录</span>
                sh <span class="hljs-string" style="color: rgb(152, 195, 121);">"mkdir -p ${DEPLOY_DIR}"</span>
                
                <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 停止旧服务（如果存在）</span>
                sh <span class="hljs-string" style="color: rgb(152, 195, 121);">""</span><span class="hljs-string" style="color: rgb(152, 195, 121);">"
                    if [ -f "</span>${DEPLOY_DIR}/${PROJECT_NAME}.pid<span class="hljs-string" style="color: rgb(152, 195, 121);">" ]; then
                        PID=\$(cat ${DEPLOY_DIR}/${PROJECT_NAME}.pid)
                        echo "</span>停止旧服务，PID: \<span class="hljs-variable" style="color: rgb(209, 154, 102);">$PID</span><span class="hljs-string" style="color: rgb(152, 195, 121);">"
                        kill -9 \$PID || true
                    fi
                "</span><span class="hljs-string" style="color: rgb(152, 195, 121);">""</span>
                
                <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 复制新包到部署目录</span>
                sh <span class="hljs-string" style="color: rgb(152, 195, 121);">"cp target/${JAR_FILE} ${DEPLOY_DIR}/"</span>
                
                <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 启动新服务，记录PID</span>
                sh <span class="hljs-string" style="color: rgb(152, 195, 121);">""</span><span class="hljs-string" style="color: rgb(152, 195, 121);">"
                    cd ${DEPLOY_DIR}
                    nohup java -jar ${JAR_FILE} &gt; ${PROJECT_NAME}.log 2&gt;&amp;1 &amp;
                    echo \$! &gt; ${PROJECT_NAME}.pid
                    echo "</span>新服务启动成功，PID: \$(cat ${PROJECT_NAME}.pid)<span class="hljs-string" style="color: rgb(152, 195, 121);">"
                "</span><span class="hljs-string" style="color: rgb(152, 195, 121);">""</span>
            }
        }
        
        <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 6. 验证部署</span>
        <span class="hljs-title function_ invoke__" style="color: rgb(97, 174, 238);">stage</span>(<span class="hljs-string" style="color: rgb(152, 195, 121);">'验证部署'</span>) {
            steps {
                <span class="hljs-keyword" style="color: rgb(198, 120, 221);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"验证服务是否正常启动..."</span>
                <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 循环检查健康接口，最多等待30秒</span>
                sh <span class="hljs-string" style="color: rgb(152, 195, 121);">""</span><span class="hljs-string" style="color: rgb(152, 195, 121);">"
                    count=0
                    while true; do
                        if curl -s "</span>http:<span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">//localhost:${SERVICE_PORT}/api/users/health" | grep -q "Service is running"; then</span>
                            <span class="hljs-keyword" style="color: rgb(198, 120, 221);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"服务验证成功"</span>
                            <span class="hljs-keyword" style="color: rgb(198, 120, 221);">exit</span> <span class="hljs-number" style="color: rgb(209, 154, 102);">0</span>
                        fi
                        count=\$((count + <span class="hljs-number" style="color: rgb(209, 154, 102);">1</span>))
                        <span class="hljs-keyword" style="color: rgb(198, 120, 221);">if</span> [ \<span class="hljs-variable" style="color: rgb(209, 154, 102);">$count</span> -ge <span class="hljs-number" style="color: rgb(209, 154, 102);">6</span> ]; then
                            <span class="hljs-keyword" style="color: rgb(198, 120, 221);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"服务启动超时"</span>
                            <span class="hljs-keyword" style="color: rgb(198, 120, 221);">exit</span> <span class="hljs-number" style="color: rgb(209, 154, 102);">1</span>
                        fi
                        sleep <span class="hljs-number" style="color: rgb(209, 154, 102);">5</span>
                    done
                <span class="hljs-string" style="color: rgb(152, 195, 121);">""</span><span class="hljs-string" style="color: rgb(152, 195, 121);">"
            }
        }
    }
    
    // 构建结果通知
    post {
        success {
            echo "</span>构建部署成功！<span class="hljs-string" style="color: rgb(152, 195, 121);">"
        }
        failure {
            echo "</span>构建部署失败！<span class="hljs-string" style="color: rgb(152, 195, 121);">"
        }
        always {
            echo "</span>构建流程结束<span class="hljs-string" style="color: rgb(152, 195, 121);">"
        }
    }
}
</span></span></code></pre></details>

#### 5.3 脚本关键步骤解析

1. **agent any**：表示在任何可用的 Jenkins 节点上执行（后续可扩展为多节点）
2. **environment**：定义环境变量，避免硬编码，方便维护
3. **tools**：指定构建工具，关联全局配置中的 JDK、Maven、Git
4. **stages** ：核心构建阶段，每个 stage 完成一个独立任务：
   - **拉取代码**：从 Git 仓库克隆代码，支持指定分支
   - **编译代码** ：`mvn clean compile`清理并编译源码
   - **运行单元测试** ：`mvn test`执行测试，`junit`插件收集测试报告
   - **打包构建** ：`mvn package`生成 jar 包，`-DskipTests`跳过测试（已单独执行）
   - **部署应用** ：
     - 停止旧服务（通过 PID 文件）
     - 复制新 jar 包到部署目录
     - 启动新服务，将进程 ID 写入 PID 文件
   - **验证部署**：通过健康检查接口确认服务启动成功
5. **post**：构建结束后执行的操作，支持成功 / 失败 / 总是执行的逻辑

#### 5.4 运行 Pipeline 并查看结果

1. 点击项目页面的 "Build Now" 触发构建
2. 点击左侧 "Build History" 中的构建编号（如 #1）进入构建详情
3. 点击 "Console Output" 查看实时构建日志
4. 构建成功后，访问`http://服务器IP:8081/api/users/health`，显示 "Service is running" 即为部署成功

### 六、自动化进阶 ------ 让部署更智能

基础 Pipeline 实现了自动部署，但在实际生产中还需要更多功能，如代码提交触发构建、远程部署、代码质量检查、邮件通知等。

#### 6.1 配置代码提交自动触发构建

无需手动点击 "Build Now"，代码提交到 Git 后自动触发构建：

1. 安装 "Generic Webhook Trigger" 插件
2. 进入项目配置，在 "构建触发器" 中勾选 "Generic Webhook Trigger"
3. 设置 "Token"（如`jenkins-demo-trigger`）
4. 在 Git 仓库中配置 WebHook：
   - Gitee：仓库 → 管理 → WebHooks → 添加
   - Payload URL：`http://JenkinsIP:8080/generic-webhook-trigger/invoke?token=jenkins-demo-trigger`
   - 触发事件：选择 "推送事件"
5. 保存配置，后续提交代码到 master 分支会自动触发构建

#### 6.2 远程服务器部署（通过 SSH）

实际场景中，Jenkins 和应用服务器通常分离，需要通过 SSH 部署：

1. 进入 "系统管理" → "系统" → "Publish over SSH"
2. 配置 SSH 服务器：
   - Name：`app-server`（自定义名称）
   - Hostname：应用服务器 IP
   - Username：登录用户名（如 root）
   - 选择认证方式（密码或密钥）
   - 点击 "Test Configuration" 验证连接
3. 修改 Pipeline 的 "部署应用" 阶段：

groovy

<details data-line="838" class="md-editor-code" open="" style="color: rgb(169, 183, 198); font-size: 12px; line-height: 1; margin: 20px 0px; font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI Variable&quot;, &quot;Segoe UI&quot;, system-ui, ui-sans-serif, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;"><summary class="md-editor-code-head" style="background-color: rgb(40, 44, 52); border-radius: 5px 5px 0px 0px; display: grid; font-size: 12px; grid-template: &quot;1rf 1rf&quot;; height: 32px; justify-content: space-between; margin-bottom: 0px; width: 683.458px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); list-style: none; cursor: pointer;"><div class="md-editor-code-flag" style="margin-left: 12px;"><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; background-color: rgb(236, 106, 94);"></span><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; margin-left: 4px; background-color: rgb(244, 191, 79);"></span><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; margin-left: 4px; background-color: rgb(97, 197, 84);"></span></div><div class="md-editor-code-action" style="align-items: center; display: flex;"><span class="md-editor-code-lang" style="margin-right: 10px; line-height: 32px;"></span><span class="md-editor-copy-button" data-tips="复制代码" style="margin-right: 10px; cursor: pointer; line-height: 32px; position: static;">复制代码</span><span class="md-editor-collapse-tips" style="margin-right: 12px;"><svg class="md-editor-icon" aria-hidden="true"><use xlink:href="#md-editor-icon-collapse-tips"></use></svg></span></div></summary><pre style="margin: 0px; position: relative;"><code class="language-" language="" style="background-color: rgb(40, 44, 52); border-radius: 0px 0px 5px 5px; color: rgb(169, 183, 198); line-height: 1.6; padding: 1em; display: block; font-family: source-code-pro, Menlo, Monaco, Consolas, &quot;Courier New&quot;, monospace; font-size: 14px; overflow: auto; position: relative; box-shadow: rgba(0, 0, 0, 0.333) 0px 2px 2px;"><span class="md-editor-code-block" style="color: rgb(169, 183, 198); display: inline-block; overflow: auto; vertical-align: bottom; width: 620.458px;"><span class="hljs-title function_ invoke__" style="color: rgb(97, 174, 238);">stage</span>(<span class="hljs-string" style="color: rgb(152, 195, 121);">'部署应用'</span>) {
    steps {
        <span class="hljs-keyword" style="color: rgb(198, 120, 221);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"开始部署到远程服务器..."</span>
        
        <span class="hljs-comment" style="color: rgb(92, 99, 112); font-style: italic;">// 通过SSH执行命令</span>
        <span class="hljs-title function_ invoke__" style="color: rgb(97, 174, 238);">sshPublisher</span>(<span class="hljs-attr" style="color: rgb(209, 154, 102);">publishers</span>: [<span class="hljs-title function_ invoke__" style="color: rgb(97, 174, 238);">sshPublisherDesc</span>(
            <span class="hljs-attr" style="color: rgb(209, 154, 102);">configName</span>: <span class="hljs-string" style="color: rgb(152, 195, 121);">'app-server'</span>, // 对应配置的SSH服务器名称
            <span class="hljs-attr" style="color: rgb(209, 154, 102);">transfers</span>: [<span class="hljs-title function_ invoke__" style="color: rgb(97, 174, 238);">sshTransfer</span>(
                <span class="hljs-attr" style="color: rgb(209, 154, 102);">cleanRemote</span>: <span class="hljs-literal" style="color: rgb(86, 182, 194);">false</span>,
                <span class="hljs-attr" style="color: rgb(209, 154, 102);">excludes</span>: <span class="hljs-string" style="color: rgb(152, 195, 121);">''</span>,
                <span class="hljs-attr" style="color: rgb(209, 154, 102);">execCommand</span>: <span class="hljs-string" style="color: rgb(152, 195, 121);">""</span><span class="hljs-string" style="color: rgb(152, 195, 121);">"
                    # 停止旧服务
                    if [ -f "</span>/opt/deploy/${PROJECT_NAME}.pid<span class="hljs-string" style="color: rgb(152, 195, 121);">" ]; then
                        PID=\$(cat /opt/deploy/${PROJECT_NAME}.pid)
                        kill -9 \$PID || true
                    fi
                    
                    # 启动新服务
                    cd /opt/deploy
                    nohup java -jar ${JAR_FILE} &gt; ${PROJECT_NAME}.log 2&gt;&amp;1 &amp;
                    echo \$! &gt; ${PROJECT_NAME}.pid
                "</span><span class="hljs-string" style="color: rgb(152, 195, 121);">""</span>,
                <span class="hljs-attr" style="color: rgb(209, 154, 102);">execTimeout</span>: <span class="hljs-number" style="color: rgb(209, 154, 102);">120000</span>,
                <span class="hljs-attr" style="color: rgb(209, 154, 102);">flatten</span>: <span class="hljs-literal" style="color: rgb(86, 182, 194);">false</span>,
                <span class="hljs-attr" style="color: rgb(209, 154, 102);">makeEmptyDirs</span>: <span class="hljs-literal" style="color: rgb(86, 182, 194);">false</span>,
                <span class="hljs-attr" style="color: rgb(209, 154, 102);">noDefaultExcludes</span>: <span class="hljs-literal" style="color: rgb(86, 182, 194);">false</span>,
                <span class="hljs-attr" style="color: rgb(209, 154, 102);">patternSeparator</span>: <span class="hljs-string" style="color: rgb(152, 195, 121);">'[, ]+'</span>,
                <span class="hljs-attr" style="color: rgb(209, 154, 102);">remoteDirectory</span>: <span class="hljs-string" style="color: rgb(152, 195, 121);">'/opt/deploy'</span>, // 远程服务器目录
                <span class="hljs-attr" style="color: rgb(209, 154, 102);">remoteDirectorySDF</span>: <span class="hljs-literal" style="color: rgb(86, 182, 194);">false</span>,
                <span class="hljs-attr" style="color: rgb(209, 154, 102);">removePrefix</span>: <span class="hljs-string" style="color: rgb(152, 195, 121);">'target'</span>, // 移除本地的target前缀
                <span class="hljs-attr" style="color: rgb(209, 154, 102);">sourceFiles</span>: <span class="hljs-string" style="color: rgb(152, 195, 121);">"target/${JAR_FILE}"</span> // 本地文件路径
            )],
            <span class="hljs-attr" style="color: rgb(209, 154, 102);">usePromotionTimestamp</span>: <span class="hljs-literal" style="color: rgb(86, 182, 194);">false</span>,
            <span class="hljs-attr" style="color: rgb(209, 154, 102);">useWorkspaceInPromotion</span>: <span class="hljs-literal" style="color: rgb(86, 182, 194);">false</span>,
            <span class="hljs-attr" style="color: rgb(209, 154, 102);">verbose</span>: <span class="hljs-literal" style="color: rgb(86, 182, 194);">true</span>
        )])
    }
}</span></code></pre></details>

#### 6.3 集成 SonarQube 代码质量检查

SonarQube 用于检测代码中的漏洞、异味和重复代码，在 Pipeline 中添加质量门禁：

1. 安装 SonarQube 服务器（参考官方文档）
2. Jenkins 中安装 "SonarQube Scanner" 插件
3. 进入 "系统管理" → "系统" → "SonarQube servers" 配置：
   - Name：`SonarQube`
   - Server URL：SonarQube 访问地址（如`http://sonarIP:9000`）
   - Server authentication token：在 SonarQube 中生成的令牌
4. 在 Pipeline 中添加代码质量检查阶段：

groovy

<details data-line="891" class="md-editor-code" open="" style="color: rgb(169, 183, 198); font-size: 12px; line-height: 1; margin: 20px 0px; font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI Variable&quot;, &quot;Segoe UI&quot;, system-ui, ui-sans-serif, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;"><summary class="md-editor-code-head" style="background-color: rgb(40, 44, 52); border-radius: 5px 5px 0px 0px; display: grid; font-size: 12px; grid-template: &quot;1rf 1rf&quot;; height: 32px; justify-content: space-between; margin-bottom: 0px; width: 683.458px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); list-style: none; cursor: pointer;"><div class="md-editor-code-flag" style="margin-left: 12px;"><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; background-color: rgb(236, 106, 94);"></span><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; margin-left: 4px; background-color: rgb(244, 191, 79);"></span><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; margin-left: 4px; background-color: rgb(97, 197, 84);"></span></div><div class="md-editor-code-action" style="align-items: center; display: flex;"><span class="md-editor-code-lang" style="margin-right: 10px; line-height: 32px;"></span><span class="md-editor-copy-button" data-tips="复制代码" style="margin-right: 10px; cursor: pointer; line-height: 32px; position: static;">复制代码</span><span class="md-editor-collapse-tips" style="margin-right: 12px;"><svg class="md-editor-icon" aria-hidden="true"><use xlink:href="#md-editor-icon-collapse-tips"></use></svg></span></div></summary><pre style="margin: 0px; position: relative;"><code class="language-" language="" style="background-color: rgb(40, 44, 52); border-radius: 0px 0px 5px 5px; color: rgb(169, 183, 198); line-height: 1.6; padding: 1em; display: block; font-family: source-code-pro, Menlo, Monaco, Consolas, &quot;Courier New&quot;, monospace; font-size: 14px; overflow: auto; position: relative; box-shadow: rgba(0, 0, 0, 0.333) 0px 2px 2px;"><span class="md-editor-code-block" style="color: rgb(169, 183, 198); display: inline-block; overflow: auto; vertical-align: bottom; width: 620.458px;">stage(<span class="hljs-string" style="color: rgb(152, 195, 121);">'代码质量检查'</span>) {
    steps {
        echo <span class="hljs-string" style="color: rgb(152, 195, 121);">"开始代码质量检查..."</span>
        withSonarQubeEnv(<span class="hljs-string" style="color: rgb(152, 195, 121);">'SonarQube'</span>) {
            sh <span class="hljs-string" style="color: rgb(152, 195, 121);">"""
                mvn sonar:sonar \
                -Dsonar.projectKey=${PROJECT_NAME} \
                -Dsonar.projectName=${PROJECT_NAME} \
                -Dsonar.projectVersion=${PROJECT_VERSION} \
                -Dsonar.sources=src/main/java \
                -Dsonar.java.binaries=target/classes
            """</span>
        }
    }
}

// 添加质量门禁检查（需安装Sonar Quality Gates插件）
stage(<span class="hljs-string" style="color: rgb(152, 195, 121);">'质量门禁检查'</span>) {
    steps {
        script {
            <span class="hljs-keyword" style="color: rgb(198, 120, 221);">def</span> <span class="hljs-title function_" style="color: rgb(97, 174, 238);">qualityGate</span> = waitForQualityGate()
            <span class="hljs-keyword" style="color: rgb(198, 120, 221);">if</span> (qualityGate.status != <span class="hljs-string" style="color: rgb(152, 195, 121);">'OK'</span>) {
                error <span class="hljs-string" style="color: rgb(152, 195, 121);">"代码质量检查未通过: ${qualityGate.status}"</span>
            }
        }
    }
}</span></code></pre></details>

#### 6.4 邮件通知构建结果

构建完成后自动发送邮件通知相关人员：

1. 安装 "Email Extension" 插件
2. 进入 "系统管理" → "系统" → "Extended E-mail Notification" 配置：
   - SMTP 服务器：如`smtp.163.com`
   - SMTP 端口：25（或 465 用于 SSL）
   - 用户名：发件人邮箱
   - 密码：邮箱授权码
   - 默认收件人：`developer@example.com`
3. 在 Pipeline 的 post 部分添加邮件通知：

groovy

<details data-line="934" class="md-editor-code" open="" style="color: rgb(169, 183, 198); font-size: 12px; line-height: 1; margin: 20px 0px; font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI Variable&quot;, &quot;Segoe UI&quot;, system-ui, ui-sans-serif, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;"><summary class="md-editor-code-head" style="background-color: rgb(40, 44, 52); border-radius: 5px 5px 0px 0px; display: grid; font-size: 12px; grid-template: &quot;1rf 1rf&quot;; height: 32px; justify-content: space-between; margin-bottom: 0px; width: 683.458px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); list-style: none; cursor: pointer;"><div class="md-editor-code-flag" style="margin-left: 12px;"><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; background-color: rgb(236, 106, 94);"></span><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; margin-left: 4px; background-color: rgb(244, 191, 79);"></span><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; margin-left: 4px; background-color: rgb(97, 197, 84);"></span></div><div class="md-editor-code-action" style="align-items: center; display: flex;"><span class="md-editor-code-lang" style="margin-right: 10px; line-height: 32px;"></span><span class="md-editor-copy-button" data-tips="复制代码" style="margin-right: 10px; cursor: pointer; line-height: 32px; position: static;">复制代码</span><span class="md-editor-collapse-tips" style="margin-right: 12px;"><svg class="md-editor-icon" aria-hidden="true"><use xlink:href="#md-editor-icon-collapse-tips"></use></svg></span></div></summary><pre style="margin: 0px; position: relative;"><code class="language-" language="" style="background-color: rgb(40, 44, 52); border-radius: 0px 0px 5px 5px; color: rgb(169, 183, 198); line-height: 1.6; padding: 1em; display: block; font-family: source-code-pro, Menlo, Monaco, Consolas, &quot;Courier New&quot;, monospace; font-size: 14px; overflow: auto; position: relative; box-shadow: rgba(0, 0, 0, 0.333) 0px 2px 2px;"><span class="md-editor-code-block" style="color: rgb(169, 183, 198); display: inline-block; overflow: auto; vertical-align: bottom; width: 620.458px;">post {
    success {
        <span class="hljs-built_in" style="color: rgb(230, 192, 123);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"构建部署成功！"</span>
        emailext(
            subject: <span class="hljs-string" style="color: rgb(152, 195, 121);">"构建成功: <span class="hljs-variable" style="color: rgb(209, 154, 102);">${PROJECT_NAME}</span> #<span class="hljs-variable" style="color: rgb(209, 154, 102);">${BUILD_NUMBER}</span>"</span>,
            body: <span class="hljs-string" style="color: rgb(152, 195, 121);">""</span><span class="hljs-string" style="color: rgb(152, 195, 121);">"
                &lt;h3&gt;构建信息&lt;/h3&gt;
                &lt;p&gt;项目名称: <span class="hljs-variable" style="color: rgb(209, 154, 102);">${PROJECT_NAME}</span>&lt;/p&gt;
                &lt;p&gt;构建编号: <span class="hljs-variable" style="color: rgb(209, 154, 102);">${BUILD_NUMBER}</span>&lt;/p&gt;
                &lt;p&gt;构建结果: 成功&lt;/p&gt;
                &lt;p&gt;查看详情: &lt;a href="</span><span class="hljs-variable" style="color: rgb(209, 154, 102);">${BUILD_URL}</span><span class="hljs-string" style="color: rgb(152, 195, 121);">"&gt;<span class="hljs-variable" style="color: rgb(209, 154, 102);">${BUILD_URL}</span>&lt;/a&gt;&lt;/p&gt;
            "</span><span class="hljs-string" style="color: rgb(152, 195, 121);">""</span>,
            to: <span class="hljs-string" style="color: rgb(152, 195, 121);">'developer@example.com'</span>
        )
    }
    failure {
        <span class="hljs-built_in" style="color: rgb(230, 192, 123);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"构建部署失败！"</span>
        emailext(
            subject: <span class="hljs-string" style="color: rgb(152, 195, 121);">"构建失败: <span class="hljs-variable" style="color: rgb(209, 154, 102);">${PROJECT_NAME}</span> #<span class="hljs-variable" style="color: rgb(209, 154, 102);">${BUILD_NUMBER}</span>"</span>,
            body: <span class="hljs-string" style="color: rgb(152, 195, 121);">""</span><span class="hljs-string" style="color: rgb(152, 195, 121);">"
                &lt;h3&gt;构建信息&lt;/h3&gt;
                &lt;p&gt;项目名称: <span class="hljs-variable" style="color: rgb(209, 154, 102);">${PROJECT_NAME}</span>&lt;/p&gt;
                &lt;p&gt;构建编号: <span class="hljs-variable" style="color: rgb(209, 154, 102);">${BUILD_NUMBER}</span>&lt;/p&gt;
                &lt;p&gt;构建结果: 失败&lt;/p&gt;
                &lt;p&gt;查看详情: &lt;a href="</span><span class="hljs-variable" style="color: rgb(209, 154, 102);">${BUILD_URL}</span><span class="hljs-string" style="color: rgb(152, 195, 121);">"&gt;<span class="hljs-variable" style="color: rgb(209, 154, 102);">${BUILD_URL}</span>&lt;/a&gt;&lt;/p&gt;
            "</span><span class="hljs-string" style="color: rgb(152, 195, 121);">""</span>,
            to: <span class="hljs-string" style="color: rgb(152, 195, 121);">'developer@example.com'</span>
        )
    }
}</span></code></pre></details>

#### 6.5 版本回滚机制

部署失败时能快速回滚到上一版本：

1. 在部署阶段保存历史版本：

groovy

<details data-line="973" class="md-editor-code" open="" style="color: rgb(169, 183, 198); font-size: 12px; line-height: 1; margin: 20px 0px; font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI Variable&quot;, &quot;Segoe UI&quot;, system-ui, ui-sans-serif, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;"><summary class="md-editor-code-head" style="background-color: rgb(40, 44, 52); border-radius: 5px 5px 0px 0px; display: grid; font-size: 12px; grid-template: &quot;1rf 1rf&quot;; height: 32px; justify-content: space-between; margin-bottom: 0px; width: 683.458px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); list-style: none; cursor: pointer;"><div class="md-editor-code-flag" style="margin-left: 12px;"><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; background-color: rgb(236, 106, 94);"></span><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; margin-left: 4px; background-color: rgb(244, 191, 79);"></span><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; margin-left: 4px; background-color: rgb(97, 197, 84);"></span></div><div class="md-editor-code-action" style="align-items: center; display: flex;"><span class="md-editor-code-lang" style="margin-right: 10px; line-height: 32px;"></span><span class="md-editor-copy-button" data-tips="复制代码" style="margin-right: 10px; cursor: pointer; line-height: 32px; position: static;">复制代码</span><span class="md-editor-collapse-tips" style="margin-right: 12px;"><svg class="md-editor-icon" aria-hidden="true"><use xlink:href="#md-editor-icon-collapse-tips"></use></svg></span></div></summary><pre style="margin: 0px; position: relative;"><code class="language-" language="" style="background-color: rgb(40, 44, 52); border-radius: 0px 0px 5px 5px; color: rgb(169, 183, 198); line-height: 1.6; padding: 1em; display: block; font-family: source-code-pro, Menlo, Monaco, Consolas, &quot;Courier New&quot;, monospace; font-size: 14px; overflow: auto; position: relative; box-shadow: rgba(0, 0, 0, 0.333) 0px 2px 2px;"><span class="md-editor-code-block" style="color: rgb(169, 183, 198); display: inline-block; overflow: auto; vertical-align: bottom; width: 620.458px;">stage(<span class="hljs-string" style="color: rgb(152, 195, 121);">'部署应用'</span>) {
    steps {
        <span class="hljs-built_in" style="color: rgb(230, 192, 123);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"开始部署应用..."</span>
        
        // 创建历史版本目录
        sh <span class="hljs-string" style="color: rgb(152, 195, 121);">"mkdir -p <span class="hljs-variable" style="color: rgb(209, 154, 102);">${DEPLOY_DIR}</span>/history"</span>
        
        // 备份当前版本（如果存在）
        sh <span class="hljs-string" style="color: rgb(152, 195, 121);">""</span><span class="hljs-string" style="color: rgb(152, 195, 121);">"
            if [ -f "</span><span class="hljs-variable" style="color: rgb(209, 154, 102);">${DEPLOY_DIR}</span>/<span class="hljs-variable" style="color: rgb(209, 154, 102);">${JAR_FILE}</span><span class="hljs-string" style="color: rgb(152, 195, 121);">" ]; then
                cp <span class="hljs-variable" style="color: rgb(209, 154, 102);">${DEPLOY_DIR}</span>/<span class="hljs-variable" style="color: rgb(209, 154, 102);">${JAR_FILE}</span> <span class="hljs-variable" style="color: rgb(209, 154, 102);">${DEPLOY_DIR}</span>/history/<span class="hljs-variable" style="color: rgb(209, 154, 102);">${JAR_FILE}</span>.bak.<span class="hljs-variable" style="color: rgb(209, 154, 102);">${BUILD_TIMESTAMP}</span>
                echo "</span>备份当前版本到历史目录<span class="hljs-string" style="color: rgb(152, 195, 121);">"
            fi
        "</span><span class="hljs-string" style="color: rgb(152, 195, 121);">""</span>
        
        // 后续部署步骤...
    }
}</span></code></pre></details>

1. 配置参数化构建：
   - 项目配置中勾选 "This project is parameterized"
   - 添加 "Choice Parameter"，名称`ACTION`，选项：`deploy`和`rollback`
   - 添加 "String Parameter"，名称`ROLLBACK_VERSION`，默认值为空
2. 修改 Pipeline 支持回滚逻辑：

groovy

<details data-line="1000" class="md-editor-code" open="" style="color: rgb(169, 183, 198); font-size: 12px; line-height: 1; margin: 20px 0px; font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI Variable&quot;, &quot;Segoe UI&quot;, system-ui, ui-sans-serif, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;"><summary class="md-editor-code-head" style="background-color: rgb(40, 44, 52); border-radius: 5px 5px 0px 0px; display: grid; font-size: 12px; grid-template: &quot;1rf 1rf&quot;; height: 32px; justify-content: space-between; margin-bottom: 0px; width: 683.458px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); list-style: none; cursor: pointer;"><div class="md-editor-code-flag" style="margin-left: 12px;"><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; background-color: rgb(236, 106, 94);"></span><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; margin-left: 4px; background-color: rgb(244, 191, 79);"></span><span style="border-radius: 50%; display: inline-block; height: 10px; margin-top: 11px; width: 10px; margin-left: 4px; background-color: rgb(97, 197, 84);"></span></div><div class="md-editor-code-action" style="align-items: center; display: flex;"><span class="md-editor-code-lang" style="margin-right: 10px; line-height: 32px;"></span><span class="md-editor-copy-button" data-tips="复制代码" style="margin-right: 10px; cursor: pointer; line-height: 32px; position: static;">复制代码</span><span class="md-editor-collapse-tips" style="margin-right: 12px;"><svg class="md-editor-icon" aria-hidden="true"><use xlink:href="#md-editor-icon-collapse-tips"></use></svg></span></div></summary><pre style="margin: 0px; position: relative;"><code class="language-" language="" style="background-color: rgb(40, 44, 52); border-radius: 0px 0px 5px 5px; color: rgb(169, 183, 198); line-height: 1.6; padding: 1em; display: block; font-family: source-code-pro, Menlo, Monaco, Consolas, &quot;Courier New&quot;, monospace; font-size: 14px; overflow: auto; position: relative; box-shadow: rgba(0, 0, 0, 0.333) 0px 2px 2px;"><span class="md-editor-code-block" style="color: rgb(169, 183, 198); display: inline-block; overflow: auto; vertical-align: bottom; width: 620.458px;">stage(<span class="hljs-string" style="color: rgb(152, 195, 121);">'部署或回滚'</span>) {
    steps {
        script {
            <span class="hljs-keyword" style="color: rgb(198, 120, 221);">if</span> (params.ACTION == <span class="hljs-string" style="color: rgb(152, 195, 121);">'deploy'</span>) {
                // 执行正常部署步骤
                <span class="hljs-built_in" style="color: rgb(230, 192, 123);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"执行部署操作..."</span>
                // 部署脚本...
            } <span class="hljs-keyword" style="color: rgb(198, 120, 221);">else</span> <span class="hljs-keyword" style="color: rgb(198, 120, 221);">if</span> (params.ACTION == <span class="hljs-string" style="color: rgb(152, 195, 121);">'rollback'</span>) {
                // 执行回滚操作
                <span class="hljs-built_in" style="color: rgb(230, 192, 123);">echo</span> <span class="hljs-string" style="color: rgb(152, 195, 121);">"执行回滚操作，版本: <span class="hljs-variable" style="color: rgb(209, 154, 102);">${params.ROLLBACK_VERSION}</span>"</span>
                sh <span class="hljs-string" style="color: rgb(152, 195, 121);">""</span><span class="hljs-string" style="color: rgb(152, 195, 121);">"
                    # 停止当前服务
                    if [ -f "</span><span class="hljs-variable" style="color: rgb(209, 154, 102);">${DEPLOY_DIR}</span>/<span class="hljs-variable" style="color: rgb(209, 154, 102);">${PROJECT_NAME}</span>.pid<span class="hljs-string" style="color: rgb(152, 195, 121);">" ]; then
                        PID=\$(cat <span class="hljs-variable" style="color: rgb(209, 154, 102);">${DEPLOY_DIR}</span>/<span class="hljs-variable" style="color: rgb(209, 154, 102);">${PROJECT_NAME}</span>.pid)
                        kill -9 \$PID || true
                    fi
                    
                    # 恢复历史版本
                    cp <span class="hljs-variable" style="color: rgb(209, 154, 102);">${DEPLOY_DIR}</span>/history/<span class="hljs-variable" style="color: rgb(209, 154, 102);">${params.ROLLBACK_VERSION}</span> <span class="hljs-variable" style="color: rgb(209, 154, 102);">${DEPLOY_DIR}</span>/<span class="hljs-variable" style="color: rgb(209, 154, 102);">${JAR_FILE}</span>
                    
                    # 启动服务
                    cd <span class="hljs-variable" style="color: rgb(209, 154, 102);">${DEPLOY_DIR}</span>
                    nohup java -jar <span class="hljs-variable" style="color: rgb(209, 154, 102);">${JAR_FILE}</span> &gt; <span class="hljs-variable" style="color: rgb(209, 154, 102);">${PROJECT_NAME}</span>.log 2&gt;&amp;1 &amp;
                    echo \$! &gt; <span class="hljs-variable" style="color: rgb(209, 154, 102);">${PROJECT_NAME}</span>.pid
                "</span><span class="hljs-string" style="color: rgb(152, 195, 121);">""</span>
            }
        }
    }
}</span></code></pre></details>

### 七、常见问题与解决方案

在 Jenkins 使用过程中，可能会遇到各种问题，以下是高频问题及解决方法：

#### 7.1 构建失败：找不到 JDK/Maven

**现象** ：日志中出现`java: command not found`或`mvn: command not found`

**原因**：全局工具配置错误，Jenkins 无法找到 JDK/Maven 路径

**解决**：

1. 确认 JDK/Maven 实际安装路径（`which java`或`which mvn`）
2. 进入 "全局工具配置"，检查路径是否正确（绝对路径）
3. 确保 Jenkins 用户有访问该路径的权限

#### 7.2 权限问题：无法创建目录或执行命令

**现象** ：日志中出现`Permission denied`

**原因**：Jenkins 运行用户（通常是 jenkins）权限不足

**解决**：

1. 查看 Jenkins 运行用户：`ps -ef | grep jenkins`
2. 为部署目录授权：`chown -R jenkins:jenkins /opt/deploy`
3. 必要时赋予 sudo 权限（谨慎操作）：编辑`/etc/sudoers`添加`jenkins ALL=(ALL) NOPASSWD: ALL`

#### 7.3 测试失败：单元测试不通过导致构建中断

**现象** ：`mvn test`执行失败，构建终止

**解决**：

1. 查看测试报告：构建详情 → "Test Result"
2. 修复代码中的测试用例
3. 紧急情况下可临时跳过测试（不推荐）：将`mvn package`改为`mvn package -DskipTests`

#### 7.4 插件冲突：安装插件后 Jenkins 无法启动

**现象**：Jenkins 启动失败，日志中有插件相关错误

**解决**：

1. 进入 Jenkins 插件目录（通常是`/root/.jenkins/plugins`）
2. 删除冲突的插件目录（如`problem-plugin/`）
3. 重启 Jenkins：`systemctl restart jenkins`

#### 7.5 远程部署超时：SSH 连接失败

**现象** ：远程部署阶段提示`Connection timed out`

**解决**：

1. 检查网络：在 Jenkins 服务器上`ping 应用服务器IP`
2. 检查端口：`telnet 应用服务器IP 22`确认 SSH 端口开放
3. 检查认证：重新配置 "Publish over SSH" 的密钥或密码
4. 增加超时时间：在 sshPublisher 中设置`execTimeout: 300000`（5 分钟）

### 八、总结与展望

通过本文的步骤，可以掌握从 0 搭建 Jenkins 环境、配置自动化工具、编写 Pipeline 脚本、实现 Java 项目自动部署的完整流程。一个成熟的 CI/CD 流水线能为团队带来显著的效率提升，让开发者从繁琐的部署工作中解放出来，专注于代码质量和业务逻辑。























