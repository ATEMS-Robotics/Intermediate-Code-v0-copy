package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
//import com.ctre.phoenix6.controls.DutyCycleOut;
import edu.wpi.first.wpilibj.DigitalInput;



public class IntakeArmMovement extends SubsystemBase {
    private final TalonFX armMotor; 

    private final DigitalInput armLimitSwitch;
    private final TalonFXConfiguration config = new TalonFXConfiguration();
    private final MotionMagicVoltage motionMagic = new MotionMagicVoltage(0);
    //private final double maxVoltage = 6.0; // Max allowable voltage
    //private final double minVoltage = 1.0; // Min holding voltage
    /*private static final double TOP_POSITION = 0;
    private static final double SLOW_ZONE = -1;
    private static final double NORMAL_SPEED = 0.2;
    private static final double SLOW_SPEED = 0.08;*/
    //private static final double MOTOR_ROTATIONS_TO_ARM_ROTATIONS = 1.0/8.4;

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

    /*public double getArmRotations() {
        return armMotor.getPosition().getValueAsDouble() * MOTOR_ROTATIONS_TO_ARM_ROTATIONS;
    }

    public double ArmDegrees() {
        return getArmRotations() * 360;
    }*/

    
    

    //Moves arm to a specific position and auto-holds 
    public Command moveToArmPosition(double targetArmPosition) {
        return run(() -> armMotor.setControl(motionMagic.withPosition(targetArmPosition)));
    } 

      
    
    
       /*public void holdPosition() {
        double angle = getArmAngle(); // Implement this based on encoder values
        double holdVoltage = kG * Math.cos(Math.toDegrees(angle)); 

        // Clamp voltage between min and max
        holdVoltage = Math.max(minVoltage, Math.min(holdVoltage, maxVoltage));

        armMotor.setVoltage(holdVoltage);
    } */
    
        /*public Command moveArmUp() {
        return run(() -> {
          
            double position = armMotor.getPosition().getValueAsDouble();
            if(position >= TOP_POSITION) {
                armMotor.setVoltage(2);
            } else {   
            //double distanceToTop = Math.max(1, (TOP_POSITION - position)); MAYBE MAKE THIS TO SCALE SPEEDa
            double speed = (position >= SLOW_ZONE) ? SLOW_SPEED : NORMAL_SPEED; 
            armMotor.setControl(new DutyCycleOut(speed));
            }

        });
    }

    public Command moveArmDown(){
        return run(() -> armMotor.setControl(new DutyCycleOut(-SLOW_SPEED)));
    }

    public Command stopArm() {
        return runOnce(() -> armMotor.setControl(new DutyCycleOut(0)));
    }

    public Command noodleSquisher(){
        return run(() -> armMotor.setVoltage(2));
    }
    /* Returns current arm position (encoder units) 
    public double getCurrentPosition() {
        return armMotor.getPosition().getValueAsDouble(); 
    } */
    @Override
    public void periodic() {
        if (!armLimitSwitch.get()) {
            System.out.println("My Dylon Is the Best");
            System.out.println("Arm Position: " + armMotor.getPosition().getValueAsDouble());
            //System.out.println("Arm Degrees:" +ArmDegrees());
        }
    }
}

