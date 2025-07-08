package org.firstinspires.ftc.teamcode.common.hardware.subsystems;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import static java.lang.Double.max;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class Camera
{

    ElapsedTime timer = new ElapsedTime();

    double cX = 0;
    double cY = 0;
    double width = 0;
    double height = 0;

    public OpenCvCamera controlHubCam;  // Use OpenCvCamera class from FTC SDK
    /** MAKE SURE TO CHANGE THE FOV AND THE RESOLUTIONS ACCORDINGLY **/
    public int CAMERA_WIDTH = 640; // width  of wanted camera resolution
    public int CAMERA_HEIGHT = 480; // height of wanted camera resolution
    public double FOV = 40;
    Scalar BLUE = new Scalar(0, 0, 255);
    Scalar YELLOW = new Scalar(255, 255, 0);

    public final int lowRed1 = 110;
    public final int lowRed2 = 0;
    public final int upRed1 = 150;
    public final int upRed2 = 0;
    public final int lowBlue = 0;
    public final int upBlue = 30;
    public final int lowYellow = 40;
    public final int upYellow = 105;

    // Calculate the distance using the formula
    public double objectWidthInRealWorldUnits = 3.2;  // Replace with the actual width of the object in real-world units
    public double focalLength = 728;
    public double finalangle = 0;// Replace with the focal length of the camera in pixels
    public boolean analyze = false;
    double objectWidth = 200;
    double objectHeight = 200;
    boolean isRed = true;
    public boolean detected = false;


    public void initOpenCV(HardwareMap hardwareMap, boolean Red) {

        isRed = Red;
        // Create an instance of the camera
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        // Use OpenCvCameraFactory class from FTC SDK to create camera instance
        controlHubCam = OpenCvCameraFactory.getInstance().createWebcam(
                hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        controlHubCam.setPipeline(new Camera.YellowBlobDetectionPipeline());

        controlHubCam.openCameraDevice();
        controlHubCam.startStreaming(CAMERA_WIDTH, CAMERA_HEIGHT, OpenCvCameraRotation.UPRIGHT);
    }

    class YellowBlobDetectionPipeline extends OpenCvPipeline {
        @Override
        public Mat processFrame(Mat input) {
            // Preprocess the frame to detect yellow regions
            Mat yellowMask = preprocessFrame(input);
            Mat redMask1 = preprocessFrame1(input);
            Mat redMask2 = preprocessFrame2(input);
            Mat blueMask = preprocessFrame3(input);

            // Find contours of the detected yellow regions
            List<MatOfPoint> contours = new ArrayList<>();
            List<MatOfPoint> contourAlliance = new ArrayList<>();
            List<MatOfPoint> contourAlliance1 = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(yellowMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            Mat hierarchy1 = new Mat();
            Mat hierarchy2 = new Mat();

            if(!isRed)
                Imgproc.findContours(blueMask, contourAlliance, hierarchy1, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            if(isRed)
            {
                Imgproc.findContours(redMask1, contourAlliance, hierarchy1, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
                Imgproc.findContours(redMask2, contourAlliance1, hierarchy2, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            }

            contours.addAll(contourAlliance);
            contours.addAll(contourAlliance1);
            // Find the largest yellow contour (blob)
            MatOfPoint largestContour = findLargestContour(contours);

            analyze = true;
            if (largestContour != null) {
                detected = true;

                Point[] points = largestContour.toArray();
                MatOfPoint2f contour2f = new MatOfPoint2f(points);

                RotatedRect rotatedRectFitToContour = Imgproc.minAreaRect(contour2f);
                if (Imgproc.contourArea(largestContour) < 2000)
                {
                    analyze = false;
                    finalangle = 0;
                    drawRotatedRect(rotatedRectFitToContour, input, "Yellow");
                }
                // Draw a red outline around the largest detected object
                if (analyze) {

                    drawRotatedRect(rotatedRectFitToContour, input, "Yellow");

                    double rotRectAngle = rotatedRectFitToContour.angle;
                    if (rotatedRectFitToContour.size.width < rotatedRectFitToContour.size.height)
                    {
                        rotRectAngle += 90;
                    }

                    double angle = -(rotRectAngle - 180);
                    finalangle = angle;
                    drawTagText(rotatedRectFitToContour, Integer.toString((int) Math.round(angle)) + " deg", input, "Yellow");
                    Imgproc.drawContours(input, contours, contours.indexOf(largestContour), new Scalar(255, 0, 0), 2);
                    // Calculate the width of the bounding box
                    width = calculateWidth(largestContour);
                    height = calculateHeight(largestContour);

                    // Display the width next to the label
                    String widthLabel = "Width: " + (int) width + " pixels";
                    objectWidth = width;
                    objectHeight = height;
                    Imgproc.putText(input, widthLabel, new Point(cX + 10, cY + 20), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);
                    //Display the Distance
                    String distanceLabel = "Distance: " + String.format("%.2f", getDistance(width)) + " inches";
                    Imgproc.putText(input, distanceLabel, new Point(cX + 10, cY + 60), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);
                    // Calculate the centroid of the largest contour
                    Moments moments = Imgproc.moments(largestContour);
                    cX = moments.get_m10() / moments.get_m00();
                    cY = moments.get_m01() / moments.get_m00();

                    // Draw a dot at the centroid
                    String label = "(" + (int) cX + ", " + (int) cY + ")";
                    Imgproc.putText(input, label, new Point(cX + 10, cY), Imgproc.FONT_HERSHEY_COMPLEX, 0.5, new Scalar(0, 255, 0), 2);
                    Imgproc.circle(input, new Point(cX, cY), 5, new Scalar(0, 255, 0), -1);

                }
            }
            else
            {
                cX = 0;
                cY = 0;
                detected = false;
            }

            return input;
        }

        public Mat preprocessFrame(Mat frame) {
            Mat hsvFrame = new Mat();
            Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);

            Scalar lowerYellow = new Scalar(lowYellow, 100, 100);
            Scalar upperYellow = new Scalar(upYellow, 255, 255);


            Mat yellowMask = new Mat();
            Core.inRange(hsvFrame, lowerYellow, upperYellow, yellowMask);

            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
            Imgproc.morphologyEx(yellowMask, yellowMask, Imgproc.MORPH_OPEN, kernel);
            Imgproc.morphologyEx(yellowMask, yellowMask, Imgproc.MORPH_CLOSE, kernel);

            return yellowMask;
        }

        public Mat preprocessFrame1(Mat frame) {
            Mat hsvFrame = new Mat();
            Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);

            Scalar lowerRed1 = new Scalar(lowRed1, 100, 100);
            Scalar upperRed1 = new Scalar(upRed1, 255, 255);


            Mat redMask1 = new Mat();
            Core.inRange(hsvFrame, lowerRed1, upperRed1, redMask1);

            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
            Imgproc.morphologyEx(redMask1, redMask1, Imgproc.MORPH_OPEN, kernel);
            Imgproc.morphologyEx(redMask1, redMask1, Imgproc.MORPH_CLOSE, kernel);

            return redMask1;
        }

        public Mat preprocessFrame2(Mat frame) {
            Mat hsvFrame = new Mat();
            Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);

            Scalar lowerRed2 = new Scalar(lowRed2, 100, 100);
            Scalar upperRed2 = new Scalar(upRed2, 255, 255);


            Mat redMask2 = new Mat();
            Core.inRange(hsvFrame, lowerRed2, upperRed2, redMask2);

            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
            Imgproc.morphologyEx(redMask2, redMask2, Imgproc.MORPH_OPEN, kernel);
            Imgproc.morphologyEx(redMask2, redMask2, Imgproc.MORPH_CLOSE, kernel);

            return redMask2;
        }

        public Mat preprocessFrame3(Mat frame) {
            Mat hsvFrame = new Mat();
            Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);

            Scalar lowerBlue = new Scalar(lowBlue, 100, 100);
            Scalar upperBlue = new Scalar(upBlue, 255, 255);


            Mat blueMask = new Mat();
            Core.inRange(hsvFrame, lowerBlue, upperBlue, blueMask);

            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
            Imgproc.morphologyEx(blueMask, blueMask, Imgproc.MORPH_OPEN, kernel);
            Imgproc.morphologyEx(blueMask, blueMask, Imgproc.MORPH_CLOSE, kernel);

            return blueMask;
        }

        public MatOfPoint findLargestContour(List<MatOfPoint> contours) {
            double maxArea = 0;
            MatOfPoint largestContour = null;

            for (MatOfPoint contour : contours) {
                double area = Imgproc.contourArea(contour);
                if (area > maxArea) {
                    maxArea = area;
                    largestContour = contour;
                }
            }

            return largestContour;
        }
        public double calculateWidth(MatOfPoint contour) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            return boundingRect.width;
        }

        public double calculateHeight(MatOfPoint contour) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            return boundingRect.height;
        }

    }
    public double getDistance(double width){
        double distance = (objectWidthInRealWorldUnits * focalLength) / width;
        return distance;
    }

    public double getXCenter(){
        return cX;
    }

    public double getYCenter(){
        return cY;
    }

    public boolean can_close(){
        return (objectWidth == 640 && objectHeight == 480);
    }

    public double angleWrap(double radians){
        while(radians > Math.PI){
            radians -= 2 * Math.PI;
        }
        while(radians < -Math.PI){
            radians += 2 * Math.PI;
        }
        return radians;
    }

    void drawRotatedRect(RotatedRect rect, Mat drawOn, String color)
    {
        Point[] points = new Point[4];
        rect.points(points);

        for(int i = 0; i < 4; ++i)
        {
            Point nextPoint = points[(i+1) % 4];
            if(color.equals("Blue"))
                Imgproc.line(drawOn, points[i], nextPoint, BLUE, 2);
            else if(color.equals("Yellow"))
                Imgproc.line(drawOn, points[i], nextPoint, YELLOW, 2);
        }
    }

    void drawTagText(RotatedRect rect, String text, Mat drawOn, String color)
    {
        double fontScale = 1;
        int thickness = 2;
        int[] baseline = new int[1];
        Size textSize = Imgproc.getTextSize(text, Imgproc.FONT_HERSHEY_SIMPLEX, fontScale, thickness, baseline);

        Point textOrigin = new Point(rect.center.x - textSize.width / 2, rect.center.y + textSize.height / 2);
        Scalar textColor = (color.equals("Blue")) ? BLUE : YELLOW;

        Imgproc.putText(drawOn, text, textOrigin, Imgproc.FONT_HERSHEY_SIMPLEX, fontScale, textColor, thickness);
    }


    public void stop()
    {
        controlHubCam.stopStreaming();
    }
}