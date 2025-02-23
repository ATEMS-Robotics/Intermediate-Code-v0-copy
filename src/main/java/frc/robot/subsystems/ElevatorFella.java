package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX; // Kraken X60 motors use TalonFX
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.*; // Needed for setting motor control modes
import com.ctre.phoenix6.configs.TalonFXConfiguration; 


public class ElevatorFella extends SubsystemBase {
    private final TalonFX elevatorMotor1; // First Kraken X60 motor
    private final TalonFX elevatorMotor2; // Second Kraken X60 motor
    private final TalonFXConfiguration config = new TalonFXConfiguration();
    private final MotionMagicDutyCycle motionMagic = new MotionMagicDutyCycle(0);

    public ElevatorFella() {
        elevatorMotor1 = new TalonFX(20, "rio"); // Replace with your actual CAN ID
        elevatorMotor2 = new TalonFX(21, "rio"); // Replace with your actual CAN ID
        
       elevatorMotor1.setNeutralMode(NeutralModeValue.Brake); // Set the brake mode
       elevatorMotor2.setNeutralMode(NeutralModeValue.Brake); //Set the brake mode

             // Configure PID values
        config.Slot0.kS = 0.25; // Static friction compensation
        config.Slot0.kV = 0.12; // Velocity feedforward
        config.Slot0.kA = 0.01; // Acceleration feedforward
        config.Slot0.kP = 4.8;  // Proportional gain
        config.Slot0.kI = 0.0;  // Integral gain (leave 0 unless needed)
        config.Slot0.kD = 0.1;  // Derivative gain

        // Configure Motion Magic
        config.MotionMagic.MotionMagicCruiseVelocity = 80; // Max velocity (rotations per second)
        config.MotionMagic.MotionMagicAcceleration = 160;  // How fast it accelerates (rps/s)
        config.MotionMagic.MotionMagicJerk = 1600;         // How smooth the motion transition is

        // Apply configuration to motors
        elevatorMotor1.getConfigurator().apply(config);
        elevatorMotor2.getConfigurator().apply(config);
    }

    /**
     * Sets the elevator speed (positive = up, negative = down).
     * @param speed -1.0 to 1.0
     */
    
       
    

    public double getCurrentPosition() {
        return elevatorMotor1.getPosition().getValueAsDouble();
    }

    public void moveToElevatorPosition(double targetElevatorPosition) {
        elevatorMotor1.setControl(motionMagic.withPosition(targetElevatorPosition));
        elevatorMotor2.setControl(motionMagic.withPosition(targetElevatorPosition));
    }

    /** Stops the elevator */
    public void stopElevator() {
        elevatorMotor1.set(0);
        elevatorMotor2.set(0);
    }

   



  public class MoveElevator extends Command {
        private final ElevatorFella elevator;
    
    
        public MoveElevator(ElevatorFella elevator, double speed) {
            this.elevator = elevator;
            addRequirements(elevator); // Ensures only one command controls the elevator at a time
        }
    
        @Override
        public void execute() {

        }
    
        @Override
        public void end(boolean interrupted) {
            elevator.stopElevator(); // Stops the elevator when the command ends
        }
    
        @Override
        public boolean isFinished() {
            return false; // Keeps running until button is released
        }
    }
    



}