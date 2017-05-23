package biz.sequ.appmoduleclipimg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ClipImgActivity extends Activity {
	private ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clipimg);

		iv = (ImageView) findViewById(R.id.iv);

		findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showImagePickDialog();
			}
		});

		ImageUtils.openLocalImage(ClipImgActivity.this);

		findViewById(R.id.activity_clipimg_send).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String path = "";
				if (ImageUtils.cropImageUri != null) {
					path = getRealPathFromURI(ImageUtils.cropImageUri);
				}
				onSaveImage(path);
			}
		});

		findViewById(R.id.activity_clipimg_return).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	protected void onSaveImage(String path) {
	}

	@Override
	protected void onDestroy() {
		if (ImageUtils.cropImageUri != null) {
			ImageUtils.cropImageUri = null;
		}
		super.onDestroy();
	}

	public String getRealPathFromURI(Uri contentUri) {
		// Uri uri = ImageUtils.cropImageUri;
		// String[] proj = { MediaStore.Images.Media.DATA };
		// Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
		// int actual_image_column_index =
		// actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// actualimagecursor.moveToFirst();
		// String img_path =
		// actualimagecursor.getString(actual_image_column_index);

		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
		if (cursor.moveToFirst()) {
			;
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}

	public void showImagePickDialog() {
		String title = "获取图片方式";
		String[] choices = new String[] { "拍照", "从手机中选择" };

		new AlertDialog.Builder(this).setTitle(title).setItems(choices, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				switch (which) {
					case 0:
						ImageUtils.openCameraImage(ClipImgActivity.this);
						break;
					case 1:
						ImageUtils.openLocalImage(ClipImgActivity.this);
						break;
				}
			}
		}).setNegativeButton("返回", null).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			finish();
			return;
		}

		switch (requestCode) {
		// 拍照获取图片
			case ImageUtils.GET_IMAGE_BY_CAMERA:
				// uri传入与否影响图片获取方式,以下二选一
				// 方式一,自定义Uri(ImageUtils.imageUriFromCamera),用于保存拍照后图片地址
				if (ImageUtils.imageUriFromCamera != null) {
					// 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
					// iv.setImageURI(ImageUtils.imageUriFromCamera);
					// 对图片进行裁剪
					ImageUtils.cropImage(this, ImageUtils.imageUriFromCamera);
					break;
				}

				break;
			// 手机相册获取图片
			case ImageUtils.GET_IMAGE_FROM_PHONE:
				if (data != null && data.getData() != null) {
					// 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
					// iv.setImageURI(data.getData());

					// 对图片进行裁剪
					ImageUtils.cropImage(this, data.getData());
				}

				if (ImageUtils.cropImageUri == null) {
					finish();
				}
				break;
			// 裁剪图片后结果
			case ImageUtils.CROP_IMAGE:
				if (ImageUtils.cropImageUri != null) {
					// 可以直接显示图片,或者进行其他处理(如压缩等)
					iv.setImageURI(ImageUtils.cropImageUri);
				} else {
					finish();
				}
				break;
			default:
				break;
		}
	}
}
