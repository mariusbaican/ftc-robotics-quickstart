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
public class Slides implements Subsystem{

    RobotHardware robot = RobotHardware.getInstance();
    public static double Po = 0, Do = 0, Fo = 0, So = 0;
    public static double Pv = 0.09, Dv = 0.0135, Fv = 0.06, Sv = 0;
    boolean isretracted = true;
    public double angle = 0, targetpos = 0, currpos = 0, power = 0;
    PDFSController PDFS = new PDFSController(Pv,Dv,Fv,Sv).setFeedForwardType(PDFSController.FeedForwardType.CONSTANT).setDeadzone(3).sethomedConstant(-0.1);


    @Override
    public void read() {
        robot.sliderr.setCurrentAlert(6, CurrentUnit.AMPS);
        currpos = getExtensionCm();
        angle = robot.pivot.getPivotAngle();
        PDFS.updateConstants(Pv, Dv, Fv, Sv);
        if(targetpos != 0)
            isretracted = false;
    }

    @Override
    public void periodic() {
        power = PDFS.run(currpos, targetpos, angle);
        if(targetpos == 0 && !isretracted)
            power = -1;
        if(robot.sliderr.isOverCurrent() && !isretracted)
        {
            if(targetpos == 0 && getExtensionCm() < 10) {
                power = 0;
                robot.sliderEnc1.reset();
                isretracted = true;
            }
        }
    }

    @Override
    public void write() {
        robot.sliderr.setPower(power);
        robot.sliderl.setPower(power);
    }

    @Override
    public void init(HardwareMap hwMap) {
        robot.sliderr.init(hwMap);
        robot.sliderl.init(hwMap);
        robot.sliderl.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.sliderEnc1.init(hwMap);
        robot.sliderEnc1.reset();
        robot.sliderr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.sliderl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double getExtensionCm()
    {
        return Math.abs(robot.sliderEnc1.getCurrentPosition() * (Math.PI * 3.2) / 145.1);
    }

    public boolean setTargetExtension(double distance)
    {
        targetpos = distance;
        return (abs(targetpos - getExtensionCm()) < 5);
    }

    public boolean addtoTargetExtension(double distance)
    {
        targetpos = currpos + distance;
        return true;
    }

}