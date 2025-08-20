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
    //daca vedeti peste tot luam instanta pentru a putea comunica intre clase
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

    //aici sunt toate actiunile posibile (nu va speriati de sintaxa e mai usor de cat pare si se poate copia)
   public SequentialCommand idle()
   {
       return new SequentialCommand(new TimedCommand(() -> {
            robot.claw.close();//functie speciala din mecanismul gherii
       }, 0.2), new TimedCommand(() -> {
            robot.claw.wrist_basket();
       }, 0), new TimedCommand(() -> {
            robot.arm.reset();
       }, 0.3), new TimedCommand(() -> {
            robot.claw.reset_angle();
       }, 0), new ConditionalCommand(() -> robot.slides.setTargetExtension(0)), new ConditionalCommand(() -> {
           return robot.pivot.setTargetangle(90);
       }));
   }

   public SequentialCommand down_auto()
   {
       return new SequentialCommand(new TimedCommand(() -> {
            robot.arm.wait_up();
       }, 0.3), new TimedCommand(() -> {
            robot.claw.wait_up();
       }, 0.3), new TimedCommand(() -> {
            robot.claw.open();
       }, 0.3));
   }

    public SequentialCommand get()
    {
        return new SequentialCommand(new TimedCommand(() -> {
             robot.arm.get();
        }, 0.15), new TimedCommand(() -> {
             robot.claw.get();
        }, 0), new TimedCommand(() -> {
             robot.claw.close();
        }, 0.3));
    }

    public SequentialCommand idle_auto()
    {
        return new SequentialCommand(new TimedCommand(() -> {
             robot.claw.reset_angle();
        }, 0), new TimedCommand(() -> {
             robot.claw.wrist_basket();
        }, 0), new TimedCommand(() -> {
             robot.arm.vert();
        }, 0.1), new TimedCommand(() -> {
             robot.claw.close();
        }, 0), new ConditionalCommand(() -> {
            return robot.slides.setTargetExtension(0);
        }), new ConditionalCommand(() -> {
            return robot.pivot.setTargetangle(90);
        }));
    }

    public SequentialCommand go_basket2()
    {
        return new SequentialCommand("basket",
                new TimedCommand(() -> {
                    robot.claw.score_spec_rotate();
                }, 0),
                new ConditionalCommand(() -> {
                    return robot.slides.setTargetExtension(71);
                }),
                new TimedCommand(() -> {
                    robot.claw.wrist_basket();
                }, 0));
    }

    public SequentialCommand go_basket2_auto()
    {
        return new SequentialCommand(new TimedCommand(() -> {
             robot.claw.score_spec_rotate();
        }, 0), new ConditionalCommand(() -> {
            return robot.slides.setTargetExtension(71);
        }), new TimedCommand(() -> {
             robot.arm.drop();
        }, 0), new TimedCommand(() -> {
             robot.claw.wrist_basket();
        }, 0));
    }

    public SequentialCommand go_basket1()
    {
        return new SequentialCommand(new TimedCommand(() -> {
             robot.claw.score_spec_rotate();
        }, 0), new ConditionalCommand(() -> {
            return robot.slides.setTargetExtension(70);
        }), new TimedCommand(() -> {
             robot.claw.wrist_basket();
        }, 0));
    }

    public SequentialCommand intake_idle()
    {
        return new SequentialCommand("intake_idle", new ConditionalCommand(() -> {
            return robot.pivot.setTargetangle(0);
        }), new ConditionalCommand(() -> {
            return robot.slides.setTargetExtension(44);
        }), new TimedCommand(() -> {
             robot.arm.intake_idle();
        }, 0.1), new TimedCommand(() -> {
             robot.claw.wrist_intake_idle();
        }, 0), new TimedCommand(() -> {
             robot.claw.open();
        }, 0));
    }

    public SequentialCommand retract()
    {
        return new SequentialCommand(new TimedCommand(() -> {
             robot.arm.intake_retract();
        }, 0.2), new ConditionalCommand(() -> {
            return robot.slides.setTargetExtension(0);
        }), new TimedCommand(() -> {
             robot.claw.wrist_intake_idle();
        }, 0));
    }

    public SequentialCommand intake()
    {
        return new SequentialCommand(new TimedCommand(() -> {
            robot.claw.wrist_intake();
        }, 0), new TimedCommand(() -> {
            robot.arm.intake();
        }, 0.25), new TimedCommand(() -> {
            robot.claw.close();
        }, 0));
    }

    public SequentialCommand spec_intake() {

        return new SequentialCommand(new TimedCommand(() -> {
            robot.arm.spec_intake_arm();
        }, 0), new TimedCommand(() -> {
            robot.claw.reset_angle();
        }, 0), new TimedCommand(() -> {
            robot.claw.spec_intake_wrist();
        }, 0), new TimedCommand(() -> {
            robot.claw.open();
        }, 0));
    }

    public SequentialCommand spec_score()
    {
        return new SequentialCommand(new TimedCommand(() -> {
                robot.arm.score_spec_arm();
        }, 0), new TimedCommand(() -> {
                robot.claw.score_spec_rotate();
        }, 0), new TimedCommand(() -> {
                robot.claw.score_spec_wrist();
        }, 0), new ConditionalCommand(() -> {
            return robot.slides.setTargetExtension(26.5);
        }));
    }
    public SequentialCommand spec_score_auto()
    {
        return new SequentialCommand(new TimedCommand(() -> {
                robot.arm.score_spec_arm();
        }, 0.3), new TimedCommand(() -> {
                robot.claw.score_spec_rotate();
        }, 0.3), new TimedCommand(() -> {
                robot.claw.score_spec_wrist();
        }, 0.3), new ConditionalCommand(() -> {
            return robot.slides.setTargetExtension(18.5);
        }));
    }

    public SequentialCommand hang()
    {
        return new SequentialCommand(new ConditionalCommand(() -> {
            return robot.pivot.setTargetangle(0);
        }), new TimedCommand(() -> { robot.pto.hang();}, 0.2));
    }

    public SequentialCommand arm_idle()
    {
        return new SequentialCommand(new TimedCommand(() -> {
             robot.arm.intake_idle();
        }, 0), new TimedCommand(() -> {
             robot.claw.open();
        }, 0), new TimedCommand(() -> {
             robot.claw.wrist_intake_idle();
        }, 0));
    }
}