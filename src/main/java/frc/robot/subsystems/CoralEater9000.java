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
    private final MotionMagicDutyCycle MotionMagic = new MotionMagicDutyCycle(0); // Motion Magic control mode for arm
    private double armHeldPosition = 0; // Inital position of arm 


    public CoralEater9000() {
        armMotor = new TalonFX(23, "rio");  // Replace with actual CAN ID
        intakeMotor = new TalonFX(22, "rio");  // Replace with actual CAN ID 
        armMotor.setNeutralMode(NeutralModeValue.Brake); // Set arm motor to brake mode

        //Magic Motion Configuration
        config.Slot0.kS = 0.0; // Static friction compensation
        config.Slot0.kV = 0.0; // Velocity feedforward
        config.Slot0.kA = 0.0; // Acceleration feedforward
        config.Slot0.kP = 0.0;  // Proportional gain
        config.Slot0.kI = 0.0;  // Integral gain (leave 0 unless needed)
        config.Slot0.kD = 0.0;  // Derivative gain
        config.Slot0.kG = 0.0;  // Gravity
        
        //Motion Magic settings
        config.MotionMagic.MotionMagicCruiseVelocity = 0; // Max velocity (rotations per second)
        config.MotionMagic.MotionMagicAcceleration = 0;  // How fast it accelerates (rps/s)
        config.MotionMagic.MotionMagicJerk = 0;  // How smooth the motion transition is

        // Apply configuration to motors
        armMotor.getConfigurator().apply(config);
    
    }

   public double getCurrentPosition() {
        return armMotor.getPosition().getValueAsDouble();
    }

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

    public void stopIntakeArm() {
        armMotor.set(0);
    }

    public void stopIntakeSpinning() {
        intakeMotor.setControl(new DutyCycleOut(0));
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        armMotor.setControl(MotionMagic.withPosition(armHeldPosition));
    }
}
