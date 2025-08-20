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
    public static double Po = 0.065, Do = 0.008, Fo = 0, So = 0;
    public static double Pv = 0.098, Dv = 0.012, Fv = 0.072, Sv = 0;
    boolean isretracted = true;
    public double angle = 0, targetpos = 0, currpos = 0, power = 0;
    PDFSController PDFS = new PDFSController(Pv,Dv,Fv,Sv);
    PDFSController PDFS2 = new PDFSController(Po,Do,Fo,So);



    @Override
    public void read() {
        robot.sliderr.setCurrentAlert(6, CurrentUnit.AMPS);
        currpos = getExtensionCm();
        angle = robot.pivot.getPivotAngle();
        PDFS.setConstants(Pv, Dv, Fv, Sv);
        PDFS2.setConstants(Po, Do, Fo, So);
        if(targetpos != 0)
            isretracted = false;
    }

    @Override
    public void periodic() {
        if(robot.pivot.getPivotAngle() > 60)
            power = PDFS.calculate(currpos, targetpos);
        else {
            power = PDFS2.calculate(currpos, targetpos);
        }
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