package org.firstinspires.ftc.teamcode.Dreamville.Robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Dreamville.Subsystems.Carousel;
import org.firstinspires.ftc.teamcode.Dreamville.Subsystems.Drivetrain;

@TeleOp(name = "MainTeleOp", group = "tele")
public class MainBot extends OpMode {
    Drivetrain drivetrain = new Drivetrain();
    Carousel carousel = new Carousel();

    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dashboardTelemetry = dashboard.getTelemetry();
    MultipleTelemetry multiTelemetry = new MultipleTelemetry(telemetry, dashboardTelemetry);

    @Override
    public void init() {
        drivetrain.init(hardwareMap);
        carousel.init(hardwareMap);
    }

    @Override
    public void loop() {
        carousel.spin(gamepad1.b, telemetry);
        drivetrain.drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_bumper, gamepad1.right_bumper, gamepad1.dpad_left, gamepad1.dpad_right, gamepad1.dpad_down, gamepad1.dpad_up, multiTelemetry);

        multiTelemetry.update();
    }
}