package frc.robot.COMMANDS;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeArmMovement;
import frc.robot.subsystems.CoralTransfer;

public class AutoIntestineSchmoovement extends Command {
    private final IntakeArmMovement armSubsystem;
    private final CoralTransfer Intestine;

    public AutoIntestineSchmoovement(IntakeArmMovement arm, CoralTransfer transfer) {
        this.armSubsystem = arm;
        this.Intestine = transfer;
        addRequirements(arm, transfer);
    }

    @Override
    public void initialize() {
        System.out.println("Schmoovement Initiated");
    } 

    @Override
    public void execute() {
        armSubsystem.moveToArmPosition(0);
        Intestine.spinIntestine(-.2);
    }

        

}



