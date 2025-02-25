package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX; // Kraken X60 motors use TalonFX
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.*; // Needed for setting motor control modes
import com.ctre.phoenix6.configs.TalonFXConfiguration; 


public class ElevatorFella extends SubsystemBase {
    private final TalonFX elevatorMotor1; // First Kraken X60 motor
    private final TalonFX elevatorMotor2; // Second Kraken X60 motor
    private final TalonFXConfiguration config = new TalonFXConfiguration();
    private final MotionMagicVoltage motionMagicControl = new MotionMagicVoltage(0);

    public ElevatorFella() {
        elevatorMotor1 = new TalonFX(20, "rio"); // Replace with your actual CAN ID
        elevatorMotor2 = new TalonFX(21, "rio"); // Replace with your actual CAN ID

       elevatorMotor1.setNeutralMode(NeutralModeValue.Brake); // Set the brake mode
       elevatorMotor2.setNeutralMode(NeutralModeValue.Brake); //Set the brake mode

             // Configure PID values
        config.Slot0.kS = 0.0; // Static friction compensation
        config.Slot0.kV = 0.0; // Velocity feedforward
        config.Slot0.kA = 0.0; // Acceleration feedforward
        config.Slot0.kP = 0.0;  // Proportional gain
        config.Slot0.kI = 0.0;  // Integral gain (leave 0 unless needed)
        config.Slot0.kD = 0.0;  // Derivative gain
        

        // Configure Motion Magic
        config.MotionMagic.MotionMagicCruiseVelocity = 10; // Max velocity (rotations per second)
        config.MotionMagic.MotionMagicAcceleration = 10;  // How fast it accelerates (rps/s)
        config.MotionMagic.MotionMagicJerk = 160;         // How smooth the motion transition is

        // Apply configuration to motors
        elevatorMotor1.getConfigurator().apply(config);
        elevatorMotor2.getConfigurator().apply(config);
    }

    /**
     * Sets the elevator speed (positive = up, negative = down).
     * @param speed -1.0 to 1.0
     */
    
    public void setElevatorPositionInInches(double targetHeightInInches) {
        double inchesPerMotorRotation = 1.1; // How much the elevator moves per motor rotation
        double motorRotationsPerElevatorRotation = 5.0; // How many motor rotations per elevator gearbox rotation
        
        // inches -> elevator rotations -> motor rotations
        double targetElevatorRotations = (targetHeightInInches / inchesPerMotorRotation) * motorRotationsPerElevatorRotation;
        
        elevatorMotor1.setControl(new MotionMagicVoltage(targetElevatorRotations));
        elevatorMotor2.setControl(new MotionMagicVoltage(targetElevatorRotations));
    }   

    public double getCurrentHeight() {
        return elevatorMotor1.getPosition().getValueAsDouble();  // Returns the current position of the elevator
    }

    //Sends target elevator position to the motors
    

    public void moveToElevatorPosition(double targetElevatorPosition) {
        elevatorMotor1.setControl(motionMagicControl.withPosition(targetElevatorPosition));
        elevatorMotor2.setControl(motionMagicControl.withPosition(targetElevatorPosition));
    }

    /** Stops the elevator */
    public void stopElevator() {
        //Holds Elevator in Place
        elevatorMotor1.setVoltage(-.35);
        elevatorMotor2.setVoltage(-.35);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
  
  }