public class Main {

    // take screenshots and send them to dropbox
    public static void main(String[] args) {

        if(args.length < 1){

            System.out.println("Usage: <dropbox_api_token>");
        }

        String ACCESS_TOKEN = args[0];

        new ScreenshoterThread(ACCESS_TOKEN).start();
    }
}