HIT 协议、原理、设计
=======

### 1.HIT 协议

HIT 协议与 GIT 协议一致，如下图所示：

![图片](https://docs.google.com/drawings/d/e/2PACX-1vTwLWoveA9JRnJHdXZWpCKSTfNfa3_EfzYEyy9HU59T5v_TxCIpHPFciIVzE9GD5cYMWZti63d2s85O/pub?w=695&amp;h=386)
[EditMe](https://docs.google.com/drawings/d/1RBDvx2bbgVKsKYBsAt6jinwDLR3DK6ZFlfrHca4P-OM/edit?usp=sharing)

#### 1.1.API-C

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

#### 1.2.API-S

API-S （API for storage）主要实现 GIT 内容存储（去中心化存储）：

#### 1.3.公有仓库访问策略

公有仓库（Public repository）对于访问没有权限控制，任何人都可以通过 DAPP 获得最新的仓库内容。

##### 1.3.1.公有仓库团队访问

公有仓库团队（Team）可以直接访问由 DAPP 验证提交者是否为 Team 的成员、提交代码内容，DAPP 同步更新仓库到最新的地址。

#### 1.4.私有仓库访问策略

私有仓库（Private repository）对于访问设有权限控制，权限控制的方式是通过产生的公钥、私钥对进行加密解密。 私有仓库访问策略设计如下：

![图片](https://docs.google.com/drawings/d/e/2PACX-1vQJK0_xZtqCPOR1mc1UjiHC5kBwu50OhL769AZjx1kcP-DPGnFybUYLy_iShZWFQc_miOSMruukslY1/pub?w=667&amp;h=631)
[EditMe](https://docs.google.com/drawings/d/15TZxxW0zZ2fq3p2l5QHXBPd1e2LjtJMfgxvb4LnsE7I/edit?usp=sharing)

#### 1.4.1.初始密钥对生成

DAPP 在私有仓库建立时就会生产一个密钥对，并且对私钥采用拥有者的公钥进行加密结果为 EO，DAPP 只存储公钥及 EO。

#### 1.4.2.仓库加密

DAPP 在把仓库存储到 Storage 的时候采用私有仓库的公钥进行加密。

#### 1.4.3.团队密钥对生成

DAPP 需要把仓库拥有者的私钥把 EO 解密，并使用团队的公钥进行加密，结果为 ET。

#### 1.4.4.仓库解密

任何提交都需要对仓库进行解密，解密过程是 DAPP 使用团队的私钥把私有仓库的团队 ET 解密出来，并对仓库内容进行解密。

#### 1.4.5.团队信息存储

DAPP 把团队信息以明文方式存储到 Storage 上，每个仓库各自存储为一个文件，文件格式为：

    GROUP_NAME:email:EO/ET













