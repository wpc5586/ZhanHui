package com.aaron.aaronlibrary.utils;

import com.xhy.zhanhui.BuildConfig;

/**
 * 全局常量
 * 
 * @author wangpc
 *
 */
public class Constants {

    /**
     * 开发阶段将DEBUGABLE设置为true，便于调试。
     */
    public static final boolean DEBUGABLE = BuildConfig.DEBUGABLE;

    /**
     * 当前版本是否为用户版本  true 为观众版   false 为企业版
     */
    public static final boolean VERSION_IS_USER = "one".equals(BuildConfig.PACKAGE);

    /**
     * 全局常量：二期开发内容是否显示
     */
    public static final boolean bVersion2 = false;

    /**
     * 全局常量：7.0以上内容提供者包名
     */
    public static final String FILE_CONTENT_FILEPROVIDER = "com.xhy.zhanhui.fileProvider";

    /**
     * 全局常量：图片压缩比例
     */
    public static final int IMAGE_COMPRESSION_RATIO = 90;

    /**
     * <p>
     * 全局常量：上拉加载个数
     * </p>
     */
    public static final int PULL_TO_FOOT_ROWS = 10;

    /**
     * <p>
     * 全局常量：页面跳转传递数据的Key
     * </p>
     */
    public static final String INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE = "AllToEtSingleTitle";
    public static final String INTENT_ALL_TO_EDITTEXT_SINGLE_CONTENT = "AllToEtSingleContent";
    public static final String INTENT_ALL_TO_EDITTEXT_SINGLE_CANEMPTY = "AllToEtSingleCanEmpty";
    public static final String INTENT_ALL_TO_EDITTEXT_SINGLE_TWO_ROW = "AllToEtSingleTwoRow";
    public static final String INTENT_ALL_TO_EDITTEXT_SINGLE_CHANGENUM = "AllToEtSingleChangeNum";
    public static final String INTENT_ALL_TO_EDITTEXT_SINGLE_BUTTONNAME = "AllToEtSingleButtonName";
    public static final String INTENT_ALL_TO_EDITTEXT_NUM = "AllToEtSingleNum";
    public static final String INTENT_ALL_TO_EDITTEXT_NUM_LEAST = "AllToEtSingleNumLeast";
    public static final String INTENT_ALL_TO_EDITTEXT_TYPE = "AllToEtSingleType";
    public static final String INTENT_EDITTEXT_SINGLE_TO_ALL_CONTENT = "EtSingleToAllContent";
    public static final String INTENT_ALL_TO_OTHER_LIBRARY = "AllToOtherLibrary";
    public static final String INTENT_ALL_TO_DETAIL_TYPE = "AllToDetailType";
    public static final String INTENT_ALL_TO_DETAIL_DATA = "AllToDetailData";
    public static final String INTENT_ALL_TO_MAP_DETAIL_IMGLIST="AllToDetailImageList";
    public static final String INTENT_ALL_TO_MAP_DETAIL_ISVIDEO="AllToDetailIsVideo";
    public static final String INTENT_ALL_TO_MAP_DETAIL_VIDEOPATH="AllToDetailVideoPath";
    public static final String INTENT_ALL_TO_DETAIL_POSITION = "AllToDetailPosition";
    public static final String INTENT_ALL_TO_MAP_DETAIL_LATITUDE = "AllToMapDetailLatitude";
    public static final String INTENT_ALL_TO_MAP_DETAIL_LONGITUDE = "AllToMapDetailLongitude";
    public static final String INTENT_ALL_TO_MAP_DETAIL_TITLE = "AllToMapDetailTitle";
    public static final String INTENT_ALL_TO_MAP_DETAIL_CONTENT = "AllToMapDetailContent";
    public static final String INTENT_PURCHASE_TO_SEARCH = "PurchaseToSearch";
    public static final String INTENT_POSITION_TO_EDIT = "PositionToEdit";
    public static final String INTENT_QUICK_TO_DETAIL_POSITION = "QuickToDetailPosition";
    public static final String INTENT_QUICK_TO_DETAIL_DATA = "QuickToDetailData";
    public static final String INTENT_ALL_TO_ALBUM_TYPE = "AllToAlbumType";
    public static final String INTENT_ALL_TO_ALBUM_NUM = "AllToAlbumNum";
    public static final String INTENT_ALL_TO_ALBUM_ENTRY = "AllToAlbumEntry";
    public static final String INTENT_ALL_TO_LIST_DIALOG = "AllToListDialog";
    public static final String INTENT_PERSON_TO_LIBRARY_ID = "PersonToLibraryId";
    public static final String INTENT_PERSON_TO_LIBRARY_USERID = "PersonToLibraryUserId";
    public static final String INTENT_PERSON_TO_LIBRARY_MONTH = "PersonToLibraryUserMonth";
    public static final String INTENT_PERSON_TO_LIBRARY_MOMENT = "PersonToLibraryUserMoment";
    public static final String INTENT_PERSON_TO_LIBRARY_HEAD = "PersonToLibraryUserHead";
    public static final String INTENT_ALL_TO_HEADIMG = "AllToHeadImg";
    public static final String INTENT_ALL_TO_HEADIMG_URL = "AllToHeadImgUrl";
    public static final String INTENT_ALL_TO_HEADIMG_SIGN = "AllToHeadImgSign";
    public static final String INTENT_ALL_TO_HEADIMG_NUM = "AllToHeadImgNum";
    public static final String INTENT_ALL_TO_MAIN_FRAGMENT_NUM = "AllToMainFragmentNum";
    public static final String INTENT_PERSONAl_TO_CENTER="PeraonalToCenter";
    public static final String INTENT_PERSONAl_TO_CENTER_ISGROUP="PeraonalToCenterIsGroup";
    public static final String INTENT_ALBUM_TO_EDIT_ISPIC = "AlbumToEditIsPic";
    public static final String INTENT_ALBUM_TO_EDIT_DATA = "AlbumToEditData";
    public static final String INTENT_ALBUM_TO_FILE_DATA = "AlbumToFileData";
    public static final String INTENT_ALBUM_TO_FILE_DATA1 = "AlbumToFileData1";
    public static final String INTENT_ALBUM_TO_FILE_DATA2 = "AlbumToFileData2";
    public static final String INTENT_ALBUM_TO_FILE_NAME = "AlbumToFileName";
    public static final String INTENT_ALBUM_TO_EDIT_DATA_LIST = "AlbumToEditDataList";
    public static final String INTENT_PERSON_TO_FRIEND_DETAILS="PersonToDetails";
    public static final String INTENT_PERSON_TO_FRIEND_DATA="PersonToDetailsData";
    public static final String INTENT_RESELECTION_DATA = "ReselectionData";
    public static final String INTENT_SEARCH_MERCHANT_TO_QUICK="MerchantToQuick";
    public static final String INTENT_HISTORY_MEICHANT_TO_QUICK="HistoryToQuick";
    public static final String INTENT_DISCOVERY_TO_VIDEO_DATA = "DiscoveryToVideoData";
    public static final String INTENT_FLOAT_TO_VIDEO_POSITION = "FloatToVideoPosition";
    public static final String INTENT_FLOAT_TO_VIDEO_MEDIA_INDEX = "FloatToVideoMediaIndex";
    public static final String INTENT_DISCOVERY_TO_VIDEO_POSITION = "DiscoveryToVideoPosition";
    public static final String INTENT_SETTING_TO_SERVER_IMG = "SettingToServerImg";
    public static final String INTENT_MOMENT_DELETE_ID = "MomentDeleteId";
    public static final String INTENT_USERID = "IntentUserId";

    /**
     * <p>
     * 全局常量：头像页面跳转的请求码
     * </p>
     */
    public static final int REQUEST_MY_USERIMAGE_ALBUM = 10;
    public static final int REQUEST_MY_USERIMAGE_CAMERA = 11;
    public static final int REQUEST_MY_USERIMAGE_CUT = 12;

    /**
     * <p>
     * 全局常量：个人库跳转库地图打开
     * </p>
     */
    public static final int REQUEST_LIBRARY_TO_MAP = 20;

    /**
     * <p>
     * 全局常量：快速编辑跳转选择地址请求码
     * </p>
     */
    public static final int REQUEST_EDIT_TO_SELECT = 30;

    /**
     * 全局常量：跳转相册请求码
     */
    public static final int REQUEST_ALL_TO_ALBUM = 40;

    /**
     * 全局常量：跳转相册请求码
     */
    public static final int REQUEST_DOUBLE_TO_DETAILS = 50;

    /**
     * 全局常量：单行编辑跳转的请求码
     */
    public static final int REQUEST_ALL_TO_EDITTEXT_SINGLE = 60;
    public static final int RESULT_ALL_TO_EDITTEXT_SINGLE = 61;

    /**
     * 全局常量：相册请求码
     */
    public static final int REQUEST_PHOTO = 70;
}
