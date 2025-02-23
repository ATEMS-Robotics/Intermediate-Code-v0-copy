package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorFella;

public class MoveElevator extends Command {
    private final ElevatorFella elevator;
    private final double speed;

    public MoveElevator(ElevatorFella elevator, double speed) {
        this.elevator = elevator;
        this.speed = speed;
        addRequirements(elevator);
    }

    @Override
    public void execute() {
        elevator.setSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        elevator.setSpeed(0); // Stop the motor when command ends
    }

    @Override
    public boolean isFinished() {
        return false; // Runs until button is released
    }
}
