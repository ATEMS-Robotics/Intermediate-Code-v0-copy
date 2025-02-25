package frc.robot.BRICKED_UP_COMMANDS;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ElevatorFella;

public class FancyElevator {
   


public class MoveAndHoldElevator extends SequentialCommandGroup {
    public MoveAndHoldElevator(ElevatorFella elevator, double targetPosition) {
        addCommands(
            elevator.moveElevatorCommand(targetPosition), // Move to position
            elevator.holdElevatorCommand()               // Hold in place after reaching
        );
    }
}
}
