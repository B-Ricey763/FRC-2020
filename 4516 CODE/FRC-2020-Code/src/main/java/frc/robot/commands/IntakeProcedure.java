/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Intake;

public class IntakeProcedure extends CommandBase {
  private final Intake intake;
  private final Conveyor conveyor; 
  


  public IntakeProcedure(Intake subsystem1, Conveyor subsystem2) {
    intake = subsystem1;  
    conveyor = subsystem2;
    addRequirements(conveyor, intake);
    
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.m_robotContainer.conveyor.initIntake();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Robot.m_robotContainer.intake.intakeDown();
    Robot.m_robotContainer.conveyor.conveyorIntakeRun();

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.m_robotContainer.intake.intakeUp();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Robot.m_robotContainer.conveyor.isFinished();
  }
}