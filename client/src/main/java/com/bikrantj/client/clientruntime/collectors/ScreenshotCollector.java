package com.bikrantj.client.clientruntime.collectors;

import com.bikrantj.client.clientruntime.data.ScreenshotMonitoringData;
import com.bikrantj.client.clientruntime.monitoring.MonitoringContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Instant;

public class ScreenshotCollector
        implements DataCollector<ScreenshotMonitoringData> {

    @Override
    public ScreenshotMonitoringData collect(
            MonitoringContext context,
            Instant timestamp
    ) throws Exception {

        Robot robot = new Robot();
        Rectangle screen =
                new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage image = robot.createScreenCapture(screen);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);

        return new ScreenshotMonitoringData(
                context.getWorkspaceId(),
                context.getClientIdentifier(),
                timestamp,
                baos.toByteArray()
        );
    }
}
