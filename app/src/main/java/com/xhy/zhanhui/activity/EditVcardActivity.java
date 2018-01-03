package com.xhy.zhanhui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.utils.PublicMethod;
import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.bean.JsonBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.CommonUtils;
import com.aaron.aaronlibrary.utils.Constants;
import com.aaron.aaronlibrary.utils.GetJsonDataUtil;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.base.ZhanHuiApplication;
import com.xhy.zhanhui.http.domain.UserInfoBean;
import com.xhy.zhanhui.http.vo.UserInfoVo;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

/**
 * 编辑我的名片页面
 * Created by Aaron on 26/12/2017.
 */

public class EditVcardActivity extends ZhanHuiActivity {

    private UserInfoBean.Obj data;
    private ImageView ivAvatar;
    private EditText etName, etCard, etCompany, etTitle, etPhone, etEmail, etWebsite;
    private TextView etAddress;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private boolean isParseFinish;
    private OnParseDataListener onParseDataListener;

    private String fileTempPath; // 头像临时文件
    private String fileOutTempPath; // 头像裁剪后文件

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_edit_vcard;
    }

    @Override
    protected void findView() {
        super.findView();
        ivAvatar = findViewById(R.id.ivAvatar);
        etName = findViewById(R.id.etName);
        etCard = findViewById(R.id.etCard);
        etCompany = findViewById(R.id.etCompany);
        etTitle = findViewById(R.id.etTitle);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etWebsite = findViewById(R.id.etWebsite);
        findAndSetClickListener(R.id.rlAvatar);
        findAndSetClickListener(R.id.rlAddress);
        findAndSetClickListener(R.id.btnSave);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("个人信息");
        etAddress.setKeyListener(null);
        initJsonData(); // 加载城市选择数据
        getData();
    }

    private void getData() {
        PostCall.get(mContext, ServerUrl.users(), new BaseMap(), new PostCall.PostResponse<UserInfoBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, UserInfoBean bean) {
                data = bean.getData();
                ImageUtils.loadImageCircle(mContext, data.getIcon(), ivAvatar);
                etName.setText(data.getV_name());
                etCard.setText(data.getV_idCard());
                etCompany.setText(data.getV_company());
                etTitle.setText(data.getV_title());
                etAddress.setText(data.getV_address());
                etPhone.setText(data.getV_telephone());
                etEmail.setText(data.getV_email());
                etWebsite.setText(data.getV_website());
//                etName.setText("王鹏程");
//                etCard.setText("210103199101051216");
//                etCompany.setText("Aaron");
//                etTitle.setText("开发经理");
//                etAddress.setText("辽宁省沈阳市沈河区");
//                etPhone.setText("18609817413");
//                etEmail.setText("wpc5586@163.com");
//                etWebsite.setText("www.baidu.com");
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, UserInfoBean.class);
    }

    private void save() {
        UserInfoVo vo = new UserInfoVo();
        vo.setV_name(etName.getText().toString());
        vo.setV_idCard(etCard.getText().toString());
        vo.setV_company(etCompany.getText().toString());
        vo.setV_title(etTitle.getText().toString());
        vo.setV_address(etAddress.getText().toString());
        vo.setV_telephone(etPhone.getText().toString());
        vo.setV_email(etEmail.getText().toString());
        vo.setV_website(etWebsite.getText().toString());
        vo.setId(data.getVcard_id());
        PostCall.postJson(mContext, ServerUrl.users(), vo, new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                showToast("保存成功");
                finish();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, BaseBean.class);
    }

    private void sendAvatar() {
        showProgressDialog("上传头像中");
        PostCall.postFile(mContext, ServerUrl.updateIcon(), new File(fileOutTempPath), new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                dismissProgressDialog();
                ImageUtils.loadImageCircle(mContext, fileOutTempPath, ivAvatar);
                ZhanHuiApplication.getInstance().updateAvatar(fileOutTempPath);
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, BaseBean.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_MY_USERIMAGE_CAMERA:
                    fileOutTempPath = AppInfo.openPhotoZoom(mContext, fileTempPath);
                    break;
                case Constants.REQUEST_PHOTO:
                    if (data.getData() != null)
                        fileOutTempPath = AppInfo.openPhotoZoom(mContext, data.getData());
                    break;
                case Constants.REQUEST_MY_USERIMAGE_CUT:
                    if (!TextUtils.isEmpty(fileOutTempPath))
                        sendAvatar();
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rlAvatar:
                showAlertListDialog(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                fileTempPath = AppInfo.openCamera(mContext);
                                break;
                            case 1:
                                AppInfo.openPhoto(mContext);
                                break;
                        }
                    }
                }, true);
                break;
            case R.id.rlAddress:
                if (!isParseFinish) {
                    onParseDataListener = new OnParseDataListener() {
                        @Override
                        public void success() {
                            ShowPickerView();
                        }
                    };
                    showProgressDialog("加载中");
                } else
                    ShowPickerView();
                break;
            case R.id.btnSave:
                save();
                break;
        }
    }

    private void ShowPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                etAddress.setText(tx);
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(16)
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
        isParseFinish = true;
        if (onParseDataListener != null)
            onParseDataListener.success();
    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public interface OnParseDataListener {
        public void success();
    }
}
