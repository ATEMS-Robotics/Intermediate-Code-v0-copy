package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX; // Kraken X60 motors use TalonFX
import com.ctre.phoenix6.controls.*; // Needed for motor control modes
import com.ctre.phoenix6.signals.NeutralModeValue; // Needed for setting motor neutral mode
import com.ctre.phoenix6.configs.TalonFXConfiguration; // Needed for setting motor configurations

public class CoralEater9000 extends SubsystemBase {
    private final TalonFX armMotor; // Rotates the whole arm
    private final TalonFX intakeMotor; // Spins Omni wheels for intake
    private final TalonFXConfiguration config = new TalonFXConfiguration(); // Motor configuration for arm
    private final MotionMagicVoltage MotionMagic = new MotionMagicVoltage(0); // Motion Magic control mode for arm
    private double armHeldPosition = 0; // Initial position of arm 
    private final double kG = 0.5; // Gravity compensation (TUNE THIS VALUE), very important
    private final double maxVoltage = 6.0; // Max allowable voltage
    private final double minVoltage = 1.0; // Min holding voltage

    public CoralEater9000() {
        armMotor = new TalonFX(23, "rio");  // Replace with actual CAN ID
        intakeMotor = new TalonFX(22, "rio");  // Replace with actual CAN ID 
        armMotor.setNeutralMode(NeutralModeValue.Brake); // Set arm motor to brake mode

        // Magic Motion Configuration
        config.Slot0.kS = 0.1; // Static friction compensation (TUNE), not needed to be maximum precision
        config.Slot0.kV = 0.12; // Velocity feedforward (TUNE), adjust as needed
        config.Slot0.kA = 0.01; // Acceleration feedforward (TUNE), adjust as needed
        config.Slot0.kP = 0.2;  // Proportional gain (TUNE), corrects errors, increase till is quick, but not overshooting
        config.Slot0.kI = 0.0;  // Integral gain (leave 0 unless needed)
        config.Slot0.kD = 0.01;  // Derivative gain (TUNE), fixes overshooting
        config.Slot0.kG = kG;  // Gravity compensation (TUNE), gravity, value is changed up above

        // Motion Magic settings
        config.MotionMagic.MotionMagicCruiseVelocity = 40; // Max velocity (TUNE)
        config.MotionMagic.MotionMagicAcceleration = 80;  // Acceleration (TUNE)
        config.MotionMagic.MotionMagicJerk = 400;  // Smoother motion

        // Apply configuration to motors
        armMotor.getConfigurator().apply(config);
    }

    /** Returns current arm position (encoder units) */
    public double getCurrentPosition() {
        return armMotor.getPosition().getValueAsDouble();
    }

    /** Moves arm to a specific position using Motion Magic */
    public void moveToArmPosition(double targetArmPosition) {
        armMotor.setControl(MotionMagic.withPosition(targetArmPosition));
        armHeldPosition = targetArmPosition;
    }

    /** Spins the intake wheels. Positive = intake, Negative = outtake */
    public void setIntakeSpeed(double speed) {
        intakeMotor.setControl(new DutyCycleOut(speed));
    }

    /** Stops all motors */
    public void stopCoralEating() {
        armMotor.set(0);
        intakeMotor.setControl(new DutyCycleOut(0));
    }

    /** Stops intake arm */
    public void stopIntakeArm() {
        armMotor.set(0);
    }

    /** Stops intake spinning */
    public void stopIntakeSpinning() {
        intakeMotor.setControl(new DutyCycleOut(0));
    }

    /** Gravity Compensation Hold Mode */
    public void holdPosition() {
        double angle = getArmAngle(); // Implement this based on encoder values
        double holdVoltage = kG * Math.cos(Math.toRadians(angle));

        // Clamp voltage between min and max
        holdVoltage = Math.max(minVoltage, Math.min(holdVoltage, maxVoltage));

        armMotor.setVoltage(holdVoltage);
    }

    /** Converts encoder position to arm angle (TUNE this for your encoder setup) */
    private double getArmAngle() {
        return getCurrentPosition() * 360.0 / 2048.0; // Adjust based on gear ratio & encoder units
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (armMotor.getVelocity().getValueAsDouble() < 0.1) { // If near stationary, hold position
            holdPosition();
        } else {
            armMotor.setControl(MotionMagic.withPosition(armHeldPosition));
        }
    }
}