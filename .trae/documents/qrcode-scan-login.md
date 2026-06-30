# 扫码登录H5页面实现方案

## Context

当前二维码登录模块是自建系统，PC端生成二维码后，手机端没有对应的扫码页面。用微信/浏览器扫码后打开的URL无处可去（没有 `/scan-login` 路由，也没有对应的H5页面）。需要创建一个移动端H5页面，用户扫码后在该页面输入账号密码，确认登录后PC端自动登录。

## 实现步骤

### 1. 创建移动端扫码登录H5页面

**新建文件**: `D:\recruitment-frontend\src\views\ScanLoginPage.vue`

功能：
- 从URL参数获取 `token`
- 显示移动端友好的登录表单（手机号 + 密码）
- 点击"确认登录"后：
  1. 调用 `POST /auth/login` 验证账号密码
  2. 验证成功后调用 `POST /qrcode/scan?token=xxx&userId=xx&phone=xx&role=xx` 标记已扫码
  3. 自动调用 `POST /qrcode/confirm?token=xxx` 确认登录
  4. 显示"登录成功，请返回电脑端"
- 如果token无效或已过期，显示提示信息

### 2. 添加前端路由

**修改文件**: `D:\recruitment-frontend\src\router\index.js`

在路由配置中添加 `/scan-login` 路由，指向 `ScanLoginPage.vue`，设置 `requiresAuth: false`

### 3. 修复后端 QRCodeController 的BUG

**修改文件**: `D:\recruitment-platform\auth-service\src\main\java\com\recruitment\auth\controller\QRCodeController.java`

- 修复 `mapRoleToUserType` 映射错误：`teacher` 应为3，`enterprise` 应为2
- 修复 `getServerHost()` 硬编码问题，改为从配置读取或使用前端地址
- 修复过期清理逻辑（当前会删除所有token）
- 修复 JWT 生成时第二个参数应传 username 而不是 phone

### 4. 修改后端 SecurityConfig 放行 /qrcode/** 路径

**修改文件**: `D:\recruitment-platform\auth-service\src\main\java\com\recruitment\auth\config\SecurityConfig.java`

添加 `/qrcode/**` 路径的 permitAll

### 5. 修改二维码生成的URL

**修改文件**: `QRCodeController.java` 中的 `generateQRCode` 方法

将 `scanUrl` 改为指向前端地址而非后端地址，格式为：`{前端origin}/scan-login?token={token}`

### 6. 修改前端 QRCodeLogin.vue

**修改文件**: `D:\recruitment-frontend\src\components\QRCodeLogin.vue`

- 优先使用后端返回的 `scanUrl` 构建二维码内容
- 更新使用说明文案

## 关键文件清单

| 操作 | 文件 |
|------|------|
| 新建 | `D:\recruitment-frontend\src\views\ScanLoginPage.vue` |
| 修改 | `D:\recruitment-frontend\src\router\index.js` |
| 修改 | `D:\recruitment-platform\auth-service\src\main\java\com\recruitment\auth\controller\QRCodeController.java` |
| 修改 | `D:\recruitment-platform\auth-service\src\main\java\com\recruitment\auth\config\SecurityConfig.java` |
| 修改 | `D:\recruitment-frontend\src\components\QRCodeLogin.vue` |

## 验证方式

1. 启动 auth-service (8081) 和前端 (5173)
2. PC端打开登录页，切换到二维码登录tab
3. 手机浏览器扫码（或直接访问 `http://{ip}:5173/scan-login?token=xxx`）
4. 在H5页面输入手机号密码，点击确认登录
5. PC端应自动跳转到对应角色的首页
