# 前端 Nginx 运行说明

## 当前确认结果

- 后端地址：`http://localhost:8080`
- 后端健康检查：`http://localhost:8080/api/auth/test-accounts`
- 网页端前端目录：`前端-app和网页/前端-网页版本-酒店系统`
- 当前项目内未发现 HBuilderX 打包产物 `unpackage/dist/build/web`
- 当前项目内未发现 `nginx-1.18.0` 目录，需要使用你本机已有的 Nginx 目录操作

## 打包网页端

1. 打开 HBuilderX。
2. 导入目录：`C:\Users\Trump\Desktop\Hotel-management-system\前端-app和网页\前端-网页版本-酒店系统`。
3. 可先点击 `运行 -> 运行到浏览器 -> Chrome` 检查页面能否打开。
4. 正式托管时点击 `发行 -> 网站-PC Web或手机H5`。
5. 打包产物通常在：`前端-网页版本-酒店系统\unpackage\dist\build\web`。

## 放到 Nginx

1. 在你的 `nginx-1.18.0/html` 下新建目录：`hrms-web`。
2. 把 HBuilderX 打包出来的 `web` 目录内容复制到 `nginx-1.18.0/html/hrms-web`。
3. 把 `后端/nginx/hrms-web.conf` 中的 server 配置复制到 `nginx-1.18.0/conf/nginx.conf` 的 `http { ... }` 内。
4. 在 `nginx-1.18.0` 目录启动或重载：

```powershell
start nginx
```

如果 Nginx 已经在运行：

```powershell
nginx -s reload
```

## 打开页面

浏览器访问：

```text
http://localhost:8081
```

如果页面调用 `/api/...`，Nginx 会代理到后端 `http://localhost:8080`。如果页面仍然是 mock 数据，说明前端请求层还没有真正接入后端接口，后端接口和代理配置已经准备好。
