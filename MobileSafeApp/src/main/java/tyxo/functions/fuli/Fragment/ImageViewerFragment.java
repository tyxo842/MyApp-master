package tyxo.functions.fuli.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.utils.permission.PermissionUtil;
import tyxo.mobilesafe.widget.TouchImageView;

/**
 * Created by ly
 * description : mm详情界面,含长按保存.
 */
public class ImageViewerFragment extends Fragment implements RequestListener<String, GlideDrawable>,View.OnLongClickListener{

    private String url;
    private TouchImageView image ;

    public static ImageViewerFragment newInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        ImageViewerFragment fragment = new ImageViewerFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getArguments().getString("url");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fuli_fragment_image_viewer, container, false);
        image = (TouchImageView) v.findViewById(R.id.picture);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade(0)
                .listener(this)
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        image.setImageDrawable(resource);
        image.setOnLongClickListener(this);
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setItems(new String[]{getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
        builder.setItems(new String[]{"保存图片"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (PermissionUtil.checkPermission(ImageViewerFragment.this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        PermissionUtil.MY_PERMISSIONS_WRITE_STORAGE)) {
                    saveImageLocal();           //点击按钮,保存图片到本地.
                }else{}
            }
        });
        builder.show();

        return true;
    }

    //保存图片到本地.
    private void saveImageLocal() {
        image.setDrawingCacheEnabled(true);
        Bitmap imageBitmap = image.getDrawingCache();
        if (imageBitmap != null) {
            new SaveImageTask().execute(imageBitmap);
        }
    }

    private class SaveImageTask extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected String doInBackground(Bitmap... params) {
//            String result = getResources().getString(R.string.save_picture_failed);
            String result = "保存失败";
            try {
                String sdcard = Environment.getExternalStorageDirectory().toString();

                File file = new File(sdcard + "/Download");
                if (!file.exists()) {
                    file.mkdirs();
                }

                File imageFile = new File(file.getAbsolutePath(),new Date().getTime()+".jpg");
                FileOutputStream outStream = null;
                outStream = new FileOutputStream(imageFile);
                Bitmap image = params[0];
                image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
                result = getResources().getString(R.string.save_picture_success,  file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            ToastUtil.showToastS(getActivity(),result);

            image.setDrawingCacheEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.MY_PERMISSIONS_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                saveImageLocal();
            } else {
                // Permission Denied
                ToastUtil.showToastS(getActivity(),"请提供权限允许");
            }
        }
    }
}
