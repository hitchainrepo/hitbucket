HIT 协议、原理、设计
=======

### 1. HIT 协议

HIT 协议与 GIT 协议一致，如下图所示：

![图片](https://docs.google.com/drawings/d/e/2PACX-1vTwLWoveA9JRnJHdXZWpCKSTfNfa3_EfzYEyy9HU59T5v_TxCIpHPFciIVzE9GD5cYMWZti63d2s85O/pub?w=695&amp;h=386)
[EditMe](https://docs.google.com/drawings/d/1RBDvx2bbgVKsKYBsAt6jinwDLR3DK6ZFlfrHca4P-OM/edit?usp=sharing)

#### 1.1. API-C

API-C (API for client) 兼容 GIT Smart 协议，详细请参看：https://git-scm.com/book/zh/v2/Git-内部原理-传输协议 。主要包括 GIT 如下操作：

    ===Git clone===
    => GET info/refs
    => GET HEAD
    => GET objects
    => GET objects/info/http-alternates
    => GET objects/info/packs
    => GET objects/pack/pack-hash.idx
    => GET objects/pack/pack-hash.pack
    ===Git push===
    => GET host.git/info/refs?service=git-receive-pack
    => POST host.git/git-receive-pack
    ===Git fetch===
    => GET host.git/info/refs?service=git-upload-pack
    => POST host.git/git-upload-pack

#### 1.2. API-S

API-S （API for storage）主要实现 GIT 内容存储（去中心化存储）：

#### 1.3. 公有仓库访问策略

公有仓库（Public repository）对于访问没有权限控制，任何人都可以通过 DAPP 获得最新的仓库内容。

##### 1.3.1 公有仓库团队


