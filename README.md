# 本地大模型驱动的日记助手

一个基于Java和Spring Boot的智能日记助手，通过HTTP调用本地大语言模型（Ollama接口）来帮助用户整理和总结日常记录。

## 功能特性

- 📝 **命令行交互**：通过Spring Shell提供友好的命令行界面
- 🤖 **本地大模型驱动**：集成Ollama本地大语言模型
- 💾 **数据持久化**：自动保存日记数据到JSON文件
- 🎯 **严格规则约束**：模型严格按照定义的日记助手规则运行
- ⚡ **轻量级架构**：基于Spring Boot，易于部署和扩展

## 系统要求

- Java 17 或更高版本
- Maven 3.6 或更高版本
- Ollama 服务运行在 `http://localhost:11434`
- 推荐模型：`qwen2.5:7b`

## 快速开始

### 1. 安装和配置Ollama

确保Ollama已安装并运行：

```bash
# 安装Ollama（如果尚未安装）
curl -fsSL https://ollama.ai/install.sh | sh

# 启动Ollama服务
ollama serve

# 拉取推荐模型
ollama pull qwen2.5:7b
```

### 2. 构建项目

```bash
# 克隆项目（如果从Git仓库获取）
git clone <repository-url>
cd Diary-Assistant

# 使用Maven构建
mvn clean package
```

### 3. 运行应用

```bash
# 运行应用
java -jar target/diary-assistant-1.0.0.jar
```

应用启动后将显示欢迎信息并进入交互式命令行界面。

## 使用指南

### 基本命令

| 命令 | 说明 | 示例 |
|------|------|------|
| `帮助` | 显示使用帮助 | `帮助` |
| `开始日记` | 开启当天日记记录 | `开始日记` |
| `记录 <内容>` | 记录一条日记内容 | `记录 今天去图书馆看书` |
| `结束日记` | 结束日记并生成总结 | `结束日记` |
| `状态` | 查看当前会话状态 | `状态` |
| `检查服务` | 检查Ollama服务状态 | `检查服务` |

### 使用流程

1. **启动应用**
   ```bash
   java -jar target/diary-assistant-1.0.0.jar
   ```

2. **开始日记记录**
   ```
   > 开始日记
   今日记录，每一笔都是生活的小闪光～
   ```

3. **记录日常事件**
   ```
   > 记录 早上7点起床
   收到啦。
   
   > 记录 去图书馆找书
   收到啦。
   
   > 记录 中午吃了好吃的拉面
   收到啦。
   ```

4. **结束日记并生成总结**
   ```
   > 结束日记
   === 完整版日记 ===
   [模型生成的完整日记内容...]
   
   === 五点条列总结 ===
   1. [第一条总结...]
   2. [第二条总结...]
   ...
   
   === 洞察与建议 ===
   [模型生成的洞察与建议...]
   ```

## 项目结构

```
diary-assistant/
├── src/main/java/com/example/diary/
│   ├── DiaryAssistantApplication.java     # 主启动类
│   ├── controller/
│   │   └── DiaryController.java           # 命令行控制器
│   ├── service/
│   │   ├── DiarySessionService.java       # 日记会话管理服务
│   │   ├── OllamaService.java             # Ollama服务调用
│   │   └── PromptBuilder.java             # Prompt构建器
│   ├── model/
│   │   ├── DiarySession.java              # 日记会话模型
│   │   ├── DiaryEntry.java                # 日记条目模型
│   │   ├── OllamaRequest.java             # Ollama请求模型
│   │   └── OllamaResponse.java            # Ollama响应模型
│   ├── persistence/
│   │   └── FileDiaryRepository.java       # 文件持久化存储
│   └── config/                            # 配置类（预留）
├── src/main/resources/
│   └── application.yml                    # 应用配置文件
└── pom.xml                                # Maven配置文件
```

## 核心设计原则

### 1. 严格的阶段分离

- **记录阶段**：只记录用户原文，不调用模型
- **结束阶段**：一次性调用模型生成完整总结

### 2. 规则约束

模型必须严格遵循以下规则（定义在 `PromptBuilder.java` 中）：

- 角色定位：日记整理助手，不是心理咨询师
- 内容处理：严格基于原始记录，不添加虚构内容
- 输出格式：完整版日记 + 五点条列总结 + 洞察与建议
- 语言风格：中性、客观、简洁

### 3. 数据完整性

- 所有用户原文原样保留
- 按时间顺序记录和存储
- 自动持久化到JSON文件

## 配置说明

### 应用配置 (application.yml)

```yaml
# Ollama配置
ollama:
  url: http://localhost:11434    # Ollama服务地址
  model: qwen2.5:7b              # 默认模型

# 日记数据配置
diary:
  data:
    directory: ./data            # 数据存储目录
```

### 自定义配置

可以通过环境变量或命令行参数覆盖默认配置：

```bash
# 使用不同模型
java -jar target/diary-assistant-1.0.0.jar --ollama.model=llama2:7b

# 指定数据存储目录
java -jar target/diary-assistant-1.0.0.jar --diary.data.directory=/path/to/data
```

## 扩展开发

### 添加新的命令

在 `DiaryController.java` 中添加新的 `@ShellMethod`：

```java
@ShellMethod(key = "新命令", value = "命令描述")
public String newCommand() {
    // 实现逻辑
    return "响应内容";
}
```

### 修改日记助手规则

编辑 `PromptBuilder.java` 中的 `DIARY_ASSISTANT_RULES` 常量。

### 支持其他大模型

修改 `OllamaService.java` 中的API调用逻辑，或实现新的服务类。

## 故障排除

### Ollama服务不可用

```
> 检查服务
❌ Ollama服务不可用，请确保Ollama已启动并运行在http://localhost:11434
```

**解决方案：**
1. 确保Ollama服务正在运行：`ollama serve`
2. 检查端口11434是否被占用
3. 验证模型是否已下载：`ollama list`

### 日记数据文件位置

日记数据默认保存在 `./data/` 目录下，文件命名格式为 `diary_yyyy-MM-dd.json`。

### 内存不足

如果处理大量日记内容时出现内存问题，可以调整JVM参数：

```bash
java -Xmx512m -jar target/diary-assistant-1.0.0.jar
```

## 许可证

本项目采用MIT许可证。

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。

## 联系方式

如有问题或建议，请通过项目Issue页面联系。