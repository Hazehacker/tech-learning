# Linux 权限管理

## 1. 权限模型概述

Linux 采用基于 **用户-组-其他** 的三层权限模型：

```
┌─────────────┬─────────────┬─────────────┐
│   所有者     │     组      │    其他用户   │
│   (Owner)   │   (Group)   │   (Other)   │
├─────────────┼─────────────┼─────────────┤
│     rwx     │     r-x     │     r--     │
│      7      │      5      │      4      │
└─────────────┴─────────────┴─────────────┘
```

**每个文件/目录**都有三组权限位，分别控制：

- **所有者权限**：文件==创建者对文件的操作权限==
- **组权限**：==文件所属组内成员的权限==
- ==**其他用户权限**==：既不是所有者也不是组内成员的权限



## 2. 权限表示方法

### 2.1 字母表示法

| 字母 | 含义 | 数值 |
|------|------|------|
| **r** | **读 (Read)** | 4 |
| **w** | **写 (Write)** | **2** |
| **x** | **执行 (Execute)** | 1 |
| - | 无权限 | 0 |

### 2.2 数字表示法

```
权限 = r + w + x
     = 4 + 2 + 1 = 7
```

**常见权限组合：**

| 权限 | 数字 | 说明 |
|------|------|------|
| rwx | 7 | 读、写、执行 |
| rw- | 6 | 读、写 |
| r-x | 5 | 读、执行 |
| r-- | 4 | 只读 |
| -w- | 2 | 只写 |
| --x | 1 | 只执行 |

### 2.3 ==查看权限==

```bash
ls -l filename
# 查看目录下所有文件的权限说明
ls -l derictory/ 
```

输出示例：
```
-rwxr-xr-- 1 owner group 4096 May  9 10:00 filename
```

权限位详解：
```
- rwx r-x r--
│ │││ │││ └┬┘
│ │││ │││  └─ 其他用户权限
│ │││ └┬┘ 
│ │││  └─ 组权限
│ └┬┘
│  └─ 所有者权限
│ 
└─────────── 文件类型 (-=普通文件, d=目录, l=链接)
```

## 3. 常用权限命令

### 3.1 ==chmod - 修改文件权限==

> change mode

**==字母方式==：**

```bash
# u=所有者, g=组, o=其他, a=所有
chmod u+x file           # 给所有者添加执行权限
chmod g-w file           # 删除组的写权限
chmod o+r file           # 给其他用户添加读权限
chmod a+x file           # 给所有人添加执行权限
chmod u+rwx,g+rx,o+r file  # 组合使用
```

**==数字方式==：**

```bash
chmod 755 file           # rwxr-xr-x
chmod 644 file           # rw-r--r--
chmod 700 file           # rwx------
chmod 600 file           # rw-------
chmod 775 file           # rwxrwxr-x
chmod 777 file           # rwxrwxrwx （慎用！）
```

**递归修改目录权限：**
```bash
chmod -R 755 directory/   # 递归修改目录及所有子文件
```

**参考权限对照表：**
| 权限 | 数字 | 典型用途 |
|------|------|---------|
| 777 | rwxrwxrwx | 开放权限（少用） |
| 755 | rwxr-xr-x | 目录、脚本、可执行文件 |
| 644 | rw-r--r-- | 普通文件 |
| 600 | rw------- | 私有文件 |
| 500 | r-x------ | 只读目录 |
| 400 | r-------- | 密钥文件 |

### 3.2 chown - 修改所有者

> change owner

```bash
# 修改文件所有者
chown user file

# 修改所有者和所属组
chown user:group file

# [递归(recursive)]修改目录
chown -R user:group directory/

# 只修改组
chown :group file
```

### 3.3 chgrp - 修改所属组

> change group

```bash
# 修改文件所属组
chgrp group file

# 递归修改
chgrp -R group directory/
```

### 3.4 典型应用场景

```bash
# 部署网站目录
chown -R nginx:nginx /var/www/html
chmod -R 755 /var/www/html

# 部署应用程序
chown -R app:app /opt/myapp
chmod -R 750 /opt/myapp          # 组成员可读执行，其他人无权限
chmod +x start.sh                # 添加执行权限

# 配置文件权限加固
chmod 600 /etc/nginx/nginx.conf
chown root:root /etc/nginx/nginx.conf

# 日志目录
chown -R syslog:adm /var/log/app
chmod -R 750 /var/log/app
```

## 4. 特殊权限

### 4.1 SUID (Set User ID)

当文件设置了 SUID 权限时，任何用户运行该程序都会以文件所有者身份执行。

```bash
# 设置 SUID
chmod u+s file

# 数值方式：4xxx
chmod 4755 program

# 典型例子：passwd 命令
ls -l /usr/bin/passwd
# -rwsr-xr-x 1 root root ... /usr/bin/passwd
```

**安全注意：** SUID 可能导致权限提升，慎用于来路不明的程序。

### 4.2 SGID (Set Group ID)

- **作用于文件**：运行程序时以文件所属组身份执行
- **作用于目录**：目录下新建的文件继承该目录的组

```bash
# 设置 SGID
chmod g+s directory/

# 数值方式：2xxx
chmod 2755 directory/

# 示例：共享目录，所有用户创建的文件都属于同一个组
chgrp developers /shared
chmod 2775 /shared
```

### 4.3 Sticky Bit (粘滞位)

保护目录中的文件只能被创建者删除，即使其他人有写权限。

```bash
# 设置粘滞位
chmod +t /shared

# 数值方式：1xxx
chmod 1777 /shared

# 典型例子：/tmp 目录
ls -ld /tmp
# drwxrwxrwt 10 root root ... /tmp
```

**说明：** `/tmp` 的 `t` 表示粘滞位，任何用户可以在其中创建文件，但只能删除自己的文件。

### 4.4 特殊权限组合

| 权限 | 数值 | 说明 |
|------|------|------|
| 4755 | rwsr-xr-x | SUID + 755 |
| 2755 | rwxr-sr-x | SGID + 755 |
| 1755 | rwxr-xr-t | Sticky Bit + 755 |

## 5. ACL 访问控制列表

对于需要更细粒度权限控制的场景，使用 ACL。

### 5.1 查看 ACL

```bash
# 查看文件 ACL
getfacl file

# 输出示例：
# # file: file
# # owner: owner
# # group: group
# user::rw-
# user:developer:rw-
# group::r--
# group:design:rw-
# mask::rw-
# other::r--
```

### 5.2 设置 ACL

```bash
# 给指定用户设置权限
setfacl -m u:username:rw file

# 给指定组设置权限
setfacl -m g:groupname:rx directory/

# 给多个对象设置权限
setfacl -m u:user1:rw,u:user2:r,g:group1:r file

# 删除指定用户的 ACL
setfacl -x u:username file

# 删除所有 ACL
setfacl -b file

# 设置默认 ACL（目录新建文件自动继承）
setfacl -m d:u:username:rw directory/

# 递归设置
setfacl -R -m u:username:rw directory/
```

### 5.3 ACL 权限优先级

1. 所有者权限
2. ACL 特定用户权限
3. 所属组 / ACL 特定组权限
4. 其他用户权限

## 6. 企业应用最佳实践

### 6.1 目录权限规范

| 目录 | 权限 | 说明 |
|------|------|------|
| /bin, /sbin | 755 root:root | 系统命令 |
| /usr/bin | 755 root:root | 用户命令 |
| /var/log | 750 root:adm | 日志目录 |
| /tmp | 1777 root:root | 临时文件 |
| /home/user | 750 user:user | 用户目录 |
| /var/www | 755 root:nginx | Web 内容 |

### 6.2 常见服务运行用户

| 服务 | 运行用户 | 配置目录权限 |
|------|---------|-------------|
| Nginx | nginx | /var/www:755 nginx:nginx |
| Apache | apache | /var/www:755 apache:apache |
| MySQL | mysql | /var/lib/mysql:755 mysql:mysql |
| Docker | root | /var/run/docker.sock:660 |

### 6.3 安全加固检查清单

```bash
# 1. 查找所有 SUID 文件
find / -perm /4000 -type f 2>/dev/null

# 2. 查找所有 SGID 文件
find / -perm /2000 -type f 2>/dev/null

# 3. 查找权限为 777 的目录
find / -type d -perm 777 2>/dev/null

# 4. 查找无主文件（无所有者或组）
find / -nouser -o -nogroup 2>/dev/null

# 5. 检查关键文件权限
ls -l /etc/passwd /etc/shadow
# passwd 应为 644，shadow 应为 000 或 600
```

### 6.4 一键权限加固脚本

```bash
#!/bin/bash
# 网站部署权限加固

WEB_ROOT="/var/www/html"
RUNNING_USER="nginx"
RUNNING_GROUP="nginx"

# 设置所有者
chown -R ${RUNNING_USER}:${RUNNING_GROUP} ${WEB_ROOT}

# 设置目录权限
find ${WEB_ROOT} -type d -exec chmod 755 {} \;

# 设置文件权限
find ${WEB_ROOT} -type f -exec chmod 644 {} \;

# 特殊脚本添加执行权限
find ${WEB_ROOT} -name "*.sh" -exec chmod 750 {} \;

echo "权限加固完成"
```

## 7. 常见问题排查

### 7.1 ==权限不足==

```bash
# 查看当前用户身份
id

# 查看当前用户所属组
groups

# 以root执行
sudo command

# 临时切换用户
su - username
```

### 7.2 权限明明够但无法访问

检查顺序：
1. 目录本身权限
2. 父目录权限
3. ACL 规则
4. SELinux 上下文（如果有）
5. 文件系统是否只读

```bash
# 检查 SELinux 状态
getenforce
# Permissive 或 Disabled 通常不影响权限

# 检查父目录
ls -ld /parent/directory

# 检查 ACL
getfacl /path/to/file
```

### 7.3 进程权限

```bash
# 查看进程运行用户和权限掩码
ps aux | grep nginx

# 查看进程打开的文件
lsof -p pid

# 查看进程Capabilities（Linux 2.6+）
getpcaps pid
```