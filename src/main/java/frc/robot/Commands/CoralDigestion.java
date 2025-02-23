package frc.robot.Commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralEater9000;

public class CoralDigestion extends Command {
    private final CoralEater9000 coralEater;
    private final DoubleSupplier speedSupplier;

    public CoralDigestion(CoralEater9000 coralEater, DoubleSupplier speedSupplier) {
        this.coralEater = coralEater;
        this.speedSupplier = speedSupplier;
    

    }


   


    @Override
    public void execute() {
        double speed = speedSupplier.getAsDouble();
        coralEater.moveArm(speed);

    }
    @Override
    public void initialize() {
    }

    @Override
    public void end(boolean interrupted) {
        coralEater.stopIntakeArm();
    }
}
