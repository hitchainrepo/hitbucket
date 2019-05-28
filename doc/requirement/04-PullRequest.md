Pull Request Design
=======

### 1.Pull Request Design

Pull Request 设计，如下图所示：

![图片](https://docs.google.com/drawings/d/e/2PACX-1vS-cTe6zRya4skDUlRr5wNwx7mIawrtJUzLFGqHviJG4HVo8bG4o11tO9emVsftAN9ZkNkWgZiaepJH/pub?w=960&h=720)
[EditMe](https://docs.google.com/drawings/d/1aDw-sOe4D_t1gdjRihNd0SzZOEz2-jcK34lmCmSnehA/edit?usp=sharing)

#### 1.1.激活 Pull Request

Hit 仓库拥有者可以激活 Pull Request（默认不开启/没有智能合约），激活步骤：

* 1、创建 PR 智能合约；
* 2、把 PR 智能合约地址写入仓库的智能合约中。

#### 1.2.提交 Pull Request

开发者可以提交 Pull Request 到智能合约中，步骤：

* 1、本地仓库生成 Commit ；
* 2、上传 Commit 到 IPFS 中；
* 3、写入 Commit 地址到 Pull Request 智能合约中。

#### 1.3.迁移仓库及 Pull Request

开发者迁移 Gitee 或 Github 仓库到 Hit 仓库中，步骤：

* 1、本地进行对已有仓库进行 clone 及生成 Hit 仓库；
* 2、激活 Pull Request；
* 3、拉取已有仓库的 Pull Request；
* 4、提交 Pull Request （见1.2）。

### 2.Pull Request 智能合约描述

Pull Request 智能合约描述：

* 1、包含；
* 2、激活 Pull Request；
* 3、拉取已有仓库的 Pull Request；
* 4、提交 Pull Request （见1.2）。

