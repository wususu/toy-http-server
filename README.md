# toy-http-server
A toy http server power by java | 简单的http服务器 | Java NIO

USE:

### 1. 运行BootStrap.java

打开浏览器输入 http://127.0.0.1:8888/{file_name}

{file_name}为请求的文件名,程序将会解析webroot目录下相应文件并输出到浏览器

例如: 输入 http://127.0.0.1:8888/index.html 就能看到我的github主页啦

### 2. Servlet解析及注解支持:

在http.servlet下创建servlet并使用@HashService注解(支持深度递归搜索servlet),

地址加上servlet对应uri即可得到相应输出

例如: 输入 http://127.0.0.1:8888/index 就能看到该servlet名

### 3. POST/GET参数和Cookie支持:

在请求Servlet地址时加上GET/POST参数,页面会显示发送参数的map

例如: 输入 http://127.0.0.1:8888/index?name=hasher 就能看到该{"name"="hasher"}
