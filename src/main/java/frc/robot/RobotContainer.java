package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.ElevatorFella;
import frc.robot.COMMANDS.*;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeArmMovement;
import frc.robot.subsystems.CoralScorer;
import frc.robot.subsystems.CoralTransfer;
import frc.robot.subsystems.WheelIntake;


public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1)
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    @SuppressWarnings ("unused")
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);
    private final CommandXboxController driverController = new CommandXboxController(0); // ✅ Using only one controller now
    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    // Subsystems
    private final ElevatorFella elevatorSubsystem = new ElevatorFella();
    private final IntakeArmMovement armMover = new IntakeArmMovement();
    private final WheelIntake IntakeWheelMover = new WheelIntake();
    private final CoralScorer coralScoring = new CoralScorer();
    private final CoralTransfer coralToScoring = new CoralTransfer();
    

    public RobotContainer() {
        configureBindings();
    }

    /*private void emergencyStop() { // Full stop code that stops everything.
        elevatorSubsystem.stopElevator();
        //armMover.stopIntakeArm();
        IntakeWheelMover.stopIntakeSpinning();
        //coralPooper.stopIntestine();
        coralPooper.stopColon();
        drivetrain.applyRequest(() -> brake); 
        System.out.println("🚨 Emergency Stop Activated! 🚨");
    } */
    private final boolean enableSysId = false; // Set to true for testing, false for competition, enables testing code

    private void configureBindings() {
        drivetrain.setDefaultCommand(
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-driverController.getLeftY() * MaxSpeed)
                    .withVelocityY(-driverController.getLeftX() * MaxSpeed)
                    .withRotationalRate(-driverController.getRightX() * MaxAngularRate)
            )
        );

        //driverController.x().whileTrue(drivetrain.applyRequest(() -> brake));
        driverController.y().and(driverController.back()).whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))
        ));


        
        drivetrain.registerTelemetry(logger::telemeterize);

        //Testing Code, for DriveTrain
        if (enableSysId) {
            driverController.back().and(driverController.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
            driverController.back().and(driverController.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
            driverController.start().and(driverController.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
            driverController.start().and(driverController.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
        }



        //CONTROLS
        driverController.y().and(driverController.start()).onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric())); // Resets the Current Direction to be Field-Oriented
        //driverController.back().and(driverController.start()).onTrue(new InstantCommand(() -> emergencyStop())); //Stops EVERYTHING when start and back are pressed at the same time.

    
        // Elevator Controls
        driverController.rightBumper().onTrue(elevatorSubsystem.moveToPosition(20)); //Elevator to middle
        driverController.rightBumper().onTrue(Commands.print("Elevator Go To 5")); // Debug Print
        driverController.rightBumper().onFalse((elevatorSubsystem.stopElevator())); // Safety Stop

        driverController.leftBumper().onTrue(elevatorSubsystem.moveToPosition(10)); //Elevator to middle
        driverController.leftBumper().onTrue(Commands.print("Elevator Go To 10")); // Debug Print
        driverController.leftBumper().onFalse((elevatorSubsystem.stopElevator())); // Safety Stop

        driverController.rightTrigger().onTrue(elevatorSubsystem.moveToPosition(0)); //Elevator to bottom
        driverController.rightTrigger().onTrue(Commands.print("Elevator Go To 0")); // Debug Print
        driverController.rightBumper().onFalse((elevatorSubsystem.stopElevator())); // Safety Stop

       
        // Coral Arm
        driverController.povUp().onTrue(armMover.moveToArmPosition(() -> 0)); // Coral Arm Up
        driverController.povUp().onTrue(Commands.print("Intake Arm Go 0")); // Debug Print
        //driverController.povUp().onFalse(armMover.stopArm());
        

        driverController.povDown().onTrue(armMover.moveToArmPosition(() -> -.8)); // Coral Arm Up
        driverController.povDown().onTrue(Commands.print("Intake Arm Go 1")); // Debug Print
       // driverController.povDown().onFalse(armMover.stopArm());
        
        driverController.y().onTrue(armMover.moveToArmPosition(() -> -1)); // Coral Arm Up
        driverController.y().onTrue(Commands.print("Intake Arm Go -1")); // Debug Print
        // driverController.y().onFalse(coralScoring.// Safety Stop 

  

        // Spin Intake Wheels
        driverController.b().whileTrue((IntakeWheelMover.spinIntakeCommand(0.3))); // Coral go out
        driverController.b().onTrue(Commands.print("Coral go out")); // Debug Print
        driverController.b().onFalse((IntakeWheelMover.stopIntakeSpinning())); // Safety Stop

        driverController.a().whileTrue((IntakeWheelMover.spinIntakeCommand(-0.3))); // Coral go out
        driverController.a().onTrue(Commands.print("Coral go in")); // Debug Print
        driverController.a().onFalse((IntakeWheelMover.stopIntakeSpinning())); // Safety Stop


        // Coral Scoring System
        driverController.leftTrigger().whileTrue(new CoralIntestine(coralToScoring, -0.2  )); // Coral Intestine (Removes coral from intake)
        driverController.leftTrigger().onTrue(Commands.print("Removing Coral From Intake")); // Debug Print
        driverController.leftTrigger().onFalse(new InstantCommand(() -> coralToScoring.stopIntestine())); // Safety Stop

        driverController.povLeft().whileTrue(new CoralColon(coralScoring, 0.1)); // Coral Colon (Moves it towards intake)
        driverController.povLeft().onTrue(Commands.print("Coral Colon Moving Back, In to Robot")); // Debug Print
        driverController.povLeft().onFalse(new InstantCommand(() -> coralScoring.stopColon())); // Safety Stop

        driverController.povRight().whileTrue(new CoralColon(coralScoring, -0.1)); // Coral Colon (Moves coral out of robot to elevator)
        driverController.povRight().onTrue(Commands.print("Coral Colon Moving Forward, Out of Robot")); // Debug Print
        driverController.povRight().onFalse(new InstantCommand(() -> coralScoring.stopColon())); // Safety Stop
    }   


    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
