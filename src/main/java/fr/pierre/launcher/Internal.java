package fr.pierre.launcher;

import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.exception.BadServerResponseException;
import fr.theshark34.supdate.exception.BadServerVersionException;
import fr.theshark34.supdate.exception.ServerDisabledException;
import fr.theshark34.supdate.exception.ServerMissingSomethingException;

import java.io.IOException;

public class Internal {
    public static final GameInfos INFOS = new GameInfos("Kuruma", new GameVersion("1.7.10", GameType.V1_7_10), new GameTweak[] {GameTweak.FORGE});

    private static AuthInfos authInfos;

    public static Thread updateThread;

    public static void auth(String eMail, String password) throws AuthenticationException {
        Main.getInstance().getPanel().infoLabel.setText("Authentification...");
        Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
        AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, eMail, password, "");
        authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());
    }

    public static void update() throws BadServerResponseException, IOException, BadServerVersionException, ServerDisabledException, ServerMissingSomethingException {
        SUpdate sUpdate = new SUpdate("http://51.210.83.207:81", INFOS.getGameDir());
        sUpdate.getServerRequester().setRewriteEnabled(true);

        updateThread = new Thread() {
            @Override
            public void run() {
                Main.getInstance().getPanel().infoLabel.setText("V\u00E9rification des fichiers...");

                while(!this.isInterrupted()) {
                    if(BarAPI.getNumberOfFileToDownload() != 0) {
                        Main.getInstance().getPanel().infoLabel.setText("T\u00E9l\u00E9chargement des fichiers : " + BarAPI.getNumberOfDownloadedFiles() + " / " + BarAPI.getNumberOfFileToDownload());
                    }
                }
            }
        };
        updateThread.start();
        sUpdate.start();
        updateThread.interrupt();

    }

    public static void launch() throws LaunchException {

        Main.getInstance().getPanel().infoLabel.setText("Lancement du jeu...");

        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(INFOS, GameFolder.BASIC, authInfos);
        ExternalLauncher launcher = new ExternalLauncher(profile);

        Process launchProcess = launcher.launch();

        try {
            Thread.sleep(5000L);
            launchProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

}
