package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.configs.TalonFXConfiguration;



public class IntakeArmMovement extends SubsystemBase {
    private final TalonFX armMotor; 
    private final TalonFXConfiguration config = new TalonFXConfiguration();
    private final MotionMagicVoltage motionMagic = new MotionMagicVoltage(0);
    private final double kG = 0.5; // Gravity compensation (TUNE THIS VALUE), very important
    private final double maxVoltage = 6.0; // Max allowable voltage
    private final double minVoltage = 1.0; // Min holding voltage

    public IntakeArmMovement() {
        armMotor = new TalonFX(23, "rio");  

        armMotor.setNeutralMode(NeutralModeValue.Brake);

        
        // Motion Magic Configuration
        config.Slot0.kS = 0.1; 
        config.Slot0.kV = 0.12; 
        config.Slot0.kA = 0.01; 
        config.Slot0.kP = 0.05;  
        config.Slot0.kI = 0.0;  
        config.Slot0.kD = 0.1;  
        config.Slot0.kG = 1.6775;  

        config.MotionMagic.MotionMagicCruiseVelocity = 40;
        config.MotionMagic.MotionMagicAcceleration = 80;
        config.MotionMagic.MotionMagicJerk = 400;
        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;

        armMotor.getConfigurator().apply(config);
    }

    /** Returns current arm position (encoder units) */
    public double getCurrentPosition() {
        return armMotor.getPosition().getValueAsDouble();
    }
    

    /** Moves arm to a specific position and auto-holds */
    public Command moveToArmPosition(double targetArmPosition) {
        return run(() -> armMotor.setControl(motionMagic.withPosition(targetArmPosition)));
    }
    
      /** Converts encoder position to arm angle (TUNE this for your encoder setup) */
      private double getArmAngle() {
        return getCurrentPosition() * 360.0 / 2048.0; // Adjust based on gear ratio & encoder units
    }
    
       /** Gravity Compensation Hold Mode */
       public void holdPosition() {
        double angle = getArmAngle(); // Implement this based on encoder values
        double holdVoltage = kG * Math.cos(Math.toDegrees(angle));

        // Clamp voltage between min and max
        holdVoltage = Math.max(minVoltage, Math.min(holdVoltage, maxVoltage));

        armMotor.setVoltage(holdVoltage);
    }


}

