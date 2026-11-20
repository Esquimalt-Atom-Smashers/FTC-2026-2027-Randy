package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

@TeleOp
public class MECHAATOM extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor  = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor   = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRightMotor  = hardwareMap.dcMotor.get("backRightMotor");
        DcMotor intake = hardwareMap.dcMotor.get("intake");
        CRServo SpinnyWheel1 =hardwareMap.crservo.get(" SpinnyWheel1");
        CRServo SpinnyWheel2 =hardwareMap.crservo.get(" SpinnyWheel2");


        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        boolean isIntaking = false;
        boolean isOutaking = false;
        boolean isSpinningRight = false;
        boolean isSpinningLeft  = false;


        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        IMU imu = hardwareMap.get(IMU.class, "imu");

        imu.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x; // Counteract imperfect strafing
            double rot = gamepad1.right_stick_x;

            double botHeading = imu.getRobotYawPitchRollAngles()
                    .getYaw(AngleUnit.RADIANS);
            // Rotate joystick inputs based on robot heading
            double robotX = x * Math.cos(botHeading)
                    + y * Math.sin(botHeading);

            double robotY = x * Math.sin(botHeading)
                    + y * Math.cos(botHeading);

            double denominator = Math.max(
                    Math.abs(robotY) + Math.abs(robotX) + Math.abs(rot), 1
            );

            double frontLeftPower = (robotY + robotX + rot) / denominator;
            double backLeftPower = (robotY - robotX + rot) / denominator;
            double frontRightPower = (robotY - robotX - rot) / denominator;
            double backRightPower = (robotY + robotX - rot) / denominator;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);



            if (BlocksOpModeCompanion.gamepad1.leftTriggerWasPressed()) {
                isIntaking= !isIntaking;
                isOutaking = false;
                isSpinningLeft = !isSpinningLeft;
                isSpinningRight = !isSpinningRight;


            }

            if (BlocksOpModeCompanion.gamepad1.leftTriggerWasPressed()) {
                isOutaking = !isOutaking;
                isIntaking = false;
                isSpinningLeft = false;
                isSpinningRight = false;

            }

            if (isIntaking) {
                intake.setPower(-1.0);
            } else {
                intake.setPower(0.0);
            }

            if (isOutaking) {
                intake.setPower(1.0);
            } else {
                intake.setPower(0.0);
            }




            if (isSpinningLeft) {
                SpinnyWheel1.setPower(1);
            } else {
                SpinnyWheel1.setPower(0);
            }

            if (isSpinningRight) {
                SpinnyWheel2.setPower(-1);
            } else {
                SpinnyWheel2.setPower(0);
            }



            // frontLeftMotor.setPower(0.1);
            // backLeftMotor.setPower(0.1);
            // frontRightMotor.setPower(0.1);
            // backRightMotor.setPower(0.1);
            telemetry.addData("yaw", botHeading);

            telemetry.update();
        }
    }
}