package just.monika.LiteraruteMC.Lucky.module.impl.render;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.render.Render2DEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.render.ShaderEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.StringSetting;
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationManager;
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationType;
import just.monika.LiteraruteMC.Lucky.utils.animations.Animation;
import just.monika.LiteraruteMC.Lucky.utils.animations.Direction;
import just.monika.LiteraruteMC.Lucky.utils.animations.impl.DecelerateAnimation;
import just.monika.LiteraruteMC.Lucky.utils.font.FontUtil;
import just.monika.LiteraruteMC.Lucky.utils.misc.NetworkingUtils;
import just.monika.LiteraruteMC.Lucky.utils.objects.Dragging;
import just.monika.LiteraruteMC.Lucky.utils.render.ColorUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.RenderUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.RoundedUtil;
import just.monika.LiteraruteMC.Lucky.utils.spotify.SpotifyAPI;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SpotifyMod extends Module {

    private final StringSetting clientID = new StringSetting("Client ID");
    private final ModeSetting backgroundColor = new ModeSetting("Background", "Average", "Average", "Spotify Grey", "Sync", "Analogous");
    private static final ModeSetting degree = new ModeSetting("Degree", "30", "30", "-30");
    private final ModeSetting progressBarColor = new ModeSetting("Progress Bar", "Green", "Average", "Green", "White");


    private final Dragging drag = LuckyClient.INSTANCE.createDrag(this, "spotify", 5, 150);
    public final float height = 50;
    public final float albumCoverSize = height;
    private final float playerWidth = 135;
    private final float width = albumCoverSize + playerWidth;

    private final Animation scrollTrack = new DecelerateAnimation(10000, 1, Direction.BACKWARDS);
    private final Animation scrollArtist = new DecelerateAnimation(10000, 1, Direction.BACKWARDS);
    public Animation playAnimation = new DecelerateAnimation(250, 1);

    public String[] buttons = {FontUtil.SKIP_LEFT, FontUtil.SKIP_RIGHT, FontUtil.SHUFFLE};
    public HashMap<String, Animation> buttonAnimations;


    public SpotifyAPI api;
    private CurrentlyPlayingContext currentPlayingContext;
    private Track currentTrack;
    public boolean playingMusic;
    public boolean hoveringPause;
    private boolean downloadedCover;
    private ResourceLocation currentAlbumCover;
    private Color imageColor = Color.WHITE;

    public SpotifyMod() {
        super("Spotify", Category.RENDER, "UI for spotify");
        addSettings(clientID, backgroundColor, degree, progressBarColor);
        degree.addParent(backgroundColor, modeSetting -> modeSetting.is("Analogous"));
    }

    private final Color greyColor = new Color(30, 30, 30);
    private final Color progressBackground = new Color(45, 45, 45);
    private final Color shuffleColor = new Color(50, 255, 100);
    private final Color hoveredColor = new Color(195, 195, 195);
    private final Color circleColor = new Color(50, 50, 50);
    private final EventListener<ShaderEvent> shaderEvent = event -> {
        if (api.currentTrack == null || api.currentPlayingContext == null) return;
        float x = drag.getX(), y = drag.getY();

        RoundedUtil.drawRound(x, y, playerWidth + (albumCoverSize), height, 6, greyColor);
    };



    private final EventListener<Render2DEvent> eventListener = event -> {
        if (api.currentTrack == null || api.currentPlayingContext == null) return;
        //Check if the song has changed
        if (currentTrack != api.currentTrack || currentPlayingContext != api.currentPlayingContext) {
            this.currentTrack = api.currentTrack;
            this.currentPlayingContext = api.currentPlayingContext;
        }
        playingMusic = currentPlayingContext.getIs_playing();


        float x = drag.getX(), y = drag.getY();
        drag.setWidth(width);
        drag.setHeight(height);


        Color color2 = ColorUtil.darker(imageColor, .65f);

        switch (backgroundColor.getMode()) {
            case "Average":
                float[] hsb = Color.RGBtoHSB(imageColor.getRed(), imageColor.getGreen(), imageColor.getBlue(), null);
                if (hsb[2] < .5f) {
                    color2 = ColorUtil.brighter(imageColor, .65f);
                }

                RoundedUtil.drawGradientVertical(x + (albumCoverSize - 15), y, playerWidth + 15, height, 6,
                        color2, imageColor);
                break;
            case "Spotify Grey":
                RoundedUtil.drawRound(x + (albumCoverSize - 15), y, playerWidth + 15, height, 6, greyColor);
                break;
            case "Sync":
                HudMod hudMod = ((HudMod) LuckyClient.INSTANCE.getModuleCollection().get(HudMod.class));
                Color[] colors = hudMod.getClientColors();
                RoundedUtil.drawGradientCornerLR(x + (albumCoverSize - 15), y, playerWidth + 15, height, 6, colors[0], colors[1]);
                break;
            case "Analogous":
                Color[] analogousColors = ColorUtil.getAnalogousColor(imageColor);
                int val = degree.is("30") ? 0 : 1;
                RoundedUtil.drawGradientCornerLR(x + (albumCoverSize - 15), y, playerWidth + 15, height, 6, analogousColors[val], imageColor);
                break;
        }


        final int diff = currentTrack.getDurationMs() - currentPlayingContext.getProgress_ms();
        final long diffSeconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;
        final long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;

        final String trackRemaining = String.format("-%s:%s", diffMinutes < 10 ? "0" + diffMinutes : diffMinutes, diffSeconds < 10 ? "0" + diffSeconds : diffSeconds);


        //Scroll and scissor the track name and artist if needed
        RenderUtil.scissor(x + albumCoverSize, y, playerWidth, height, () -> {
            final StringBuilder artistsDisplay = new StringBuilder();
            for (int artistIndex = 0; artistIndex < currentTrack.getArtists().length; artistIndex++) {
                final ArtistSimplified artist = currentTrack.getArtists()[artistIndex];
                artistsDisplay.append(artist.getName()).append(artistIndex + 1 == currentTrack.getArtists().length ? '.' : ", ");
            }
            if (scrollTrack.finished(Direction.BACKWARDS)) {
                scrollTrack.reset();
            }
            if (scrollArtist.finished(Direction.BACKWARDS)) {
                scrollArtist.reset();
            }
            boolean needsToScrollTrack = FontUtil.tenacityBoldFont26.getStringWidth(currentTrack.getName()) > playerWidth;
            boolean needsToScrollArtist = FontUtil.tenacityFont22.getStringWidth(artistsDisplay.toString()) > playerWidth;

            float trackX = (float) ((float) ((x + albumCoverSize) - FontUtil.tenacityBoldFont22.getStringWidth(currentTrack.getName())) +
                    ((FontUtil.tenacityBoldFont22.getStringWidth(currentTrack.getName()) + playerWidth) * scrollTrack.getLinearOutput()));

            FontUtil.tenacityBoldFont22.drawString(currentTrack.getName(), needsToScrollTrack ? trackX : x + albumCoverSize + 3, y + 3, -1);

            float artistX = (float) ((float) ((x + albumCoverSize) - FontUtil.tenacityFont18.getStringWidth(artistsDisplay.toString())) +
                    ((FontUtil.tenacityFont18.getStringWidth(artistsDisplay.toString()) + playerWidth) * scrollArtist.getLinearOutput()));

            FontUtil.tenacityFont18.drawString(artistsDisplay.toString(), needsToScrollArtist ? artistX : x + albumCoverSize + 4, y + 17, -1);
        });

        //Draw time left on song
        FontUtil.tenacityFont16.drawString(trackRemaining, x + width - (FontUtil.tenacityFont16.getStringWidth(trackRemaining) + 3),
                y + height - (FontUtil.tenacityFont16.getHeight() + 3), -1);

        float progressBarWidth = (playerWidth - 35);
        float progressBarHeight = 3;
        float progress = progressBarWidth * (currentPlayingContext.getProgress_ms() / (float) currentTrack.getDurationMs());
        Color progressColor;

        switch (progressBarColor.getMode()) {
            case "Average":
                progressColor = backgroundColor.is("Average") ? color2 : imageColor;
                break;
            case "Green":
                progressColor = new Color(50, 255, 100);
                break;
            default:
                progressColor = Color.WHITE;
                break;
        }

        RoundedUtil.drawRound(x + albumCoverSize + 5, y + height - (progressBarHeight + 4.5f), progressBarWidth, progressBarHeight, 1.5f, progressBackground);
        RoundedUtil.drawRound(x + albumCoverSize + 5, y + height - (progressBarHeight + 4.5f), progress, progressBarHeight, 1.5f, progressColor);


        float spacing = 0;

        RenderUtil.resetColor();
        for (String button : buttons) {
            Color normalColor = button.equals(FontUtil.SHUFFLE) && currentPlayingContext.getShuffle_state() ? shuffleColor : Color.WHITE;
            RenderUtil.resetColor();
            FontUtil.iconFont20.drawString(button, x + albumCoverSize + 6 + spacing, y + height - 19,
                    ColorUtil.interpolateColor(normalColor, hoveredColor, (float) buttonAnimations.get(button).getOutput()));
            spacing += 15;
        }


        if (currentAlbumCover != null && downloadedCover) {
            mc.getTextureManager().bindTexture(currentAlbumCover);
            GlStateManager.color(1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
            RoundedUtil.drawRoundTextured(x, y, albumCoverSize, albumCoverSize, 6, 1);
        }
        if ((currentAlbumCover == null || !currentAlbumCover.getResourcePath().contains(currentTrack.getAlbum().getId()))) {
            downloadedCover = false;
            final ThreadDownloadImageData albumCover = new ThreadDownloadImageData(null, currentTrack.getAlbum().getImages()[1].getUrl(), null, new IImageBuffer() {
                @Override
                public BufferedImage parseUserSkin(BufferedImage image) {
                    imageColor = ColorUtil.averageColor(image, image.getWidth(), image.getHeight(), 1);
                    downloadedCover = true;
                    return image;
                }

                @Override
                public void skinAvailable() {
                }
            });
            mc.getTextureManager().loadTexture(currentAlbumCover = new ResourceLocation("spotifyAlbums/" + currentTrack.getAlbum().getId()), albumCover);

        }

        playAnimation.setDirection(!playingMusic || hoveringPause ? Direction.FORWARDS : Direction.BACKWARDS);
        RoundedUtil.drawRound(x + albumCoverSize/2f - 22.5f, y + albumCoverSize/2f - 22.5f, 45, 45, 23, ColorUtil.applyOpacity(circleColor, (float) (.47 * playAnimation.getOutput())));

        FontUtil.iconFont40.drawCenteredString(currentPlayingContext.getIs_playing() ? FontUtil.PLAY : FontUtil.PAUSE,
                x + albumCoverSize / 2f + 2, y + albumCoverSize / 2f - FontUtil.iconFont40.getHeight() / 2f + 2,
                ColorUtil.applyOpacity(-1, (float) playAnimation.getOutput()));



    };


    @Override
    public void onEnable() {
        if (mc.thePlayer == null) {
            toggle();
            return;
        }
        if (buttonAnimations == null) {
            buttonAnimations = new HashMap<>();
            for (String button : buttons) {
                buttonAnimations.put(button, new DecelerateAnimation(250, 1, Direction.BACKWARDS));
            }
        }

        String clientID = this.clientID.getString();
        if (api == null) api = new SpotifyAPI();

        if (clientID.equals("")) {
            clientID = getClientIDFromJson();
            if (clientID.equals("")) {
                toggleSilent();
                return;
            }
        }

        api.build(clientID);
        setClientID(clientID);
        api.startConnection();
        super.onEnable();
    }


    public void setClientID(String clientID) {
        JsonObject keyObject = new JsonObject();
        keyObject.addProperty("clientID", clientID);
        try {
            Writer writer = new BufferedWriter(new FileWriter(SpotifyAPI.CLIENT_ID_DIR));
            SpotifyAPI.GSON.toJson(keyObject, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getClientIDFromJson() {
        JsonObject fileContent;
        JsonParser jsonParser = new JsonParser();
        try {
            fileContent = jsonParser.parse(new FileReader(SpotifyAPI.CLIENT_ID_DIR)).getAsJsonObject();
            if (fileContent.has("clientID")) {
                return fileContent.get("clientID").getAsString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        openYoutubeTutorial();
        NotificationManager.post(NotificationType.WARNING, "Error", "No Client ID found");
        return "";
    }

    public void openYoutubeTutorial() {
        NetworkingUtils.openLink("https://www.youtube.com/watch?v=3jOR29h1i40");
    }


}
