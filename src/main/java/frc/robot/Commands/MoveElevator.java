package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorFella;

public class MoveElevator extends Command {
    private final ElevatorFella elevator;
    private final double targetHeight;

    public MoveElevator(ElevatorFella elevator, double targetHeight) {
        this.elevator = elevator;
        this.targetHeight = targetHeight;
        addRequirements(elevator);
    }


    @Override
    public void initialize() {
        elevator.setElevatorPositionInInches(targetHeight);
    }
  
    @Override
    public void end(boolean interrupted) {
        // Ends the code if it is interrupted
        if (interrupted) {
            elevator.stopElevator();
        }
    }

    @Override
    public boolean isFinished() {
        //Stops the code when it is close to position
        return Math.abs(elevator.getCurrentHeight() - targetHeight) < 0.5;
    }
}
