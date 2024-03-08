package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.WristConstants;

public class WristSubsystem extends SubsystemBase {
    private final CANSparkMax wristMotor;
    private final SparkPIDController wristPidController;
    private final RelativeEncoder wristRelativeEncoder;
    private final double currentPosition;
    private double desiredPosition;
    private final boolean autoLockEnabledAmp = false;
    private final boolean autoLockEnabledSource = false;
    private final double ampPosition = 218; // Measured from cad, rounded to the nearest whole number
    private final double sourcePosition = 1; // TBD

    // Constructor for WristSubsystem
    public WristSubsystem() {

        wristMotor = new CANSparkMax(Constants.CANAssignments.WRIST_MOTOR_ID, MotorType.kBrushless);
        wristRelativeEncoder = wristMotor.getEncoder();
        wristPidController = wristMotor.getPIDController();
        wristPidController.setP(Constants.WristConstants.WRIST_PID_P);
        currentPosition = wristRelativeEncoder.getPosition();

    }

    // Sets the desired position to a specified angle
    public void adjustAngle(double changeInPosition) {

        desiredPosition = changeInPosition;

    }

    // Sets the desired position to a pre-determined angle for the amp
    public void ampPreset() {

        desiredPosition = (ampPosition / 360) * WristConstants.WRIST_GEAR_RATIO;

    }

    // Sets the desired position to a pre-determined angle for the source
    public void sourcePreset() {

        desiredPosition = (sourcePosition / 360) * WristConstants.WRIST_GEAR_RATIO;

    }

    // Called once per scheduler run
    @Override
    public void periodic() {

        // Moves the arm towards the desired position with PID
        wristPidController.setReference(desiredPosition, ControlType.kPosition);

        //this makes sure the wrist does not move anymore if the same position preset is pressed away
        if(currentPosition == desiredPosition){
            wristMotor.stopMotor();
        }

        Logger.recordOutput("Wrist Desired Position", desiredPosition);
        Logger.recordOutput("Wrist Current Position", currentPosition);
        Logger.recordOutput("Wrist Auto Lock Enabled Amp", autoLockEnabledAmp);
        Logger.recordOutput("Wrist Auto Lock Enabled Source", autoLockEnabledSource);

    }

}
