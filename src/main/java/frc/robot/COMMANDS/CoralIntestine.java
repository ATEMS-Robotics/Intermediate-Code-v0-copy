package frc.robot.COMMANDS;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralTransfer;

public class CoralIntestine extends Command {
    private final CoralTransfer outtake;
    private final double speed;

    public CoralIntestine(CoralTransfer outtake, double speed) {
        this.outtake = outtake;
        this.speed = speed;
    }

    @Override
    public void execute() {
        outtake.setIntestineSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        outtake.setIntestineSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}