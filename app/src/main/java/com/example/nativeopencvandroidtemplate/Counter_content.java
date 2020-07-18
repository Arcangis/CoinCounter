package com.example.nativeopencvandroidtemplate;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.opencv.imgproc.Imgproc.putText;

public class Counter_content extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "Counter_content";
    Classifier classifier;
    private static final int CAMERA_PERMISSION_REQUEST = 1;

    private CameraBridgeViewBase mOpenCvCameraView;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.i(TAG, "OpenCV loaded successfully");

                // Load native library after(!) OpenCV initialization
                System.loadLibrary("native-lib");

                mOpenCvCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        classifier = new Classifier(Utils2.assetFilePath(this,"MyMobileNet.pt"));

        // Permissions for Android 6+
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST
        );

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.counter_content);


        mOpenCvCameraView = findViewById(R.id.main_surface);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mOpenCvCameraView.setCameraPermissionGranted();
            } else {
                String message = "Camera permission was not granted";
                Log.e(TAG, message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e(TAG, "Unexpected permission request");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame frame) {
        double tot = 0;

        Mat frame_mat = frame.rgba(); // RGB Frame
        Mat contoursFrame = frame.gray().clone(); // Grayscale frame (for image processing)

        Mat rotationMatrix = Imgproc.getRotationMatrix2D(new Point((int)(frame_mat.width()/2), (int)(frame_mat.height()/2)), 270, 1);
        Mat rotationMatrix2 = Imgproc.getRotationMatrix2D(new Point((int)(frame_mat.width()/2), (int)(frame_mat.height()/2)), 90, 1.25);
        Imgproc.warpAffine(frame_mat, frame_mat,rotationMatrix, new Size(frame_mat.width(), frame_mat.height()));
        Imgproc.warpAffine(contoursFrame, contoursFrame,rotationMatrix, new Size(frame_mat.width(), frame_mat.height()));

        // Otsu Threshold
        Imgproc.threshold(contoursFrame, contoursFrame, 0, 255, Imgproc.THRESH_OTSU);

        // Invert colors (Depending on background color)
        //Mat invertColorMat = new Mat(contoursFrame.rows(), contoursFrame.cols(), contoursFrame.type(), new Scalar(255));
        //Core.subtract(invertColorMat, contoursFrame, contoursFrame);

        // Morphology
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size((2*2) + 1, (2*2)+1));
        Imgproc.dilate(contoursFrame, contoursFrame, kernel);
        Imgproc.erode(contoursFrame,contoursFrame,kernel);

        // Find contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(contoursFrame, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.cvtColor(contoursFrame, contoursFrame, Imgproc.COLOR_GRAY2BGR);

        // Bound boxes and crop
        Mat img2 = new Mat();
        float[] radius = new float[1];
        Point center = new Point();
        Rect[] boundRect = new Rect[contours.size()];
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint c = contours.get(i);
            if (Imgproc.contourArea(c) > 500) {
                MatOfPoint2f c2f = new MatOfPoint2f(c.toArray());
                Imgproc.minEnclosingCircle(c2f, center, radius);
                boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contours.get(i).toArray()));
                double circle_area = 3.1415*radius[0]*radius[0];
                if (Imgproc.contourArea(c)/circle_area>0.85){
                    Imgproc.rectangle(frame_mat, boundRect[i].tl(), boundRect[i].br(), new Scalar(0, 255, 0), 1);

                    // Crop and give to CNN
                    Rect rectCrop = new Rect(boundRect[i].x, boundRect[i].y, boundRect[i].width, boundRect[i].height);
                    img2 = new Mat(frame_mat,rectCrop);

                    Bitmap bmp = null;
                    try {
                        bmp = Bitmap.createBitmap(img2.cols(), img2.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(img2, bmp);
                    }
                    catch (CvException e){Log.d("Exception",e.getMessage());}

                    assert bmp != null;
                    String pred = "";
                    try {
                        if (bmp.getWidth() > 2)
                            pred = classifier.predict(bmp);
                    }
                    catch (Exception e){pred="FAIL";}
                    // Put Text
                    putText(frame_mat, pred, boundRect[i].br(), 1, 1.0, new Scalar(0, 255, 0));
                    switch (pred){
                        case "R$0.05": tot = tot+0.05;break;
                        case "R$0.10": tot = tot+0.1;break;
                        case "R$0.25": tot = tot+0.25;break;
                        case "R$0.50": tot = tot+0.5;break;
                        case "R$1.00": tot = tot+1;break;
                    }
                }
            }
            //else { tot=0; }
        }
        if (tot>0){
            final String mytot = "Total: R$"+String.format(Locale.ENGLISH,"%.2f", tot);
            final TextView result_text = (TextView) findViewById(R.id.total);
            result_text.post(new Runnable() {
                public void run() {
                    result_text.setText(mytot);
                }
            });
            tot=0;
        }
        Imgproc.warpAffine(frame_mat, frame_mat,rotationMatrix2, new Size(frame_mat.width(), frame_mat.height()));
        return frame_mat;
    }
}
