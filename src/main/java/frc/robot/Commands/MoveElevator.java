package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorFella;

public class MoveElevator extends Command {
    private final ElevatorFella elevator;
    private final double targetPosition;

    public MoveElevator(ElevatorFella elevator, double targetPosition) {
        this.elevator = elevator;
        this.targetPosition = targetPosition;
        addRequirements(elevator);
    }


    @Override
    public void initialize() {
        elevator.moveToElevatorPosition(targetPosition);
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
        return Math.abs(elevator.getCurrentPosition() - targetPosition) < 0.5;
    }
}
