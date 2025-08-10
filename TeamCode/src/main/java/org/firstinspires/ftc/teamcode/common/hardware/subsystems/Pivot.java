package org.firstinspires.ftc.teamcode.common.hardware.subsystems;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.common.controltheory.PDFSController;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;

@Config
public class Pivot implements Subsystem{

    RobotHardware robot = RobotHardware.getInstance();
    public double angle = 0, power = 0, extension = 30, init_extension = 30, targetangle = 45, offset = 90;
    private boolean isdown = false;
    private boolean isup = false;



    @Override
    public void read() {
        robot.pivoting.setCurrentAlert(7 , CurrentUnit.AMPS);
        angle = getPivotAngle();
        if(targetangle != 0)
            isdown = false;
        if(targetangle != 90)
            isup = false;
        if(angle > 90 && targetangle == 90)
            angle = 90;
        if(angle < 0 && targetangle == 0)
            angle = 0;
        //extension = robot.slides.getExtensionCm() + init_extension;
    }

    @Override
    public void periodic() {
       if(targetangle == 90 && !isup && (angle >= 88 || angle <= 50))
            power = 1;
       else if(targetangle == 90 && !isup && angle > 50 && angle < 88)
            power = 0.5;
        else if(targetangle == 0 && !isdown && (angle > 40 || angle <= 6))
            power = -1;
       else if(targetangle == 0 && !isdown && (angle < 40 && angle > 6))
           power = -0.2;

        if(isup)
            power = 0.3;
        if(isdown)
            robot.pivotEnc.reset();


        if(robot.pivoting.isOverCurrent() || getPivotAngle() < 5 || getPivotAngle() > 80)
        {
            if(targetangle == 0 && getPivotAngle() < 10)
            {
                power = 0;
                isdown = true;
                offset = 0;
                if(Math.abs(robot.slides.getExtensionCm()) < 10)
                {
                    if(robot.slides.targetpos == 0)
                        robot.sliderEnc1.reset();
                }
                robot.pivotEnc.reset();

            }
            if(targetangle == 90)
            {
                if(Math.abs(robot.slides.getExtensionCm()) < 10 && getPivotAngle() > 80)
                {
                    power = 0.2;
                    if(robot.slides.targetpos == 0)
                        robot.sliderEnc1.reset();
                    isup = true;
                }
            }
        }
    }

    @Override
    public void write() {
        robot.pivoting.setPower(power);
    }

    @Override
    public void init(HardwareMap hwMap) {
        robot.pivoting.init(hwMap);
        robot.pivotEnc.init(hwMap);
        robot.pivoting.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.pivotEnc.reset();
    }

    public double getPivotAngle()
    {
        if((robot.pivotEnc.getCurrentPosition() * 90) / 2875.0 + offset < 0)
            return 0;
        if((robot.pivotEnc.getCurrentPosition() * 90) / 2875.0 + offset > 90)
            return 90;
        return (robot.pivotEnc.getCurrentPosition() * 90) / 2875.0 + offset;
    }

    public boolean setTargetangle(double angle)
    {
        targetangle = angle;
        return (abs(getPivotAngle() - targetangle) < 5);
    }
}