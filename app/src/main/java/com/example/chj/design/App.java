package com.example.chj.design;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chj.design.utils.FileUtils;
import com.example.chj.design.widget.CustomProgressDialog;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by ff on 2018/5/22.
 */

public class App extends Application {

    private BigDecimal PROPORTION;// 屏幕宽比例
    private int WIDTH_PIXELS;// 屏幕宽度
    private int HEIGHT_PIXELS;// 屏幕高度
    private float DENSITY;// 屏幕密度
    public final float TITLE_HEIGHT = 0.12f;// title高
    public final float TITLE_TEXT_WIDTH = 0.55f;// title正文宽度
    public final float TITLE_LR_IMG_BUTTON_WIDTH = 0.1f;// title左右两侧字体图片按钮宽度
    public final float TITLE_LR_FONT_BUTTON_WIDTH = 0.2f;// title左右两侧文字按钮宽度（上限4个字）
    public final int TITLE_CENTER_SIZE = 20;// title标题字体大小
    public final int TITLE_IMAGE_SIZE = 19;// title字体图标大小
    public final int TITLE_STRING_SIZE = 15;// title右侧字符串大小
    public final int CARD_TITLE_CENTER_SIZE = 15;// card_title标题字体大小
    public final float CARD_TITLE_HEIGHT = 0.08f;// card_title高
    public String IMAGE_CACHE_DIR;
    public String PACKAGE_NAME;
    public String DOWNLOAD_FILE_DIR;
    private Toast toast;
    private CustomProgressDialog dialogWait = null;
    public boolean isAutoLoginSuccess;//自动登录成功标志

    private static App mContext;
    public static Handler mMainThreadHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        init();

        initLogger();
        mMainThreadHandler = new Handler();

        //在6.0(M)版本下直接创建应用对应的文件夹
        //在6.0(M)版本以上的需要先进行权限申请
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            FileUtils.init(this);
        }
    }

    /**
     * 初始化Logger库
     */
    private void initLogger() {
        Logger.init("com.example.chj.design");
    }

    public static App getApplication() {
        return mContext;
    }

    /**
     * 初始化
     */
    public void init() {

        //获取各种地址、包名
        IMAGE_CACHE_DIR = getExternalFilesDir("imgcache").toString();
        DOWNLOAD_FILE_DIR = getExternalFilesDir("downloadfile").toString();
        PACKAGE_NAME = getPackageName();
    }

    /**
     * Toast显示提示信息
     *
     * @param msg 提示信息
     */
    public void showMessage(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示键盘
     *
     * @param view
     */
    public void showKeyboard(final View view) {
        InputMethodManager imm = (InputMethodManager) getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    /**
     * 获取键盘显示状态
     *
     * @return
     */
    public boolean isKeyboardShowing() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        return isOpen;
    }

    /**
     * 检测手机号码
     *
     * @param mobiles
     * @return
     */
    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^13[0-9]{9}$|15[012356789]{1}[0-9]{8}$|18[0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 过滤字符串中的特殊字符
     *
     * @return time
     */
    public String stringFilter(String str, String filter) {
        String filterStr = null;
        Matcher m;
        try {
            Pattern p = Pattern.compile(filter);
            m = p.matcher(str);
            filterStr = m.replaceAll("");
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
        return filterStr;
    }

    /**
     * 检测网络连接
     *
     * @return
     */
    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取计算比例后dp大小
     *
     * @param dpSize
     * @return
     */
    public int getDpSize(int dpSize) {
        return getProportion().multiply(new BigDecimal(dpSize)).intValue();
    }

    /**
     * 获取本地比例数据，没有则重新计算
     *
     * @return
     */
    public BigDecimal getProportion() {
        if (PROPORTION == null) {
            SharedPreferences mSharedPreferences = getApplicationContext()
                    .getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            String str = mSharedPreferences.getString("proportion", "");
            if (str == null || str.equals("")) {
                calculateWidthAndHeight(mSharedPreferences);
            } else {
                WIDTH_PIXELS = mSharedPreferences.getInt("widthPixels", 0);
                HEIGHT_PIXELS = mSharedPreferences.getInt("heightPixels", 0);
                PROPORTION = new BigDecimal(str);
                DENSITY = mSharedPreferences.getFloat("density", 0);
            }
        }
        return PROPORTION;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getWidthPixels() {
        if (WIDTH_PIXELS == 0) {
            SharedPreferences mSharedPreferences = getApplicationContext()
                    .getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            WIDTH_PIXELS = mSharedPreferences.getInt("widthPixels", 0);
            if (WIDTH_PIXELS == 0) {
                calculateWidthAndHeight(mSharedPreferences);
            } else {
                HEIGHT_PIXELS = mSharedPreferences.getInt("heightPixels", 0);
                PROPORTION = new BigDecimal(mSharedPreferences.getString(
                        "proportion", ""));
                DENSITY = mSharedPreferences.getFloat("density", 0);
            }
        }
        return WIDTH_PIXELS;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public int getHeightPixels() {
        if (HEIGHT_PIXELS == 0) {
            SharedPreferences mSharedPreferences = getApplicationContext()
                    .getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            HEIGHT_PIXELS = mSharedPreferences.getInt("heightPixels", 0);
            if (HEIGHT_PIXELS == 0) {
                calculateWidthAndHeight(mSharedPreferences);
            } else {
                WIDTH_PIXELS = mSharedPreferences.getInt("widthPixels", 0);
                PROPORTION = new BigDecimal(mSharedPreferences.getString(
                        "proportion", ""));
                DENSITY = mSharedPreferences.getFloat("density", 0);
            }
        }

        return HEIGHT_PIXELS;
    }

    /**
     * 获取宽高与DP比例（DP比例用于字体与字体图标的大小，非精准）
     */
    private void calculateWidthAndHeight(SharedPreferences mSharedPreferences) {
        DisplayMetrics dm = getApplicationContext().getResources()
                .getDisplayMetrics();

        // 计算当前设备宽度密度比与基础宽度密度比的比例，适用于分辨率800*480，密度1.5为基准的开发
        BigDecimal phone = new BigDecimal(dm.widthPixels).divide(
                new BigDecimal(dm.density), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal normal = new BigDecimal(480).divide(new BigDecimal(1.5), 2,
                BigDecimal.ROUND_HALF_UP);
        BigDecimal proportion = phone.divide(normal, 2, BigDecimal.ROUND_HALF_UP);


        PROPORTION = proportion;
        WIDTH_PIXELS = dm.widthPixels;
        HEIGHT_PIXELS = dm.heightPixels;
        DENSITY = dm.density;
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("widthPixels", dm.widthPixels);
        mEditor.putInt("heightPixels", dm.heightPixels);
        mEditor.putString("proportion", proportion.toString());
        mEditor.putFloat("density", dm.density);
        mEditor.commit();
    }

    /**
     * 设置控件宽高 <br>
     * 特殊设置时说明<br>
     * 1f ： MATCH_PARENT <br>
     * 0f ： WRAP_CONTENT
     *
     * @param vc     View控件
     * @param width  宽(相对宽度的比例)
     * @param height 高(相对宽度的比例)
     */
    public void setMLayoutParam(View vc, float width, float height) {
        ViewGroup.LayoutParams lp = vc.getLayoutParams();
        lp.width = width == 1f ? ViewGroup.LayoutParams.MATCH_PARENT
                : (width == 0f ? ViewGroup.LayoutParams.WRAP_CONTENT
                : (int) (getWidthPixels() * width));
        lp.height = height == 1f ? ViewGroup.LayoutParams.MATCH_PARENT
                : (height == 0f ? ViewGroup.LayoutParams.WRAP_CONTENT
                : (int) (getWidthPixels() * height));
        vc.setLayoutParams(lp);
    }

    /**
     * 设置控件Margin
     *
     * @param vc     View控件
     * @param left   左侧(相对宽度的比例)
     * @param top    顶部(相对宽度的比例)
     * @param right  右侧(相对宽度的比例)
     * @param bottom 底部(相对宽度的比例)
     */
    public void setMViewMargin(View vc, float left, float top, float right,
                               float bottom) {
        try {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) vc
                    .getLayoutParams();
            params.setMargins((int) (getWidthPixels() * left),
                    (int) (getWidthPixels() * top),
                    (int) (getWidthPixels() * right),
                    (int) (getWidthPixels() * bottom));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置View相对比例的Padding
     *
     * @param vc     View控件
     * @param left   左侧(相对宽度的比例)
     * @param top    顶部(相对宽度的比例)
     * @param right  右侧(相对宽度的比例)
     * @param bottom 底部(相对宽度的比例)
     */
    public void setMViewPadding(View vc, float left, float top, float right,
                                float bottom) {
        vc.setPadding((int) (getWidthPixels() * left),
                (int) (getWidthPixels() * top),
                (int) (getWidthPixels() * right),
                (int) (getWidthPixels() * bottom));

    }

    /**
     * 设置控件字体
     *
     * @param tv
     * @param dpSize
     */
    public void setMTextSize(TextView tv, int dpSize) {
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                getProportion().multiply(new BigDecimal(dpSize)).intValue());
    }

    /**
     * 设置TextView的最大宽度
     *
     * @param tv
     * @param maxwidth
     */
    public void setMMaxWidth(TextView tv, float maxwidth) {
        tv.setMaxWidth((int) (getWidthPixels() * maxwidth));
    }

    public int dip2px(float dpValue) {
        final float scale = DENSITY;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dip(float pxValue) {
        final float scale = DENSITY;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * byte[]转string
     *
     * @param message
     * @return string or null
     */
    public String byteToString(byte[] message) {
        String result;
        try {
            result = new String(message, "utf-8");
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得图片缓存目录
     **/
    public String getImageDirectory() {
        String dir = IMAGE_CACHE_DIR;
        if (dir == null) {
            dir = getSDCardPath() + "/Android/data/" + PACKAGE_NAME + "/files/imgcache";
        }
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dir;
    }

    /**
     * 获得下载缓存目录
     **/
    public String getDownloadFileDirectory() {
        String dir = DOWNLOAD_FILE_DIR;
        if (dir == null) {
            dir = getSDCardPath() + "/Android/data/" + PACKAGE_NAME
                    + "/files/downloadfile";
        }
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dir;
    }

    /**
     * 取SD卡路径
     **/
    public String getSDCardPath() {
        String sdDir;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在

        // 获取根目录
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory().toString();
        } else {
            sdDir = this.getCacheDir().getPath();
        }
        return sdDir;
    }

    /**
     * 根据比例设置ImageView大小
     *
     * @param view
     * @param viewMaxSize
     * @param width
     * @param height
     */
    public void setImageViewWidthHeight(View view, float viewMaxSize, int width, int height) {
        int maxSize = (int) (getWidthPixels() * viewMaxSize);
        if (width > height) {
            //计算比例
            BigDecimal proportion = new BigDecimal(maxSize).divide(new BigDecimal(width), 3, BigDecimal.ROUND_HALF_UP);
            //按照比例换算图片高度
            float h = new BigDecimal(height).multiply(proportion).divide(new BigDecimal(getWidthPixels()), 3, BigDecimal.ROUND_HALF_UP).floatValue();
            //设置图片宽高
            setMLayoutParam(view, viewMaxSize, h);
        } else {
            //计算比例
            BigDecimal proportion = new BigDecimal(maxSize).divide(new BigDecimal(height), 3, BigDecimal.ROUND_HALF_UP);
            //按照比例换算图片宽度
            float w = new BigDecimal(width).multiply(proportion).divide(new BigDecimal(getWidthPixels()), 3, BigDecimal.ROUND_HALF_UP).floatValue();
            //设置图片宽高
            setMLayoutParam(view, w, viewMaxSize);
        }
    }


    /**
     * 获取唯一标示
     *
     * @return
     */
    public String getUniqueID() {
        StringBuffer uniqueID = new StringBuffer();
        try {
            //The IMEI
            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String imei = TelephonyMgr.getDeviceId();
            uniqueID.append(imei + ";");

            //Pseudo-Unique ID
            String pseudoUniqueID = "35" + //we make this look like a valid IMEI
                    Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.CPU_ABI.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 +
                    Build.HOST.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 +
                    Build.TYPE.length() % 10 +
                    Build.USER.length() % 10; //13 digits
            uniqueID.append(pseudoUniqueID + ";");

            //The Android ID
            String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            uniqueID.append(androidID + ";");

            //The WLAN MAC Address string
            WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            String macAddress = wm.getConnectionInfo().getMacAddress();
            uniqueID.append(macAddress);
        } catch (Exception e) {
            uniqueID.append("获取失败");
            e.printStackTrace();
        }

        return uniqueID.toString();
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = getApplicationContext().getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

    /**
     * 网络请求dialog
     *
     * @param context
     * @param text
     */
    public void showDialog(Context context, String text) {
        if (dialogWait != null && dialogWait.isShowing()) {
            return;
        }
        dialogWait = CustomProgressDialog.createDialog(context);
        dialogWait.setMessage("");// 暂时显示文字
        dialogWait.setCanceledOnTouchOutside(false);
        dialogWait.setCancelable(true);
        dialogWait.show();
    }

    /**
     * 关闭dialog
     */
    public void dissMissDialog() {
        if (dialogWait != null && dialogWait.isShowing()) {
            dialogWait.dismiss();
        }
    }

    /**
     * 判断应用是否在后台
     *
     * @return
     */
    public boolean isBackground() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getPackageName())) {
                Log.i("后台", getPackageName());
                return true;
            }
        }
        Log.i("前台", getPackageName());
        return false;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }
}
