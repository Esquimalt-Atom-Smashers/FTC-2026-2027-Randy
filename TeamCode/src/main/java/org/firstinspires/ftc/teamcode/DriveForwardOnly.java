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
//    DcMotor intake = hardwareMap.dcMotor.get("intake");
//    DcMotor transfer = hardwareMap.dcMotor.get("transfer");
//    DcMotor kicker = hardwareMap.dcMotor.get("kicker");
//    CRServo spinyWheel1 = hardwareMap.crservo.get(" spinyWheel1");
//    CRServo spinyWheel2 = hardwareMap.crservo.get(" spinyWheel2");
//    Servo blocker = hardwareMap.servo.get("blocker");



    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration

        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        backRightMotor = hardwareMap.dcMotor.get("backRightMotor");


        DcMotorEx shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter");

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();


        driveForward();
        sleep(1000);
        haltDriveMotors();
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

//    private void turnLeft()
//    {
//        frontLeftMotor.setPower(-1.0);
//        backLeftMotor.setPower(-1.0);
//        frontRightMotor.setPower(1.0);
//        backRightMotor.setPower(1.0);
//    }
//
//    private void goRight()
//    {
//        frontLeftMotor.setPower(1.0);
//        backLeftMotor.setPower(1.0);
//        frontRightMotor.setPower(-1.0);
//        backRightMotor.setPower(-1.0);
//    }


}
