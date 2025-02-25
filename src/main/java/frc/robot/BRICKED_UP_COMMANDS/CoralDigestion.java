package frc.robot.BRICKED_UP_COMMANDS;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralEater9000;

public class CoralDigestion extends Command {
    private final CoralEater9000 arm;
    private final double targetPosition;

    public CoralDigestion(CoralEater9000 arm, double targetPosition) {
        this.arm = arm;
        this.targetPosition = targetPosition;
        

    }


   


    @Override
    public void execute() {

    }
    @Override
    public void initialize() {
        arm.moveToArmPosition(targetPosition);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(arm.getCurrentPosition() - targetPosition) < 0.1;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            arm.stopIntakeArm();
        }
    }
}
