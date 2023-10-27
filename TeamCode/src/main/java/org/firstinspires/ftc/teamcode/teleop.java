package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
@TeleOp(name="Robot: teleop", group="Robot")
public class teleop extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        hardware358 map = new hardware358(hardwareMap);
        map.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            double max;
            double test;

            //POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            //Combine the joystick requests for each axis-motion to determine each wheel's power.
            //Set up a variable for each drive wheel to save the power level for telemetry.
            double LeftFrontPower = axial + lateral + yaw;
            double RightFrontPower = axial - lateral - yaw;
            double LeftBackPower = axial - lateral + yaw;
            double RightBackPower = axial + lateral - yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(LeftFrontPower), Math.abs(RightFrontPower));
            max = Math.max(max, Math.abs(LeftBackPower));
            max = Math.max(max, Math.abs(RightBackPower));

            if (max > 1.0) {
                LeftFrontPower /= max;
                RightFrontPower /= max;
                LeftBackPower /= max;
                RightBackPower /= max;
            }

            // This is test code:
            //
            // Uncomment the following code to test your motor directions.
            // Each button should make the corresponding motor run FORWARD.
            //   1) First get all the motors to take to correct positions on the robot
            //      by adjusting your Robot Configuration if necessary.
            //   2) Then make sure they run in the correct direction by modifying the
            //      the setDirection() calls above.
            // Once the correct motors move in the correct direction re-comment this code.

            /*
            leftFrontPower  = gamepad1.x ? 1.0 : 0.0;  // X gamepad
            leftBackPower   = gamepad1.a ? 1.0 : 0.0;  // A gamepad
            rightFrontPower = gamepad1.y ? 1.0 : 0.0;  // Y gamepad
            rightBackPower  = gamepad1.b ? 1.0 : 0.0;  // B gamepad
            */
            // Send calculated power to wheels
            map.LeftFront.setPower(LeftFrontPower);
            map.RightFront.setPower(RightFrontPower);
            map.LeftBack.setPower(LeftBackPower);
            map.RightBack.setPower(RightBackPower);


////////////////// arm servo ////////////////////

            // This loop[l runs as long as the OpMode is active

            // Check the gamepad input
            if (gamepad2.dpad_up) {
                hardware358.lift_servo1.setPower(0.5);
                hardware358.lift_servo2.setPower(-0.5);
                telemetry.addData(">", "Lift servo moving up");
            } else if (gamepad2.dpad_down) {
                hardware358.lift_servo1.setPower(-0.5);
                hardware358.lift_servo2.setPower(0.5);
                telemetry.addData(">", "Lift servo moving down");
            } else {
                hardware358.lift_servo1.setPower(0);
                hardware358.lift_servo2.setPower(0);
                telemetry.addData(">", "Lift servo at rest");
            }

            // Update telemetry to show the current state
            telemetry.update();


            ////////////////// INTAKE //////////////////

            // IN //
            if (gamepad2.left_stick_y < -0.5) {
                hardware358.intake.setPower(1);
                telemetry.addData(">", "intaking...");

            }
            // OUT //
            else if (gamepad2.left_stick_y > 0.5) {
                hardware358.intake.setPower(-1);
                telemetry.addData(">", "spitting out pixels...");
            }
            // at rest //
            else {
                hardware358.intake.setPower(0);
                telemetry.addData(">", "not intaking");

            }

            telemetry.update();

            /////////////////Lift////////////////
            //======================================
            //----------Lift--------------
            //======================================
            double vm = 0.0;
            boolean firstActivated = false;

            if (gamepad2.left_trigger > 0.05) {
                // Check if the right stick is pushed forward or backward

                telemetry.addData(">", "lifting...");
                telemetry.addData("power is",-vm);
            }
            else if (gamepad2.right_trigger > 0.05) {
                // Check if the right stick is pushed forward or backward
                vm = -gamepad2.right_trigger;
                firstActivated = true;
                telemetry.addData(">", "unlifting...");
                long wait =System.currentTimeMillis();
                while (((System.currentTimeMillis()-wait<5000))){
                }
                vm=1.0;

            }else if (firstActivated){
                vm = 1;
            }
            else {
                // set vm to default to stay up
                vm = 0.0;
                telemetry.addData(">", "staying still");
            }
            hardware358.hang1.setPower(-vm);
            hardware358.hang2.setPower(-vm);
            //hang2.setPower(vm1);
            telemetry.update();

            ////////////////////////////////////////
            /////////////DRONE LAUNCHER/////////////
            ////////////////////////////////////////

            if (gamepad2.right_bumper) {
                hardware358.launcher.setPosition(1.0);
                telemetry.addData(">", "launching drone");
            }
            else {
                hardware358.launcher.setPosition(0.0);
                telemetry.addData(">", "nothing");
            }

            /////////hanger////////
            double vm1 = 0.0;

            if (gamepad2.left_trigger > 0.05) {
                // Check if the right stick is pushed forward or backward
                vm1 = gamepad2.left_trigger;

                telemetry.addData(">", "lifting...");
            }
            else if (gamepad2.right_trigger > 0.05) {
                // Check if the right stick is pushed forward or backward
                vm1 = -gamepad2.right_trigger;
                telemetry.addData(">", "unlifting...");

            } else {
                // set vm to default to stay up
                vm1 = 0.0;
                telemetry.addData(">", "staying still");
            }
            hardware358.hang1.setPower(-vm1);
            hardware358.hang2.setPower(-vm1);
            telemetry.update();
        }



    }
    }
