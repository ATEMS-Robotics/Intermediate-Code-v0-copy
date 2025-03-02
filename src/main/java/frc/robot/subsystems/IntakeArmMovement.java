package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;


//import java.util.function.DoubleSupplier;
//import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

//import com.ctre.phoenix6.controls.DutyCycleOut;



public class IntakeArmMovement extends SubsystemBase {
    private final TalonFX armMotor; 

    private final TalonFXConfiguration config = new TalonFXConfiguration();
    private final MotionMagicVoltage motionMagic = new MotionMagicVoltage(0);

   
    public IntakeArmMovement() {
        armMotor = new TalonFX(23, "rio"); 
        armMotor.setNeutralMode(NeutralModeValue.Brake);
        armMotor.setPosition(0);
        // Motion Magic Configuration
        config.Slot0.kS = 0.075; 
        config.Slot0.kV = 0.12; 
        config.Slot0.kA = 0.01; 
        config.Slot0.kP = 0.3;  
        config.Slot0.kI = 0.0;  
        config.Slot0.kD = 0.1;  
        config.Slot0.kG = 1.85;  

        config.MotionMagic.MotionMagicCruiseVelocity = .1; // In Rotations
        config.MotionMagic.MotionMagicAcceleration = .1; // In Rotations
        config.MotionMagic.MotionMagicJerk = 5; // In Rotations
        config.Feedback.SensorToMechanismRatio = 8.4;
        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
        

        

        armMotor.getConfigurator().apply(config); 
        
    }

    
        
    
    
    //Moves arm to a specific position and auto-holds 
    public Command moveToArmPosition(double targetRotations) {

        return run(() -> {
        System.out.println("Moving to Position:" + targetRotations + "rotations");
        armMotor.setControl(motionMagic.withPosition(targetRotations));

    }).until(() -> Math.abs(getCurrentAngle() - targetRotations) < 1); }
  

    private double getCurrentAngle() {
        return armMotor.getPosition().getValueAsDouble();
    }

    public Command printArmRotations() {
        return runOnce(() -> {
            System.out.println(getCurrentAngle() + ": Rotations");
        });
    }
    public Command setPositionToZero() {
        return runOnce(() -> {
            armMotor.setPosition(0);
        });
    }





   
    
    }

