/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;

public class ConveyorFeedRight extends CommandBase {
  private final Conveyor conveyor; 
  public ConveyorFeedRight(Conveyor subsystem2) {
    conveyor = subsystem2;
    addRequirements(conveyor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    conveyor.startTime(); 
    conveyor.setTime();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(conveyor.getTime() > conveyor.getWaitTime() + 1 ){
      conveyor.rightActivate(0);
    } else {
      conveyor.rightActivate(.5);
    }
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    conveyor.rightActivate(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return conveyor.rightStatus();
  }
}
