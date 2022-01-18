package org.firstinspires.ftc.teamcode.Dreamville.Robot;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Dreamville.AutoFunctions;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class MainAuto extends AutoFunctions {
    @Override
    public void runOpMode() {
        dropOdometry();

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(-23, 62.5, Math.toRadians(270));

        drive.setPoseEstimate(startPose);

        TrajectorySequence myTrajectory = drive.trajectorySequenceBuilder(startPose)
                .splineToLinearHeading(new Pose2d(-54, 59, Math.toRadians(0)), Math.toRadians(180))
                .addTemporalMarker(() -> autoSpinCarousel())
                .lineToConstantHeading(new Vector2d(-54, 24))
                .splineToConstantHeading(new Vector2d(-30, 24), Math.toRadians(0))
                .build();

        waitForStart();

        if(isStopRequested()) return;

        drive.followTrajectorySequence(myTrajectory);

        raiseOdometry();
    }
}
