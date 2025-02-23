package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralEater9000;

public class CoralNomNom extends Command{
    private final CoralEater9000 arm;
    private final double speed;

    public CoralNomNom(CoralEater9000 arm, double speed) {
        this.arm = arm;
        this.speed = speed;
        addRequirements(arm);
    }

    @Override
    public void execute() {
        arm.setIntakeSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        arm.setIntakeSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return false; // Runs until button is released
    }
}