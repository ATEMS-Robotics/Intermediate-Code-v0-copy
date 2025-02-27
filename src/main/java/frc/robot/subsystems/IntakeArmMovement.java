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
import edu.wpi.first.wpilibj.DigitalInput;



public class IntakeArmMovement extends SubsystemBase {
    private final TalonFX armMotor; 

    private final DigitalInput armLimitSwitch;
    private final TalonFXConfiguration config = new TalonFXConfiguration();
    private final MotionMagicVoltage motionMagic = new MotionMagicVoltage(0);

   
    public IntakeArmMovement() {
        armMotor = new TalonFX(23, "rio"); 
        armLimitSwitch = new DigitalInput(0);
        armMotor.setNeutralMode(NeutralModeValue.Brake);
        armMotor.setPosition(0);

        
        // Motion Magic Configuration
        config.Slot0.kS = 0.05; 
        config.Slot0.kV = 0.12; 
        config.Slot0.kA = 0.01; 
        config.Slot0.kP = 0.05;  
        config.Slot0.kI = 0.0;  
        config.Slot0.kD = 0.1;  
        config.Slot0.kG = 1.65;  

        config.MotionMagic.MotionMagicCruiseVelocity = 80;
        config.MotionMagic.MotionMagicAcceleration = 80;
        config.MotionMagic.MotionMagicJerk = 400;
        config.Feedback.SensorToMechanismRatio = 8.4;
        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;

        armMotor.getConfigurator().apply(config); 
        
    }

    
        
    
    
    //Moves arm to a specific position and auto-holds 
    public Command moveToArmPosition(double targetAngle) {

        return run(() -> {
        System.out.println("Moving to Position:" + targetAngle + "degrees");
        armMotor.setControl(motionMagic.withPosition(targetAngle));

    }).until(() -> Math.abs(getCurrentAngle() - targetAngle) < 2.0); }
  

    private double getCurrentAngle() {
        return armMotor.getPosition().getValueAsDouble();
    }

    //public Command stopArm() {
     //   return runOnce(() -> armMotor.setPosition(-1));
    //}

    @Override
    public void periodic() {
        if (!armLimitSwitch.get()) {
            System.out.println("My Dylon Is the Best");
            System.out.println("Arm Position: " + armMotor.getPosition().getValueAsDouble()); 
            //System.out.println("Arm Degrees:" +ArmDegrees());
        }
    }
}

