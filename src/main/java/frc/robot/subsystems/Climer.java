package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue; // Kraken X60 motors use TalonFX

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase; // Needed for setting motor control modes

public class Climer extends SubsystemBase {
    private final TalonFX climerMotor1; // First Kraken X60 motor
    private final TalonFX climerMotor2; // Second Kraken X60 motor
    public Climer() {
        climerMotor1 = new TalonFX(26,"rio"); // Replace with your actual CAN ID
        climerMotor2 = new TalonFX(27,"rio"); // Replace with your actual CAN ID
        
        climerMotor1.setNeutralMode(NeutralModeValue.Brake); // Set the brake mode
        climerMotor2.setNeutralMode(NeutralModeValue.Brake); //Set the brake mode
    }

// in init function
var talonFXConfigs = new TalonFXConfiguration();

// set slot 0 gains
var slot0Configs = talonFXConfigs.Slot0;
slot0Configs.kS = 0.25; // Add 0.25 V output to overcome static friction
slot0Configs.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
slot0Configs.kA = 0.01; // An acceleration of 1 rps/s requires 0.01 V output
slot0Configs.kP = 0.11; // An error of 1 rps results in 0.11 V output
slot0Configs.kI = 0; // no output for integrated error
slot0Configs.kD = 0; // no output for error derivative

// set Motion Magic Velocity settings
var motionMagicConfigs = talonFXConfigs.MotionMagic;
motionMagicConfigs.MotionMagicAcceleration = 400; // Target acceleration of 400 rps/s (0.25 seconds to max)
motionMagicConfigs.MotionMagicJerk = 4000; // Target jerk of 4000 rps/s/s (0.1 seconds)

m_talonFX.getConfigurator().apply(talonFXConfigs);


    /**
     * Sets the elevator speed (positive = up, negative = down).
     * @param speed -1.0 to 1.0
     */
    public void setSpeed(double speed) {
        double holdVoltage = -.315  ; //Positin Holding Power
        double elevatorDeadband = 0.02; //Ignores small values of stuff so it aint tweak out
        if (Math.abs(speed) < elevatorDeadband) {
            climerMotor1.setVoltage(holdVoltage);
            climerMotor2.setVoltage(holdVoltage);
        } else {
            climerMotor1.setControl(new DutyCycleOut(speed));
            climerMotor2.setControl(new DutyCycleOut(speed));
        }
       
    }

    /** Stops the elevator */
    public void stop() {
        climerMotor1.set(0);
        climerMotor2.set(0);
    }





  public class MoveClimer extends Command {
        private final Climer climb;
        private final double speed;
    
        public MoveClimer(Climer climb, double speed) {

            this.climb = climb;
            this.speed = speed;
            addRequirements(climb); // Ensures only one command controls the elevator at a time
        }
    
        @Override
        public void execute() {
            climb.setSpeed(speed);
        }
    
        @Override
        public void end(boolean interrupted) {
            climb.stop(); // Stops the elevator when the command ends
        }
    
        @Override
        public boolean isFinished() {
            return false; // Keeps running until button is released
        }
    }
    



}