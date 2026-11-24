package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import com.qualcomm.robotcore.hardware.DcMotorEx;
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
        DcMotor transfer = hardwareMap.dcMotor.get("transfer");
        DcMotor pusher = hardwareMap.dcMotor.get("pusher");
        CRServo spinnyWheel1 =hardwareMap.crservo.get(" spinnyWheel1");
        CRServo spinnyWheel2 =hardwareMap.crservo.get(" spinnyWheel2");
        
        DcMotorEx shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter");
//
//        CRServo turret = hardwareMap.crservo.get("turret");


        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        boolean isIntaking = false;
        boolean isOutaking = false;
        double targetVelocity = 1500.0;
        double tolerance = 200.0;


        boolean isSpinningLeft = false;
        boolean isSpinningRight = false;


        // boolean isFlywheelMotorOn = false;
        // boolean lastButtonState = false;


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
                    - y * Math.sin(botHeading);

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

            transfer.setPower(-1.0);


            shooter.setVelocity(1550);


            if (gamepad1.right_trigger >= 0.5) {
//                isIntaking= !isIntaking;
//                isOutaking = false;
//                isSpinningLeft = !isSpinningLeft;
//                isSpinningRight = !isSpinningRight;

                isOutaking = true;


            } else {


                isOutaking = false;
            }

            if (gamepad1.left_trigger >= 0.5) {
//                isOutaking = !isOutaking;
//                isIntaking = false;
                isSpinningLeft = true;
                isSpinningRight = true;
                isIntaking = true;

            } else {
                isIntaking = false;
            }

//            if (isIntaking) {
//                intake.setPower(1.0);
//            } else {
//
//                intake.setPower(0.0);
//            }
            if(gamepad1.a)
            {
                if(Math.abs(shooter.getVelocity()-targetVelocity) <= tolerance) {
                    pusher.setPower(-0.7);
                }
            }else{

                pusher.setPower(0.0);
            }

            if (isOutaking) {
                intake.setPower(-1.0);
                spinnyWheel2.setPower (-1.0);
                spinnyWheel1.setPower(1.0);
            } else if (isIntaking ) {
            intake.setPower(1.0);
                spinnyWheel2.setPower (1.0);
                spinnyWheel1.setPower(-1.0);

            }
            else
            {
                spinnyWheel2.setPower (0.0);
                spinnyWheel1.setPower(0.0);
                intake.setPower(0.0);

            }
            
            telemetry.addData("yaw", botHeading);


            telemetry.update();
        }
    }
}