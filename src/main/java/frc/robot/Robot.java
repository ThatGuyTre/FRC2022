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
  private static final int kFrontLeftChannel = 2;
  private static final int kRearLeftChannel = 3;
  private static final int kFrontRightChannel = 1;
  private static final int kRearRightChannel = 0;
  private static final int kConveyorChannel = 5;

  private static final int kDriveChannel = 0;
  private static final int kControlChannel = 1;

  private MecanumDrive m_robotDrive;
  private Joystick m_stick;
  private Joystick c_stick;

  PWMSparkMax conveyor = new PWMSparkMax(kConveyorChannel);

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

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_stick = new Joystick(kDriveChannel);
    c_stick = new Joystick(kControlChannel);
  }

  double forward,backward;

  @Override
  public void teleopPeriodic() {
    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    m_robotDrive.driveCartesian(m_stick.getX(), m_stick.getY(), m_stick.getZ(), 0.0);
    //May need to square the x and y inputs to get a smoother curve

    forward = c_stick.getThrottle();
    backward = -c_stick.getTwist();
    //These commands may need to change. The goal was to make the right trigger forward and the left trigger backward.

    conveyor.set((Math.abs(forward)>Math.abs(backward)) ? forward : backward);
    //The set command above is a little convoluted if you dont know about ternary operators
      //This is basically the same as saying if forward is greater than backward then pick forward, otherwise pick backward to set the conveyor to
  }
}
