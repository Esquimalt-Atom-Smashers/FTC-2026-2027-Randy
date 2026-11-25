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

//      CRServo turret = hardwareMap.crservo.get("turret");

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        boolean isIntaking = false;
        boolean isOutaking = false;
        double targetVelocity = 1500.0;
        final double kTolerance = 200.0;
        final double kSlowMode = 0.4;

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
            //Field Centric Code
            double y  = -gamepad1.left_stick_y;
            double x  =  gamepad1.left_stick_x;
            double rx =  gamepad1.right_stick_x;

            double speed = gamepad1.right_bumper ? 0.4 : 1.0; // slow mode
            if (gamepad1.options || gamepad1.back) {
                imu.resetYaw();
            }

            double botHeading = GetIMUHeadingRadians(imu);

            // Rotate the translation vector by -heading
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX *= 1.1; // counteract imperfect strafing

            // Normalize so no wheel exceeds 1.0 and ratios are preserved
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower * speed);
            backLeftMotor.setPower(backLeftPower * speed);
            frontRightMotor.setPower(frontRightPower * speed);
            backRightMotor.setPower(backRightPower * speed);

            transfer.setPower(-1.0);

            shooter.setVelocity(1550);

            if (gamepad1.right_trigger >= 0.5) {
//              isIntaking= !isIntaking;
//              isOutaking = false;
//              isSpinningLeft = !isSpinningLeft;
//              isSpinningRight = !isSpinningRight;

                isOutaking = true;
            } else {
                isOutaking = false;
            }

            if (gamepad1.left_trigger >= 0.5) {
//              isOutaking = !isOutaking;
//              isIntaking = false;
                isSpinningLeft = true;
                isSpinningRight = true;
                isIntaking = true;
            } else {
                isIntaking = false;
            }

//          if (isIntaking) {
//              intake.setPower(1.0);
//          } else {
//              intake.setPower(0.0);
//          }

            if(gamepad1.a) {
                if(Math.abs(shooter.getVelocity()-targetVelocity) <= kTolerance) {
                    pusher.setPower(-0.7);
                }
            } else{
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
            } else {
                spinnyWheel2.setPower (0.0);
                spinnyWheel1.setPower(0.0);
                intake.setPower(0.0);
            }
            
            telemetry.addData("yaw", botHeading);

            telemetry.update();
        }
    }

    private static double GetIMUHeadingRadians(IMU imu) {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }
}