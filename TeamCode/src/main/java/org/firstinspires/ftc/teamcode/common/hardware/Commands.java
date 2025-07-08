package org.firstinspires.ftc.teamcode.common.hardware;

import android.util.*;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.common.commandbase.ConditionalCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.InstantCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.ParallelCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.SequentialCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.TimedCommand;

import java.util.HashMap;
import java.util.Map;

public class Commands {

    RobotHardware robot = RobotHardware.getInstance();
    SequentialCommand Basket_1;
    public SequentialCommand Basket1;
    SequentialCommand Basket_2;
    public SequentialCommand Basket2;
    SequentialCommand Spec_1;
    public SequentialCommand Spec1;
    SequentialCommand Spec_2;
    public SequentialCommand Spec2;
    SequentialCommand Spec_intake;
    public SequentialCommand SpecIntake;
    SequentialCommand Intake_idle;
    public SequentialCommand IntakeIdle;
    public SequentialCommand Intake;

   public SequentialCommand idle()
   {
       return new SequentialCommand(new TimedCommand(() -> {
           return robot.claw.reset_angle();
       }, 0), new TimedCommand(() -> {
           return robot.claw.wrist_basket();
       }, 0), new TimedCommand(() -> {
           return robot.claw.close();
       }, 0), new TimedCommand(() -> {
           return robot.arm.reset();
       }, 0.5), new ConditionalCommand(() -> {
           return robot.slides.setTargetExtension(0);
       }), new ConditionalCommand(() -> {
           return robot.pivot.setTargetangle(90);
       }));
   }

    public SequentialCommand score_spec()
    {
        return new SequentialCommand(new TimedCommand(() -> { return robot.slides.addtoTargetExtension( -15);}, 0.3), new TimedCommand(() -> {
            return robot.claw.open();
        }, 0.2), new ConditionalCommand(() -> { return robot.slides.setTargetExtension(0);}));
    }

    public SequentialCommand go_basket2()
    {
        return new SequentialCommand(new ConditionalCommand(() -> {
            return robot.slides.setTargetExtension(65);
        }), new TimedCommand(() -> {
            return robot.claw.wrist_basket();
        }, 0));
    }

    public SequentialCommand go_spec2()
    {
        return new SequentialCommand(new ConditionalCommand(() -> {
            return robot.slides.setTargetExtension(38);
        }), new TimedCommand(() -> {
            return robot.arm.spec_score();
        }, 0), new TimedCommand(()->{return  robot.claw.wrist_spec();}, 0), new TimedCommand(() -> {return  robot.claw.spec_angle();}, 0));
    }

    public SequentialCommand intake_idle()
    {
        return new SequentialCommand(new ConditionalCommand(() -> {
            return robot.pivot.setTargetangle(0);
        }), new ConditionalCommand(() -> {
            return robot.slides.setTargetExtension(44);
        }), new TimedCommand(() -> {
            return robot.arm.intake_idle();
        }, 0.2), new TimedCommand(() -> {
            return robot.claw.wrist_intake_idle();
        }, 0), new TimedCommand(() -> {
            return robot.claw.open();
        }, 0));
    }

    public SequentialCommand intake()
    {
        return new SequentialCommand(new TimedCommand(() -> {
            return robot.claw.wrist_intake();
        }, 0), new TimedCommand(() -> {
            return robot.arm.intake();
        }, 0.5), new TimedCommand(() -> {
            return robot.claw.close();
        }, 0));
    }

    public SequentialCommand spec_intake()
    {
        return new SequentialCommand(new TimedCommand(() -> { return
                robot.arm.spec_intake();
        }, 0.3), new TimedCommand(() -> { return
                robot.claw.spec_intake();
        }, 0));
    }

    public SequentialCommand arm_idle()
    {
        return new SequentialCommand(new TimedCommand(() -> {
            return robot.arm.intake_idle();
        }, 0), new TimedCommand(() -> {
            return robot.claw.open();
        }, 0), new TimedCommand(() -> {
            return robot.claw.wrist_intake_idle();
        }, 0));
    }
}