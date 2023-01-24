/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : starry-blog

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 20/03/2022 20:01:03
*/
CREATE DATABASE IF NOT EXISTS `starry-blog`;
USE `starry-blog`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '作者',
  `category_id` int NULL DEFAULT NULL COMMENT '文章分类',
  `article_cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文章缩略图',
  `article_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '文章类型 1原创 2转载 3翻译',
  `original_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '原文链接',
  `is_top` tinyint(1) NULL DEFAULT NULL COMMENT '是否置顶',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态值 1公开 2私密 3评论可见',
  `create_time` datetime NULL DEFAULT NULL COMMENT '发表时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_article_user`(`user_id` ASC) USING BTREE,
  INDEX `fk_article_category`(`category_id` ASC) USING BTREE,
  FULLTEXT INDEX `fk_title_content`(`article_title`, `article_content`) WITH PARSER `ngram`
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES (14, 1, 1, 'https://view.amogu.cn/images/2020/09/17/20200917003756.jpg', '开放测试 |  这里可以随意评论', '# 开放测试\n## markdown测试\n### 唉难过\n#### 还能说什么呢\n##### 就让悲伤默默埋在心里吧\n###### 就让悲伤藏进梦里吧\n\n---\n2020-12-27\n\n## 图片测试\n![star.png](https://gitee.com/codeprometheus/starry-blog-image/raw/master/articles/2020-12-27/2683c275c28d49db93f5635ff8303047.png)\n图片测试暂时没出现问题\n可以显示\n\n## 代码测试\n```java\n@Autowire\nprivate Sadness sadness;\n```\n代码测试暂时没问题\n\n## 发表测试\n图片还是有问题\n后台能拿到url\n前台显示不出来\n\n---\n\n2020-12-29\n\n没时间了😔\n\n刚才的测试全是BUG！崩溃😩\n\n---\n2020-12-30 0:07\n\nelasticsearch以及ik，kibana在本地是跑起来了\n\n加之redis，rabbitmq还是有很多很多地方要优化和改进\n\n这网站翻来覆去，左看右看，最后只看出了 *全是漏洞*  这几个大字 \n\n\n---\n2020-12-30 13:43\n\n接口明明能正确返回数据，却又是满屏飘红，疯狂报错\n\n这回我是真的怕了，准备上线得了吧\n\n流量估计也没几个，图床暂时就这样吧，暂时只支持邮箱登录，头像上传也只支持png，这个可能对我来说又是一个无解的bug\n\n要上线的话再把安全做好，毕竟服务器开的第一天就给我来了个下马威！我真的是太好欺负了，把不说话的人都当傻子是吧\n\n---\n2020-12-30 23:51\n\n能赶在之前定下的deadline前把我这搞了快半个月却一次又一次搞了个寂寞的项目上线吗，我脸都快被打没了，服务器上搭环境了，我估计nginx部署又是一道送命题\n\n乱七八糟的事太多了，唉😔，现在的我已经习惯性地在前台npm run serve然后把音乐列表选到第33首单曲循环，每年考研结束后图书馆的人都少到恰到好处，可惜可惜我却不得其时啊，唉😔\n\n希望明天顺利，如果老天爷总是反着来的话，希望明天不顺\n\n---\n2020-12-31 17:12\n\n服务器环境已经基本搭建完毕\n\n图片上传可以接受至少对我写博客来说，图片过大不支持！\n\n准备上线\n\n----\n2020-01-01 22:37\n\n终于成功了，终于成功了！！！\n\n感谢这一段时间那些帮助过我的老哥\n\nDocker Yes!\n\n---\n2020-01-03 11:15\n\n唉又出问题，优化也不见效果，暂时关站吧\n\n---\n2020-01-07 15:30\n\n无聊的事终于结束了\n\n从现在起项目全面开始（是不是要起个代号什么的，比如starry计划）\n\n首先尝试着挽救一下第一个失败的前后端不分离的项目，然后starry-blog v1.5正式开始，重中之重的starry正在孵化中，这次我再也不会放弃了，再也不会了\n\n\n---\n天空\n', 1, NULL, 1, 0, 1, '2020-12-27 20:06:05', '2022-01-28 10:00:52');
INSERT INTO `tb_article` VALUES (20, 1, 9, 'https://gitee.com/codeprometheus/starry-blog-image/raw/master/articles/2021-01-07/6f696a1dfdfb402e9f9d00726f35d213.jpg', '今夜  我的狂欢', '   公元2021-01-01 0:13，多年以后,在面对一个又一个对于我来说无解的问题前，准会想起那泛冒起白色泡沫的凯尔特人黑牌的那个遥远的夜晚。\n\n\n   我无法描述现在的我应该是一个怎样的心情，没错，我再一次的失败了，一次又一次的错误在我身上接连不断的复现了一遍又一遍，今晚，不谈技术。\n\n\n  也许是因为这些酒短暂的掩盖了我的难过，也许这过去的一年里太多太多的失望让我已经习惯了这种算是意料之中的结果，最后一天紧接着而来的就是第一天，一段悲惨的结束又是一段崭新的开始，这个多舛的时段，转眼间就要匆匆告别别。\n  \n\n  至今我也想不明白，当初在这个项目技术选型的时候，我是何而来的勇气去选择Vue3，可以说这些拍脑袋想出来的浪费太多太多的时间，我那是大概都忘记了我是来干什么的了，而这又会是一个失败的项目吗，会和第一个项目一样，已经无法继续了吗，我也无法给出答案。在服务器上接连不断的问题涌现，当我卸载MySQL的那一瞬间起，噩梦就开始了。被迫转向Docker，再次被真香定律所圈套。\n\n\n  不得不说的是，技术的更新迭代实在是太快了，就在这一个月来项目的着手设计构建中，Springboot已经来到了2.4.1，踩着年关将至，SpringCloud的I版本也正式GA了，终于终于把Netflix的那套给移除了，全面拥抱Alibaba这肯定是未来趋势；Redis6 GA，这也是该项目迄今最大版本，Redis在所有的中间件里可谓前途无量；不得不提到的，那肯定是九月份，Vue3正式版发布，把this拿掉，取而代之的，是setup()，随之而变化的，那就是各种组件库的适配了，ElementUI还不好说，Element3和ElementPlus都已经对Vue3全面适配了；Ruby3也在不久之前发布，号称是比Ruby2快3倍；除了这些之外，.net5的发布，虽然我还是不太看好c#；Deno 1.0发布，Deno就像是一个游戏大佬，玩的等级越高，越想开一个小号，来避免之前的那些坑；非常值得一提的就是，9月份Huawei的HarmonyOS开源，祝愿中国的开源社区越来越好，能够少一点利益，少一点功利；总之，更新迭代的速度真的是俯仰之间，已为陈迹。\n\n  过去的这一年里，应该是技术基础成长的主要的一年，年初真的不想回忆了，如果没有这场灾难，或许我能走上一条更轻松的路，至少不会像现在这样（~~不是我的错~~，这一切的一切都要从那只蝙蝠说起），从7月确定走上Java以来，8月的Spring全家桶，以及各种中间件的整合，9月份的JVM（虽然看了基本全忘，到现在还能记得多少呢，垃圾回收，内存模型），10月的多线程高并发（应用几乎没有，只有输入没有输出，又犯了大忌），以及区块链比特币的相关（V神的成神之路），11月的数据结构，然后就开始了项目这条血路。进展的是实在的太慢了，心里的焦虑感总是随着时间的流逝而成倍的增加。现在的样子远远远远不是我想要的，想要跑的更快，又担心脚踩的不踏实。\n\n   而形成鲜明对的，对我的性格来说，很难把时间全花在一件东西之上，总是忍不住搞各种领域下的小demo，比如网安，比如大数据，比如尝试各种语言的特性（除了宇宙第一的PHP），像当下火热的Go，像Rust，Ruby，Scala，再比如一些更为小众的语言，比如说perl，haskell，dlang等等，对于Web开发来说，Python的Django，Ruby的Rails，Dart的Flutter等等，虽然基本上都是一些流于hello world级别的尝试，但至今我仍清楚的记得，Rails成功的那个页面，一群小孩手牵着手，站在地球之上，正如Ruby的设计者，Yukihiro Matsumoto所说\n> 减少编程时候的不必要的琐碎时间，令编写程序的人高兴，是设计Ruby语言的一个首要的考虑；其次是良好的界面设计。强调系统设计必须强调人性化，而不是一味从机器的角度设想。\n\n   Ruby的动态特性令人惊异，让人兴奋，在我刚入门的时候，语言只是工具的说法常常见到，的确，在我现在的角度来看，编程语言，可以往小了说，只是个工具，是我们与底层交互的工具，是我们构建上层应用的工具；而往大了说，语言可以是一种意识形态，可以是一种展示个性，表达想法的平台，当然，在内卷十分严重的当下，谁会去考虑这些呢。\n\n本以为在最初的想法得以实现后，我面对的环境能有所改善，然而事实并非这样，不过无所谓了，不痛不痒，毕竟是新年的依始，难免落入感慨一番然后在祝愿今后的俗套，惟愿时间能再快些，拿到这可怜的毕业证之后我也立马滚蛋；惟愿今后一定一定要扎扎实实学好技术，做好记录与总结，既然东搞西搞改不了，那就顺其自然也罢，我也常常想，下一个风口是哪，区块链？AI？云原生？虚拟化容器化？\n\n也许吧，正如马尔克斯说的那样，过去都是假的，回忆是一条没有归途的路，以往的一切春天都无法复原，即使最狂热最坚贞的爱情，归根结底也不过是一种瞬息即逝的现实，挡在前面的，会是IDEA满屏飘红的疯狂报错，会是现实中虚拟中处处碰壁之后的沉默，抑或是跌倒后再教育后的成长呢。从当下开始，真的要全面面向数据结构与算法了，全面面向各路各类型的项目，全面面向简历实习了\n\n', 1, NULL, 0, 0, 1, '2021-01-01 01:01:08', '2022-01-27 23:31:31');
INSERT INTO `tb_article` VALUES (21, 1, 9, 'https://gitee.com/codeprometheus/starry-blog-image/raw/master/articles/2021-01-07/faaef5c195994097bdfb0bb3169e8fce.png', '关于比特币以及区块链的一点思考', '元旦依始，到现在的一个星期里，我想，这足以让币圈疯狂到一塌糊涂\n\n正如封面这张图显示的一样，1月2号，比特币再破万元大关，正式站在了3万美元的台阶，令全世界瞩目，随后，增长之势丝毫不见其减，1月3号，比特币持续走强，最高已经快够到了3万5美元，1月4日，大瀑布来了，下午3点，开始暴跌，3小时内跌幅超过5000美元，涨跌到了15%，为历史首次，再次刷新记录，邻近晚上7-8点，回涨到3万附近，从5号到7号，开始报复性反弹，一次又一次突破，这势头已经到了万夫莫开的境地了，就在7号上午，突破3.7万美元，再次创下历史新高。\n\n暴跌下的BTC![暴跌下的BTC](https://gitee.com/codeprometheus/starry-blog-image/raw/master/articles/2021-01-07/9790999ea63a409aad7aca457651ac45.png)\n\n比特币，最初诞生在世界经济危机的背景之下，如996/ICU的匿名作者身份一样，比特币的创造者中本聪的庐山真面目至今也不得而知，让我感到好奇的是，比特币如此精妙的设计体系，真的是中本聪一个人单枪匹马打造的吗？\n\n而伴随着比特币的出现，其背后的实现技术区块链也在近几年里大火。谈起区块链，就不得不说去中心化，这个概念可以说经常被各种媒体大捧特捧，吹的沸沸扬扬，到底什么是去中心化呢，去中心化相比较于中心化，又有什么特别的地方呢？\n\n在我看来，所谓去中心化，并不是说不要中心，而是中心多元化，任何沟通交流与往来，都在你我之间进行即可，不再需要第三方的加入，去中心化让每个人都有机会成为中心。就比方说日常的交易，我们常用的支付宝，就相当于一个第三方的存在，我们把钱通过支付宝这个机构转发给商家，而如果是比特币，那么交易就由区块链来实现，一旦交易信息确认，由内部的数据结构实现不可篡改的机制，这和Git一样，都使用了默克尔树。\n\n但是，去中心化很多时候都被大大滥用，一个重要的话题就是支付方式，现实生活中，也就是在中心化的世界里，我们有良好的如政府，社会和法律的监管体系，而在区块链中，许多的保护功能都没有了，比如若是本地的私钥丢失，已有的比特币那就再也找不到了，而且区块链当中的交易也是无法撤销的。中心化的监管并不是一件坏事，从这点来看，既然现有的支付方式，如现金，银行卡，支付宝等等，这些已经是解决的非常好的领域，再去引入比特币这样的加密货币来支付，意义又从何说起？加密货币本就不应该用来与已有的支付方式做竞争。\n\n此外，中心化和去中心化并不是黑白分明的，并不是中心化里就不能有去中心化，不管是去不去中心化，最终是要服务于老百姓的。中心化和去中心化各有利弊，或许，去中心化并没有我们想象的那么完美，中心化也并没有充满了束缚', 1, NULL, 0, 0, 1, '2021-01-07 16:55:04', '2022-01-27 23:31:19');
INSERT INTO `tb_article` VALUES (29, 1, 10, 'https://unsplash.it/600/400?random=3', '2021-08-22 test1', 'test1', 1, '', 0, 0, 1, '2021-08-22 16:31:17', '2021-12-13 10:54:23');
INSERT INTO `tb_article` VALUES (30, 1, 10, 'https://unsplash.it/600/400?random=2', '2021-08-22', 'mm', 1, '', 1, 0, 1, '2021-08-22 16:54:08', '2022-01-27 23:31:11');

-- ----------------------------
-- Table structure for tb_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `tb_article_tag`;
CREATE TABLE `tb_article_tag`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `article_id` int NOT NULL COMMENT '文章id',
  `tag_id` int NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_article_tag_1`(`article_id` ASC) USING BTREE,
  INDEX `fk_article_tag_2`(`tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 135 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_article_tag
-- ----------------------------
INSERT INTO `tb_article_tag` VALUES (71, 1, 1);
INSERT INTO `tb_article_tag` VALUES (117, 24, 12);
INSERT INTO `tb_article_tag` VALUES (118, 25, 12);
INSERT INTO `tb_article_tag` VALUES (125, 29, 12);
INSERT INTO `tb_article_tag` VALUES (131, 14, 1);
INSERT INTO `tb_article_tag` VALUES (132, 30, 12);
INSERT INTO `tb_article_tag` VALUES (133, 21, 11);
INSERT INTO `tb_article_tag` VALUES (134, 20, 11);

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_category
-- ----------------------------
INSERT INTO `tb_category` VALUES (1, '测试', '2020-12-27 23:23:19', NULL);
INSERT INTO `tb_category` VALUES (9, '思考', '2021-01-07 15:58:28', NULL);
INSERT INTO `tb_category` VALUES (10, '一发', '2021-01-07 15:58:28', NULL);

-- ----------------------------
-- Table structure for tb_chat_record
-- ----------------------------
DROP TABLE IF EXISTS `tb_chat_record`;
CREATE TABLE `tb_chat_record`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '聊天内容',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `type` tinyint NULL DEFAULT NULL COMMENT '类型',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_chat_record
-- ----------------------------
INSERT INTO `tb_chat_record` VALUES (4, NULL, '未知IP', NULL, 'ls', '未知IP', '内网IP', 3, '2021-03-29 19:42:25', NULL);
INSERT INTO `tb_chat_record` VALUES (5, NULL, '未知IP', NULL, 'ne', '未知IP', '内网IP', 3, '2021-03-29 19:42:33', NULL);
INSERT INTO `tb_chat_record` VALUES (6, NULL, '未知IP', NULL, 'ls', '未知IP', '内网IP', 3, '2021-03-29 19:42:38', NULL);
INSERT INTO `tb_chat_record` VALUES (7, NULL, '未知IP', NULL, '<img style=\'vertical-align: middle\' src= \'//i0.hdslb.com/bfs/emote/431432c43da3ee5aab5b0e4f8931953e649e9975.png@112w_112h.webp\' width=\'22\'height=\'20\' style=\'padding: 0 1px\'/>', '未知IP', '内网IP', 3, '2021-03-29 19:42:42', NULL);
INSERT INTO `tb_chat_record` VALUES (9, NULL, '未知IP', 'https://gravatar.loli.net/avatar/d41d8cd98f00b204e9800998ecf8427e?d=mp&v=1.4.14', 'ls', '未知IP', '内网IP', 3, '2021-03-29 19:45:07', NULL);
INSERT INTO `tb_chat_record` VALUES (10, NULL, '未知IP', 'https://gravatar.loli.net/avatar/d41d8cd98f00b204e9800998ecf8427e?d=mp&v=1.4.14', '<img style=\'vertical-align: middle\' src= \'//i0.hdslb.com/bfs/emote/431432c43da3ee5aab5b0e4f8931953e649e9975.png@112w_112h.webp\' width=\'22\'height=\'20\' style=\'padding: 0 1px\'/>', '未知IP', '内网IP', 3, '2021-03-29 19:51:02', NULL);
INSERT INTO `tb_chat_record` VALUES (13, NULL, '未知IP', 'https://gravatar.loli.net/avatar/d41d8cd98f00b204e9800998ecf8427e?d=mp&v=1.4.14', 'ls', '未知IP', '内网IP', 3, '2021-03-29 20:15:31', NULL);
INSERT INTO `tb_chat_record` VALUES (14, NULL, '未知IP', 'https://gravatar.loli.net/avatar/d41d8cd98f00b204e9800998ecf8427e?d=mp&v=1.4.14', 'kk', '未知IP', '内网IP', 3, '2021-03-29 22:20:36', NULL);
INSERT INTO `tb_chat_record` VALUES (15, NULL, '未知IP', 'https://gravatar.loli.net/avatar/d41d8cd98f00b204e9800998ecf8427e?d=mp&v=1.4.14', 'ls', '未知IP', '内网IP', 3, '2021-03-29 22:27:15', NULL);
INSERT INTO `tb_chat_record` VALUES (16, 21, 'test2413', 'https://gitee.com/codeprometheus/starry-blog-image/raw/master/avatar/2020-12-29/785c600a76d04d5895b51b93980587b2.png', 'test', '未知IP', '内网IP', 3, '2021-03-29 22:28:13', NULL);
INSERT INTO `tb_chat_record` VALUES (17, NULL, '未知IP', 'https://gravatar.loli.net/avatar/d41d8cd98f00b204e9800998ecf8427e?d=mp&v=1.4.14', 'test', '未知IP', '内网IP', 3, '2021-03-29 22:35:14', NULL);
INSERT INTO `tb_chat_record` VALUES (18, NULL, '未知IP', 'https://gravatar.loli.net/avatar/d41d8cd98f00b204e9800998ecf8427e?d=mp&v=1.4.14', 'test', '未知IP', '内网IP', 3, '2021-03-29 22:35:18', NULL);

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '评论用户Id',
  `article_id` int NULL DEFAULT NULL COMMENT '评论文章id',
  `moment_id` int NULL DEFAULT NULL COMMENT '评论动态id',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `reply_user_id` int NULL DEFAULT NULL COMMENT '回复用户id',
  `parent_id` int NULL DEFAULT NULL COMMENT '父评论id',
  `type` tinyint NOT NULL COMMENT '评论类型 1.文章 2.友链 3.动态',
  `is_delete` tinyint NULL DEFAULT 0 COMMENT '是否删除',
  `is_review` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否审核',
  `create_time` datetime NOT NULL COMMENT '评论时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_comment_user`(`user_id` ASC) USING BTREE,
  INDEX `fk_comment_article`(`article_id` ASC) USING BTREE,
  INDEX `fk_comment_parent`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 128 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_comment
-- ----------------------------
INSERT INTO `tb_comment` VALUES (22, 1, 14, NULL, '<img src= \'//i0.hdslb.com/bfs/emote/2caafee2e5db4db72104650d87810cc2c123fc86.png@112w_112h.webp\' width=\'22\'height=\'20\' style=\'padding: 0 1px\'/>烦死', NULL, NULL, 1, 0, 1, '2020-12-29 23:18:59', NULL);
INSERT INTO `tb_comment` VALUES (23, 1, 14, NULL, '博主', NULL, NULL, 1, 1, 1, '2020-12-29 23:19:15', NULL);
INSERT INTO `tb_comment` VALUES (24, 21, 14, NULL, '真的全是bug，这还敢上线吗', NULL, NULL, 1, 0, 1, '2020-12-29 23:22:10', NULL);
INSERT INTO `tb_comment` VALUES (25, 22, 14, NULL, '头像居然只能上传png', NULL, NULL, 1, 0, 1, '2020-12-29 23:54:12', NULL);
INSERT INTO `tb_comment` VALUES (26, 22, 14, NULL, '嵬嵬逸夫楼，一跃解千愁', NULL, NULL, 1, 0, 1, '2020-12-29 23:54:55', NULL);
INSERT INTO `tb_comment` VALUES (27, 21, 14, NULL, '利物浦万岁！！！<img src= \'//i0.hdslb.com/bfs/emote/1a67265993913f4c35d15a6028a30724e83e7d35.png@112w_112h.webp\' width=\'22\'height=\'20\' style=\'padding: 0 1px\'/><img src= \'//i0.hdslb.com/bfs/emote/1a67265993913f4c35d15a6028a30724e83e7d35.png@112w_112h.webp\' width=\'22\'height=\'20\' style=\'padding: 0 1px\'/>', NULL, NULL, 1, 0, 1, '2020-12-30 00:00:51', NULL);
INSERT INTO `tb_comment` VALUES (28, 21, 14, NULL, '伟大的意大利的左后卫！他继承了意大利的光荣的传统！法切蒂、卡布里尼、马尔蒂尼在这一刻灵魂附体！格罗索一个人！他代表了意大利足球悠久的历史和传统！在这一刻，他不是一个人在战斗！他不是一个人！！！！', NULL, NULL, 1, 0, 1, '2020-12-30 00:03:11', NULL);
INSERT INTO `tb_comment` VALUES (29, 21, 14, NULL, '立功了！绝杀！！绝对的绝杀！！！绝对的死角！！！！', 21, 28, 1, 0, 1, '2020-12-30 00:04:37', NULL);
INSERT INTO `tb_comment` VALUES (30, 1, 14, NULL, '我们是冠军！！！<img src= \'//i0.hdslb.com/bfs/emote/4683fd9ffc925fa6423110979d7dcac5eda297f4.png@112w_112h.webp\' width=\'22\'height=\'20\' style=\'padding: 0 1px\'/><img src= \'//i0.hdslb.com/bfs/emote/4683fd9ffc925fa6423110979d7dcac5eda297f4.png@112w_112h.webp\' width=\'22\'height=\'20\' style=\'padding: 0 1px\'/>', 21, 27, 1, 0, 1, '2020-12-30 13:53:36', NULL);
INSERT INTO `tb_comment` VALUES (31, 1, 14, NULL, '确实太多需要完善的了......', 21, 24, 1, 0, 1, '2020-12-30 13:55:29', NULL);
INSERT INTO `tb_comment` VALUES (32, 1, 14, NULL, '现在的巴萨已经是惨不忍睹了<img src= \'//i0.hdslb.com/bfs/emote/c5c6d6982e1e53e478daae554b239f2b227b172b.png@112w_112h.webp\' width=\'22\'height=\'20\' style=\'padding: 0 1px\'/>，再这样踢下去，谁还看', NULL, NULL, 1, 0, 1, '2020-12-31 00:07:12', NULL);
INSERT INTO `tb_comment` VALUES (33, 1, 14, NULL, 'Docker 部署测试', NULL, NULL, 1, 0, 1, '2021-01-01 22:16:24', NULL);
INSERT INTO `tb_comment` VALUES (34, 1, 14, NULL, '好久没来了哈哈<img src= \'//i0.hdslb.com/bfs/emote/cb321684ed5ce6eacdc2699092ab8fe7679e4fda.png@112w_112h.webp\' width=\'22\'height=\'20\' style=\'padding: 0 1px\'/>', NULL, NULL, 1, 0, 1, '2021-02-21 16:50:53', NULL);
INSERT INTO `tb_comment` VALUES (35, 21, 14, NULL, 'qq', NULL, NULL, 1, 0, 1, '2021-06-04 10:49:23', NULL);
INSERT INTO `tb_comment` VALUES (36, 22, 14, NULL, '163', 21, 35, 1, 0, 1, '2021-06-04 11:07:32', NULL);
INSERT INTO `tb_comment` VALUES (37, 1, 14, NULL, '爱你到最后不痛不痒', NULL, NULL, 1, 0, 1, '2021-08-21 22:50:17', '2021-12-09 16:24:15');
INSERT INTO `tb_comment` VALUES (40, 1, 14, NULL, '爱你到最后不痛不痒', NULL, NULL, 1, 0, 1, '2021-08-28 23:25:29', NULL);
INSERT INTO `tb_comment` VALUES (41, 1, 14, NULL, '我爱你刘诗雯', NULL, NULL, 1, 0, 1, '2021-08-28 23:40:38', '2021-12-09 16:23:26');
INSERT INTO `tb_comment` VALUES (42, 1, 14, NULL, 'kkk', NULL, NULL, 1, 0, 1, '2021-08-28 23:56:19', '2021-12-09 16:23:26');
INSERT INTO `tb_comment` VALUES (43, 1, 14, NULL, 'ss', NULL, NULL, 1, 0, 1, '2021-08-28 23:57:46', '2021-12-09 16:23:25');
INSERT INTO `tb_comment` VALUES (45, 1, 14, NULL, '所以人们都拿起咖啡', NULL, NULL, 1, 0, 1, '2021-08-29 11:11:45', '2021-12-09 16:23:24');
INSERT INTO `tb_comment` VALUES (46, 1, 29, NULL, '如果有人在顶塔', NULL, NULL, 1, 0, 1, '2021-08-29 11:17:35', '2021-12-09 16:23:24');
INSERT INTO `tb_comment` VALUES (47, 1, 29, NULL, '该配合你演出的我尽力在表演', NULL, NULL, 1, 0, 1, '2021-08-29 11:25:37', '2021-12-09 16:23:24');
INSERT INTO `tb_comment` VALUES (48, 1, 29, NULL, '不再进化，动物世界里都太傻', NULL, NULL, 1, 0, 1, '2021-08-29 11:29:19', '2021-12-09 16:23:23');
INSERT INTO `tb_comment` VALUES (49, 1, 30, NULL, '无趣的画面被遗忘', NULL, NULL, 1, 0, 1, '2021-08-29 11:32:43', '2021-12-09 16:23:23');
INSERT INTO `tb_comment` VALUES (50, 1, 30, NULL, '我举止要限量，你不会看见我的抵抗', NULL, NULL, 1, 0, 1, '2021-08-29 11:37:22', '2021-12-09 16:22:57');
INSERT INTO `tb_comment` VALUES (51, 1, 30, NULL, '你退半步的动作认真吗', NULL, NULL, 1, 0, 1, '2021-08-29 11:46:37', '2021-12-09 16:22:56');
INSERT INTO `tb_comment` VALUES (83, 1, 30, NULL, '说不清他比我合适', NULL, NULL, 1, 0, 1, '2021-12-09 16:45:14', '2021-12-09 16:45:45');
INSERT INTO `tb_comment` VALUES (84, 1, 30, NULL, '最好不要记得我', NULL, NULL, 1, 0, 1, '2021-12-09 16:47:25', '2021-12-09 16:48:25');
INSERT INTO `tb_comment` VALUES (85, 1, 30, NULL, '最好***都已经送你不要', NULL, NULL, 1, 0, 1, '2021-12-09 16:49:33', '2021-12-09 16:49:54');
INSERT INTO `tb_comment` VALUES (86, 1, 30, NULL, '反正你不要了最好', NULL, NULL, 1, 0, 1, '2021-12-09 16:53:42', '2021-12-09 16:54:43');
INSERT INTO `tb_comment` VALUES (113, 1, NULL, 4, '将我们温柔的覆盖', NULL, NULL, 3, 0, 1, '2022-02-19 19:56:34', '2022-02-19 19:56:41');
INSERT INTO `tb_comment` VALUES (114, 1, NULL, 4, '你停在了这条我们熟悉的街', NULL, NULL, 3, 0, 1, '2022-02-19 19:57:04', '2022-02-19 19:57:15');
INSERT INTO `tb_comment` VALUES (115, 1, NULL, 4, '111', 1, 114, 3, 0, 1, '2022-02-19 19:57:46', '2022-02-19 20:00:25');
INSERT INTO `tb_comment` VALUES (116, 1, NULL, 4, '222', 1, 113, 3, 0, 1, '2022-02-19 19:57:52', '2022-02-19 20:00:25');
INSERT INTO `tb_comment` VALUES (117, 1, NULL, 4, '333', 1, 114, 3, 0, 1, '2022-02-19 19:58:25', '2022-02-19 20:00:25');
INSERT INTO `tb_comment` VALUES (118, 1, NULL, 4, '444', 1, 113, 3, 0, 1, '2022-02-19 19:58:32', '2022-02-19 20:00:25');
INSERT INTO `tb_comment` VALUES (119, 1, NULL, 3, '好久没见了什么角色呢', NULL, NULL, 3, 0, 1, '2022-02-19 19:59:18', '2022-02-19 20:00:25');
INSERT INTO `tb_comment` VALUES (120, 1, NULL, 3, '细心装扮着', NULL, NULL, 3, 0, 1, '2022-02-19 19:59:36', '2022-02-19 20:00:25');
INSERT INTO `tb_comment` VALUES (121, 1, NULL, 3, '555', 1, 120, 3, 0, 1, '2022-02-19 20:00:00', '2022-02-19 20:00:25');
INSERT INTO `tb_comment` VALUES (122, 1, NULL, 3, '666', 1, 120, 3, 0, 1, '2022-02-19 20:00:04', '2022-02-19 20:00:25');
INSERT INTO `tb_comment` VALUES (123, 1, NULL, 3, '777', 1, 119, 3, 0, 1, '2022-02-19 20:00:08', '2022-02-19 20:00:25');
INSERT INTO `tb_comment` VALUES (124, 1, NULL, 3, '888', 1, 119, 3, 0, 1, '2022-02-19 20:00:12', '2022-02-19 20:00:25');
INSERT INTO `tb_comment` VALUES (125, 1, NULL, 4, '1313', 1, 114, 3, 0, 1, '2022-02-19 20:12:17', '2022-02-19 20:12:31');
INSERT INTO `tb_comment` VALUES (126, 1, NULL, 4, 'lmlm', 1, 114, 3, 0, 1, '2022-02-19 20:14:03', '2022-02-19 20:14:14');
INSERT INTO `tb_comment` VALUES (127, 1, NULL, 4, 'dffff', 1, 114, 3, 0, 1, '2022-02-19 20:21:35', NULL);

-- ----------------------------
-- Table structure for tb_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `tb_friend_link`;
CREATE TABLE `tb_friend_link`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `link_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '链接名',
  `link_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '链接头像',
  `link_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '链接地址',
  `link_intro` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '链接介绍',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_friend_link_user`(`link_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_friend_link
-- ----------------------------

-- ----------------------------
-- Table structure for tb_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单名',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单路径',
  `component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单icon',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `order_num` tinyint NULL DEFAULT NULL COMMENT '排序',
  `parent_id` int NULL DEFAULT NULL COMMENT '父id',
  `is_hidden` tinyint(1) NULL DEFAULT NULL COMMENT '是否隐藏  0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 220 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_menu
-- ----------------------------
INSERT INTO `tb_menu` VALUES (1, '首页', '/', '/home/Home.vue', 'el-icon-myshouye', '2021-01-26 17:06:51', '2021-08-20 23:19:57', 1, NULL, 0);
INSERT INTO `tb_menu` VALUES (2, '文章管理', '/article-submenu', 'Layout', 'el-icon-mywenzhang-copy', '2021-01-25 20:43:07', '2021-08-20 23:19:58', 2, NULL, 0);
INSERT INTO `tb_menu` VALUES (3, '消息管理', '/message-submenu', 'Layout', 'el-icon-myxiaoxi', '2021-01-25 20:44:17', '2021-01-25 20:44:20', 3, NULL, 0);
INSERT INTO `tb_menu` VALUES (4, '系统管理', '/system-submenu', 'Layout', 'el-icon-myshezhi', '2021-01-25 20:45:57', '2021-01-25 20:45:59', 5, NULL, 0);
INSERT INTO `tb_menu` VALUES (5, '个人中心', '/setting', '/setting/Setting.vue', 'el-icon-myuser', '2021-01-26 17:22:38', '2022-01-13 00:47:22', 8, NULL, 0);
INSERT INTO `tb_menu` VALUES (6, '发布文章', '/articles', '/article/Article.vue', 'el-icon-myfabiaowenzhang', '2021-01-26 14:30:48', '2021-08-20 23:42:39', 1, 2, 0);
INSERT INTO `tb_menu` VALUES (7, '修改文章', '/articles/*', '/article/Article.vue', 'el-icon-myfabiaowenzhang', '2021-01-26 14:31:32', '2021-12-12 17:36:21', 2, 2, 1);
INSERT INTO `tb_menu` VALUES (8, '文章列表', '/articles-list', '/article/ArticleList.vue', 'el-icon-mywenzhangliebiao', '2021-01-26 14:32:13', '2022-01-13 00:53:03', 3, 2, 0);
INSERT INTO `tb_menu` VALUES (9, '分类管理', '/categories', '/category/Category.vue', 'el-icon-myfenlei', '2021-01-26 14:33:42', '2021-01-26 14:33:43', 4, 2, 0);
INSERT INTO `tb_menu` VALUES (10, '标签管理', '/tags', '/tag/Tag.vue', 'el-icon-myicontag', '2021-01-26 14:34:33', '2021-01-26 14:34:36', 5, 2, 0);
INSERT INTO `tb_menu` VALUES (11, '评论管理', '/comments', '/comment/Comment.vue', 'el-icon-mypinglunzu', '2021-01-26 14:35:31', '2021-01-26 14:35:34', 1, 3, 0);
INSERT INTO `tb_menu` VALUES (12, '留言管理', '/messages', '/message/Message.vue', 'el-icon-myliuyan', '2021-01-26 14:36:09', '2021-01-26 14:36:13', 2, 3, 0);
INSERT INTO `tb_menu` VALUES (13, '用户列表', '/users', '/user/User.vue', 'el-icon-myyonghuliebiao', '2021-01-26 14:38:09', '2021-01-26 14:38:12', 1, 202, 0);
INSERT INTO `tb_menu` VALUES (14, '角色管理', '/roles', '/role/Role.vue', 'el-icon-myjiaoseliebiao', '2021-01-26 14:39:01', '2021-01-26 14:39:03', 2, 213, 0);
INSERT INTO `tb_menu` VALUES (15, '接口管理', '/resources', '/resource/Resource.vue', 'el-icon-myjiekouguanli', '2021-01-26 14:40:14', '2021-08-07 20:00:28', 2, 213, 0);
INSERT INTO `tb_menu` VALUES (16, '菜单管理', '/menus', '/menu/Menu.vue', 'el-icon-mycaidan', '2021-01-26 14:40:54', '2021-08-07 10:18:49', 2, 213, 0);
INSERT INTO `tb_menu` VALUES (17, '友链管理', '/links', '/friendLink/FriendLink.vue', 'el-icon-mydashujukeshihuaico-', '2021-01-26 14:41:35', '2021-01-26 14:41:37', 3, 4, 0);
INSERT INTO `tb_menu` VALUES (18, '关于我', '/about', '/about/About.vue', 'el-icon-myguanyuwo', '2021-01-26 14:42:05', '2021-01-26 14:42:10', 4, 4, 0);
INSERT INTO `tb_menu` VALUES (19, '日志管理', '/log-submenu', 'Layout', 'el-icon-myguanyuwo', '2021-01-31 21:33:56', '2021-01-31 21:33:59', 6, NULL, 0);
INSERT INTO `tb_menu` VALUES (20, '操作日志', '/operation/log', '/operationlog/OperationLog.vue', 'el-icon-myguanyuwo', '2021-01-31 15:53:21', '2021-12-12 15:27:48', 1, 19, 0);
INSERT INTO `tb_menu` VALUES (201, '在线用户', '/online/users', '/user/Online.vue', 'el-icon-myyonghuliebiao', '2021-02-05 14:59:51', '2021-02-05 14:59:53', 7, 202, 0);
INSERT INTO `tb_menu` VALUES (202, '用户管理', '/users-submenu', 'Layout', 'el-icon-myyonghuliebiao', '2021-02-06 23:44:59', '2021-02-06 23:45:03', 4, NULL, 0);
INSERT INTO `tb_menu` VALUES (209, '页面管理', '/pages', '/page/Page.vue', 'el-icon-myyemianpeizhi', '2021-08-04 11:36:27', '2021-08-07 20:01:26', 2, 4, 0);
INSERT INTO `tb_menu` VALUES (213, '权限管理', '/permission-submenu', 'Layout', 'el-icon-mydaohanglantubiao_quanxianguanli', '2021-08-07 19:56:55', '2021-08-20 23:42:52', 4, NULL, 0);
INSERT INTO `tb_menu` VALUES (214, '网站管理', '/website', '/website/Website.vue', 'el-icon-myxitong', '2021-08-07 20:06:41', NULL, 1, 4, 0);
INSERT INTO `tb_menu` VALUES (215, '访问日志', '/visitlog/VisitLog', '/visitlog/VisitLog.vue', 'el-icon-myyonghuliebiao', '2022-01-11 00:11:38', '2022-01-11 20:27:35', 2, 19, 0);
INSERT INTO `tb_menu` VALUES (216, '动态管理', '/moment-submenu', 'Layout', 'el-icon-myyonghuliebiao', '2022-01-13 00:47:14', '2022-01-13 00:47:37', 7, NULL, 0);
INSERT INTO `tb_menu` VALUES (217, '发布动态', '/moments', '/moment/Moment.vue', 'el-icon-myliuyan', '2022-01-13 00:48:32', '2022-01-13 00:51:00', 1, 216, 0);
INSERT INTO `tb_menu` VALUES (218, '修改动态', '/moments/*', '/moment/Moment.vue', 'el-icon-myfabiaowenzhang', '2022-01-13 00:51:41', '2022-01-13 00:54:09', 3, 216, 1);
INSERT INTO `tb_menu` VALUES (219, '动态列表', '/moments-list', '/moment/MomentList.vue', 'el-icon-myxiaoxi', '2022-01-13 00:52:35', '2022-01-13 00:53:39', 1, 216, 0);

-- ----------------------------
-- Table structure for tb_message
-- ----------------------------
DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  `message_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '留言内容',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ip',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户地址',
  `time` tinyint(1) NOT NULL COMMENT '弹幕速度',
  `is_review` tinyint NOT NULL DEFAULT 1 COMMENT '是否审核',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_message
-- ----------------------------
INSERT INTO `tb_message` VALUES (48, '游客', 'https://gravatar.loli.net/avatar/d41d8cd98f00b204e9800998ecf8427e?d=mp&v=1.4.14', 'test', '127.0.0.1', '内网IP', 11, 1, '2020-12-29 15:42:59', NULL);

-- ----------------------------
-- Table structure for tb_moment
-- ----------------------------
DROP TABLE IF EXISTS `tb_moment`;
CREATE TABLE `tb_moment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '用户',
  `moment_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '动态内容',
  `images` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `is_top` tinyint(1) NULL DEFAULT 0 COMMENT '是否置顶',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态值 1公开 2私密',
  `create_time` datetime NULL DEFAULT NULL COMMENT '发表时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_moment_content`(`moment_content` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_moment
-- ----------------------------
INSERT INTO `tb_moment` VALUES (3, 1, 'www', '', 44, 0, 0, 1, '2022-01-13 11:12:17', '2022-01-13 13:46:50');
INSERT INTO `tb_moment` VALUES (4, 1, 'lll', '', 23, 1, 0, 1, '2022-01-13 11:12:39', '2022-01-13 13:45:10');
INSERT INTO `tb_moment` VALUES (5, 1, 'love you', '', 88, 1, 0, 0, '2022-01-13 14:30:55', NULL);

-- ----------------------------
-- Table structure for tb_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_operation_log`;
CREATE TABLE `tb_operation_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `opt_module` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作模块',
  `opt_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作url',
  `opt_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作方法',
  `opt_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作描述',
  `request_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '请求参数',
  `request_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方式',
  `response_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '返回数据',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `ip_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作ip',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作地址',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1852 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for tb_page
-- ----------------------------
DROP TABLE IF EXISTS `tb_page`;
CREATE TABLE `tb_page`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '页面id',
  `page_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '页面名',
  `page_label` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '页面标签',
  `page_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '页面封面',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 905 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '页面' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_page
-- ----------------------------
INSERT INTO `tb_page` VALUES (1, '首页', 'home', 'https://cdn.pixabay.com/photo/2011/12/15/11/29/orion-nebula-11185_960_720.jpg', '2021-08-07 10:32:36', NULL);
INSERT INTO `tb_page` VALUES (2, '归档', 'archive', 'https://rmt.dogedoge.com/fetch/tzk/storage/20200815214126.jpg?fmt=webp', '2021-08-07 10:32:36', NULL);
INSERT INTO `tb_page` VALUES (3, '分类', 'category', 'https://cdn.jsdelivr.net/gh/volantis-x/cdn-wallpaper-minimalist/2020/052.jpg', '2021-08-07 10:32:36', NULL);
INSERT INTO `tb_page` VALUES (4, '标签', 'tag', 'https://cdn.jsdelivr.net/gh/volantis-x/cdn-wallpaper-minimalist/2020/046.jpg', '2021-08-07 10:32:36', '2021-08-07 11:21:54');
INSERT INTO `tb_page` VALUES (5, '相册', 'album', 'https://view.amogu.cn/images/2020/09/17/20200917003756.jpg', '2021-08-07 10:32:36', NULL);
INSERT INTO `tb_page` VALUES (6, '友链', 'link', 'https://tva1.sinaimg.cn/large/832afe33ly1gbi1rf2bppj21hc0jgwmw.jpg', '2021-08-07 10:32:36', NULL);
INSERT INTO `tb_page` VALUES (7, '关于', 'about', 'https://cdn.jsdelivr.net/gh/first19326/hexo-liveforcode@master/static/image/header/home.jpg', '2021-08-07 10:32:36', NULL);
INSERT INTO `tb_page` VALUES (8, '留言', 'message', 'https://view.amogu.cn/images/2020/09/17/20200917003756.jpg', '2021-08-07 10:32:36', NULL);
INSERT INTO `tb_page` VALUES (9, '个人中心', 'user', 'https://cdn.jsdelivr.net/gh/zyoushuo/Blog/images/BG_10.jpg', '2021-08-07 10:32:36', NULL);
INSERT INTO `tb_page` VALUES (902, '文章列表', 'articleList', 'https://cdn.jsdelivr.net/gh/gudaonanfeng/Hexo/Pic/backord123.jpg', '2021-08-10 15:36:19', NULL);
INSERT INTO `tb_page` VALUES (904, '动态', 'moment', 'https://gitee.com/codeprometheus/starry-blog-image/raw/master/page/2022-01-16/dbb547d5070744ee96dd4aea70736b0c.jpeg', '2022-01-16 21:44:43', NULL);

-- ----------------------------
-- Table structure for tb_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource`;
CREATE TABLE `tb_resource`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '资源名',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限路径',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方式',
  `parent_id` int NULL DEFAULT NULL COMMENT '父权限id',
  `is_anonymous` tinyint NULL DEFAULT NULL COMMENT '是否匿名访问 0否 1是',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 286 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
INSERT INTO `tb_resource` VALUES (165, '分类模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (166, '博客信息模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (167, '友链模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (168, '文章模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (169, '日志模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (170, '标签模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (172, '用户信息模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (173, '用户账号模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (174, '留言模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (176, '菜单模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (177, '角色模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (178, '评论模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (179, '资源模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (180, '页面模块', NULL, NULL, NULL, 0, '2021-08-11 21:04:21', NULL);
INSERT INTO `tb_resource` VALUES (181, '查看博客信息', '/', 'GET', 166, 1, '2021-08-11 21:04:22', '2021-08-11 21:05:29');
INSERT INTO `tb_resource` VALUES (182, '查看关于我信息', '/about', 'GET', 166, 1, '2021-08-11 21:04:22', '2021-08-11 21:05:29');
INSERT INTO `tb_resource` VALUES (183, '查看后台信息', '/admin', 'GET', 166, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (184, '修改关于我信息', '/admin/about', 'PUT', 166, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (185, '查看后台文章', '/admin/articles', 'GET', 168, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (186, '添加或修改文章', '/admin/articles', 'POST', 168, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (187, '恢复或删除文章', '/admin/articles', 'PUT', 168, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (188, '物理删除文章', '/admin/articles', 'DELETE', 168, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (189, '上传文章图片', '/admin/articles/images', 'POST', 168, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (190, '修改文章置顶', '/admin/articles/top', 'PUT', 168, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (191, '根据id查看后台文章', '/admin/articles/*', 'GET', 168, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (192, '查看后台分类列表', '/admin/categories', 'GET', 165, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (193, '添加或修改分类', '/admin/categories', 'POST', 165, 0, '2021-08-11 21:04:22', '2021-08-20 22:18:22');
INSERT INTO `tb_resource` VALUES (194, '删除分类', '/admin/categories', 'DELETE', 165, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (195, '搜索文章分类', '/admin/categories/search', 'GET', 165, 0, '2021-08-11 21:04:22', '2021-08-20 22:18:25');
INSERT INTO `tb_resource` VALUES (196, '查询后台评论', '/admin/comments', 'GET', 178, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (197, '删除评论', '/admin/comments', 'DELETE', 178, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (198, '审核评论', '/admin/comments/review', 'PUT', 178, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (199, '查看后台友链列表', '/admin/links', 'GET', 167, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (200, '保存或修改友链', '/admin/links', 'POST', 167, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (201, '删除友链', '/admin/links', 'DELETE', 167, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (202, '查看菜单列表', '/admin/menus', 'GET', 176, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (203, '新增或修改菜单', '/admin/menus', 'POST', 176, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (204, '删除菜单', '/admin/menus/*', 'DELETE', 176, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (205, '查看后台留言列表', '/admin/messages', 'GET', 174, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (206, '删除留言', '/admin/messages', 'DELETE', 174, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (207, '审核留言', '/admin/messages/review', 'PUT', 174, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (208, '查看操作日志', '/admin/operation', 'GET', 169, 0, '2021-08-11 21:04:22', '2021-12-12 15:27:03');
INSERT INTO `tb_resource` VALUES (209, '删除操作日志', '/admin/operation', 'DELETE', 169, 0, '2021-08-11 21:04:22', '2021-12-12 15:27:15');
INSERT INTO `tb_resource` VALUES (210, '获取页面列表', '/admin/pages', 'GET', 180, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (211, '保存或更新页面', '/admin/pages', 'POST', 180, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (212, '删除页面', '/admin/pages/*', 'DELETE', 180, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (225, '查看资源列表', '/admin/resources', 'GET', 179, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (226, '新增或修改资源', '/admin/resources', 'POST', 179, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (227, '导入swagger接口', '/admin/resources/import/swagger', 'GET', 179, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (228, '删除资源', '/admin/resources/*', 'DELETE', 179, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (229, '保存或更新角色', '/admin/role', 'POST', 177, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (230, '查看角色菜单选项', '/admin/role/menus', 'GET', 176, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (231, '查看角色资源选项', '/admin/role/resources', 'GET', 179, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (232, '查询角色列表', '/admin/roles', 'GET', 177, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (233, '删除角色', '/admin/roles', 'DELETE', 177, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (234, '查询后台标签列表', '/admin/tags', 'GET', 170, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (235, '添加或修改标签', '/admin/tags', 'POST', 170, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (236, '删除标签', '/admin/tags', 'DELETE', 170, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (237, '搜索文章标签', '/admin/tags/search', 'GET', 170, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (238, '查看当前用户菜单', '/admin/user/menus', 'GET', 176, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (239, '查询后台用户列表', '/admin/users', 'GET', 173, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (240, '修改用户禁用状态', '/admin/users/disable', 'PUT', 172, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (241, '查看在线用户', '/admin/users/online', 'GET', 172, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (242, '修改管理员密码', '/admin/users/password', 'PUT', 173, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (243, '查询用户角色选项', '/admin/users/role', 'GET', 177, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (244, '修改用户角色', '/admin/users/role', 'PUT', 172, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (245, '下线用户', '/admin/users/*/online', 'DELETE', 172, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (246, '获取网站配置', '/admin/website/config', 'GET', 166, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (247, '更新网站配置', '/admin/website/config', 'PUT', 166, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (249, '查看首页文章', '/articles', 'GET', 168, 1, '2021-08-11 21:04:22', '2021-08-11 21:05:45');
INSERT INTO `tb_resource` VALUES (250, '查看文章归档', '/articles/archives', 'GET', 168, 1, '2021-08-11 21:04:22', '2021-08-11 21:05:47');
INSERT INTO `tb_resource` VALUES (251, '根据条件查询文章', '/articles/condition', 'GET', 168, 1, '2021-08-11 21:04:22', '2021-08-11 21:05:47');
INSERT INTO `tb_resource` VALUES (252, '搜索文章', '/articles/search', 'GET', 168, 1, '2021-08-11 21:04:22', '2021-08-11 21:05:48');
INSERT INTO `tb_resource` VALUES (253, '根据id查看文章', '/articles/*', 'GET', 168, 1, '2021-08-11 21:04:22', '2021-08-11 21:05:49');
INSERT INTO `tb_resource` VALUES (254, '点赞文章', '/articles/*/like', 'POST', 168, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (255, '查看分类列表', '/categories', 'GET', 165, 1, '2021-08-11 21:04:22', '2021-08-22 11:12:47');
INSERT INTO `tb_resource` VALUES (256, '查询评论', '/comments', 'GET', 178, 1, '2021-08-11 21:04:22', '2021-08-11 21:07:33');
INSERT INTO `tb_resource` VALUES (257, '添加评论', '/comments', 'POST', 178, 0, '2021-08-11 21:04:22', '2021-08-11 21:10:05');
INSERT INTO `tb_resource` VALUES (258, '评论点赞', '/comments/*/like', 'POST', 178, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (259, '查询评论下的回复', '/comments/*/replies', 'GET', 178, 1, '2021-08-11 21:04:22', '2021-08-11 21:07:30');
INSERT INTO `tb_resource` VALUES (260, '查看友链列表', '/links', 'GET', 167, 1, '2021-08-11 21:04:22', '2021-08-11 21:05:41');
INSERT INTO `tb_resource` VALUES (261, '查看留言列表', '/messages', 'GET', 174, 1, '2021-08-11 21:04:22', '2021-08-11 21:07:14');
INSERT INTO `tb_resource` VALUES (262, '添加留言', '/messages', 'POST', 174, 1, '2021-08-11 21:04:22', '2021-08-11 21:07:15');
INSERT INTO `tb_resource` VALUES (264, '用户注册', '/register', 'POST', 173, 1, '2021-08-11 21:04:22', '2021-08-11 21:07:01');
INSERT INTO `tb_resource` VALUES (265, '查询标签列表', '/tags', 'GET', 170, 1, '2021-08-11 21:04:22', '2021-08-11 21:06:30');
INSERT INTO `tb_resource` VALUES (267, '更新用户头像', '/users/avatar', 'POST', 172, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (268, '发送邮箱验证码', '/users/code', 'GET', 173, 1, '2021-08-11 21:04:22', '2021-08-11 21:07:02');
INSERT INTO `tb_resource` VALUES (269, '绑定用户邮箱', '/users/email', 'POST', 172, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (270, '更新用户信息', '/users/info', 'PUT', 172, 0, '2021-08-11 21:04:22', NULL);
INSERT INTO `tb_resource` VALUES (271, 'qq登录', '/users/oauth/qq', 'POST', 173, 1, '2021-08-11 21:04:22', '2021-08-11 21:07:06');
INSERT INTO `tb_resource` VALUES (272, '微博登录', '/users/oauth/weibo', 'POST', 173, 1, '2021-08-11 21:04:22', '2021-08-11 21:07:06');
INSERT INTO `tb_resource` VALUES (273, '修改密码', '/users/password', 'PUT', 173, 1, '2021-08-11 21:04:22', '2021-08-11 21:07:09');
INSERT INTO `tb_resource` VALUES (274, '上传语音', '/voice', 'POST', 166, 1, '2021-08-11 21:04:22', '2021-08-11 21:05:33');
INSERT INTO `tb_resource` VALUES (275, '查看访问日志', '/admin/visit', 'GET', 169, NULL, '2022-01-11 00:09:43', NULL);
INSERT INTO `tb_resource` VALUES (276, '删除访问日志', '/admin/visit', 'DELETE', 169, NULL, '2022-01-11 00:10:24', NULL);
INSERT INTO `tb_resource` VALUES (277, '动态模块', NULL, NULL, NULL, NULL, '2022-01-13 00:41:47', NULL);
INSERT INTO `tb_resource` VALUES (278, '查询动态', '/moments', 'GET', 277, 1, '2022-01-13 00:43:04', '2022-01-13 00:50:11');
INSERT INTO `tb_resource` VALUES (279, '添加或修改动态', '/admin/moments', 'POST', 277, NULL, '2022-01-13 00:43:34', '2022-01-13 00:50:15');
INSERT INTO `tb_resource` VALUES (280, '修改动态置顶状态', '/admin/moments/top', 'PUT', 277, NULL, '2022-01-13 00:44:04', '2022-01-13 00:50:20');
INSERT INTO `tb_resource` VALUES (281, '上传动态图片', '/admin/moments/images', 'POST', 277, NULL, '2022-01-13 00:44:28', '2022-01-13 00:50:26');
INSERT INTO `tb_resource` VALUES (282, '恢复或删除动态', '/admin/moments', 'PUT', 277, NULL, '2022-01-13 00:44:47', '2022-01-13 00:50:30');
INSERT INTO `tb_resource` VALUES (283, '物理删除动态', '/admin/moments', 'DELETE', 277, NULL, '2022-01-13 00:45:13', '2022-01-13 00:50:33');
INSERT INTO `tb_resource` VALUES (284, '根据Id查看动态', '/admin/moments/*', 'GET', 277, 0, '2022-01-13 00:45:30', '2022-01-13 00:50:37');
INSERT INTO `tb_resource` VALUES (285, '点赞动态', '/moments/*/like', 'POST', 277, 1, '2022-01-13 00:46:10', '2022-01-13 00:50:41');

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色名',
  `role_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
  `is_disable` tinyint(1) NULL DEFAULT NULL COMMENT '是否禁用  0否 1是',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1, '管理员', 'admin', 0, '2021-01-11 17:21:57', '2022-01-13 00:57:29');
INSERT INTO `tb_role` VALUES (2, '用户', 'user', 0, '2021-01-11 20:17:05', '2022-01-12 20:52:22');
INSERT INTO `tb_role` VALUES (3, '测试', 'test', 0, '2021-01-11 20:17:23', '2022-01-13 00:56:00');

-- ----------------------------
-- Table structure for tb_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_menu`;
CREATE TABLE `tb_role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `menu_id` int NULL DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2531 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_role_menu
-- ----------------------------
INSERT INTO `tb_role_menu` VALUES (1397, 8, 1);
INSERT INTO `tb_role_menu` VALUES (1398, 8, 2);
INSERT INTO `tb_role_menu` VALUES (1399, 8, 6);
INSERT INTO `tb_role_menu` VALUES (1400, 8, 7);
INSERT INTO `tb_role_menu` VALUES (1401, 8, 8);
INSERT INTO `tb_role_menu` VALUES (1402, 8, 9);
INSERT INTO `tb_role_menu` VALUES (1403, 8, 10);
INSERT INTO `tb_role_menu` VALUES (1404, 8, 3);
INSERT INTO `tb_role_menu` VALUES (1405, 8, 11);
INSERT INTO `tb_role_menu` VALUES (1406, 8, 12);
INSERT INTO `tb_role_menu` VALUES (1407, 8, 202);
INSERT INTO `tb_role_menu` VALUES (1408, 8, 13);
INSERT INTO `tb_role_menu` VALUES (1409, 8, 14);
INSERT INTO `tb_role_menu` VALUES (1410, 8, 201);
INSERT INTO `tb_role_menu` VALUES (1411, 8, 4);
INSERT INTO `tb_role_menu` VALUES (1412, 8, 16);
INSERT INTO `tb_role_menu` VALUES (1413, 8, 15);
INSERT INTO `tb_role_menu` VALUES (1414, 8, 17);
INSERT INTO `tb_role_menu` VALUES (1415, 8, 18);
INSERT INTO `tb_role_menu` VALUES (1416, 8, 19);
INSERT INTO `tb_role_menu` VALUES (1417, 8, 20);
INSERT INTO `tb_role_menu` VALUES (1418, 8, 5);
INSERT INTO `tb_role_menu` VALUES (1595, 9, 1);
INSERT INTO `tb_role_menu` VALUES (1596, 9, 2);
INSERT INTO `tb_role_menu` VALUES (1597, 9, 6);
INSERT INTO `tb_role_menu` VALUES (1598, 9, 7);
INSERT INTO `tb_role_menu` VALUES (1599, 9, 8);
INSERT INTO `tb_role_menu` VALUES (1600, 9, 9);
INSERT INTO `tb_role_menu` VALUES (1601, 9, 10);
INSERT INTO `tb_role_menu` VALUES (1602, 9, 3);
INSERT INTO `tb_role_menu` VALUES (1603, 9, 11);
INSERT INTO `tb_role_menu` VALUES (1604, 9, 12);
INSERT INTO `tb_role_menu` VALUES (1605, 9, 202);
INSERT INTO `tb_role_menu` VALUES (1606, 9, 13);
INSERT INTO `tb_role_menu` VALUES (1607, 9, 14);
INSERT INTO `tb_role_menu` VALUES (1608, 9, 201);
INSERT INTO `tb_role_menu` VALUES (1609, 9, 4);
INSERT INTO `tb_role_menu` VALUES (1610, 9, 16);
INSERT INTO `tb_role_menu` VALUES (1611, 9, 15);
INSERT INTO `tb_role_menu` VALUES (1612, 9, 17);
INSERT INTO `tb_role_menu` VALUES (1613, 9, 18);
INSERT INTO `tb_role_menu` VALUES (1614, 9, 19);
INSERT INTO `tb_role_menu` VALUES (1615, 9, 20);
INSERT INTO `tb_role_menu` VALUES (1616, 9, 5);
INSERT INTO `tb_role_menu` VALUES (1639, 13, 2);
INSERT INTO `tb_role_menu` VALUES (1640, 13, 6);
INSERT INTO `tb_role_menu` VALUES (1641, 13, 7);
INSERT INTO `tb_role_menu` VALUES (1642, 13, 8);
INSERT INTO `tb_role_menu` VALUES (1643, 13, 9);
INSERT INTO `tb_role_menu` VALUES (1644, 13, 10);
INSERT INTO `tb_role_menu` VALUES (1645, 13, 3);
INSERT INTO `tb_role_menu` VALUES (1646, 13, 11);
INSERT INTO `tb_role_menu` VALUES (1647, 13, 12);
INSERT INTO `tb_role_menu` VALUES (2392, 3, 1);
INSERT INTO `tb_role_menu` VALUES (2393, 3, 2);
INSERT INTO `tb_role_menu` VALUES (2394, 3, 6);
INSERT INTO `tb_role_menu` VALUES (2395, 3, 7);
INSERT INTO `tb_role_menu` VALUES (2396, 3, 8);
INSERT INTO `tb_role_menu` VALUES (2397, 3, 9);
INSERT INTO `tb_role_menu` VALUES (2398, 3, 10);
INSERT INTO `tb_role_menu` VALUES (2399, 3, 3);
INSERT INTO `tb_role_menu` VALUES (2400, 3, 11);
INSERT INTO `tb_role_menu` VALUES (2401, 3, 12);
INSERT INTO `tb_role_menu` VALUES (2402, 3, 202);
INSERT INTO `tb_role_menu` VALUES (2403, 3, 13);
INSERT INTO `tb_role_menu` VALUES (2404, 3, 201);
INSERT INTO `tb_role_menu` VALUES (2405, 3, 213);
INSERT INTO `tb_role_menu` VALUES (2406, 3, 14);
INSERT INTO `tb_role_menu` VALUES (2407, 3, 15);
INSERT INTO `tb_role_menu` VALUES (2408, 3, 16);
INSERT INTO `tb_role_menu` VALUES (2409, 3, 4);
INSERT INTO `tb_role_menu` VALUES (2410, 3, 214);
INSERT INTO `tb_role_menu` VALUES (2411, 3, 209);
INSERT INTO `tb_role_menu` VALUES (2412, 3, 17);
INSERT INTO `tb_role_menu` VALUES (2413, 3, 18);
INSERT INTO `tb_role_menu` VALUES (2414, 3, 19);
INSERT INTO `tb_role_menu` VALUES (2415, 3, 20);
INSERT INTO `tb_role_menu` VALUES (2416, 3, 215);
INSERT INTO `tb_role_menu` VALUES (2417, 3, 5);
INSERT INTO `tb_role_menu` VALUES (2470, 2, 1);
INSERT INTO `tb_role_menu` VALUES (2501, 1, 1);
INSERT INTO `tb_role_menu` VALUES (2502, 1, 2);
INSERT INTO `tb_role_menu` VALUES (2503, 1, 6);
INSERT INTO `tb_role_menu` VALUES (2504, 1, 7);
INSERT INTO `tb_role_menu` VALUES (2505, 1, 8);
INSERT INTO `tb_role_menu` VALUES (2506, 1, 9);
INSERT INTO `tb_role_menu` VALUES (2507, 1, 10);
INSERT INTO `tb_role_menu` VALUES (2508, 1, 3);
INSERT INTO `tb_role_menu` VALUES (2509, 1, 11);
INSERT INTO `tb_role_menu` VALUES (2510, 1, 12);
INSERT INTO `tb_role_menu` VALUES (2511, 1, 202);
INSERT INTO `tb_role_menu` VALUES (2512, 1, 13);
INSERT INTO `tb_role_menu` VALUES (2513, 1, 201);
INSERT INTO `tb_role_menu` VALUES (2514, 1, 213);
INSERT INTO `tb_role_menu` VALUES (2515, 1, 14);
INSERT INTO `tb_role_menu` VALUES (2516, 1, 15);
INSERT INTO `tb_role_menu` VALUES (2517, 1, 16);
INSERT INTO `tb_role_menu` VALUES (2518, 1, 4);
INSERT INTO `tb_role_menu` VALUES (2519, 1, 214);
INSERT INTO `tb_role_menu` VALUES (2520, 1, 209);
INSERT INTO `tb_role_menu` VALUES (2521, 1, 17);
INSERT INTO `tb_role_menu` VALUES (2522, 1, 18);
INSERT INTO `tb_role_menu` VALUES (2523, 1, 19);
INSERT INTO `tb_role_menu` VALUES (2524, 1, 20);
INSERT INTO `tb_role_menu` VALUES (2525, 1, 215);
INSERT INTO `tb_role_menu` VALUES (2526, 1, 216);
INSERT INTO `tb_role_menu` VALUES (2527, 1, 217);
INSERT INTO `tb_role_menu` VALUES (2528, 1, 219);
INSERT INTO `tb_role_menu` VALUES (2529, 1, 218);
INSERT INTO `tb_role_menu` VALUES (2530, 1, 5);

-- ----------------------------
-- Table structure for tb_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_resource`;
CREATE TABLE `tb_role_resource`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `resource_id` int NULL DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4461 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_role_resource
-- ----------------------------
INSERT INTO `tb_role_resource` VALUES (4011, 2, 254);
INSERT INTO `tb_role_resource` VALUES (4012, 2, 267);
INSERT INTO `tb_role_resource` VALUES (4013, 2, 269);
INSERT INTO `tb_role_resource` VALUES (4014, 2, 270);
INSERT INTO `tb_role_resource` VALUES (4015, 2, 257);
INSERT INTO `tb_role_resource` VALUES (4016, 2, 258);
INSERT INTO `tb_role_resource` VALUES (4363, 1, 165);
INSERT INTO `tb_role_resource` VALUES (4364, 1, 192);
INSERT INTO `tb_role_resource` VALUES (4365, 1, 193);
INSERT INTO `tb_role_resource` VALUES (4366, 1, 194);
INSERT INTO `tb_role_resource` VALUES (4367, 1, 195);
INSERT INTO `tb_role_resource` VALUES (4368, 1, 166);
INSERT INTO `tb_role_resource` VALUES (4369, 1, 183);
INSERT INTO `tb_role_resource` VALUES (4370, 1, 184);
INSERT INTO `tb_role_resource` VALUES (4371, 1, 246);
INSERT INTO `tb_role_resource` VALUES (4372, 1, 247);
INSERT INTO `tb_role_resource` VALUES (4373, 1, 167);
INSERT INTO `tb_role_resource` VALUES (4374, 1, 199);
INSERT INTO `tb_role_resource` VALUES (4375, 1, 200);
INSERT INTO `tb_role_resource` VALUES (4376, 1, 201);
INSERT INTO `tb_role_resource` VALUES (4377, 1, 168);
INSERT INTO `tb_role_resource` VALUES (4378, 1, 185);
INSERT INTO `tb_role_resource` VALUES (4379, 1, 186);
INSERT INTO `tb_role_resource` VALUES (4380, 1, 187);
INSERT INTO `tb_role_resource` VALUES (4381, 1, 188);
INSERT INTO `tb_role_resource` VALUES (4382, 1, 189);
INSERT INTO `tb_role_resource` VALUES (4383, 1, 190);
INSERT INTO `tb_role_resource` VALUES (4384, 1, 191);
INSERT INTO `tb_role_resource` VALUES (4385, 1, 254);
INSERT INTO `tb_role_resource` VALUES (4386, 1, 169);
INSERT INTO `tb_role_resource` VALUES (4387, 1, 208);
INSERT INTO `tb_role_resource` VALUES (4388, 1, 209);
INSERT INTO `tb_role_resource` VALUES (4389, 1, 170);
INSERT INTO `tb_role_resource` VALUES (4390, 1, 234);
INSERT INTO `tb_role_resource` VALUES (4391, 1, 235);
INSERT INTO `tb_role_resource` VALUES (4392, 1, 236);
INSERT INTO `tb_role_resource` VALUES (4393, 1, 237);
INSERT INTO `tb_role_resource` VALUES (4394, 1, 172);
INSERT INTO `tb_role_resource` VALUES (4395, 1, 240);
INSERT INTO `tb_role_resource` VALUES (4396, 1, 241);
INSERT INTO `tb_role_resource` VALUES (4397, 1, 244);
INSERT INTO `tb_role_resource` VALUES (4398, 1, 245);
INSERT INTO `tb_role_resource` VALUES (4399, 1, 267);
INSERT INTO `tb_role_resource` VALUES (4400, 1, 269);
INSERT INTO `tb_role_resource` VALUES (4401, 1, 270);
INSERT INTO `tb_role_resource` VALUES (4402, 1, 173);
INSERT INTO `tb_role_resource` VALUES (4403, 1, 239);
INSERT INTO `tb_role_resource` VALUES (4404, 1, 242);
INSERT INTO `tb_role_resource` VALUES (4405, 1, 174);
INSERT INTO `tb_role_resource` VALUES (4406, 1, 205);
INSERT INTO `tb_role_resource` VALUES (4407, 1, 206);
INSERT INTO `tb_role_resource` VALUES (4408, 1, 207);
INSERT INTO `tb_role_resource` VALUES (4409, 1, 176);
INSERT INTO `tb_role_resource` VALUES (4410, 1, 202);
INSERT INTO `tb_role_resource` VALUES (4411, 1, 203);
INSERT INTO `tb_role_resource` VALUES (4412, 1, 204);
INSERT INTO `tb_role_resource` VALUES (4413, 1, 230);
INSERT INTO `tb_role_resource` VALUES (4414, 1, 238);
INSERT INTO `tb_role_resource` VALUES (4415, 1, 177);
INSERT INTO `tb_role_resource` VALUES (4416, 1, 229);
INSERT INTO `tb_role_resource` VALUES (4417, 1, 232);
INSERT INTO `tb_role_resource` VALUES (4418, 1, 233);
INSERT INTO `tb_role_resource` VALUES (4419, 1, 243);
INSERT INTO `tb_role_resource` VALUES (4420, 1, 178);
INSERT INTO `tb_role_resource` VALUES (4421, 1, 196);
INSERT INTO `tb_role_resource` VALUES (4422, 1, 197);
INSERT INTO `tb_role_resource` VALUES (4423, 1, 198);
INSERT INTO `tb_role_resource` VALUES (4424, 1, 257);
INSERT INTO `tb_role_resource` VALUES (4425, 1, 258);
INSERT INTO `tb_role_resource` VALUES (4426, 1, 179);
INSERT INTO `tb_role_resource` VALUES (4427, 1, 225);
INSERT INTO `tb_role_resource` VALUES (4428, 1, 226);
INSERT INTO `tb_role_resource` VALUES (4429, 1, 227);
INSERT INTO `tb_role_resource` VALUES (4430, 1, 228);
INSERT INTO `tb_role_resource` VALUES (4431, 1, 231);
INSERT INTO `tb_role_resource` VALUES (4432, 1, 180);
INSERT INTO `tb_role_resource` VALUES (4433, 1, 210);
INSERT INTO `tb_role_resource` VALUES (4434, 1, 211);
INSERT INTO `tb_role_resource` VALUES (4435, 1, 212);
INSERT INTO `tb_role_resource` VALUES (4436, 3, 192);
INSERT INTO `tb_role_resource` VALUES (4437, 3, 195);
INSERT INTO `tb_role_resource` VALUES (4438, 3, 183);
INSERT INTO `tb_role_resource` VALUES (4439, 3, 246);
INSERT INTO `tb_role_resource` VALUES (4440, 3, 199);
INSERT INTO `tb_role_resource` VALUES (4441, 3, 185);
INSERT INTO `tb_role_resource` VALUES (4442, 3, 191);
INSERT INTO `tb_role_resource` VALUES (4443, 3, 254);
INSERT INTO `tb_role_resource` VALUES (4444, 3, 208);
INSERT INTO `tb_role_resource` VALUES (4445, 3, 234);
INSERT INTO `tb_role_resource` VALUES (4446, 3, 237);
INSERT INTO `tb_role_resource` VALUES (4447, 3, 241);
INSERT INTO `tb_role_resource` VALUES (4448, 3, 239);
INSERT INTO `tb_role_resource` VALUES (4449, 3, 205);
INSERT INTO `tb_role_resource` VALUES (4450, 3, 202);
INSERT INTO `tb_role_resource` VALUES (4451, 3, 230);
INSERT INTO `tb_role_resource` VALUES (4452, 3, 238);
INSERT INTO `tb_role_resource` VALUES (4453, 3, 232);
INSERT INTO `tb_role_resource` VALUES (4454, 3, 243);
INSERT INTO `tb_role_resource` VALUES (4455, 3, 196);
INSERT INTO `tb_role_resource` VALUES (4456, 3, 257);
INSERT INTO `tb_role_resource` VALUES (4457, 3, 258);
INSERT INTO `tb_role_resource` VALUES (4458, 3, 225);
INSERT INTO `tb_role_resource` VALUES (4459, 3, 231);
INSERT INTO `tb_role_resource` VALUES (4460, 3, 210);

-- ----------------------------
-- Table structure for tb_tag
-- ----------------------------
DROP TABLE IF EXISTS `tb_tag`;
CREATE TABLE `tb_tag`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签名',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_tag
-- ----------------------------
INSERT INTO `tb_tag` VALUES (1, '文章', '2020-12-27 23:24:04', NULL);
INSERT INTO `tb_tag` VALUES (11, '生活', '2021-01-07 15:58:19', NULL);
INSERT INTO `tb_tag` VALUES (12, '爱你', '2021-01-07 15:58:19', NULL);

-- ----------------------------
-- Table structure for tb_unique_view
-- ----------------------------
DROP TABLE IF EXISTS `tb_unique_view`;
CREATE TABLE `tb_unique_view`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `views_count` int NOT NULL COMMENT '访问量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_unique_view
-- ----------------------------
INSERT INTO `tb_unique_view` VALUES (12, 2, '2020-12-29 00:00:00', NULL);
INSERT INTO `tb_unique_view` VALUES (13, 2, '2020-12-30 00:00:00', NULL);
INSERT INTO `tb_unique_view` VALUES (14, 2, '2021-08-28 00:00:00', NULL);
INSERT INTO `tb_unique_view` VALUES (15, 1, '2022-01-03 00:00:00', NULL);
INSERT INTO `tb_unique_view` VALUES (16, 5, '2022-01-16 00:00:11', NULL);
INSERT INTO `tb_unique_view` VALUES (17, 14, '2022-02-18 00:00:03', NULL);

-- ----------------------------
-- Table structure for tb_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_auth`;
CREATE TABLE `tb_user_auth`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_info_id` int NOT NULL COMMENT '用户信息id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `login_type` tinyint(1) NOT NULL COMMENT '登录类型',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户登录ip',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '上次登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户登录信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_user_auth
-- ----------------------------
INSERT INTO `tb_user_auth` VALUES (1, 1, '2413245708@qq.com', '$2a$10$DzvvQ73tQ86AtSKSiINuqel4cCfXRgCl8LZf/Jm7jGCaHjM4mjG3u', 0, '127.0.0.1', '内网IP|内网IP', '2020-12-22 23:13:43', '2022-02-19 19:56:32', '2022-02-19 19:56:31');
INSERT INTO `tb_user_auth` VALUES (20, 21, '241324570820@qq.com', '$2a$10$Ok7LA.uuuQU7LOzaWRThq.dwMMvkDGI9OvBRQOS/svPlIQBAK84oK', 0, '192.168.43.145', ' 局域网', '2020-12-29 23:10:56', NULL, '2021-06-17 13:35:08');
INSERT INTO `tb_user_auth` VALUES (21, 22, 'codeprince2020@163.com', '$2a$10$DzvvQ73tQ86AtSKSiINuqel4cCfXRgCl8LZf/Jm7jGCaHjM4mjG3u', 0, '10.158.88.89', ' 局域网IP', '2020-12-29 23:39:24', '2021-11-26 13:12:36', '2021-11-26 13:12:36');

-- ----------------------------
-- Table structure for tb_user_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_info`;
CREATE TABLE `tb_user_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'email',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户昵称',
  `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'https://www.static.talkxj.com/avatar/user.png' COMMENT '用户头像',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户简介',
  `web_site` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '个人网站',
  `is_disable` tinyint(1) NULL DEFAULT 0 COMMENT '是否禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_user`(`nickname` ASC) USING BTREE COMMENT '用户名，昵称，邮箱不可重复'
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_user_info
-- ----------------------------
INSERT INTO `tb_user_info` VALUES (1, '2413245708@qq.com', 'zzStar', 'https://gitee.com/codeprometheus/MyPicBed/raw/master/img/star.png', 'Starry-Blog', '', 0, '2020-12-27 20:25:37', '2022-02-13 20:27:16');
INSERT INTO `tb_user_info` VALUES (21, '241324570820@qq.com', 'test2413', 'https://gitee.com/codeprometheus/starry-blog-image/raw/master/avatar/2020-12-29/785c600a76d04d5895b51b93980587b2.png', 'test123', NULL, 0, '2020-12-29 23:10:56', NULL);
INSERT INTO `tb_user_info` VALUES (22, 'codeprince2020@163.com', '163test', 'https://gitee.com/codeprometheus/starry-blog-image/raw/master/avatar/2020-12-29/5d603e48bc3e4f85acad9ab715ca3881.png', 'info163', NULL, 0, '2020-12-29 23:39:24', NULL);

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 219 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES (2, 23, 3);
INSERT INTO `tb_user_role` VALUES (4, 2, 2);
INSERT INTO `tb_user_role` VALUES (5, 3, 2);
INSERT INTO `tb_user_role` VALUES (6, 4, 2);
INSERT INTO `tb_user_role` VALUES (7, 5, 2);
INSERT INTO `tb_user_role` VALUES (8, 7, 2);
INSERT INTO `tb_user_role` VALUES (9, 8, 2);
INSERT INTO `tb_user_role` VALUES (10, 9, 2);
INSERT INTO `tb_user_role` VALUES (11, 10, 2);
INSERT INTO `tb_user_role` VALUES (12, 11, 2);
INSERT INTO `tb_user_role` VALUES (13, 15, 2);
INSERT INTO `tb_user_role` VALUES (14, 16, 2);
INSERT INTO `tb_user_role` VALUES (15, 18, 2);
INSERT INTO `tb_user_role` VALUES (16, 19, 2);
INSERT INTO `tb_user_role` VALUES (17, 21, 2);
INSERT INTO `tb_user_role` VALUES (18, 22, 2);
INSERT INTO `tb_user_role` VALUES (19, 23, 2);
INSERT INTO `tb_user_role` VALUES (20, 24, 2);
INSERT INTO `tb_user_role` VALUES (21, 25, 2);
INSERT INTO `tb_user_role` VALUES (22, 26, 2);
INSERT INTO `tb_user_role` VALUES (23, 27, 2);
INSERT INTO `tb_user_role` VALUES (24, 28, 2);
INSERT INTO `tb_user_role` VALUES (25, 29, 2);
INSERT INTO `tb_user_role` VALUES (26, 30, 2);
INSERT INTO `tb_user_role` VALUES (27, 31, 2);
INSERT INTO `tb_user_role` VALUES (28, 33, 2);
INSERT INTO `tb_user_role` VALUES (29, 35, 2);
INSERT INTO `tb_user_role` VALUES (30, 36, 2);
INSERT INTO `tb_user_role` VALUES (31, 37, 2);
INSERT INTO `tb_user_role` VALUES (32, 38, 2);
INSERT INTO `tb_user_role` VALUES (33, 39, 2);
INSERT INTO `tb_user_role` VALUES (34, 40, 2);
INSERT INTO `tb_user_role` VALUES (35, 41, 2);
INSERT INTO `tb_user_role` VALUES (36, 42, 2);
INSERT INTO `tb_user_role` VALUES (37, 44, 2);
INSERT INTO `tb_user_role` VALUES (38, 45, 2);
INSERT INTO `tb_user_role` VALUES (39, 46, 2);
INSERT INTO `tb_user_role` VALUES (40, 47, 2);
INSERT INTO `tb_user_role` VALUES (41, 48, 2);
INSERT INTO `tb_user_role` VALUES (42, 49, 2);
INSERT INTO `tb_user_role` VALUES (43, 52, 2);
INSERT INTO `tb_user_role` VALUES (44, 54, 2);
INSERT INTO `tb_user_role` VALUES (45, 55, 2);
INSERT INTO `tb_user_role` VALUES (46, 56, 2);
INSERT INTO `tb_user_role` VALUES (47, 57, 2);
INSERT INTO `tb_user_role` VALUES (48, 58, 2);
INSERT INTO `tb_user_role` VALUES (49, 59, 2);
INSERT INTO `tb_user_role` VALUES (50, 60, 2);
INSERT INTO `tb_user_role` VALUES (51, 61, 2);
INSERT INTO `tb_user_role` VALUES (52, 62, 2);
INSERT INTO `tb_user_role` VALUES (53, 63, 2);
INSERT INTO `tb_user_role` VALUES (54, 64, 2);
INSERT INTO `tb_user_role` VALUES (55, 65, 2);
INSERT INTO `tb_user_role` VALUES (56, 67, 2);
INSERT INTO `tb_user_role` VALUES (57, 68, 2);
INSERT INTO `tb_user_role` VALUES (58, 69, 2);
INSERT INTO `tb_user_role` VALUES (59, 70, 2);
INSERT INTO `tb_user_role` VALUES (60, 71, 2);
INSERT INTO `tb_user_role` VALUES (61, 72, 2);
INSERT INTO `tb_user_role` VALUES (62, 73, 2);
INSERT INTO `tb_user_role` VALUES (63, 74, 2);
INSERT INTO `tb_user_role` VALUES (64, 75, 2);
INSERT INTO `tb_user_role` VALUES (65, 76, 2);
INSERT INTO `tb_user_role` VALUES (66, 77, 2);
INSERT INTO `tb_user_role` VALUES (67, 78, 2);
INSERT INTO `tb_user_role` VALUES (68, 79, 2);
INSERT INTO `tb_user_role` VALUES (69, 80, 2);
INSERT INTO `tb_user_role` VALUES (70, 81, 2);
INSERT INTO `tb_user_role` VALUES (71, 82, 2);
INSERT INTO `tb_user_role` VALUES (72, 83, 2);
INSERT INTO `tb_user_role` VALUES (73, 84, 2);
INSERT INTO `tb_user_role` VALUES (74, 85, 2);
INSERT INTO `tb_user_role` VALUES (75, 86, 2);
INSERT INTO `tb_user_role` VALUES (76, 87, 2);
INSERT INTO `tb_user_role` VALUES (77, 88, 2);
INSERT INTO `tb_user_role` VALUES (78, 89, 2);
INSERT INTO `tb_user_role` VALUES (79, 90, 2);
INSERT INTO `tb_user_role` VALUES (80, 91, 2);
INSERT INTO `tb_user_role` VALUES (81, 92, 2);
INSERT INTO `tb_user_role` VALUES (100, 105, 2);
INSERT INTO `tb_user_role` VALUES (133, 138, 2);
INSERT INTO `tb_user_role` VALUES (134, 139, 2);
INSERT INTO `tb_user_role` VALUES (135, 140, 2);
INSERT INTO `tb_user_role` VALUES (136, 141, 2);
INSERT INTO `tb_user_role` VALUES (137, 142, 2);
INSERT INTO `tb_user_role` VALUES (138, 143, 2);
INSERT INTO `tb_user_role` VALUES (139, 144, 2);
INSERT INTO `tb_user_role` VALUES (140, 145, 2);
INSERT INTO `tb_user_role` VALUES (141, 146, 2);
INSERT INTO `tb_user_role` VALUES (142, 147, 2);
INSERT INTO `tb_user_role` VALUES (143, 148, 2);
INSERT INTO `tb_user_role` VALUES (144, 149, 2);
INSERT INTO `tb_user_role` VALUES (145, 150, 2);
INSERT INTO `tb_user_role` VALUES (146, 151, 2);
INSERT INTO `tb_user_role` VALUES (147, 152, 2);
INSERT INTO `tb_user_role` VALUES (148, 153, 2);
INSERT INTO `tb_user_role` VALUES (149, 154, 2);
INSERT INTO `tb_user_role` VALUES (150, 155, 2);
INSERT INTO `tb_user_role` VALUES (151, 156, 2);
INSERT INTO `tb_user_role` VALUES (152, 157, 2);
INSERT INTO `tb_user_role` VALUES (153, 158, 2);
INSERT INTO `tb_user_role` VALUES (154, 159, 2);
INSERT INTO `tb_user_role` VALUES (155, 160, 2);
INSERT INTO `tb_user_role` VALUES (156, 161, 2);
INSERT INTO `tb_user_role` VALUES (157, 162, 2);
INSERT INTO `tb_user_role` VALUES (158, 163, 2);
INSERT INTO `tb_user_role` VALUES (159, 164, 2);
INSERT INTO `tb_user_role` VALUES (160, 165, 2);
INSERT INTO `tb_user_role` VALUES (161, 167, 2);
INSERT INTO `tb_user_role` VALUES (162, 166, 2);
INSERT INTO `tb_user_role` VALUES (163, 168, 2);
INSERT INTO `tb_user_role` VALUES (164, 169, 2);
INSERT INTO `tb_user_role` VALUES (165, 170, 2);
INSERT INTO `tb_user_role` VALUES (166, 171, 2);
INSERT INTO `tb_user_role` VALUES (167, 172, 2);
INSERT INTO `tb_user_role` VALUES (168, 173, 2);
INSERT INTO `tb_user_role` VALUES (169, 174, 2);
INSERT INTO `tb_user_role` VALUES (170, 175, 2);
INSERT INTO `tb_user_role` VALUES (171, 176, 2);
INSERT INTO `tb_user_role` VALUES (172, 177, 2);
INSERT INTO `tb_user_role` VALUES (173, 178, 2);
INSERT INTO `tb_user_role` VALUES (174, 179, 2);
INSERT INTO `tb_user_role` VALUES (175, 180, 2);
INSERT INTO `tb_user_role` VALUES (176, 181, 2);
INSERT INTO `tb_user_role` VALUES (177, 182, 2);
INSERT INTO `tb_user_role` VALUES (178, 183, 2);
INSERT INTO `tb_user_role` VALUES (179, 184, 2);
INSERT INTO `tb_user_role` VALUES (180, 185, 2);
INSERT INTO `tb_user_role` VALUES (181, 186, 2);
INSERT INTO `tb_user_role` VALUES (182, 187, 2);
INSERT INTO `tb_user_role` VALUES (183, 188, 2);
INSERT INTO `tb_user_role` VALUES (184, 189, 2);
INSERT INTO `tb_user_role` VALUES (185, 190, 2);
INSERT INTO `tb_user_role` VALUES (186, 191, 2);
INSERT INTO `tb_user_role` VALUES (187, 192, 2);
INSERT INTO `tb_user_role` VALUES (188, 193, 2);
INSERT INTO `tb_user_role` VALUES (189, 194, 2);
INSERT INTO `tb_user_role` VALUES (191, 196, 2);
INSERT INTO `tb_user_role` VALUES (192, 195, 2);
INSERT INTO `tb_user_role` VALUES (193, 198, 2);
INSERT INTO `tb_user_role` VALUES (194, 197, 2);
INSERT INTO `tb_user_role` VALUES (195, 199, 2);
INSERT INTO `tb_user_role` VALUES (196, 200, 2);
INSERT INTO `tb_user_role` VALUES (197, 201, 2);
INSERT INTO `tb_user_role` VALUES (198, 202, 2);
INSERT INTO `tb_user_role` VALUES (199, 203, 2);
INSERT INTO `tb_user_role` VALUES (200, 204, 2);
INSERT INTO `tb_user_role` VALUES (201, 205, 2);
INSERT INTO `tb_user_role` VALUES (202, 206, 2);
INSERT INTO `tb_user_role` VALUES (203, 207, 2);
INSERT INTO `tb_user_role` VALUES (204, 208, 2);
INSERT INTO `tb_user_role` VALUES (205, 209, 2);
INSERT INTO `tb_user_role` VALUES (206, 210, 2);
INSERT INTO `tb_user_role` VALUES (217, 1, 1);
INSERT INTO `tb_user_role` VALUES (218, 1, 2);

-- ----------------------------
-- Table structure for tb_visit_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_visit_log`;
CREATE TABLE `tb_visit_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `visit_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '\r\n请求接口',
  `visit_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作内容',
  `ip_addr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `ip_source` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地理位置',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `browser` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '浏览器',
  `os` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作系统',
  `request_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `request_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `response_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '返回数据',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1033 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_visit_log
-- ----------------------------

-- ----------------------------
-- Table structure for tb_website_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_website_config`;
CREATE TABLE `tb_website_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `config` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置信息',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_website_config
-- ----------------------------
INSERT INTO `tb_website_config` VALUES (1, '{\"alipayQRCode\":\"https://gitee.com/codeprometheus/MyPicBed/raw/master/img/alipay.jpg\",\"bilibili\":\"https://space.bilibili.com/342251858\",\"github\":\"https://github.com/CodePrometheus\",\"isChatRoom\":1,\"isCommentReview\":0,\"isEmailNotice\":0,\"isLive2D\":1,\"isMessageReview\":0,\"isReward\":1,\"qq\":\"2413245708\",\"socialLoginList\":[\"qq\",\"github\",\"bilibili\"],\"socialUrlList\":[\"qq\",\"github\",\"bilibili\"],\"touristAvatar\":\"https://gitee.com/codeprometheus/MyPicBed/raw/master/img/star.png\",\"userAvatar\":\"https://gitee.com/codeprometheus/MyPicBed/raw/master/img/star.png\",\"websiteAuthor\":\"zzStar\",\"websiteAvatar\":\"https://gitee.com/codeprometheus/MyPicBed/raw/master/img/star.png\",\"websiteCreateTime\":\"2021-01-01T00:00:00\",\"websiteIntro\":\"网站简介\",\"websiteName\":\"zzStar Blog | 你的美好, 我都记得\",\"websiteNotice\":\"zzStar blog \",\"websiteRecordNo\":\"备案号\",\"websocketUrl\":\"ws://127.0.0.1:8989/websocket\",\"weiXinQRCode\":\"https://gitee.com/codeprometheus/MyPicBed/raw/master/img/wechat.png\"}', '2021-08-17 17:37:30', '2022-02-19 19:58:48');

SET FOREIGN_KEY_CHECKS = 1;
