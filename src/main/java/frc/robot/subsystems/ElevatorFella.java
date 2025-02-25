package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class ElevatorFella extends SubsystemBase {
    private final TalonFX elevatorMotor1;
    private final TalonFX elevatorMotor2;
    private final MotionMagicVoltage motionMagicControl = new MotionMagicVoltage(0);

    public ElevatorFella() {
        elevatorMotor1 = new TalonFX(20, "rio");
        elevatorMotor2 = new TalonFX(21, "rio");

        elevatorMotor1.setNeutralMode(NeutralModeValue.Brake);
        elevatorMotor2.setNeutralMode(NeutralModeValue.Brake);
    }

    public void setElevatorPosition(double targetPosition) {
        elevatorMotor1.setControl(motionMagicControl.withPosition(targetPosition));
        elevatorMotor2.setControl(motionMagicControl.withPosition(targetPosition));
    }

    public void stopElevator() {
        elevatorMotor1.set(0);
        elevatorMotor2.set(0);
    }

    public double getCurrentPosition() {
        return elevatorMotor1.getPosition().getValueAsDouble();
    }

    // Move Elevator Command
    public Command moveElevatorCommand(double targetPosition) {
        return Commands.run(() -> setElevatorPosition(targetPosition), this);
    }

    // Hold Elevator Command
    public Command holdElevatorCommand() {
        return Commands.run(() -> setElevatorPosition(getCurrentPosition()), this);
    }
}