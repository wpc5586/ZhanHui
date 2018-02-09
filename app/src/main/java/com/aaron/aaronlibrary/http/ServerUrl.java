package com.aaron.aaronlibrary.http;

import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.Constants;
import com.xhy.zhanhui.base.ZhanHuiApplication;

/**
 * 服务Url
 * Created by wpc on 2016/12/1 0001.
 */
public class ServerUrl {

    /**
     * 服务器地址
     */
    public static final String SERVICE = "http://192.168.0.112:8080/services/";
//    public static final String SERVICES = "http://ibp.tunnel.echomod.cn/services/"; // 本地
    public static final String SERVICES = "http://api.nebintel.xyz:8090/services/"; // 测试
    public static final String SERVICES_NORMAL = "http://bj.nebintel.com/ibp/services/"; // 正式

    /**
     * JS调原生 类名
     */
    public static final String JS_CLASS_NAME = "NEBINTEL";

    /**
     * 判断系统版本是否大于等于7.0，7.0以上使用https
     * @return true 需要Https false 不需要
     */
    private static boolean isUseHttps() {
        return AppInfo.isNeedHttps();
    }

    public static String getService() {
        return isUseHttps() ? (Constants.DEBUGABLE ? SERVICES : SERVICES_NORMAL) : SERVICE;
    }

    /**
     * 版本信息接口
     * @return 数据
     */
    public static String getVersion() {
        return getService() + "aaron/getVersion.do";
    }

    /**
     * 获取朋友列表
     * @return 数据
     */
    public static String getTrustFriends(String userId) {
        return getService() + "relation/users/" + userId + "/trustFriends";
    }

    /**
     * 根据环信ID获取朋友详情
     * @param hxId 环信ID
     * @return 数据
     */
    public static String hxidFriend(String hxId) {
        return getService() + "users/hxid/" + hxId;
    }

    /**
     * 获取朋友详情
     * @return 数据
     */
    public static String trustFriends(String userId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/trustFriends/" + userId;
    }

    /**
     * 删除朋友
     * @return 数据
     */
    public static String deleteFriends() {
        return getService() + "relation/deleteFriend";
    }

    /**
     * 登录接口
     * @return 数据
     */
    public static String login() {
        return getService() + "users/login";
    }

    /**
     * 注册接口
     * @return 数据
     */
    public static String regist() {
        return getService() + "users/register";
    }

    /**
     * 修改密码接口
     * @return 数据
     */
    public static String updatePassword() {
        return getService() + "users/" + ZhanHuiApplication.getInstance().getUserId() + "/updatePassword";
    }

    /**
     * 修改手机号接口
     * @return 数据
     */
    public static String updateMobile() {
        return getService() + "users/" + ZhanHuiApplication.getInstance().getUserId() + "/updateMobile";
    }

    /**
     * 获取验证码接口
     * @param phone 电话号码
     * @return 数据
     */
    public static String getSmsCode(String phone) {
        return getService() + "users/getSMSCode?mobile=" + phone;
    }

    /**
     * 展会-展会接口
     * @return 数据
     */
    public static String expos() {
        return getService() + "expos/10";
    }

    /**
     * 展会-展会介绍H5
     * @return 数据
     */
    public static String exposH5() {
        return getService() + "expos/10/introduction";
    }

    /**
     * 展会-展会Banner
     * @return 数据
     */
    public static String exposAds() {
        return getService() + "expos/10/ads";
    }

    /**
     * 展会-新闻Banner
     * @return 数据
     */
    public static String exposNews() {
        return getService() + "expos/10/news";
    }

    /**
     * 展会-新闻详情Banner
     * @return url
     */
    public static String exposNewsUrl(String newsId) {
        return getService() + "news/" + newsId;
    }

    /**
     * 展会-资料Banner
     * @return 数据
     */
    public static String exposDocuments() {
        return getService() + "expos/10/documents";
    }

    /**
     * 展会-企业接口
     * @return 数据
     */
    public static String exposCompany() {
        return getService() + "expos/10/companys";
    }

    /**
     * 展会-企业信息接口
     * @param companyId 企业ID
     * @return 数据
     */
    public static String company(String companyId) {
        return getService() + "companies/" + companyId;
    }

    /**
     * 展会-企业列表接口
     * @param hallId 展厅ID
     * @return 数据
     */
    public static String halls(String hallId) {
        return getService() + "expos/10/halls/" + hallId + "/companys";
    }

    /**
     * 展会-企业信息-资料接口
     * @param companyId 企业ID
     * @return 数据
     */
    public static String companyDocument(String companyId) {
        return getService() + "companies/" + companyId + "/documents";
    }

    /**
     * 展会-企业信息-产品接口
     * @param companyId 企业ID
     * @return 数据
     */
    public static String companyProduct(String companyId) {
        return getService() + "companies/" + companyId + "/products";
    }

    /**
     * 展会-企业介绍H5
     * @param companyId 企业ID
     * @return 数据
     */
    public static String companyIntroduction(String companyId) {
        return getService() + "companies/" + companyId + "/introduction";
    }

    /**
     * 展会-企业关注接口
     * @param companyId 企业ID
     * @return 数据
     */
    public static String attentionCompany(String companyId) {
        return getService() + "companies/" + companyId + "/focus?user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 展会-企业取消关注接口
     * @param companyId 企业ID
     * @return 数据
     */
    public static String cancelCompany(String companyId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/onlineFocusCompanies/" + companyId;
    }

    /**
     * 展会-产品接口
     * @return 数据
     */
    public static String exposProduct() {
        return getService() + "expos/10/products";
    }

    /**
     * 展会-产品信息接口
     * @param productId 产品ID
     * @return 数据
     */
    public static String products(String productId) {
        return getService() + "products/" + productId;
    }

    /**
     * 展会-产品介绍H5
     * @param productId 企业ID
     * @return 数据
     */
    public static String productIntroduction(String productId) {
        return getService() + "products/" + productId + "/introduction";
    }

    /**
     * 展会-产品关注接口
     * @param productId 产品ID
     * @return 数据
     */
    public static String attentionProduct(String productId) {
        return getService() + "products/" + productId + "/favorite?user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 展会-产品取消关注接口
     * @param productId 产品ID
     * @return 数据
     */
    public static String cancelProduct(String productId) {
        return getService() + "products/" + productId + "/favorite?user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 展会-产品列表接口
     * @param industry2Id 展厅ID
     * @return 数据
     */
    public static String industry(String industry2Id) {
        return getService() + "expos/10/industry2/" + industry2Id + "/products";
    }

    /**
     * 资料中心接口
     * @param userId 用户ID
     * @param module 分类
     * @param filter 筛选
     * @param group 分组
     * @param order 排序
     * @return 数据
     */
    public static String center(String userId, String module, String filter, String group, String order) {
        return getService() + "dataCenter/documents?user_id=" + userId + "&module=" + module
                + "&filter=" + filter + "&group=" + group + "&order_by=" + order;
    }

    /**
     * 资料中心-消息列表接口
     * @return 数据
     */
    public static String messages() {
        return getService() + "dataCenter/messages?user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 资料中心-删除通知接口
     * @param messageId 消息ID
     * @return 数据
     */
    public static String deleteMessages(String messageId) {
        return getService() + "dataCenter/messages/" + messageId + "?user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 资料中心-通知详情接口
     * @param messageId 消息ID
     * @return 数据
     */
    public static String messagesDetail(String messageId) {
        return getService() + "dataCenter/messages/" + messageId + "?user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 资料中心-通知已读接口
     * @param messageId 消息ID
     * @return 数据
     */
    public static String messagesIsRead(String messageId) {
        return getService() + "dataCenter/messages/" + messageId;
    }

    /**
     * 资料中心-资料详情接口
     * @param documentId 资料ID
     * @return 数据
     */
    public static String getDocument(String documentId) {
        return getService() + "documents/" + documentId + "?user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 资料中心-收藏资料接口
     * @param documentId 资料ID
     * @return 数据
     */
    public static String collectDocument(String documentId) {
        return getService() + "documents/" + documentId + "/favorite?user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 资料中心-取消收藏资料接口
     * @param documentId 资料ID
     * @return 数据
     */
    public static String cancelCollectDocument(String documentId) {
        return getService() + "dataCenter/favorites/" + documentId + "?user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 获取Other请求
     * @return 数据
     */
    public static String getUserIdBody() {
        return "user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 商圈-获取线上关注企业列表接口
     * @return 数据
     */
    public static String onlineFocus() {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/onlineFocusCompanies";
    }

    /**
     * 商圈-获取线下关注企业列表接口
     * @return 数据
     */
    public static String offlineFocus() {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/offlineFocusCompanies";
    }

    /**
     * 商圈-获取推荐企业列表接口
     * @return 数据
     */
    public static String targetCompanies() {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/targetCompanies";
    }

    /**
     * 商圈-获取信任企业列表接口
     * @return 数据
     */
    public static String trustCompanies() {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/trustCompanies";
    }

    /**
     * 商圈-获取线上关注客户列表接口
     * @return 数据
     */
    public static String onlineFocusCustomers() {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/onlineFocusCustomers";
    }

    /**
     * 商圈-获取线下关注客户列表接口
     * @return 数据
     */
    public static String offlineFocusCustomers() {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/offlineFocusCustomers";
    }

    /**
     * 商圈-获取推荐客户列表接口
     * @return 数据
     */
    public static String targetCustomers() {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/targetCustomers";
    }

    /**
     * 商圈-获取信任客户列表接口
     * @return 数据
     */
    public static String trustCustomers() {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/trustCustomers";
    }

    /**
     * 商圈-客户取消关注接口
     * @param userId 客户ID
     * @return 数据
     */
    public static String cancelAttentionUser(String userId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/onlineFocusCustomers/" + userId;
    }

    /**
     * 商圈-取消推荐客户接口
     * @param userId 客户ID
     * @return 数据
     */
    public static String cancelTargetUser(String userId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/targetCustomers/" + userId;
    }

    /**
     * 商圈-获取目标企业详情接口
     * @param companyId 公司ID
     * @return 数据
     */
    public static String targetCompanies(String companyId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/targetCompanies/" + companyId;
    }

    /**
     * 商圈-获取信任企业详情接口
     * @param companyId 公司ID
     * @return 数据
     */
    public static String trustCompanies(String companyId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/trustCompanies/" + companyId;
    }

    /**
     * 商圈-获取关注企业详情接口
     * @param companyId 公司ID
     * @return 数据
     */
    public static String focusCompanies(String companyId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/focusCompanies/" + companyId;
    }

    /**
     * 商圈-获取目标客户详情接口
     * @param toUserId 客户ID
     * @return 数据
     */
    public static String targetCustomers(String toUserId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/targetCustomers/" + toUserId;
    }

    /**
     * 商圈-获取信任客户详情接口
     * @param toUserId 客户ID
     * @return 数据
     */
    public static String trustCustomers(String toUserId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/trustCustomers/" + toUserId;
    }

    /**
     * 商圈-获取关注客户详情接口
     * @param toUserId 客户ID
     * @return 数据
     */
    public static String focusCustomers(String toUserId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/focusCustomers/" + toUserId;
    }

    /**
     * 商圈-获取关注产品接口
     * @return 数据
     */
    public static String favoritesProduct() {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/favorites";
    }

    /**
     * 商圈-提交信任接口
     * @return 数据
     */
    public static String requestFrien() {
        return getService() + "relation/requestFriend";
    }

    /**
     * 商圈-删除推荐企业接口
     * @param companyId 公司ID
     * @return 数据
     */
    public static String deleteTargetCompanies(String companyId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/targetCompanies/" + companyId;
    }

    /**
     * 首页-获取名片接口
     * @return 数据
     */
    public static String vcard(String userId) {
        return getService() + "users/" + userId + "/vcard";
    }

    /**
     * 首页-获取二维码好友信息接口
     * @return 数据
     */
    public static String getOfflineScanFriendInfo(String vcardNo, String timestp, String type) {
        return getService() + "relation/getOfflineScanFriendInfo?vcard_no=" + vcardNo + "&timestp=" + timestp + "&type=" + type;
    }

    /**
     * 商圈-提交线下信任接口
     * @return 数据
     */
    public static String offlineScanToAddFriend() {
        return getService() + "relation/offlineScanToAddFriend";
    }

    /**
     * 首页-获取收到的信任列表信息接口
     * @return 数据
     */
    public static String acceptInvitations() {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/acceptInvitations";
    }

    /**
     * 首页-获取发出的信任列表信息接口
     * @return 数据
     */
    public static String requestInvitations() {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/requestInvitations";
    }

    /**
     * 首页-获取收到信任详情信息接口
     * @param requestId 申请方id
     * @return 数据
     */
    public static String acceptInvitations(String requestId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/acceptInvitations/" + requestId;
    }

    /**
     * 首页-获取发出信任详情信息接口
     * @param acceptId 接受方id
     * @return 数据
     */
    public static String requestInvitations(String acceptId) {
        return getService() + "relation/users/" + ZhanHuiApplication.getInstance().getUserId() + "/requestInvitations/" + acceptId;
    }

    /**
     * 首页-接受邀请接口
     * @return 数据
     */
    public static String acceptFriend() {
        return getService() + "relation/acceptFriend";
    }

    /**
     * 首页-获取接受或拒绝参数
     * @return 数据
     */
    public static String getTrustDetail(String requestId, String acceptId) {
        return "request_id=" + requestId + "&accept_id=" + acceptId;
    }

    /**
     * 首页-拒绝邀请接口
     * @return 数据
     */
    public static String declineFriend() {
        return getService() + "relation/declineFriend";
    }

    /**
     * 首页-删除邀请接口
     * @return 数据
     */
    public static String deleteAcceptRecord() {
        return getService() + "relation/deleteAcceptRecord";
    }

    /**
     * 首页-删除申请接口
     * @return 数据
     */
    public static String deleteRequestRecord() {
        return getService() + "relation/deleteRequestRecord";
    }

    /**
     * 首页-需求发布接口
     * @return 数据
     */
    public static String demands() {
        return getService() + "ibusiness/demands";
    }

    /**
     * 首页-获取需求发布列表接口
     * @return 数据
     */
    public static String demandsList() {
        return getService() + "ibusiness/demands?user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 首页-获取需求发布详情接口
     * @return 数据
     */
    public static String demandsDetail(String demandId) {
        return getService() + "ibusiness/demands?" + demandId;
    }

    /**
     * 首页-获得需求匹配结果(企业列表)接口
     * @return 数据
     */
    public static String demandsCompanyList(String demandId) {
        return getService() + "ibusiness/matchings?demand_id=" + demandId + "&type=poster";
    }

    /**
     * 首页-获得需求匹配结果(企业列表)接口
     * @return 数据
     */
    public static String demandsResult(String demandId) {
        return getService() + "ibusiness/matchings/" + demandId + "?type=" + (Constants.VERSION_IS_USER ? "poster" : "receiver");
    }

    /**
     * 首页-删除需求匹配结果接口
     * @return 数据
     */
    public static String deleteDemandsResult(String demandId) {
        return getService() + "ibusiness/matchings/" + demandId;
    }

    /**
     * 首页-获取匹配的需求列表接口（企业版）
     * @param state company_handle_state=0 为最新的需求匹配结果(未同意的)
                     company_handle_state=1 为已同意的需求匹配结
     * @return 数据
     */
    public static String demandsList(String state) {
        return getService() + "ibusiness/matchings?" + ZhanHuiApplication.getInstance().getCompanyId() + "&type=receiver&company_handle_state=" + state;
    }

    /**
     * 首页-获取预约列表接口
     * @return 数据
     */
    public static String reservations() {
        return getService() + "reservations?user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 首页-获取预约详情接口
     * @param reservationId 预约Id
     * @return 数据
     */
    public static String reservationDetail(String reservationId) {
        return getService() + "reservations/" + reservationId + "?user_id=" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 首页-取消预约接口
     * @param reservationId 预约Id
     * @return 数据
     */
    public static String cancelReservation(String reservationId, String state) {
        return getService() + "reservations/" + reservationId + "?state=" + state;
    }

    /**
     * 首页-发起预约接口
     * @return 数据
     */
    public static String applyReservation() {
        return getService() + "reservations/apply";
    }

    /**
     * 首页-参展指南H5接口
     * @return 数据
     */
    public static String expoGuide() {
        return getService() + "users/" + ZhanHuiApplication.getInstance().getUserId() + "/expo-guide";
    }

    /**
     * 首页-电子导览H5接口
     * @return 数据
     */
    public static String expoMap() {
        return getService() + "users/" + ZhanHuiApplication.getInstance().getUserId() + "/expo-map";
    }

    /**
     * 侧滑-获取个人信息接口
     * @return 数据
     */
    public static String users() {
        return getService() + "users/" + ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 侧滑-上传头像接口
     * @return 数据
     */
    public static String updateIcon() {
        return getService() + "users/" + ZhanHuiApplication.getInstance().getUserId() + "/updateIcon";
    }
}

