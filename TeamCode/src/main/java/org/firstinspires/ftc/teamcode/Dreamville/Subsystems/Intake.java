package org.firstinspires.ftc.teamcode.Dreamville.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Config
public class Intake {
    public static double ltDivisor = 2;

    private double g1rt, g1lt;
    private boolean g1t1;
    private boolean oldt1 = false;
    private Rumbler rumbler;
    private Telemetry telemetry;

    public static double distanceTolerance = 3.5;
    public static double rumblePower1 = 0.5;
    public static double rumblePower2 = 0.5;

    private static DcMotor intake;
    private static RevColorSensorV3 colorSensor;

    private enum intakeMode {
        INTAKE,
        DEPOSIT,
        STOP,
        COLOR
    }

    private intakeMode intakeState = intakeMode.STOP;

    public Intake(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        colorSensor = hardwareMap.get(RevColorSensorV3.class, "sensor_color");
    }

    public void intake(double g1rt, double g1lt, boolean g1t1, Rumbler rumbler, Telemetry telemetry) {
        this.g1rt = g1rt;
        this.g1lt = g1lt;

        this.g1t1 = g1t1;

        this.rumbler = rumbler;

        this.telemetry = telemetry;

        controls();
    }

    public void controls() {
        double distance = ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM);

        telemetry.addData("Distance (cm)", "%.3f", distance);
        telemetry.addData("g1TouchpadState", g1t1);

        if (g1t1 && !oldt1) {
            if (distance < distanceTolerance) {
                rumbler.rumbleBlips(2);
            }
        }
        oldt1 = g1t1;

        switch (intakeState) {
            case STOP:
                intake.setPower(0);
                if (g1rt != 0) {
                    if (distance > distanceTolerance) {
                        intakeState = intakeMode.INTAKE;
                    }
                } else if (g1lt != 0) {
                    intakeState = intakeMode.DEPOSIT;
                }
                break;
            case INTAKE:
                intake.setPower(g1rt);
                if (distance < distanceTolerance) {
                    intakeState = intakeMode.COLOR;
                    if (!rumbler.isRumbling()) {
                        rumbler.rumble(rumblePower1, rumblePower2, 1000);
                    }
                }
                if (g1rt == 0) {
                    intakeState = intakeMode.STOP;
                }
                if (g1lt != 0) {
                    intakeState = intakeMode.DEPOSIT;
                }
                break;
            case DEPOSIT:
                intake.setPower(-(g1lt / ltDivisor));
                //if (distance > 4.5) {
                //    intakeState = intakeMode.COLOR;
                //}
                if (g1lt == 0) {
                    intakeState = intakeMode.STOP;
                }
                if (g1rt != 0) {
                    intakeState = intakeMode.INTAKE;
                }
                break;
            case COLOR:
                intake.setPower(0);
                if (g1lt == 0 && g1rt == 0) {
                    intakeState = intakeMode.STOP;
                }
                break;
        }
    }
}