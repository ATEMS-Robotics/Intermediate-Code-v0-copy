package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;


public class CoralPooper extends SubsystemBase {
    private final SparkMax coralIntestine; // Takes coral out of intake
    private final SparkMax coralColon; // Scores coral from the elevator

    public CoralPooper() {
        coralIntestine = new SparkMax(24, MotorType.kBrushless); // Replace with actual CAN ID
        coralColon = new SparkMax(25, MotorType.kBrushless); // Replace with actual CAN ID
        SparkMaxConfig coralColonConfig = new SparkMaxConfig();

        coralColonConfig.idleMode(IdleMode.kBrake);
  

    }

    /* Spins the Coral Intestine wheel */
    public void setIntestineSpeed(double speed) {
        coralIntestine.set(speed);
    }

    /* Spins the Coral Colon wheel */
    public void setColonSpeed(double speed) {
        coralColon.set(speed);
    }

    /* Stops Intestine Wheel */
    public void stopIntestine() {
        coralIntestine.set(0);
    }

    /* Stops Colon Wheel */
    public void stopColon() {
        coralColon.set(0);
    }

    public void stopCoralPooping() {
        coralColon.set(0);
        coralIntestine.set(0);
    }
}
