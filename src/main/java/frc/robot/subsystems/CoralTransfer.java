package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;


public class CoralTransfer extends SubsystemBase {
    private final SparkMax coralIntestine; // Takes coral out of intake

    public CoralTransfer(){
        coralIntestine = new SparkMax(24, MotorType.kBrushless); // Replace with actual CAN ID
        SparkMaxConfig coralColonConfig = new SparkMaxConfig();

        coralColonConfig.idleMode(IdleMode.kBrake);
  
    }

    /* Spins the Coral Intestine wheel */
    public void setIntestineSpeed(double speed) {
        coralIntestine.set(speed);
    }

    /* Stops Intestine Wheel */
    public void stopIntestine() {
        coralIntestine.set(0);
    }

    public void stopCoralPooping() {
        coralIntestine.set(0);
    }
}
