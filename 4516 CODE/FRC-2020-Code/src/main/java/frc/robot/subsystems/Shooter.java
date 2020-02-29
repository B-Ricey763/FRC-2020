package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

    // Starts Shooter Motors
    private static TalonSRX shooter1Master = new TalonSRX(Constants.shooter1MasterPort);
    private static VictorSPX shooter1Slave = new VictorSPX(Constants.shooter1SlavePort);
    private static TalonSRX shooter2Master = new TalonSRX(Constants.shooter2MasterPort);
    private static VictorSPX shooter2Slave = new VictorSPX(Constants.shooter2SlavePort);
    private  double sensorVelocity;
    private double initVelocity;

    
    public Shooter() {

    }
  
    @Override
    public void periodic() {
        //comment
    }
  
    /**
 * 
 * @param deltaX X distance from target
 * 
 */
  public void shooterRPM(double deltaX){
    initVelocity = Math.sqrt( ( (-Constants.gravity * deltaX)/( ((Constants.outerPortHeightDelta * Math.cos(Math.toRadians(Constants.launchAngle)))/(deltaX)) - Math.sin(Math.toRadians(Constants.launchAngle)) ) )/( 2 * Math.cos( Math.toRadians(Constants.launchAngle)) ) );
    sensorVelocity = ( Constants.CPR* ( initVelocity/(6 * Constants.shooterRadius) ) )/(600 * Constants.gearRatioShooter);

    shooter1Master.set(ControlMode.Velocity, sensorVelocity, DemandType.Neutral, sensorVelocity);
    shooter1Slave.follow(shooter1Master);
    shooter2Master.set(ControlMode.Velocity, sensorVelocity, DemandType.Neutral, sensorVelocity);
    shooter2Slave.follow(shooter1Master);
    SmartDashboard.putNumber("Shooter Speed in m/s", initVelocity);
    SmartDashboard.putNumber("Shooter Sensor Speed", sensorVelocity);
  }

  public void setMotorSpeed(double speed){
    shooter1Master.set(ControlMode.PercentOutput, speed);
    shooter1Slave.set(ControlMode.PercentOutput, -speed);
    shooter2Master.set(ControlMode.PercentOutput, speed);
    shooter2Slave.set(ControlMode.PercentOutput, -speed);

  }

  


  
  }
    