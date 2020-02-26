package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;

import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Conveyor extends SubsystemBase {

    // Starts Conveyor Motors
    private static TalonSRX leftSideMotor       = new TalonSRX(Constants.leftSideMotorPort);
    private static VictorSPX rightSideMotor     = new VictorSPX(Constants.rightSideMotorPort);
    private static VictorSPX centerMotor        = new VictorSPX(Constants.centerMotorPort);
    private static TimeOfFlight checkPointOne   = new TimeOfFlight(Constants.checkPointOnePort);
    private static TimeOfFlight checkPointRight = new TimeOfFlight(Constants.checkPointRightPort);
    private static TimeOfFlight checkPointLeft  = new TimeOfFlight(Constants.checkPointLeftPort);
    private static PWMSparkMax ledMotor         = new PWMSparkMax(Constants.ledMotorPort);
    private int genericCount        = 0;
    private int ballsCount          = 0;
    private int rightBallCount      = 0;
    private int leftBallCount       = 0; 
    private boolean flag            = true;
    private boolean s               = false; 
    private boolean isFinished      = false;


    public Conveyor() {
        leftSideMotor.setNeutralMode(NeutralMode.Brake);
        rightSideMotor.setNeutralMode(NeutralMode.Brake);
        centerMotor.setNeutralMode(NeutralMode.Brake);
        leftSideMotor.setInverted(false);
        rightSideMotor.setInverted(true);
        leftSideMotor.setNeutralMode(NeutralMode.Brake);
        rightSideMotor.setNeutralMode(NeutralMode.Brake);
        centerMotor.setNeutralMode(NeutralMode.Brake);
        checkPointOne.setRangingMode(RangingMode.Short, 24);
        checkPointRight.setRangingMode(RangingMode.Short, 24);
        checkPointLeft.setRangingMode(RangingMode.Short, 24);
    }
  
    @Override
    public void periodic() {
        //comment
    }

    
    public void LEDRed(){
        SmartDashboard.putBoolean("Intake Complete?", false);
        ledMotor.set(Constants.red);
    }

    public void LEDGreen(){
        SmartDashboard.putBoolean("Intake Complete?", true);
        ledMotor.set(Constants.green);
    }

    public void blinkRainbow(){
        ledMotor.set(Constants.rainbow);
    }


    public void leftActivate(double speed){
        leftSideMotor.set(ControlMode.PercentOutput, (speed));
        centerMotor.set(ControlMode.PercentOutput, speed/2);
        rightSideMotor.set(ControlMode.PercentOutput, (speed/2));
    }

    public void rightActivate(double speed){
        rightSideMotor.set(ControlMode.PercentOutput, (speed));
        centerMotor.set(ControlMode.PercentOutput, (-speed/2));
        leftSideMotor.set(ControlMode.PercentOutput, (speed/2));
    }

    /**
     * 
     * @param speed Speed to run conveyor at
     * @param time Time to run in seconds
     */
    public void rightActivateTime(double speed, long time){
        Timer.delay(time);
        rightActivate(speed);
    }

    public void countBalls(TimeOfFlight sensor, int ballCount){
        if(sensor.getRange() < 50 && flag){
            genericCount++;
            flag = false;
            ballCount = genericCount;
        } else if(sensor.getRange() > 50 ){
            flag = true;
        }
    }



    public int getBallCount(int ballCount){
        return ballCount;
    }
        
   
    public boolean isBallIn (TimeOfFlight sensor){
        if(sensor.getRange() < 150 ){
            return true;
        } else {
            return false;
        }
    }

    public void conveyorIntakeRun(){
        countBalls(checkPointOne, ballsCount); // count balls everytime loop runs
        if(ballsCount <= 2){ // Enter loop b/w 0 balls and 2 balls
            LEDRed(); // TURN LED RED
            if(isBallIn(checkPointRight) == true){ // Check if there is a ball right under shooter
                rightActivate(0);
                s = true; 
            } else { // No ball under shooter -> continue
                if(isBallIn(checkPointOne) == true){ // check if there is ball on the first sensor
                    rightActivate(0.75); // Run right intake when sensor detects ball
                } else {
                    rightActivate(0); // Stop when ball leaves the sensor
                }
            }
        } else { 
            s = true; // Set flag variable to true after 2 balls have entered
        }

        if(s==true && ballsCount <= 5){ // Flag set to true -> Switch conveyor sides
            if(isBallIn(checkPointLeft) == true){ // Check if there is a ball right under shooter
                leftActivate(0);
                s = true; 
                LEDGreen(); // TURN LED GREEN - INTAKE FINISHED
            } else { // No ball under shooter -> continue
                if(isBallIn(checkPointOne) == true){ // check if there is ball on the first sensor
                    leftActivate(0.75); // Run right intake when sensor detects ball
                } else {
                    leftActivate(0); // Stop when ball leaves the sensor
                }
            }
        } else { // Intake finished -> reset all variables
            s = false;
            isFinished = true;
            resetBallCount();
        }
        
    }

    public void printTOFValues(){
        SmartDashboard.putBoolean("Time Of Flight Value Right Side ", isBallIn(checkPointRight));
        
        SmartDashboard.putBoolean("Time Of Flight Value Left Side ", isBallIn(checkPointLeft));
        
        SmartDashboard.putBoolean("Time Of Flight Value Intake Side ",isBallIn(checkPointOne));
    }

 
    public boolean leftStatus(){
        countBalls(checkPointLeft, leftBallCount);
        if(leftBallCount >= 3){
            return true; 
        }
        else {
            return false; 
        }
    }

    public void runWithSensor(){
        if(isBallIn(checkPointLeft)){
            rightActivate(0);
        } else {
            if(isBallIn(checkPointOne)){
                rightActivate(0.375);
            } else {
                rightActivate(0);
                leftActivate(0);
            }
        }
    }

    public boolean rightStatus(){
        countBalls(checkPointRight, rightBallCount);
        if(rightBallCount >= 2){
            return true;
        }
        else{
            return false;
        }
    }
    
    public void conveyorStop(){
        leftActivate(0);
        rightActivate(0);
    }

    public void resetBallCount(){
        ballsCount = 0;
    }
    
    public boolean isFinished(){
        return isFinished;
    }

    public void initIntake(){
        isFinished = false;
    }

    public void conveyorShooterRun(){

    }
  

  
  }
    
