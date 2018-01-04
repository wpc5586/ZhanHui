1、预约更新状态接口会提示失败( http://api.nebintel.xyz:8090/services/reservations/5?state=2    response:{"code":-8003,"msg":"预约状态更新失败"} )
2、应用图片ICON
3、收到新增好友回调时，拿不到对方的nickname，是后台无法上传环信服务器昵称吗？那是需要前端重新调用获取朋友列表的接口更新下？
4、扫描新注册用户个人电子名片，会获取不到默认的头像等个人信息，只有填写过个人信息才可以。
5、根据userid 46 获取的电子名片返回字段key都不正确
6、帮我删除一下用户 18609817413 的账户信息  我重新注册过一遍流程
7、
8、http://api.nebintel.xyz:8090/services/relation/offlineScanToAddFriend?{"accept_hx_username":"nebintel1514985279710","accept_id":"48","request_hx_username":"nebintel1513347950808","request_id":"46"}
   response:{"code":-5026,"msg":"对方已经是您的信任好友!"}
   http://api.nebintel.xyz:8090/services/relation/deleteFriend?{"delete_id":"48","user_id":"46"}
   response:{"code":-5015,"msg":"删除信任关系失败"}
9、线下添加好友，在收到的申请为什么直接就显示“已同意”了。普通的添加信任好友接口，在收到的申请列表可以正常显示，但接受提示
10、获取朋友详细信息接口是什么时候哪个页面调用的



1、帮我把账户13238852995、18609817413、15142030921删除一下，或者直接改下账号的手机号，让我能重新注册一下，过一遍流程，之前的好友关系和邀请信息有点混乱，我们服务器和环信服务器应该不统一。

1、扫描新注册用户个人电子名片，会获取不到默认的头像等个人信息，只有填写过个人信息才可以。
2、预约更新状态接口会提示失败( http://api.nebintel.xyz:8090/services/reservations/5?state=2    response:{"code":-8003,"msg":"预约状态更新失败"} )
3、应用图片ICON
4、我发现获取个人电子名片的接口，如果是企业账户，获取出来的字段都是公司的信息，这个是这样设计的还是有问题。
5、添加线下信任，提示“对方已经是您的信任好友！”，但调用删除信任会提示失败，这个是昨晚时候测出来的。刚刚重新添加了一下，直接删除还是会提示删除信任关系失败，应该是删除接口有问题。
   http://api.nebintel.xyz:8090/services/relation/offlineScanToAddFriend?{"accept_hx_username":"nebintel1514985279710","accept_id":"48","request_hx_username":"nebintel1513347950808","request_id":"46"}
   response:{"code":-5026,"msg":"对方已经是您的信任好友!"}
   http://api.nebintel.xyz:8090/services/relation/deleteFriend?{"delete_id":"48","user_id":"46"}
   response:{"code":-5015,"msg":"删除信任关系失败"}
6、编辑个人名片的用户名之后，登录返回的昵称还是电话号码，并没关联修改。
7、线下添加好友，在收到的申请直接就显示“已同意”了，是本就这样设计的吗。普通的添加信任好友接口，在收到的申请列表可以正常显示，但接受或拒绝，提示成功，重新获取发现状态并没改变。


1、产品详情需要增加个是否已关注字段。
2、原本UI上设计的，展会-展会以及企业、产品详情中，都有“赞”、“关注”、“评论”、“分享”那四个按钮的布局，现在是如何修改，因为只有目前关注功能。
3、企业详情里的企业头条还有资料中心里一样的广播滚动消息，现在是先隐藏掉吗？
4、展会-企业、产品上方的轮播图UI里是有的，但接口没有，是需要隐藏掉吗？之前看IOS那边显示的和展会-展会里的一样。
5、资料移除收藏后，提示移除收藏成功，但再次添加收藏，会提示重复添加。
http://api.nebintel.xyz:8090/services/documents/14/favorite?user_id=28?{}    response:{"code":0,"msg":"添加收藏成功","data":null,"meta":null}
http://api.nebintel.xyz:8090/services/dataCenter/favorites/14?user_id=28    response:{"code":0,"msg":"移除收藏成功","data":null,"meta":null}
http://api.nebintel.xyz:8090/services/documents/14/favorite?user_id=28?{}    response:{"code":-3005,"msg":"重复添加"}
6、企业详情上方的背景图
## 7、昨晚测试，多次线下信任、删除信任操作之后，突然一次删除成功后再次添加信任，提示“对方已是您的信任好友”，获取好友列表也没有对方。  两个账号id  49  50   目前无法线下添加信任。
7、关注企业的接口我在文档中没找到，这个应该是有的吧。
8、关于正常信任企业的逻辑需要确定下流程，是需要先在展会-企业中的企业详情内关注这个企业，然后再到商圈-关注企业中去信任吗？
9、填写个人信息保存接口，增加个vcard_id返回字段吧。目前是{"code":0,"msg":"编辑个人信息成功","data":null,"meta":null}，这样的话前端第一次保存完，想要获取新的vcard_id，需要重新调用一遍查询个人信息接口才能知道。
10、预约列表，目前不做筛选的话，那获取的列表，前端如何得知这条预约是发起的还是接收到的。