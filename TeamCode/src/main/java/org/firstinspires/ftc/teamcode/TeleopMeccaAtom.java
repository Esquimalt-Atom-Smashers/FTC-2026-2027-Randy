package org.firstinspires.ftc.teamcode;

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

@TeleOp(name = "MECHAATOM")
public class TeleopMeccaAtom extends LinearOpMode {
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
        DcMotor kicker = hardwareMap.dcMotor.get("kicker");
        CRServo spinyWheel1 = hardwareMap.crservo.get(" spinyWheel1");
        CRServo spinyWheel2 = hardwareMap.crservo.get(" spinyWheel2");
        Servo blocker = hardwareMap.servo.get("blocker");
        
        DcMotorEx shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter");

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        boolean isIntaking = false;
        boolean isOutaking = false;
        double flywheelTargetVelocity = 1570.0;
        final double flywheelRPMTolerance = 200.0;
        final double slowMode = 0.4;
        boolean blocked =  true;
        double openBlocker = 1.0;
        double blockerTimer = 0.0;
        double closedBlocker = 0.6;
        double shooterP = 0;
        double shooterD = 0;
        double shooterF = 0;
        double increment = 0.1;

        // Track previous D-pad states so holding the button doesn't rapidly spam changes
        boolean prevDpadUp = false;
        boolean prevDpadDown = false;



        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        IMU imu = hardwareMap.get(IMU.class, "imu");

        shooter.setVelocityPIDFCoefficients(176,0,0,11.1);
        imu.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // Field Centric Code
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



            // Mechanisms
            transfer.setPower(1.0);

            shooter.setVelocity(flywheelTargetVelocity);

//            if(gamepad1.xWasPressed())
//            {
//                blocked =!blocked;
//            }
//
//            if (blocked){
//                blocker.setPosition(closedBlocker);
//            } else {
//                blocker.setPosition(openBlocker);
//            }



            if(gamepad1.back) {
                blocker.setPosition(openBlocker);
                intake.setPower(-0.5);
                transfer.setPower(-1.0);
                kicker.setPower(1.0);
            } else {
                if (gamepad1.right_trigger >= 0.5) {
                    isOutaking = true;
                } else {
                    isOutaking = false;
                }

                if (gamepad1.left_trigger >= 0.5) {
                    isIntaking = true;
                } else {
                    isIntaking = false;
                }

                if (gamepad1.a) {
                    if (Math.abs(shooter.getVelocity() - flywheelTargetVelocity) <= flywheelRPMTolerance) {
                        kicker.setPower(-0.7);
                        blocker.setPosition(openBlocker);
                        blockerTimer = getRuntime();
                    }
                } else {
                    blocker.setPosition(closedBlocker);
                    if (getRuntime() > (blockerTimer + 1)) {//if it has been more than 1s since the blocker was open, turn off the kicker
                        kicker.setPower(0.0);
                    }
                }
                // Detect single button presses (rising edge) for D-pad Up and Down
//                boolean dpadUpPressed = gamepad1.dpad_up && !prevDpadUp;
//                boolean dpadDownPressed = gamepad1.dpad_down && !prevDpadDown;
//
//                // Update previous states for the next loop iteration
//                prevDpadUp = gamepad1.dpad_up;
//                prevDpadDown = gamepad1.dpad_down;
//
//                // Determine direction (+1 for up, -1 for down, 0 for neither)
//                int direction = 0;
//                if (dpadUpPressed) {
//                    direction = 1;
//                } else if (dpadDownPressed) {
//                    direction = -1;
//                }
//
//                // Apply changes only when D-pad Up or Down is newly pressed
//                if (direction != 0) {
//                    // If X is pressed, change shooterP values by the increment
//                    if (gamepad1.x) {
//                        shooterP += direction * increment;
//                    } else if (gamepad1.right_bumper) {
//                        flywheelTargetVelocity += increment * direction;
//                    }
//                    // If Y is pressed, change shooterD values by the increment
//                    else if (gamepad1.y) {
//                        shooterD += direction * increment;
//                    }
//                    // If B is pressed, change shooterF values by the increment
//                    else if (gamepad1.b) {
//                        shooterF += direction * increment;
//                    }
//                    // If Left Bumper is pressed, change increment by an order of magnitude (x10 or /10)
//                    else if (gamepad1.left_bumper) {
//                        if (direction > 0) {
//                            increment *= 10.0;
//                        } else {
//                            increment /= 10.0;
//                        }
//                    }
//                    shooter.setVelocityPIDFCoefficients(shooterP, 0, shooterD, shooterF);
//                }

                if (isOutaking) {
                    intake.setPower(1.0);
                    spinyWheel2.setPower(-1.0);
                    spinyWheel1.setPower(1.0);
                } else if (isIntaking) {
                    intake.setPower(-1.0);
                    spinyWheel2.setPower(1.0);
                    spinyWheel1.setPower(-1.0);
                } else {
                    spinyWheel2.setPower(0.0);
                    spinyWheel1.setPower(0.0);
                    intake.setPower(0.0);
                }

            }
            //if the x button is pressed, use the up and down arrows on the dpad to change the shooterP values by the increment
            //if the y button is pressed, use the up and down arrows on the dpad to change the shooterD values by the increment
            //if the b button is pressed, use the up and down arrows on the dpad to change the shooterF values by the increment
            //if the left bumper button is pressed, use the up and down arrows on the dpad to change the increment value by an order of magnitude


            telemetry.addData("yaw", botHeading);
            telemetry.addData("RPM",shooter.getVelocity());
            telemetry.addData("Target RPM",flywheelTargetVelocity);
            //telemetry.addData("BlockerPos", blocker.getPosition());
            telemetry.addData("shooterP x ", shooterP);
            telemetry.addData("shooterD y ",shooterD);
            telemetry.addData("shooterF b ", shooterF);
            telemetry.addData("increment LB ", increment);

            telemetry.update();
        }
    }

    private static double GetIMUHeadingRadians(IMU imu) {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }
}