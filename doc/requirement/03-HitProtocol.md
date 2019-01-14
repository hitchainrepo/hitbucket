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

    ===Upload===
    => POST add
    => GET ls
    => GET cat

上面几个 API 参照：https://docs.ipfs.io/reference/api/http 对应的 API 。其中 POST add 应自动更新 IPNS。

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

DAPP 把团队信息以明文方式存储到 Storage 上，每个仓库各自存储为一个文件，文件格式参看索引文件。

### 1.5.索引文件

索引文件是项目的主入口，如下图所示：

![图片](https://docs.google.com/drawings/d/e/2PACX-1vS9b7t4ZW2i00kQ7v2ODtVdBTLcW4ngpkSem1iclA2jsYEX88Z9xRJ94HQvTxcnpTelkNvCdIsLM57r/pub?w=562&amp;h=599)
[EditMe](https://docs.google.com/drawings/d/16tVCaBD9YwzhYGPQw5YgJAGxL1p5TB_WWiXZS3XEnkg/edit?usp=sharing)

索引文件结构如下：

    PROJ:projectName:hash
    IPFS:-:url
    RKEY:encrypt_repoPrivateKey_by_owner_pubKey:pubKey
    OKEY:owner:pubKey
    MKEY*:member:pubKey
    TKEY*:encrypt_repoPrivateKey_by_member_pubKey:pubKey

索引文件说明：
- PROJ, 项目名称：项目 HASH 地址，IPNS 地址
- IPFS, IPFS 的入口 URL，客户端的 HOST 地址
- RKEY, 私有仓库需要此项，拥有者公钥与仓库私钥的加密结果：仓库公钥
- OKEY, 仓库拥有者的 email：仓库拥有者的公钥
- MKEY, 可允许多个，成员的 email：仓库成员的公钥
- TKEY, 可允许多个，成员的公钥与仓库私钥的加密结果：成员的公钥

#### 1.5.1.索引文件权限

只有仓库拥有者才有权更改索引文件。所有人可以读取这个文件。

#### 1.5.2.索引文件使用流程

GIT 仓库在创建及添加团队成员时使用：

- 仓库创建时生成这个文件，公有仓库 PROJ、IPFS、OKEY 有值，私有仓库多一个 RKEY 值。
- 添加团队成员时需要获得团队成员的 email 及公钥，并且 MKEY 有值，私有仓库还需要多一个 TKEY 值。
- 删减团队成员时同时删除 MKEY 及 TKEY 值。
- 更改仓库密钥对时，需要对整个仓库重新加密，并重置 RKEY、MKEY、TKEY 值。

### 1.5. 索引文件与 DAPP

索引文件与 DAPP 入口如下：

![图片](https://docs.google.com/drawings/d/e/2PACX-1vRfc6wlcWSTWdY5Af8TaIxLI3hDH0U3l-zogH_i6m_uWbdxua5o1jfDQdnF29oR4hvNrXJcnNB3op4V/pub?w=633&amp;h=694)
[EditMe](https://docs.google.com/drawings/d/1qI19hS86AhF85HkpJiRAKUqALae2T0T7w-HsURGcBls/edit?usp=sharing)

DAPP的作用就是维护索引文件，确保索引文件的更新是合法的完整的，同时向成员提供索引中公开的信息。

使用流程说明：
- 创建项目，创建的同时也是建立 DAPP 时，包括初始的项目地址，及必要的索引信息
- 拉取项目，客户端先从 DAPP 中获得最新的项目地址及必要的索引信息，然后再从 D-Storage 中拉取内容，如果是私有仓库还需要对内容解密
- 更新项目，客户端先从 DAPP 中获得必要的索引信息，把内容更新到 D-Storage 中，然后再通过 D-APP 更新索引信息，如果是私有仓库则需要先加密后传输


### 2.HIT 协议(0.2)

HIT 协议0.2版本架构图如下：

![图片](https://docs.google.com/drawings/d/e/2PACX-1vRwB1_4oG4Rgufoh1nzlueqqZnrL46Mzue9CvGR9EFvmTyxWP4ocbeMcNl3ns0DefP-suftzcGbVUQZ/pub?w=960&h=720)
[EditMe](https://docs.google.com/drawings/d/1SVB5wzVhKqobR4q6Yi-gNVVzdRhYf8GWrJlRzhWiKJI/edit?usp=sharing)

#### 2.1.HIT仓储结构说明

HIT 仓储结构在基于 GIT 的仓储结构上添加两个文件：ProjectInfoFile 和 GitFileIndex：

- ProjectInfoFile：用于存储项目的基础信息包括：版本、仓库名称及合约地址及加密信息、拥有者及以太坊地址、团队成员及以太坊地址。
- GitFileIndex：是对整个.git 目录下的文件进行索引，上传更新及下载更新都需要这个文件。

ProjectInfoFile结构规范：

    存储位置：objects/info/projectinfo
    文件内容如下：
    sign: 数据签名的 HASH 码，以用于验证下面数据的准确性
    {
    version: 1, 版本
    ethereumUrl: the ethereum entrance url, Erhereum入口 URL
    fileServerUrl: the file server url, could be ipfs entrance url, 文件服务器入口 URL
    repoName: /userName/repoName.git, 仓库名称
    repoPriKey: 代码仓库的私钥（由拥有者的私钥加密）
    repoPubKey: 代码仓库的公钥
    repoAddress: 代码仓库的合约地址
    owner: 仓库拥有者
    ownerPubKeyRsa: 仓库拥有者公钥
    ownerAddressEcc: 仓库拥有者Ethereum地址
    [{
      member: 团队成员
      memberPubKeyRsa: 成员的公钥
      memberAddressEcc: 成员的Ethereum地址
      memberRepoPriKey: 成员的仓库私钥加密
    }*]
    }


GitFileIndex结构规范：

    存储位置：objects/pack/gitfile.idx
    压缩：GZIP
    文件内容如下：
    ipfsHash:sha1:fileName*

#### 2.2.HIT与智能合约

智能合约部署到以太坊链中，当进行 Push 操作时，需要先把 GitFileIndex 上传到 IPFS 中，再把返回的 HASH 值更新到智能合约中。

当进行 Pull 操作时，需要先从智能合约中取得最新的 GitFileIndex HASH 值，再进行内容下载更新。

#### 2.3.HIT与IPFS

目前采用 IPFS 为仓库的存储，按目前的机制，可以采用其他的存储方案如 SIA。在项目进行 PUSH 时，需要把文件先与 GitFileIndex 进行比对，对差异部份进行上传，同时更新 GitFileIndex，最后更新到智能合约中。

#### 2.4.HIT与DAPP

- DAPP 可以监听以太坊上的区块链变化，并取得项目的地址等信息，对于公开项目可以下载整个仓库的代码并进行索引，以及提供相关的服务。
- DAPP 也可以根据 HIT 规范进行代理 HIT 功能，这样可以使得采用原生 GIT 就可以直接访问 HIT 仓库，用户无须改变任何操作习惯。
- DAPP 可以根据监控的仓库变化对项目成员进行奖励(需要配合用于奖励的智能合约)，包括迁移项目、Commit、PR、Fork 等。



