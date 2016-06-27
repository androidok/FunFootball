package com.qysports.funfootball.present;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.boredream.bdcodehelper.activity.ImageBrowserActivity;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.utils.ImageUtils;
import com.boredream.bdcodehelper.utils.LogUtils;
import com.boredream.bdcodehelper.utils.ToastUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.entity.SimpleImageUrl;
import com.qysports.funfootball.net.GlideHelper;
import com.qysports.funfootball.net.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class Image3UploadPresent {

    public interface OnAllUploadedListener {
        void onAllUploaded(List<FileUploadResponse> responses);
        void onError(Throwable e);
    }

    private Activity context;

    private ImageView iv_image1;
    private ImageView iv_image1_remove;
    private ImageView iv_image2;
    private ImageView iv_image2_remove;
    private ImageView iv_image3;
    private ImageView iv_image3_remove;

    private List<Uri> imgUris = new ArrayList<>();
    private List<FileUploadResponse> responses = new ArrayList<>();

    public List<Uri> getImgUris() {
        return imgUris;
    }

    private View.OnClickListener addOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageUtils.showImagePickDialog(context);
        }
    };

    private View.OnClickListener removeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_image1_remove:
                    imgUris.remove(0);
                    break;
                case R.id.iv_image2_remove:
                    imgUris.remove(1);
                    break;
                case R.id.iv_image3_remove:
                    imgUris.remove(2);
                    break;
            }
            updateImageViews();
        }
    };

    private View.OnClickListener browserOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList<SimpleImageUrl> iuis = new ArrayList<>();
            for(Uri uri : imgUris) {
                SimpleImageUrl siu = new SimpleImageUrl();
                siu.setUrl(uri.toString());
                iuis.add(siu);
            }

            Intent intent = new Intent(context, ImageBrowserActivity.class);
            intent.putExtra("images", iuis);

            switch (view.getId()) {
                case R.id.iv_image1:
                    intent.putExtra("position", 0);
                    break;
                case R.id.iv_image2:
                    intent.putExtra("position", 1);
                    break;
                case R.id.iv_image3:
                    intent.putExtra("position", 2);
                    break;
            }
            context.startActivity(intent);
        }
    };

    public Image3UploadPresent(Activity context, View include_upload_3image) {
        this.context = context;

        iv_image1 = (ImageView) include_upload_3image.findViewById(R.id.iv_image1);
        iv_image1_remove = (ImageView) include_upload_3image.findViewById(R.id.iv_image1_remove);
        iv_image2 = (ImageView) include_upload_3image.findViewById(R.id.iv_image2);
        iv_image2_remove = (ImageView) include_upload_3image.findViewById(R.id.iv_image2_remove);
        iv_image3 = (ImageView) include_upload_3image.findViewById(R.id.iv_image3);
        iv_image3_remove = (ImageView) include_upload_3image.findViewById(R.id.iv_image3_remove);

        iv_image1_remove.setOnClickListener(removeOnClickListener);
        iv_image2_remove.setOnClickListener(removeOnClickListener);
        iv_image3_remove.setOnClickListener(removeOnClickListener);

        updateImageViews();
    }

    private void updateImageViews() {
        switch (imgUris.size()) {
            case 0:
                iv_image1.setVisibility(View.VISIBLE);
                iv_image2.setVisibility(View.INVISIBLE);
                iv_image3.setVisibility(View.INVISIBLE);

                iv_image1_remove.setVisibility(View.GONE);
                iv_image2_remove.setVisibility(View.GONE);
                iv_image3_remove.setVisibility(View.GONE);

                iv_image1.setImageResource(R.mipmap.ic_publish_camera_photos);

                iv_image1.setOnClickListener(addOnClickListener);
                iv_image2.setOnClickListener(null);
                iv_image3.setOnClickListener(null);
                break;
            case 1:
                iv_image1.setVisibility(View.VISIBLE);
                iv_image2.setVisibility(View.VISIBLE);
                iv_image3.setVisibility(View.INVISIBLE);

                iv_image1_remove.setVisibility(View.VISIBLE);
                iv_image2_remove.setVisibility(View.GONE);
                iv_image3_remove.setVisibility(View.GONE);

                GlideHelper.showImage(context, imgUris.get(0).toString(), iv_image1);
                iv_image2.setImageResource(R.mipmap.ic_publish_camera_photos);

                iv_image1.setOnClickListener(browserOnClickListener);
                iv_image2.setOnClickListener(addOnClickListener);
                iv_image3.setOnClickListener(null);
                break;
            case 2:
                iv_image1.setVisibility(View.VISIBLE);
                iv_image2.setVisibility(View.VISIBLE);
                iv_image3.setVisibility(View.VISIBLE);

                iv_image1_remove.setVisibility(View.VISIBLE);
                iv_image2_remove.setVisibility(View.VISIBLE);
                iv_image3_remove.setVisibility(View.GONE);

                GlideHelper.showImage(context, imgUris.get(0).toString(), iv_image1);
                GlideHelper.showImage(context, imgUris.get(1).toString(), iv_image2);
                iv_image3.setImageResource(R.mipmap.ic_publish_camera_photos);

                iv_image1.setOnClickListener(browserOnClickListener);
                iv_image2.setOnClickListener(browserOnClickListener);
                iv_image3.setOnClickListener(addOnClickListener);
                break;
            case 3:
                iv_image1.setVisibility(View.VISIBLE);
                iv_image2.setVisibility(View.VISIBLE);
                iv_image3.setVisibility(View.VISIBLE);

                iv_image1_remove.setVisibility(View.VISIBLE);
                iv_image2_remove.setVisibility(View.VISIBLE);
                iv_image3_remove.setVisibility(View.VISIBLE);

                GlideHelper.showImage(context, imgUris.get(0).toString(), iv_image1);
                GlideHelper.showImage(context, imgUris.get(1).toString(), iv_image2);
                GlideHelper.showImage(context, imgUris.get(2).toString(), iv_image3);

                iv_image1.setOnClickListener(browserOnClickListener);
                iv_image2.setOnClickListener(browserOnClickListener);
                iv_image3.setOnClickListener(browserOnClickListener);
                break;
        }
    }

    public boolean upload(final OnAllUploadedListener listener) {
        if(imgUris.size() == 0) {
            ToastUtils.showToast(context, "无上传的图片");
            return false;
        }

        int reqW = iv_image1.getWidth();
        int reqH = iv_image1.getHeight();

        LogUtils.showLog("upload image width = " + reqW + ", height = " + reqH);

        responses.clear();
        for(int i=0; i<imgUris.size(); i++) {
            HttpRequest.fileUpload(context, imgUris.get(0), reqW, reqH,
                    new Subscriber<FileUploadResponse>(){

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if(listener != null) {
                                listener.onError(e);
                            }
                        }

                        @Override
                        public void onNext(FileUploadResponse fileUploadResponse) {
                            handleUploadResponse(fileUploadResponse, listener);
                        }
                    });
        }

        return true;
    }

    private void handleUploadResponse(FileUploadResponse response, OnAllUploadedListener listener) {
        responses.add(response);
        if(responses.size() == imgUris.size()) {
            if(listener != null) {
                listener.onAllUploaded(responses);
            }
        }
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        Uri imgUri = null;
        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_FROM_ALBUM:
                imgUri = data.getData();
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                imgUri = ImageUtils.imageUriFromCamera;
                break;
        }

        imgUris.add(imgUri);
        updateImageViews();
    }
}
