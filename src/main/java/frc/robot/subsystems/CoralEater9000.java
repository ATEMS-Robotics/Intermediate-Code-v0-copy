package frc.robot.subsystems;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX; // Kraken X60 motors use TalonFX
import com.ctre.phoenix6.controls.*; // Needed for motor control modes

public class CoralEater9000 extends SubsystemBase {
    private final TalonFX armMotor; // Rotates the whole arm
    private final TalonFX intakeMotor; // Spins Omni wheels for intake
    private final SlewRateLimiter armNoBreaky = new SlewRateLimiter(0.5);


    public CoralEater9000() {
        armMotor = new TalonFX(23, "rio");  // Replace with actual CAN ID
        intakeMotor = new TalonFX(22, "rio");  // Replace with actual CAN ID 
    }

    /** Moves the arm up/down. Positive = up, Negative = down */
    public void setArmSpeed(double speed) {
        armMotor.setControl(new DutyCycleOut(speed));
    }


    public void moveArm(double desiredPosition) {
        double armDeadband = 0.02; //Ignores small values of stuff so it aint tweak out
        if (Math.abs(desiredPosition) < armDeadband) {
            armMotor.setVoltage(1);
        } else {
            armMotor.setControl(new DutyCycleOut(desiredPosition));
        }
        double limitedSpeed = armNoBreaky.calculate(desiredPosition);
        armMotor.set(limitedSpeed);

    }
    /** Spins the intake wheels. Positive = intake, Negative = outtake */
    public void setIntakeSpeed(double speed) {
        intakeMotor.setControl(new DutyCycleOut(speed));
    }

    /** Stops all motors */
    public void stopCoralEating() {
        armMotor.setControl(new DutyCycleOut(0));
        intakeMotor.setControl(new DutyCycleOut(0));
    }

    public void stopIntakeArm() {
        armMotor.setControl(new DutyCycleOut(0));
    }

    public void stopIntakeSpinning() {
        intakeMotor.setControl(new DutyCycleOut(0));
    }
}
