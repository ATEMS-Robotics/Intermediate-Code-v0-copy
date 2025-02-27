package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.TalonFXConfiguration;

//import com.ctre.phoenix6.controls.DutyCycleOut;
import edu.wpi.first.wpilibj.DigitalInput;



public class IntakeArmMovement extends SubsystemBase {
    private final TalonFX armMotor; 
    private static final double MOTOR_ROTATIONS_TO_ARM_ROTATIONS = 8.4;
    private static final double TICKS_PER_REVOLUTION_OF_MOTOR = 2048.0;

    private final DigitalInput armLimitSwitch;
    private final TalonFXConfiguration config = new TalonFXConfiguration();
    private final MotionMagicVoltage motionMagic = new MotionMagicVoltage(0);
    //private final double maxVoltage = 6.0; // Max allowable voltage
    //private final double minVoltage = 1.0; // Min holding voltage
    /*private static final double TOP_POSITION = 0;
    private static final double SLOW_ZONE = -1;
    private static final double NORMAL_SPEED = 0.2;
    private static final double SLOW_SPEED = 0.08;*/

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
        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;

        armMotor.getConfigurator().apply(config); 
        
    }

    
        
    
    
    //Moves arm to a specific position and auto-holds 
    public Command moveToArmPosition(DoubleSupplier targetAngleSupplier) {
        return run(() -> {
       double targetAngle = targetAngleSupplier.getAsDouble();
       double targetRotations = (targetAngle / 360) * MOTOR_ROTATIONS_TO_ARM_ROTATIONS;
       double targetTicks = targetRotations * TICKS_PER_REVOLUTION_OF_MOTOR;

        System.out.println("Moving to Position:" + targetAngle + "degrees");
        armMotor.setControl(motionMagic.withPosition(targetTicks));

    }).until(() -> Math.abs(getCurrentAngle() - targetAngleSupplier.getAsDouble()) < 2.0); }
  

    private double getCurrentAngle() {
        double motorRotations = armMotor.getPosition().getValueAsDouble() / TICKS_PER_REVOLUTION_OF_MOTOR;
        return (motorRotations / MOTOR_ROTATIONS_TO_ARM_ROTATIONS) * 360;
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

