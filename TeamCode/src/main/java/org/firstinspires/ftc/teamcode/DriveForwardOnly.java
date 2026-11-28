package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous
public class DriveForwardOnly extends LinearOpMode {

    DcMotor frontLeftMotor;
    DcMotor backLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backRightMotor;
    DcMotor intake;

    DcMotorEx shooter;




    DcMotor transfer;
    DcMotor kicker;
    CRServo spinyWheel1;
    CRServo spinyWheel2;
    Servo blocker;



    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration

        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        intake = hardwareMap.dcMotor.get("intake");
        shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter");
        spinyWheel1 = hardwareMap.crservo.get(" spinyWheel1");
        spinyWheel2 = hardwareMap.crservo.get(" spinyWheel2");
        transfer = hardwareMap.dcMotor.get("transfer");
        kicker  = hardwareMap.dcMotor.get("kicker");
        blocker  = hardwareMap.servo.get("blocker");


        shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter");

        shooter.setVelocityPIDFCoefficients(176,0,0,11.1);

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

//        double flywheelTargetVelocity = 1550.0;
//        final double flywheelRPMTolerance = 200.0;

        waitForStart();

        intake.setPower(1.0);
        spinyWheel1.setPower(1.0);
        spinyWheel2.setPower(1.0);

        shoot();
        turnLeft();
        haltDriveMotors();
        sleep(1000);
        driveForward();
        sleep(800);
        haltDriveMotors();
        sleep(1000);
        reverseOnward();
        sleep(250);
        haltDriveMotors();
        sleep(200);
        turnRight();
        sleep(1000);
        haltDriveMotors();
        driveForward();
        sleep(2000);
        haltDriveMotors();
//        reverseOnward();
//        sleep(100);
//        haltDriveMotors();
        // sleep(100);
//        turnRight();
        //haltDriveMotors();
        //sleep(1000);
        // driveForward();
        //sleep
//




//        driveForward();
//        sleep(500);
//        haltDriveMotors();
//        sleep(500);
//        turnRight();
//        haltDriveMotors();
//        sleep(500);
//        turnLeft();
//        haltDriveMotors();
//        sleep(500);
//        reverseOnward();
//        sleep(500);
//        haltDriveMotors();
//        sleep(500);
//        goRight();
//        sleep(500);
//        haltDriveMotors();
//        sleep(500);
//        goLeft();
//        sleep(500);
//        haltDriveMotors();


    }

    private void haltDriveMotors()
    {
        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
    }
    private void driveForward()
    {
        frontLeftMotor.setPower(1.0);
        backLeftMotor.setPower(1.0);
        frontRightMotor.setPower(1.0);
        backRightMotor.setPower(1.0);
    }

    private void reverseOnward()
    {
        frontLeftMotor.setPower(-1.0);
        backLeftMotor.setPower(-1.0);
        frontRightMotor.setPower(-1.0);
        backRightMotor.setPower(-1.0);
    }

    private void turnLeft()
    {
        frontLeftMotor.setPower(-1.0);
        backLeftMotor.setPower(-1.0);
        frontRightMotor.setPower(1.0);
        backRightMotor.setPower(1.0);
        sleep(360);

    }

    private void turnRight()
    {
        frontLeftMotor.setPower(1.0);
        backLeftMotor.setPower(1.0);
        frontRightMotor.setPower(-1.0);
        backRightMotor.setPower(-1.0);
        sleep(360);
    }

    private void goLeft()
    {
        frontLeftMotor.setPower(-1.0);
        backLeftMotor.setPower(1.0);
        frontRightMotor.setPower(1.0);
        backRightMotor.setPower(-1.0);


    }

    private void goRight()
    {
        frontLeftMotor.setPower(1.0);
        backLeftMotor.setPower(-1.0);
        frontRightMotor.setPower(-1.0);
        backRightMotor.setPower(1.0);

    }
    private void shoot()
    {
        shooter.setVelocity(1550);
        kicker.setPower(-0.7);
        sleep(1000);
        blocker.setPosition(1.0);
        transfer.setPower(1.0);
        sleep(5000);
        kicker.setPower(0.0);
        shooter.setVelocity(0);
        blocker.setPosition(0.6);
        transfer.setPower(0.0);




    }


}
