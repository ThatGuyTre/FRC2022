// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import java.lang.Math;

/** This is a demo program showing how to use Mecanum control with the MecanumDrive class. */
public class Robot extends TimedRobot {
  private static final int kFrontLeftChannel = 2; // 15?
  private static final int kRearLeftChannel = 3; // 0?
  private static final int kFrontRightChannel = 1;
  private static final int kRearRightChannel = 0;
  private static final int kConveyorChannel = 4;
  private static final int kArmChannel = 5;
  private static final int kIntakeChannel = 6; // Not testable yet - no controller

  private static final int kDriveChannel = 0;
  private static final int kControlChannel = 1;

  private MecanumDrive m_robotDrive;
  private Joystick m_stick;//Gamepad for driving
  private Joystick c_stick;//Gamepad for controls

  private PWMSparkMax conveyor = new PWMSparkMax(kConveyorChannel);
  private PWMSparkMax arm = new PWMSparkMax(kArmChannel);
  private PWMVictorSPX intake = new PWMVictorSPX(kIntakeChannel);
    
  private enum Buttons {
    A(0),B(1),X(2),Y(3),LB(4),RB(5),BACK(6),START(7),L3(8),R3(9);

    private final int value;
    private Buttons(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
  }
  
  @Override
  public void robotInit() {
    PWMVictorSPX frontLeft = new PWMVictorSPX(kFrontLeftChannel);
    PWMVictorSPX rearLeft = new PWMVictorSPX(kRearLeftChannel);
    PWMVictorSPX frontRight = new PWMVictorSPX(kFrontRightChannel);
    PWMVictorSPX rearRight = new PWMVictorSPX(kRearRightChannel);


    // Invert the right side motors.
    // You may need to change or remove this to match your robot.
    frontRight.setInverted(true);
    rearRight.setInverted(true);

    conveyor.setInverted(false);
    arm.setInverted(false);
    intake.setInverted(false);

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_stick = new Joystick(kDriveChannel);
    c_stick = new Joystick(kControlChannel);
    conveyor.close();
  }

  double forward,backward;

  @Override
  public void teleopPeriodic() {
    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    m_robotDrive.driveCartesian(m_stick.getX(), m_stick.getY(), m_stick.getZ(), 0.0);
    //May need to square the x and y inputs to get a smoother curve

    forward = c_stick.getRawAxis(3);
    backward = -c_stick.getTwist();
    System.out.println(c_stick.getAxisCount());
    //These commands may need to change. The goal was to make the right trigger forward and the left trigger backward.

    conveyor.set((Math.abs(forward)>Math.abs(backward)) ? forward : backward);
    //The set command above is a little convoluted if you dont know about ternary operators
      //This is basically the same as saying if forward is greater than backward then pick forward,
      //otherwise pick backward to set the conveyor to
    arm.set((c_stick.getRawButton(Buttons.A.getValue())) ? .1 : 0); // Need to set button to someting nice
    //intake.set((c_stick.getRawButton(Buttons.B.getValue())) ? .1 : 0); //
  }
}
