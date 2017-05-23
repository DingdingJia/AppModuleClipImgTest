package biz.sequ.appmoduleclipimgtest;

import android.widget.Toast;

import biz.sequ.appmoduleclipimg.ClipImgActivity;

/**
 * Created by Administrator on 17/5/22.
 */

public class ClipImgExpendActivity extends ClipImgActivity {

    @Override
    protected void onSaveImage(String path) {
        super.onSaveImage(path);
        Toast.makeText(ClipImgExpendActivity.this,path, Toast.LENGTH_SHORT).show();
    }
}
