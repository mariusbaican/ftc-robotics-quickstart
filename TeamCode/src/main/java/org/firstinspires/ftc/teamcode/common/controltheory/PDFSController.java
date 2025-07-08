package org.firstinspires.ftc.teamcode.common.controltheory;

import static java.lang.Math.abs;
import static java.lang.Math.max;

import org.firstinspires.ftc.teamcode.common.controltheory.Timer;

import java.lang.Math;
import java.sql.Time;

public class PDFSController{
    public enum FeedForwardType {
        CONSTANT,
        SIN,
        COS
    }

    public double kP, kD, kF, kL;

    private double deadzone;

    private double homedConstant;

    private FeedForwardType feedForwardType;

    private Timer timer = new Timer();

    private RingBuffer<Double> timeBuffer = new RingBuffer<Double>(3, 0.0);
    private RingBuffer<Double> errorBuffer = new RingBuffer<Double>(3, 0.0);

    public PDFSController setFeedForwardType(FeedForwardType feedforwardType) {
        this.feedForwardType = feedforwardType;
        return this;
    }
    public PDFSController setDeadzone(double deadzone){
        this.deadzone = deadzone;
        return this;
    }

    public PDFSController sethomedConstant(double Constant){
        this.homedConstant = Constant;
        return this;
    }


    public PDFSController(double kP, double kD, double kF, double kL){
        this.kP = kP;
        this.kD = kD;
        this.kF = kF;
        this.kL = kL;
    }

    public void updateConstants(double kP, double kD, double kF, double kL){
        this.kP = kP;
        this.kD = kD;
        this.kF = kF;
        this.kL = kL;
    }


    public void reset(){
        timeBuffer.fill(0.0);
        errorBuffer.fill(0.0);
        timer.resetTimer();
    }


    public double run(double current, double target,  double currentangle){

        double error = target - current;

        if (target == 0 && abs(error) < deadzone){
            return homedConstant;
        }

        double time = timer.getElapsedTime();

        double previous_time = timeBuffer.getValue(time);
        double previous_error = errorBuffer.getValue(error);

        double delta_time = time - previous_time;
        double delta_error = error - previous_error;

        //If the PDFL hasn't been updated, reset it
        if (delta_time > 200){
            reset();
            return run(current, target, currentangle);
        }

        double p = pComponent(error);
        double d = dComponent(delta_error, delta_time);
        double f = Math.sin(Math.toRadians(currentangle)) * fComponenet();
        double l = lComponent(error);

        double response = p + d + f + l;

        if (abs(error) < deadzone){
            //same response but without lower limit
            response = p + d + f;
        }

        return Math.max(Math.abs(updatedPower(response,currentangle)),0) * Math.signum(error);
    }


    public double runauto(double current, double target,  double currentangle){

        double error = target - current;

        double time = timer.getElapsedTime();

        double previous_time = timeBuffer.getValue(time);
        double previous_error = errorBuffer.getValue(error);

        double delta_time = time - previous_time;
        double delta_error = error - previous_error;

        //If the PDFL hasn't been updated, reset it
        if (delta_time > 200){
            reset();
            return runauto(current, target, currentangle);
        }

        double p = pComponent(error);
        double d = dComponent(delta_error, delta_time);
        double f = Math.sin(Math.toRadians(currentangle)) * fComponenet();
        double l = lComponent(error);

        double response = p + d + f + l;

        if (abs(error) < deadzone){
            //same response but without lower limit
            response = p + d + f;
        }

        return response;
    }

    public double runauto(double error,  double currentangle){
        double time = timer.getElapsedTime();

        double previous_time = timeBuffer.getValue(time);
        double previous_error = errorBuffer.getValue(error);

        double delta_time = time - previous_time;
        double delta_error = error - previous_error;

        //If the PDFL hasn't been updated, reset it
        if (delta_time > 200){
            reset();
            return runauto(error, currentangle);
        }

        double p = pComponent(error);
        double d = dComponent(delta_error, delta_time);
        double f = Math.sin(Math.toRadians(currentangle)) * fComponenet();
        double l = lComponent(error);

        double response = p + d + f + l;

        if (abs(error) < deadzone){
            //same response but without lower limit
            response = p + d + f;
        }

        return response;
    }


    private double pComponent(double error){

        double response = kP * error;

        return response;
    }

    private double dComponent(double delta_error, double delta_time){

        double derivative = delta_error / delta_time;

        double response = derivative * kD;

        return response;
    }

    private double fComponenet(){

        double response = kF;

        return response;
    }

    private double lComponent(double error){

        double direction = Math.signum(error);

        double response = direction * kL;

        return response;
    }
    private double updatedPower(double power,double currentAngle) {
        switch (feedForwardType) {
            case CONSTANT:
                return power;
            case SIN:
                return power * Math.sin(Math.toRadians(currentAngle));
            case COS:
                return power * Math.cos(Math.toRadians(currentAngle));
            default:
                return 0.0;
        }
    }
}