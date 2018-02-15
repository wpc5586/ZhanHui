package com.xhy.zhanhui.domain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.aaron.aaronlibrary.easeui.ui.ChatActivity;
import com.xhy.zhanhui.activity.BusinessCompanyDetailActivity;
import com.xhy.zhanhui.activity.BusinessUserDetailActivity;
import com.xhy.zhanhui.activity.CenterDetailActivity;
import com.xhy.zhanhui.activity.CenterMessageDetailActivity;
import com.xhy.zhanhui.activity.CompanyDetailActivity;
import com.xhy.zhanhui.activity.CompanyListActivity;
import com.xhy.zhanhui.activity.IntelligentBusinessDetailActivity;
import com.xhy.zhanhui.activity.OrderDetailActivity;
import com.xhy.zhanhui.activity.OrderSponsorActivity;
import com.xhy.zhanhui.activity.ProductDetailActivity;
import com.xhy.zhanhui.activity.ProductListActivity;
import com.xhy.zhanhui.activity.TrustConfirmCompanyActivity;
import com.xhy.zhanhui.activity.TrustConfirmUserActivity;
import com.xhy.zhanhui.activity.TrustDetailActivity;
import com.xhy.zhanhui.activity.UrlPdfActivity;
import com.xhy.zhanhui.activity.UrlWebActivity;
import com.xhy.zhanhui.activity.VcardUserActivity;
import com.xhy.zhanhui.http.domain.CenterBean;
import com.xhy.zhanhui.http.domain.CenterMessageBean;
import com.xhy.zhanhui.http.domain.TrustCompanyBean;
import com.xhy.zhanhui.http.domain.TrustUserBean;

/**
 * 跳转公共方法
 * Created by Aaron on 2017/12/12.
 */

public class StartActivityUtils {

    /**
     * 跳转企业详情页面
     *
     * @param mContext  上下文
     * @param companyId 企业ID
     */
    public static void startCompanyDetail(Context mContext, String companyId) {
        Intent intent = new Intent(mContext, CompanyDetailActivity.class);
        intent.putExtra("company_id", companyId);
        mContext.startActivity(intent);
    }

    /**
     * 跳转展厅企业列表页面
     *
     * @param mContext 上下文
     * @param hallId   展厅ID
     * @param hallName 展厅名称
     */
    public static void startCompanyList(Context mContext, String hallId, String hallName) {
        Intent intent = new Intent(mContext, CompanyListActivity.class);
        intent.putExtra("hall_id", hallId);
        intent.putExtra("hall_name", hallName);
        mContext.startActivity(intent);
    }

    /**
     * 跳转企业发起预约页面
     *
     * @param mContext    上下文
     * @param eventId     展会ID
     * @param eventName   展会名称
     * @param boothNo     展位
     * @param companyId   企业ID
     * @param companyName 企业名称
     */
    public static void startSponsorOrder(Context mContext, String eventId, String eventName, String boothNo, String companyId, String companyName) {
        Intent intent = new Intent(mContext, OrderSponsorActivity.class);
        intent.putExtra("eventId", eventId);
        intent.putExtra("eventName", eventName);
        intent.putExtra("boothNo", boothNo);
        intent.putExtra("companyId", companyId);
        intent.putExtra("companyName", companyName);
        mContext.startActivity(intent);
    }

    /**
     * 跳转产品详情页面
     *
     * @param mContext  上下文
     * @param productId 产品ID
     */
    public static void startProductDetail(Context mContext, String productId) {
        Intent intent = new Intent(mContext, ProductDetailActivity.class);
        intent.putExtra("product_id", productId);
        mContext.startActivity(intent);
    }

    /**
     * 跳转行业产品列表页面
     *
     * @param mContext      上下文
     * @param industry2Id   行业ID
     * @param industry2Name 行业名称
     */
    public static void startProductList(Context mContext, String industry2Id, String industry2Name) {
        Intent intent = new Intent(mContext, ProductListActivity.class);
        intent.putExtra("industry2_id", industry2Id);
        intent.putExtra("industry2_name", industry2Name);
        mContext.startActivity(intent);
    }

    /**
     * 跳转网址
     *
     * @param mContext 上下文
     * @param url      地址
     */
    public static void startWebUrl(Context mContext, String url, String title) {
        Intent intent = new Intent(mContext, UrlWebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        mContext.startActivity(intent);
    }

    /**
     * 跳转Pdf
     *
     * @param mContext 上下文
     * @param url      地址
     */
    public static void startPdfUrl(Context mContext, String url, String title) {
        Intent intent = new Intent(mContext, UrlPdfActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        mContext.startActivity(intent);
    }

    /**
     * 跳转资料详情页面
     *
     * @param mContext 上下文
     * @param item     资料信息
     */
    public static void startCenterDetail(Context mContext, CenterBean.Obj.Item item) {
        Intent intent = new Intent(mContext, CenterDetailActivity.class);
        intent.putExtra("item", item);
        mContext.startActivity(intent);
    }

    /**
     * 跳转资料详情页面
     *
     * @param mContext     上下文
     * @param documentId   资料Id
     * @param documentIcon 资料Icon
     * @param documentName 资料信息
     */
    public static void startCenterDetail(Context mContext, String documentId, String documentIcon, String documentName) {
        CenterBean.Obj.Item itemData = new CenterBean().new Obj().new Item();
        itemData.setDocument_id(documentId);
        itemData.setDocument_icon(documentIcon);
        itemData.setDocument_name(documentName);
        Intent intent = new Intent(mContext, CenterDetailActivity.class);
        intent.putExtra("item", itemData);
        mContext.startActivity(intent);
    }

    /**
     * 跳转资料详情页面
     *
     * @param mContext  上下文
     * @param item      资料信息
     * @param isCollect true 已收藏  false 未收藏
     */
    public static void startCenterDetail(Context mContext, CenterBean.Obj.Item item, boolean isCollect) {
        Intent intent = new Intent(mContext, CenterDetailActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("is_collect", isCollect);
        mContext.startActivity(intent);
    }

    /**
     * 跳转通知详情页面
     *
     * @param mContext    上下文
     * @param messageData 通知数据
     */
    public static void startCenterMessageDetail(Context mContext, CenterMessageBean.Obj messageData) {
        Intent intent = new Intent(mContext, CenterMessageDetailActivity.class);
        intent.putExtra("messageData", messageData);
        mContext.startActivity(intent);
    }

    /**
     * 跳转商圈企业详情页面
     *
     * @param mContext  上下文
     * @param companyId 企业
     * @param type      企业详情类型   0：目标企业  1：信任企业  2：关注企业
     */
    public static void startTrustCompanyDetail(Context mContext, String companyId, int type) {
        Intent intent = new Intent(mContext, BusinessCompanyDetailActivity.class);
        intent.putExtra("company_id", companyId);
        intent.putExtra("type", type);
        mContext.startActivity(intent);
    }

    /**
     * 跳转商圈客户详情页面
     *
     * @param mContext 上下文
     * @param toUserId 客户
     * @param type     客户详情类型   0：目标客户  1：信任客户  2：关注客户
     */
    public static void startTrustUserDetail(Context mContext, String toUserId, int type) {
        Intent intent = new Intent(mContext, BusinessUserDetailActivity.class);
        intent.putExtra("toUserId", toUserId);
        intent.putExtra("type", type);
        mContext.startActivity(intent);
    }

    /**
     * 跳转商圈客户详情页面
     *
     * @param mContext 上下文
     * @param toUserId 客户
     * @param type     客户详情类型   0：目标客户  1：信任客户  2：关注客户
     * @param title 标题
     */
    public static void startTrustUserDetail(Context mContext, String toUserId, int type, String title) {
        Intent intent = new Intent(mContext, BusinessUserDetailActivity.class);
        intent.putExtra("toUserId", toUserId);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        mContext.startActivity(intent);
    }

    /**
     * 跳转商圈企业信任页面
     *
     * @param mContext 上下文
     * @param bean     企业信息
     */
    public static void startTrustCompany(Context mContext, TrustCompanyBean bean) {
        Intent intent = new Intent(mContext, TrustConfirmCompanyActivity.class);
        intent.putExtra("bean", bean);
        mContext.startActivity(intent);
    }

    /**
     * 跳转商圈用户信任页面
     *
     * @param mContext  上下文
     * @param trustData 客户信息
     */
    public static void startTrustUser(Context mContext, TrustUserBean.Obj trustData) {
        Intent intent = new Intent(mContext, TrustConfirmUserActivity.class);
        intent.putExtra("trustData", trustData);
        mContext.startActivity(intent);
    }

    /**
     * 跳转二维码用户信任页面
     *
     * @param mContext 上下文
     * @param vcardNo  id
     * @param timestp  时间戳
     * @param type  类型
     */
    public static void startTrustUser(Context mContext, String vcardNo, String timestp, String type) {
        Intent intent = new Intent(mContext, TrustConfirmUserActivity.class);
        intent.putExtra("vcard_no", vcardNo);
        intent.putExtra("timestp", timestp);
        intent.putExtra("type", type);
        mContext.startActivity(intent);
    }

    /**
     * 跳转邀请详情页面
     *
     * @param mContext  上下文
     * @param requestId 申请Id
     * @param acceptId  接受Id
     * @param isReceive 是否为收到邀请详情
     */
    public static void startTrustDetail(Context mContext, String requestId, String acceptId, boolean isReceive) {
        Intent intent = new Intent(mContext, TrustDetailActivity.class);
        intent.putExtra("request_id", requestId);
        intent.putExtra("accept_id", acceptId);
        intent.putExtra("is_receive", isReceive);
        ((Activity) mContext).startActivityForResult(intent, 2);
    }

    /**
     * 跳转聊天详情页面
     *
     * @param mContext 上下文
     * @param userId   环信userId
     */
    public static void startChat(Context mContext, String userId) {
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("userId", userId);
        mContext.startActivity(intent);
    }

    /**
     * 跳转聊天详情页面
     *
     * @param mContext 上下文
     * @param userId   环信userId
     * @param nickName 用户昵称
     */
    public static void startChat(Context mContext, String userId, String nickName) {
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("nickName", nickName);
        mContext.startActivity(intent);
    }

    /**
     * 跳转电子名片页面
     *
     * @param mContext 上下文
     * @param userId   userId
     */
    public static void startVcard(Context mContext, String userId) {
        Intent intent = new Intent(mContext, VcardUserActivity.class);
        intent.putExtra("userId", userId);
        mContext.startActivity(intent);
    }

    /**
     * 跳转电子名片页面（不显示二维码）
     *
     * @param mContext 上下文
     * @param userId   userId
     */
    public static void startVcardNoQRcode(Context mContext, String userId) {
        Intent intent = new Intent(mContext, VcardUserActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("isShow", false);
        mContext.startActivity(intent);
    }

    /**
     * 跳转预约详情页面
     *
     * @param mContext      上下文
     * @param reservationId 预约Id
     */
    public static void startOrderDetail(Context mContext, String reservationId) {
        Intent intent = new Intent(mContext, OrderDetailActivity.class);
        intent.putExtra("reservationId", reservationId);
        ((Activity) mContext).startActivityForResult(intent, 1);
    }

    /**
     * 跳转需求详情页面
     *
     * @param mContext      上下文
     * @param demandId 需求Id
     */
    public static void startDemandDetail(Context mContext, String demandId) {
        Intent intent = new Intent(mContext, IntelligentBusinessDetailActivity.class);
        intent.putExtra("demandId", demandId);
        mContext.startActivity(intent);
    }

    /**
     * 跳转需求结果详情页面
     *
     * @param mContext      上下文
     * @param matchingId 结果Id
     */
    public static void startDemandResultDetail(Context mContext, String matchingId) {
        Intent intent = new Intent(mContext, IntelligentBusinessDetailActivity.class);
        intent.putExtra("matchingId", matchingId);
        mContext.startActivity(intent);
    }
}
