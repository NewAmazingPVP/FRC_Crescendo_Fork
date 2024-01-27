package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.FavoritePositions;
import frc.robot.commands.SwerveAutoWait;
import frc.robot.commands.SwerveClover;
import frc.robot.commands.SwerveJoystickDefaultCmd;
import frc.robot.subsystems.DriverControls;
import frc.robot.subsystems.SwerveSubsystem;

public class RobotContainer {
    //intialization of different subsystems and commands
    public final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
    public final Joystick driver = new Joystick(0);
    public final DriverControls driverControls = new DriverControls(swerveSubsystem, driver);

    //object for presenting selection of options in shuffleboard/ smartdashboard
    SendableChooser<Command> autoChooser = new SendableChooser<>();
    //XboxController controller = new XboxController(0);

    public RobotContainer() {
        NamedCommands.registerCommand("wait", new WaitCommand(5.0));
        NamedCommands.registerCommand("test", Commands.print("Ok this is a test"));
        NamedCommands.registerCommand("waitTest", new SwerveAutoWait(this));
        //NamedCommands.registerCommand("wait", new SwerveClover(swerveSubsystem));
        autoChooser = AutoBuilder.buildAutoChooser("Auto Square");
        
        SmartDashboard.putData("Auto Chooser", autoChooser);

        swerveSubsystem.setDefaultCommand(new SwerveJoystickDefaultCmd(swerveSubsystem, driverControls));

        configureButtonBindings();
    }

    private void configureButtonBindings() {
        new JoystickButton(driver, 5).onTrue(new InstantCommand(() -> swerveSubsystem.gyro.zeroYaw()
        ));
        new JoystickButton(driver, 3).onTrue(new InstantCommand(() -> swerveSubsystem.autonavigator.toggle()));
        new JoystickButton(driver, 12).onTrue(new InstantCommand(() -> swerveSubsystem.autonavigator.navigateTo(FavoritePositions.AMP)));
        new JoystickButton(driver, 11).onTrue(new InstantCommand(() -> swerveSubsystem.autonavigator.navigateTo(FavoritePositions.SOURCE)));
        new JoystickButton(driver, 10).onTrue(new InstantCommand(() -> swerveSubsystem.autonavigator.navigateTo(FavoritePositions.STAGE)));
    }

    public Command getAutonomousCommand() {
        //returns what autonomous path is chosen in shuffleboard currently
        return autoChooser.getSelected();
        //return new PathPlannerAuto("Test Auto");
    }
}
